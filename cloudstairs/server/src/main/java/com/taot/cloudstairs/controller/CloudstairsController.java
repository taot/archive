package com.taot.cloudstairs.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taot.cloudstairs.CSRequest;
import com.taot.cloudstairs.CSResponse;
import com.taot.cloudstairs.JsonParser;
import com.taot.cloudstairs.Session;
import com.taot.cloudstairs.SessionManager;
import com.taot.cloudstairs.util.AesUtil;
import com.taot.cloudstairs.util.IoUtil;
import com.taot.cloudstairs.util.RsaUtil;


@Controller
@RequestMapping(value = "/")
public class CloudstairsController {

    private static Logger logger = LoggerFactory.getLogger(CloudstairsController.class);

    private static HttpClient httpClient = new DefaultHttpClient(new PoolingClientConnectionManager());

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/index";
    }
    
    @RequestMapping(value = "session", method = RequestMethod.GET)
    public ModelAndView session(Model model, HttpServletRequest request) {
        logger.info("Session request from " + request.getRemoteAddr());
        
        // create a new session, and encrypt it with RSA public key
        PublicKey[] pks = RsaUtil.loadPubicKeys();
        if (pks == null || pks.length == 0) {
            throw new RuntimeException("No public key found.");
        }
        Session session = new Session();
        SessionManager.getInstance().add(session);
        logger.debug("Created session: " + session.toString());
        String json = JsonParser.session2Json(session.toCSSession());
        System.out.println();
        System.out.println("json: " + json);
        System.out.println();
        byte[] encryptedJson = RsaUtil.encrypt(pks[0], json.getBytes());
        
        // base64 encode the encrypted session
        String sessionEncryptedBase64 = Base64.encodeBase64String(encryptedJson);
        
        ModelAndView mav = new ModelAndView("/stringcontent");
        mav.addObject("content", sessionEncryptedBase64);
        return mav;
    }

    @RequestMapping(value = "climb", method = RequestMethod.POST)
    public ModelAndView climb(@RequestParam(value = "s") String sUuid,
            @RequestParam(value = "content") String encryptedContentBase64)
            throws IOException, URISyntaxException {
        
        Session session = SessionManager.getInstance().get(sUuid);
        if (session == null) {
            // TODO redirect to new session path
            throw new RuntimeException("Session is null for " + sUuid);
        }
        
        byte[] encryptedContentBytes = Base64.decodeBase64(encryptedContentBase64);
        
        byte[] contentBytes = AesUtil.decrypt(session.getAesKey(), encryptedContentBytes);
        String content = new String(contentBytes);
        
        CSRequest csReq = JsonParser.json2Request(content);

        logger.info(csReq.getMethod() + " " + csReq.getRequestURI());

        HttpRequestBase proxyReq = toProxyRequest(csReq);
        HttpResponse proxyResp = httpClient.execute(proxyReq);
        CSResponse csResp = toCloudstairsResponse(proxyReq, proxyResp);

        String retContent = JsonParser.response2Json(csResp);
        if (logger.isDebugEnabled()) {
            logger.debug("Cloudstairs response (json): " + retContent);
        }

        ModelAndView mav = new ModelAndView("/stringcontent");
        mav.addObject("content", retContent);
        return mav;
    }

    private HttpRequestBase toProxyRequest(CSRequest csReq) throws URISyntaxException {
        HttpRequestBase proxyReq = null;
        String method = csReq.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            proxyReq = new HttpGet();
        } else if ("POST".equalsIgnoreCase(method)) {
            proxyReq = new HttpPost();
            proxyReq.setParams(convertParams(csReq.getPostForm()));
        } else {
            logger.error("Unsupported method: " + method);
            throw new RuntimeException("Unsupported method: " + method);
        }

        String fixedUri = fixUri(csReq.getRequestURI());
        URI uri = new URI(fixedUri);
        proxyReq.setURI(uri);
        proxyReq.setHeaders(convertHeaders(csReq.getHeader()));

        return proxyReq;
    }

    private String fixUri(String uri) {
        String fixed = uri.replace("|", "%7C").replace("{", "%7B").replace("}", "%7D");
        return fixed;
    }

    private CSResponse toCloudstairsResponse(HttpRequestBase proxyReq, HttpResponse proxyResp) throws IOException {
        CSResponse csResp = new CSResponse();
        csResp.setHeader(convertHeaders(proxyResp.getAllHeaders()));
        csResp.setStatusCode(proxyResp.getStatusLine().getStatusCode());
        csResp.setStatus(proxyResp.getStatusLine().getReasonPhrase());

        // set the response body in base64
        HttpEntity entity = proxyResp.getEntity();
        if (entity != null) {
            InputStream instream = null;
            try {
                instream = entity.getContent();
                byte[] bytes = IoUtil.readToByteArray(instream);
                String base64 = Base64.encodeBase64String(bytes);
                csResp.setBody(base64);
            } catch (RuntimeException e) {
                proxyReq.abort();
                throw e;
            } finally {
                // Closing the input stream will trigger connection release
                if (instream != null) {
                    instream.close();
                }
            }
        } else {
            csResp.setBody("");
        }

        return csResp;
    }

    private Header[] convertHeaders(Map<String, List<String>> headers) {
        List<Header> list = new ArrayList<Header>();
        for (Map.Entry<String, List<String>> ent : headers.entrySet()) {
            Header h = new BasicHeader(ent.getKey(), ent.getValue().get(0));
            list.add(h);
        }
        return list.toArray(new Header[list.size()]);
    }

    private Map<String, List<String>> convertHeaders(Header[] headers) {
        Map<String, List<String>> m = new HashMap<String, List<String>>();
        for (Header h : headers) {
            List<String> list = new ArrayList<String>();
            list.add(h.getValue());
            m.put(h.getName(), list);
        }
        return m;
    }

    private HttpParams convertParams(Map<String, List<String>> params) {
        HttpParams httpParams = new BasicHttpParams();
        if (params != null) {
            for (Map.Entry<String, List<String>> ent : params.entrySet()) {
                httpParams.setParameter(ent.getKey(), ent.getValue().get(0));
            }
        }
        return httpParams;
    }

}
