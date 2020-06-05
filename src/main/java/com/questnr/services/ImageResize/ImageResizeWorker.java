package com.questnr.services.ImageResize;

import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.util.ImageResizer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageResizeWorker extends Thread {

    private static final Logger LOG = Logger.getLogger(ImageResizeWorker.class.getName());

    private boolean iCanContinue = true;

    private final Queue<ImageResizeJobRequest> queue = new LinkedList<>();

    private ImageResizeJobRequest item;

    private final int MAX_SLEEP_TIME = 3 * 60 * 60 * 1000; //3 hours

    private AmazonS3Client amazonS3Client;

    private final int id;

    private final int ICON_SIZE = 45;

    private final int SMALL_SIZE = 70;

    private final int MEDIUM_SIZE = 230;

    private final int LARGE_SIZE = 400;

    public ImageResizeWorker(int id, AmazonS3Client amazonS3Client) {
        super("ImageResizeWorker-" + id);
        setDaemon(true);
        this.id = id;
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public void run() {

        LOG.log(Level.INFO, "ImageResize worker-{0} is up and running.", id);

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
                // Take new item from the top of the queue
                item = queue.poll();
                // Null if queue is empty
                if (item == null) {
                    continue;
                }
                try {
                    item.getImageResizer().setFormat(item.getFormat());

                    ImageResizer imageResizer = item.getImageResizer();
                    imageResizer.setScaledHeight(ICON_SIZE);
                    imageResizer.setScaledWidth(ICON_SIZE);
                    imageResizer.setOutputFileName("icon");
                    Thread imageResizeIconThread = new Thread(imageResizer, "ImageResizeIconThread-" + id);
                    imageResizeIconThread.run();
                    imageResizeIconThread.join();

                    this.amazonS3Client.uploadFileToPath(imageResizer.getOutputFile(), item.getPathToFile());
                    imageResizer.getOutputFile().delete();

                    ImageResizer imageResizerSmall = item.getImageResizer();
                    imageResizerSmall.setScaledHeight(SMALL_SIZE);
                    imageResizerSmall.setScaledWidth(SMALL_SIZE);
                    imageResizerSmall.setOutputFileName("small");
                    Thread imageResizeSmallThread = new Thread(imageResizerSmall, "ImageResizeSmallThread-" + id);
                    imageResizeSmallThread.run();
                    imageResizeSmallThread.join();

                    this.amazonS3Client.uploadFileToPath(imageResizerSmall.getOutputFile(), item.getPathToFile());
                    imageResizer.getOutputFile().delete();

                    ImageResizer imageResizerMedium = item.getImageResizer();
                    imageResizerMedium.setScaledHeight(MEDIUM_SIZE);
                    imageResizerMedium.setScaledWidth(MEDIUM_SIZE);
                    imageResizerMedium.setOutputFileName("medium");
                    Thread imageResizeMediumThread = new Thread(imageResizerMedium, "ImageResizeMediumThread-" + id);
                    imageResizeMediumThread.run();
                    imageResizeMediumThread.join();

                    this.amazonS3Client.uploadFileToPath(imageResizerMedium.getOutputFile(), item.getPathToFile());
                    imageResizer.getOutputFile().delete();

                    try{
                        item.getImageResizer().getInputFile().delete();
                    }catch (Exception e){

                    }

//                    item.getImageResizer().setScaledHeight(LARGE_SIZE);
//                    item.getImageResizer().setScaledWidth(LARGE_SIZE);
//                    Thread imageResizeLargeThread = new Thread(item.getImageResizer(), "imageResizeLargeThread-" + id);
//                    imageResizeLargeThread.run();
//                    threadList.add(imageResizeIconThread);


                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Exception while sending ImageResize ...{0}",
                            ex.getMessage());
                }

            }
        }
    }

    public void add(ImageResizeJobRequest item) {
        synchronized (queue) {
            queue.add(item);
            queue.notify();
            LOG.log(Level.INFO, "New ImageResize added into queue for  worker-{0}...", id);
        }
    }

    public void stopWorker() {
        LOG.log(Level.INFO, "Stopping ImageResize worker-{0}...", id);
        try {
            iCanContinue = false;
            this.interrupt();
            this.join();
        } catch (InterruptedException | NullPointerException e) {
            LOG.log(Level.SEVERE, "Exception while stopping ImageResize worker...{0}",
                    e.getMessage());
        }
    }


}