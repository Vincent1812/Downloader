package com.cxn.downloader;

import com.cxn.downloader.config.ConfigUtil;
import com.cxn.downloader.factory.MyThreadFactory;
import com.cxn.downloader.thread.DetailPrinter;
import com.cxn.downloader.thread.DownloadThread;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DownloadStartup {
    public static void main(String[] args) throws Exception {

        ConfigUtil.init();
        System.out.println(ConfigUtil.getDownloadOriginPath());
        System.out.println(ConfigUtil.getDownloadDestPath());

        int fileSize = getDownLoadSize();
        System.out.println("fileSize:" + fileSize);
        int threadNum = ConfigUtil.getThreadNum();
        int perSize = fileSize / threadNum;

        ExecutorService es = Executors.newFixedThreadPool(threadNum+1, new MyThreadFactory("downloader-sub-"));
        int start = 0;
        int end = perSize - 1;
        Future<Boolean>[] futureArr = new Future[threadNum];
        int[] progress = new int[threadNum];
        for (int i = 0; i < threadNum; ++i) {
            futureArr[i] = es.submit(new DownloadThread(progress, i, start, end, fileSize));
            start += perSize;
            end += perSize;
            if (i == threadNum - 2) {
                end = fileSize - 1;
            }
        }
        DetailPrinter dp = new DetailPrinter(1000, progress);
        es.submit(dp);
        int successCnt = 0;
        for (int i = 0; i < threadNum; ++i) {
            if (futureArr[i].get()) {
                successCnt++;
            }
        }
        dp.stop();
        es.shutdown();
        System.out.println("**********************complete : success thread:" + successCnt + ", failed:" + (threadNum - successCnt));
    }

    private static int getDownLoadSize() throws Exception {
        URL url = new URL(ConfigUtil.getDownloadOriginPath());
        URLConnection conn = url.openConnection();
        return conn.getContentLength();
    }

}
