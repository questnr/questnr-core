package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Indexed;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
}
