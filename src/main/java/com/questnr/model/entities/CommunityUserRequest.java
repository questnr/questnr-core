package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.questnr.common.NotificationTitles;
import com.questnr.common.enums.NotificationType;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "qr_community_user_requests")
@Indexed
public class CommunityUserRequest implements NotificationBase{
    @Id
    @Column(name = "community_user_request_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_user_request_seq")
    @SequenceGenerator(name = "community_user_request_seq", sequenceName = "community_user_request_seq", allocationSize = 1)
    public Long communityUserRequestId;

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

    public Long getCommunityUserRequestId() {
        return communityUserRequestId;
    }

    public void setCommunityUserRequestId(Long communityUserRequestId) {
        this.communityUserRequestId = communityUserRequestId;
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

    @JsonIgnore
    public NotificationType getNotificationType() {
        return NotificationType.requestedCommunity;
    }

    @JsonIgnore
    public String getNotificationTitles() {
        return NotificationTitles.REQUESTED_COMMUNITY;
    }
}
