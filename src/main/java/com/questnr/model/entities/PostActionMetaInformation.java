package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "qr_post_meta_information")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostActionMetaInformation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_meta_seq")
    @SequenceGenerator(name = "post_meta_seq", sequenceName = "post_meta_seq", allocationSize = 1)
    private long id;

    @JsonBackReference(value = "meta-reference")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_action_id")
    private PostAction postAction;

    @Embedded
    private MetaInformation metaInformation;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the post
     */
    public PostAction getPostAction() {
        return postAction;
    }

    /**
     * @param postAction the post to set
     */
    public void setPostAction(PostAction postAction) {
        this.postAction = postAction;
    }

    /**
     * @return the metaInformation
     */
    public MetaInformation getMetaInformation() {
        return metaInformation;
    }

    /**
     * @param metaInformation the metaInformation to set
     */
    public void setMetaInformation(MetaInformation metaInformation) {
        this.metaInformation = metaInformation;
    }

}
