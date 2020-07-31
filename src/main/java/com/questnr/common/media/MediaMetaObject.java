package com.questnr.common.media;

public class MediaMetaObject {
    private long duration;
    private String format;
    private VideoMetaObject video;
    private AudioMetaObject audio;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public VideoMetaObject getVideo() {
        return video;
    }

    public void setVideo(VideoMetaObject video) {
        this.video = video;
    }

    public AudioMetaObject getAudio() {
        return audio;
    }

    public void setAudio(AudioMetaObject audio) {
        this.audio = audio;
    }
}
