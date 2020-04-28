package com.questnr.model.entities;

import com.questnr.common.enums.MediaType;
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

    @Column(name ="post_media_key")
    private String mediaKey;

    @Column(name ="post_media_type")
    private MediaType mediaType = MediaType.image;

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

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
