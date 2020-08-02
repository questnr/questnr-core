package com.questnr.model.entities.media;

import com.questnr.common.enums.ResourceType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Media {
    @Id
    @Column(name = "post_media_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_media_seq")
    @SequenceGenerator(name = "post_media_seq", sequenceName = "post_media_seq", allocationSize = 1)
    private Long postMediaId;

    @Column(name = "post_media_key")
    private String mediaKey;

    @Column(name = "resource_type", columnDefinition = "int4 default 0")
    private ResourceType resourceType = ResourceType.image;

    @Column(name = "file_extension")
    private String fileExtension;

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

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
