package com.questnr.util;

import ws.schild.jave.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoCompression implements Runnable {
    /* Step 1. Declaring source file and Target file */
    private File source;

    private File target;

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

    public void run() {

        /* https://github.com/a-schild/jave2/wiki/Usage */

        /* Step 2. Set Audio Attrributes for conversion*/
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec(AudioAttributes.DIRECT_STREAM_COPY);
// here 64kbit/s is 64000
//        audio.setBitRate(64000);
//        audio.setChannels(2);
//        audio.setSamplingRate(44100);

        /* Step 3. Set Video Attributes for conversion*/
        VideoAttributes video = new VideoAttributes();
//        video.setCodec(VideoAttributes.DIRECT_STREAM_COPY);
        video.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
// Here 160 kbps video is 160000
//        video.setBitRate(160000);
// More the frames more quality and size, but keep it low based on devices like mobile
        video.setFrameRate(30);
//        video.setSize(new VideoSize(400, 300));

        /* Step 4. Set Encoding Attributes*/
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);

        /* Step 5. Do the Encoding*/
        try {
            Encoder encoder = new Encoder();
            List<MultimediaObject> multimediaObjects = new ArrayList<>();
            multimediaObjects.add(new MultimediaObject(source));
            encoder.encode(multimediaObjects, target, attrs);
        } catch (Exception e) {
            /*Handle here the video failure*/
            e.printStackTrace();
        }
    }
}
