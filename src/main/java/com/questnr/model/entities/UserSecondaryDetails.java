package com.questnr.model.entities;

import com.questnr.common.enums.CommunitySuggestionDialogActionType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;

@Entity
@Table(name = "qr_user_secondary_details")
@Indexed
public class UserSecondaryDetails extends DomainObject {

    @Id
    @Column(name = "user_secondary_details_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_secondary_details_seq")
    @SequenceGenerator(name = "user_secondary_details_seq", sequenceName = "user_secondary_details_seq", allocationSize = 1)
    private Long userSecondaryDetailsId;

    @Column(name = "logged_in_count", columnDefinition = "integer default 0")
    private Integer loggedInCount;

    @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
    @Column(name = "community_suggestion", columnDefinition = "varchar default 'remained'")
    @Enumerated(EnumType.STRING)
    private CommunitySuggestionDialogActionType communitySuggestion;

    @OneToOne(mappedBy = "userSecondaryDetails",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private User user;

    public UserSecondaryDetails() {
    }

    public Long getUserSecondaryDetailsId() {
        return userSecondaryDetailsId;
    }

    public void setUserSecondaryDetailsId(Long userSecondaryDetailsId) {
        this.userSecondaryDetailsId = userSecondaryDetailsId;
    }

    public Integer getLoggedInCount() {
        return loggedInCount;
    }

    public void setLoggedInCount(Integer loggedInCount) {
        this.loggedInCount = loggedInCount;
    }

    public CommunitySuggestionDialogActionType getCommunitySuggestion() {
        return communitySuggestion;
    }

    public void setCommunitySuggestion(CommunitySuggestionDialogActionType communitySuggestion) {
        this.communitySuggestion = communitySuggestion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void defaultData(){
        this.loggedInCount = 0;
        this.communitySuggestion = CommunitySuggestionDialogActionType.remained;
    }
}
