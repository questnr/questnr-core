package com.questnr.model.entities;

import com.questnr.common.enums.ResourceType;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@Entity
@Indexed
@Table(name = "qr_post_media")
public class PostMedia {
    @Id
    @Column(name = "post_media_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_media_seq")
    @SequenceGenerator(name = "post_media_seq", sequenceName = "post_media_seq", allocationSize = 1)
    private Long postMediaId;

    @Column(name = "post_media_key")
    private String mediaKey;

    @Column(name = "resource_type", columnDefinition = "int4 default 0")
    private ResourceType resourceType = ResourceType.image;

    @Column(name = "format")
    private String format;

    @Column(name = "version")
    private String version;

    @Column(name = "is_audio")
    private String isAudio;

    @Column(name = "duration")
    private String duration;

    @Column(name = "bit_rate")
    private String bitRate;

    @Column(name = "frame_rate")
    private String frameRate;

    public Long getPostMediaId() {
        return postMediaId;
    }

    public void setPostMediaId(Long postMediaId) {
        this.postMediaId = postMediaId;
    }

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
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

    public String getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(String frameRate) {
        this.frameRate = frameRate;
    }
}
