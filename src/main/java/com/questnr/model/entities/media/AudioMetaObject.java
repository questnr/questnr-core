package com.questnr.model.entities.media;

public class AudioMetaObject {
    private int bitRate;
    private int samplingRate;
    private int channels;
    private String decoder;

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public String getDecoder() {
        return decoder;
    }

    public void setDecoder(String decoder) {
        this.decoder = decoder;
    }
}
