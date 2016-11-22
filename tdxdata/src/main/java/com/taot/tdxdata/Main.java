package com.taot.tdxdata;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    private static final String inputBasedir = "/home/taot/stock-data/tdx5min/sz5fz/";

    private static final String outputBasedir = "/home/taot/stock-data/tdx5min/sz5fz-csv/";

    private static final String filename = "sz150176.5";

    public static void main(String[] args) throws IOException {
        String inputFilename = inputBasedir + filename;
        String outputFilename = outputBasedir + filename + ".csv";
        FileInputStream fis = new FileInputStream(inputFilename);
        FileOutputStream fos = new FileOutputStream(outputFilename);
        TDX5MinExtractor extractor = new TDX5MinExtractor(fis, fos, PriceMode.THOUSAND);
        extractor.run();
    }
}
