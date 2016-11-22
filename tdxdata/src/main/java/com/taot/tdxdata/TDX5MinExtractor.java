package com.taot.tdxdata;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TDX5MinExtractor {

    private InputStream inStream;

    private OutputStream outStream;

    private PriceMode priceMode;

    public TDX5MinExtractor(InputStream inStream, OutputStream outStream, PriceMode priceMode) {
        this.inStream = inStream;
        this.outStream = outStream;
        this.priceMode = priceMode;
    }

    public void run() throws IOException {
        byte[] buffer = new byte[32];
        PrintStream ps = null;
        try {
            ps = new PrintStream(new BufferedOutputStream(outStream));
            printHeader(ps);

            int n = inStream.read(buffer);
            while (n > 0) {
                extractOneLine(ps, buffer);
                n = inStream.read(buffer);
            }
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }
    }

    private void printHeader(PrintStream ps) {
        String[] header = new String[] {
                "Date", "Time", "PriceOpen", "PriceHigh", "PriceLow",
                "PriceClose", "TurnoverValue", "TurnoverVolume"
        };
        boolean isFirst = true;
        for (String s : header) {
            if (isFirst) {
                isFirst = false;
            } else {
                ps.print(',');
            }
            ps.print(s);
        }
        ps.println();
    }

    private void extractOneLine(PrintStream ps, byte[] buffer) throws IOException {
        // 每32个字节为一个5分钟数据，每字段内低字节在前
        // 00 ~ 01 字节：日期，整型，设其值为num，则日期计算方法为：
        // year=floor(num/2048)+2004;
        // month=floor(mod(num,2048)/100);
        // day=mod(mod(num,2048),100);

        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        short date = bb.getShort();
        int year = date / 2048 + 2004;
        int month = (date % 2048) / 100;
        int day = (date % 2048) % 100;
        String sDate = formatDate(year, month, day);
        ps.print(sDate);
        ps.print(',');

        // 02 ~ 03 字节： 从0点开始至目前的分钟数，整型
        int minutesFromMidnight = bb.getShort();
        int hours = minutesFromMidnight / 60;
        int minutes = minutesFromMidnight % 60;
        String sTime = formatTime(hours, minutes);
        ps.print(sTime);
        ps.print(',');

        // 04 ~ 07 字节：开盘价*100，整型
        int priceOpen = bb.getInt();
        printPrice(ps, priceOpen);
        ps.print(',');

        // 08 ~ 11 字节：最高价*100，整型
        int priceHigh = bb.getInt();
        printPrice(ps, priceHigh);
        ps.print(',');

        // 12 ~ 15 字节：最低价*100，整型
        int priceLow = bb.getInt();
        printPrice(ps, priceLow);
        ps.print(',');

        // 16 ~ 19 字节：收盘价*100，整型
        int priceClose = bb.getInt();
        printPrice(ps, priceClose);
        ps.print(',');

        // 20 ~ 23 字节：成交额*100，float型
        float turnoverValue = bb.getFloat();
        ps.print(turnoverValue / 100);
        ps.print(',');

        // 24 ~ 27 字节：成交量（股），整型
        int turnoverVolume = bb.getInt();
        ps.print(turnoverVolume);
        ps.print(',');

        // 28 ~ 31 字节：（保留）

        ps.println();
    }

    private void printPrice(PrintStream ps, int price) {
        double dPrice = price / priceMode.getDivider();
        ps.print(dPrice);
    }

    private String formatTime(int hour, int minute) {
        return padding(hour, 2) + padding(minute, 2);
    }

    private String formatDate(int year, int month, int day) {
        return padding(year, 4) + padding(month, 2) + padding(day, 2);
    }

    private String padding(int a, int len) {
        String s = String.valueOf(a);
        if (s.length() > len) {
            throw new RuntimeException("The number is longer than specified length");
        } else if (s.length() < len) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len - s.length(); i++) {
                sb.append('0');
            }
            sb.append(s);
            s = sb.toString();
        }
        return s;
    }
}
