package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "qr_community_invitations")
@Indexed
public class CommunityInvitedUser {
    @Id
    @Column(name = "community_invitation_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_invitation_seq")
    @SequenceGenerator(name = "community_invitation_seq", sequenceName = "community_invitation_seq", allocationSize = 1)
    public Long communityInvitationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @Basic(optional = false)
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = Timestamp.valueOf(LocalDateTime.now());

    public Long getCommunityInvitationId() {
        return communityInvitationId;
    }

    public void setCommunityInvitationId(Long communityInvitationId) {
        this.communityInvitationId = communityInvitationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    @JsonIgnore
    public Date getCreatedAt() {
        return createdAt;
    }
}
