package com.cxn.downloader.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {
    private static final String DOWNLOAD_ORIGIN_PATH = "downlaod.origin.path";
    private static final String DOWNLOAD_DEST_PATH = "downlaod.dest.path";
    private static final String DOWNLOAD_THREAD_NUM = "downlaod.thread.num";

    public static void init() {
        init("src" + File.separator + "main" + File.separator + "resources" + File.separator + "config.properties");
    }

    public static void init(String filePath) {
        Properties pro = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(filePath);
            pro.load(in);
            downloadOriginPath = pro.getProperty(DOWNLOAD_ORIGIN_PATH);
            downloadDestPath = pro.getProperty(DOWNLOAD_DEST_PATH);
            threadNum = Integer.parseInt(pro.getProperty(DOWNLOAD_THREAD_NUM, "10"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String downloadOriginPath;
    private static String downloadDestPath;
    private static int threadNum;

    public static String getDownloadOriginPath() {
        return downloadOriginPath;
    }

    public static String getDownloadDestPath() {
        return downloadDestPath;
    }

    public static int getThreadNum() {
        return threadNum;
    }
}
