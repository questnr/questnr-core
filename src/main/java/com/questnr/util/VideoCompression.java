package com.questnr.util;

import com.questnr.responses.ResourceStorageData;
import ws.schild.jave.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoCompression implements Runnable {
    /* Step 1. Declaring source file and Target file */
    private File source;

    private File target;

    private int quality = 70;

    private float duration = 300;

    private int maxFrameRate = 30;

    private static final int PERCENTAGE = 100;

    private ResourceStorageData resourceStorageData;

    public VideoCompression() {

    }

    public VideoCompression(File source, File target) {
        this.source = source;
        this.target = target;
    }

    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }

    public File getTarget() {
        return target;
    }

    public void setTarget(File target) {
        this.target = target;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public int getMaxFrameRate() {
        return maxFrameRate;
    }

    public void setMaxFrameRate(int maxFrameRate) {
        this.maxFrameRate = maxFrameRate;
    }

    private Float getQualityInPercentage() {
        return Float.valueOf(quality) / PERCENTAGE;
    }

    public void run() {
        MultimediaInfo infos;
        try {
            MultimediaObject mmObject = new MultimediaObject(source);
            infos = mmObject.getInfo();

            Encoder encoder = new Encoder();
//            String[] ven = encoder.getVideoEncoders();
//            String[] aen = encoder.getAudioEncoders();

            /* https://github.com/a-schild/jave2/wiki/Usage */

            /* Step 2. Set Audio Attrributes for conversion*/
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec(AudioAttributes.DIRECT_STREAM_COPY);
            // here 64kbit/s is 64000
            audio.setBitRate(64000);
            audio.setChannels(infos.getAudio().getChannels());
            audio.setSamplingRate(infos.getAudio().getSamplingRate());

            /* Step 3. Set Video Attributes for conversion*/
            VideoAttributes video = new VideoAttributes();
            video.setCodec(VideoAttributes.DIRECT_STREAM_COPY);

            // Here 160 kbps video is 160000
            video.setBitRate(infos.getVideo().getBitRate());
            // More the frames more quality and size, but keep it low based on devices like mobile
            video.setFrameRate(infos.getVideo().getFrameRate() > this.getMaxFrameRate()
                    ? this.getMaxFrameRate() : (int) infos.getVideo().getFrameRate());
//            video.setSize(
//                    new VideoSize((int) (infos.getVideo().getSize().getWidth() * this.getQualityInPercentage()),
//                            (int) (infos.getVideo().getSize().getHeight() * this.getQualityInPercentage())));

            /* Step 4. Set Encoding Attributes*/
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat(infos.getFormat());
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            // @Todo: Set encoding and decoding threads as required
            attrs.setEncodingThreads(21);
            attrs.setDecodingThreads(21);
            attrs.setDuration(infos.getDuration() > this.getDuration() ? this.getDuration() : infos.getDuration());

            /* Step 5. Do the Encoding*/
            try {
                List<MultimediaObject> multimediaObjects = new ArrayList<>();
                multimediaObjects.add(new MultimediaObject(source));
                encoder.encode(multimediaObjects, target, attrs);
            } catch (Exception e) {
                /*Handle here the video failure*/
                e.printStackTrace();
            }
        } catch (EncoderException ex) {

        }
    }
}
