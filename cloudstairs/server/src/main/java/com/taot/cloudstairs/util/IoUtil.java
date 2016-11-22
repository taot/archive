package com.taot.cloudstairs.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IoUtil {

    private static int BUF_SIZE = 4096;

    private IoUtil() {
    }

    public static byte[] readToByteArray(InputStream instream) {
        try {
            byte[] buf = new byte[BUF_SIZE];
            BufferedInputStream bufferedInStream = null;

            bufferedInStream = new BufferedInputStream(instream);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            int n = bufferedInStream.read(buf, 0, buf.length);
            while (n > 0) {
                bao.write(buf, 0, n);
                n = bufferedInStream.read(buf, 0, buf.length);
            }

            return bao.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}