package com.questnr.model.entities.media;

public class VideoMetaObject {
    private Integer bitRate;
    private String decoder;
    private Float frameRate;
    private MediaSize mediaSize;

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public String getDecoder() {
        return decoder;
    }

    public void setDecoder(String decoder) {
        this.decoder = decoder;
    }

    public Float getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(Float frameRate) {
        this.frameRate = frameRate;
    }

    public MediaSize getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(MediaSize mediaSize) {
        this.mediaSize = mediaSize;
    }
}
