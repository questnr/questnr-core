package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "qr_community_meta_information")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CommunityMetaInformation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_meta_seq")
    @SequenceGenerator(name = "community_meta_seq", sequenceName = "community_meta_seq", allocationSize = 1)
    private long id;

    @JsonBackReference(value = "meta-reference")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

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
     * @return the community
     */
    public Community getCommunity() {
        return community;
    }

    /**
     * @param community the community to set
     */
    public void setCommunity(Community community) {
        this.community = community;
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
