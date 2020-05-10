package com.questnr.responses;

import com.questnr.common.enums.ResourceType;

import java.util.Map;

public class ResourceStorageData {
    private String url;
    private String key;
    private ResourceType resourceType;
    private String format;
    private String duration;
    private String bitRate;
    private String version;
    private String isAudio;
    private String frameRate;

    public ResourceStorageData map(Map result) {
        try {
            this.setResourceType(ResourceType.valueOf(result.get("resource_type").toString()));
        } catch (Exception e) {
            this.setResourceType(ResourceType.image);
        }

        this.setKey(result.get("public_id").toString());
        this.setFormat(result.get("format").toString());
        this.setVersion(result.get("version").toString());
        this.setDuration(result.get("duration").toString());
        this.setBitRate(result.get("bit_rate").toString());
        this.setIsAudio(result.get("is_audio").toString());
        this.setFrameRate(result.get("frame_rate").toString());
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBitRate() {
        return bitRate;
    }

    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIsAudio() {
        return isAudio;
    }

    public void setIsAudio(String isAudio) {
        this.isAudio = isAudio;
    }

    public String getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(String frameRate) {
        this.frameRate = frameRate;
    }
}
