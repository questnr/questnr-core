package com.questnr.services.notification;

import com.questnr.model.entities.Notification;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.services.CommonService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationRevertProcessor extends Thread {

    private static final Logger LOG = Logger.getLogger(NotificationRevertProcessor.class.getName());

    private boolean iCanContinue = true;

    private static NotificationRevertProcessor sInstance = null;

    private NotificationRevertWorker[] workers;

    private final Queue<Notification> queue = new LinkedList<>();

    private final int MAX_SLEEP_TIME = 3 * 60 * 60 * 1000; //3 hours

    private final int MAX_WORKERS = 40; // @Todo: Set SES workers as required

    private int current = 0;

    private NotificationRepository notificationRepository;

    public NotificationRevertProcessor(NotificationRepository notificationRepository) {
        super("NotificationRevertProcessor");
        setDaemon(true);
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run() {
        LOG.log(Level.INFO, "NotificationRevert processor is up and running.");
        //initialize workers
        initializeWorkers();
        //start processing
        while (iCanContinue) {
            synchronized (queue) {
                // Check for a new item from the queue
                if (queue.isEmpty()) {
                    // Sleep for it, if there is nothing to do
                    LOG.log(Level.INFO, "Waiting for NotificationRevertProcessor to send...{0}", CommonService.getTime());
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

    public static synchronized NotificationRevertProcessor getInstance(NotificationRepository notificationRepository) {
        if (sInstance == null) {
            sInstance = new NotificationRevertProcessor(notificationRepository);
            sInstance.start();
        }
        return sInstance;
    }

    private void initializeWorkers() {
        LOG.info("NotificationRevert workers are initializing ....");
        workers = new NotificationRevertWorker[MAX_WORKERS];
        for (int i = 0; i < MAX_WORKERS; i++) {
            workers[i] = new NotificationRevertWorker(i + 1, this.notificationRepository);
            workers[i].start();
        }
    }

    private void stopWorkers() {
        LOG.info("NotificationRevert workers are stopping...");
        for (int i = 0; i < MAX_WORKERS; i++) {
            workers[i].stopWorker();
        }
    }

    public void add(Notification item) {
        synchronized (queue) {
            queue.add(item);
            queue.notify();
            LOG.info("New NotificationRevert added into queue...");
        }
    }

    public static void stopProcessor() {
        if (sInstance == null) {
            return;
        }
        LOG.info("Stopping NotificationRevert processor...");
        try {
            //stop workers first
            sInstance.stopWorkers();
            sInstance.iCanContinue = false;
            sInstance.interrupt();
            sInstance.join();
        } catch (InterruptedException | NullPointerException e) {
            LOG.log(Level.SEVERE, "Exception while stop NotificationRevert processor...{0}",
                    e.getMessage());
        }
    }
}