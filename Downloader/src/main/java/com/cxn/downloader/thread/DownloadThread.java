package com.cxn.downloader.thread;

import com.cxn.downloader.config.ConfigUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class DownloadThread implements Callable<Boolean> {
    int start;
    int end;
    int fileSize;
    int idx;
    int[] progress;

    public DownloadThread(int[] progress, int idx, int start, int end, int fileSize) {
        this.start = start;
        this.end = end;
        this.fileSize = fileSize;
        this.idx = idx;
        this.progress = progress;
    }

    @Override
    public Boolean call() {
        System.out.println(Thread.currentThread().getName() + " start");
        RandomAccessFile raFile = null;
        InputStream is = null;
        try {
            raFile = new RandomAccessFile(ConfigUtil.getDownloadDestPath(), "rw");

            URL url = new URL(ConfigUtil.getDownloadOriginPath());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String value = "bytes=" + start + "-" + end;
            conn.setRequestProperty("Range", value);
            conn.connect();
            System.out.println(Thread.currentThread().getName() + " value :" + value + " size:" + conn.getContentLength());
            is = conn.getInputStream();

            raFile.seek(start);
            byte[] buffer = new byte[1024];
            int size = 0;
            int left = end - start + 1;
            while ((size = is.read(buffer)) != -1) {
                raFile.write(buffer, 0, size);
                left -= size;
                progress[idx] = 100 - left * 100 / (end - start + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (raFile != null) {
                try {
                    raFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
