package com.cxn.downloader.thread;

public class DetailPrinter implements Runnable {
    long interval;
    boolean stop;
    int[] progress;

    public DetailPrinter(long interval, int[] progress) {
        this.interval = interval;
        this.stop = false;
        this.progress = progress;
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            print();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        print();
    }

    public void print() {
        System.out.println("***********************************************");
        StringBuilder sb = new StringBuilder(1000);
        for (int i = 0; i < progress.length; ++i) {
            sb.append("thread ").append(i).append("[");
            int j = 0;
            for (; j < progress[i]; ++j) {
                sb.append("=");
            }
            for (; j < 100; ++j) {
                sb.append(" ");
            }
            sb.append("]").append(progress[i]).append("%");
            System.out.println(sb.toString());
            sb.setLength(0);
        }
        System.out.println("***********************************************");
    }
}
