package com.questnr.services.notification;

import com.questnr.model.entities.Notification;
import com.questnr.model.mapper.NotificationMapper;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.model.repositories.UserNotificationControlRepository;
import com.questnr.model.repositories.UserNotificationSettingsRepository;
import com.questnr.services.CommonService;
import com.questnr.services.notification.firebase.PushNotificationService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationProcessor extends Thread {

    private static final Logger LOG = Logger.getLogger(NotificationProcessor.class.getName());

    private boolean iCanContinue = true;

    private static NotificationProcessor sInstance = null;

    private NotificationWorker[] workers;

    private final Queue<Notification> queue = new LinkedList<>();

    private final int MAX_SLEEP_TIME = 3 * 60 * 60 * 1000; //3 hours

    private final int MAX_WORKERS = 40; // @Todo: Set SES workers as required

    private int current = 0;

    private NotificationRepository notificationRepository;

    private UserNotificationControlRepository userNotificationControlRepository;

    private UserNotificationSettingsRepository userNotificationSettingsRepository;

    private PushNotificationService pushNotificationService;

    private NotificationMapper notificationMapper;

    private NotificationProcessor(NotificationRepository notificationRepository, UserNotificationControlRepository userNotificationControlRepository, UserNotificationSettingsRepository userNotificationSettingsRepository, PushNotificationService pushNotificationService, NotificationMapper notificationMapper) {
        super("NotificationProcessor");
        setDaemon(true);
        this.notificationRepository = notificationRepository;
        this.userNotificationControlRepository = userNotificationControlRepository;
        this.userNotificationSettingsRepository = userNotificationSettingsRepository;
        this.pushNotificationService = pushNotificationService;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void run() {
        LOG.log(Level.INFO, "Notification processor is up and running.");
        //initialize workers
        initializeWorkers();
        //start processing
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
                //distribute tasks among workers
                while (!queue.isEmpty()) {
                    workers[current++].add(queue.poll());
                    current = current % MAX_WORKERS;
                }
            }
        }
    }

    public static synchronized NotificationProcessor getInstance(NotificationRepository notificationRepository, UserNotificationControlRepository userNotificationControlRepository, UserNotificationSettingsRepository userNotificationSettingsRepository, PushNotificationService pushNotificationService, NotificationMapper notificationMapper) {
        if (sInstance == null) {
            sInstance = new NotificationProcessor(notificationRepository, userNotificationControlRepository, userNotificationSettingsRepository, pushNotificationService, notificationMapper);
            sInstance.start();
        }
        return sInstance;
    }

    private void initializeWorkers() {
        LOG.info("Notification workers are initializing ....");
        workers = new NotificationWorker[MAX_WORKERS];
        for (int i = 0; i < MAX_WORKERS; i++) {
            workers[i] = new NotificationWorker(i + 1, this.notificationRepository, this.userNotificationControlRepository, this.userNotificationSettingsRepository, this.pushNotificationService, this.notificationMapper);
            workers[i].start();
        }
    }

    private void stopWorkers() {
        LOG.info("Notification workers are stopping...");
        for (int i = 0; i < MAX_WORKERS; i++) {
            workers[i].stopWorker();
        }
    }

    public void add(Notification item) {
        synchronized (queue) {
            queue.add(item);
            queue.notify();
            LOG.info("New Notification added into queue...");
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