package com.questnr.services.ImageResize;

import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageResizeProcessor extends Thread {

    private static final Logger LOG = Logger.getLogger(ImageResizeProcessor.class.getName());

    private boolean iCanContinue = true;

    private static ImageResizeProcessor sInstance = null;

    private ImageResizeWorker[] workers;

    private final Queue<ImageResizeJobRequest> queue = new LinkedList<>();

    private final int MAX_SLEEP_TIME = 3 * 60 * 60 * 1000; // 3 hours

    private final int MAX_WORKERS = 40; // @Todo: Set SES workers as required

    private int current = 0;

    private AmazonS3Client amazonS3Client;

    private ImageResizeProcessor(AmazonS3Client amazonS3Client) {
        super("ImageResizeProcessor");
        setDaemon(true);
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public void run() {
        LOG.log(Level.INFO, "ImageResize processor is up and running.");
        //initialize workers
        initializeWorkers();
        //start processing
        while (iCanContinue) {
            synchronized (queue) {
                // Check for a new item from the queue
                if (queue.isEmpty()) {
                    // Sleep for it, if there is nothing to do
                    LOG.log(Level.INFO, "Waiting for ImageResize to send...{0}", CommonService.getTime());
                    try {
                        queue.wait(MAX_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        LOG.log(Level.INFO, "Interrupted...{0}", CommonService.getTime());
                    }
                }
                //distribute tasks among workers
                while (!queue.isEmpty()) {
                    workers[current++].add(queue.poll());
                    current = current % MAX_WORKERS;
                }
            }
        }
    }

    public static synchronized ImageResizeProcessor getInstance(AmazonS3Client amazonS3Client) {
        if (sInstance == null) {
            sInstance = new ImageResizeProcessor(amazonS3Client);
            sInstance.start();
        }
        return sInstance;
    }

    private void initializeWorkers() {
        LOG.info("ImageResize workers are initializing ....");
        workers = new ImageResizeWorker[MAX_WORKERS];
        for (int i = 0; i < MAX_WORKERS; i++) {
            workers[i] = new ImageResizeWorker(i + 1, this.amazonS3Client);
            workers[i].start();
        }
    }

    private void stopWorkers() {
        LOG.info("ImageResize workers are stopping...");
        for (int i = 0; i < MAX_WORKERS; i++) {
            workers[i].stopWorker();
        }
    }

    public void add(ImageResizeJobRequest item) {
        synchronized (queue) {
            queue.add(item);
            queue.notify();
            LOG.info("New ImageResize added into queue...");
        }
    }

    public static void stopProcessor() {
        if (sInstance == null) {
            return;
        }
        LOG.info("Stopping Notification processor...");
        try {
            //stop workers first
            sInstance.stopWorkers();
            sInstance.iCanContinue = false;
            sInstance.interrupt();
            sInstance.join();
        } catch (InterruptedException | NullPointerException e) {
            LOG.log(Level.SEVERE, "Exception while stop Notification processor...{0}",
                    e.getMessage());
        }
    }
}