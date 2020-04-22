package com.questnr.services.notification;

import com.questnr.model.entities.Notification;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.services.CommonService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationWorker extends Thread {

    private static final Logger LOG = Logger.getLogger(NotificationWorker.class.getName());

    private boolean iCanContinue = true;

    private final Queue<Notification> queue = new LinkedList<>();

    private Notification item;

    private final int MAX_SLEEP_TIME = 3 * 60 * 60 * 1000; //3 hours

    private NotificationRepository notificationRepository;

    private final int id;

    public NotificationWorker(int id, NotificationRepository notificationRepository) {
        super("NotificationWorker-" + id);
        setDaemon(true);
        this.id = id;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run() {

        LOG.log(Level.INFO, "Notification worker-{0} is up and running.", id);

        while (iCanContinue) {
            synchronized (queue) {
                // Check for a new item from the queue
                if (queue.isEmpty()) {
                    // Sleep for it, if there is nothing to do
                    LOG.log(Level.INFO, "Waiting for Notification to send...{0}", CommonService.getTime());
                    try {
                        queue.wait(MAX_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        LOG.log(Level.INFO, "Interrupted...{0}", CommonService.getTime());
                    }
                }
                // Take new item from the top of the queue
                item = queue.poll();
                // Null if queue is empty
                if (item == null) {
                    continue;
                }
                try {
                    this.notificationRepository.save(item);
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Exception while sending Notification ...{0}",
                            ex.getMessage());
                }
            }
        }
    }

    public void add(Notification item) {
        synchronized (queue) {
            queue.add(item);
            queue.notify();
            LOG.log(Level.INFO, "New Notification added into queue for  worker-{0}...", id);
        }
    }

    public void stopWorker() {
        LOG.log(Level.INFO, "Stopping Notification worker-{0}...", id);
        try {
            iCanContinue = false;
            this.interrupt();
            this.join();
        } catch (InterruptedException | NullPointerException e) {
            LOG.log(Level.SEVERE, "Exception while stopping Notification worker...{0}",
                    e.getMessage());
        }
    }


}