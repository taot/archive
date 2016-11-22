package com.taot.cloudstairs;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionManager {
    
    private static SessionManager instance = new SessionManager();
    private static Logger logger = LoggerFactory.getLogger(SessionManager.class);
    
    private final ConcurrentMap<String, Session> sessionMap = new ConcurrentHashMap<String, Session>(); 

    private SessionManager(){
        final Runnable cleaner = new Runnable() {
            public void run() {
                removeExpired();
            }
        };
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(cleaner, 0, 5 * 60, TimeUnit.SECONDS);
    }
    
    public static SessionManager getInstance() {
        return instance;
    }
    
    public void add(Session session) {
        sessionMap.put(session.getUuid(), session);
    }
    
    public Session get(String uuid) {
        Session session = sessionMap.get(uuid);
        if (session == null) {
            return null;
        }
        if (session.isExpired()) {
            sessionMap.remove(uuid);
            return null;
        }
        return session;
    }
    
    private void removeExpired() {
        logger.info("Start cleaning expired sessions.");
        int count = 0;
        for (String uuid : sessionMap.keySet()) {
            Session s = sessionMap.get(uuid);
            if (s.isExpired()) {
                logger.debug("Removing session " + s.toString());
                sessionMap.remove(uuid);
                count += 1;
            }
        }
        logger.info("Finished cleaning expired sessions. " + count + " session(s) removed");
    }
}
