package com.questnr.model.entities;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;


@Entity
@Table(name = "qr_community_tags")
@Indexed
public class CommunityTag {
    @Id
    @Column(name = "community_tag_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_tag_seq")
    @SequenceGenerator(name = "community_tag_seq", sequenceName = "community_tag_seq", allocationSize = 1)
    public Long communityTagId;

    @ManyToOne
    @JoinColumn(name = "entity_id")
    private EntityTag entityTag;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    public Long getCommunityTagId() {
        return communityTagId;
    }

    public void setCommunityTagId(Long communityTagId) {
        this.communityTagId = communityTagId;
    }

    public EntityTag getEntityTag() {
        return entityTag;
    }

    public void setEntityTag(EntityTag entityTag) {
        this.entityTag = entityTag;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }
}
