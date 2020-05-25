package com.questnr.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.questnr.common.enums.LoginType;
import com.questnr.common.enums.SignUpSourceType;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "qr_users")
@Indexed
public class User extends DomainObject {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long userId;

    @Column(name = "username", length = 32, unique = true, nullable = false)
    @Size(min = 3, max = 32)
    private String username;

    @Column(name = "password", length = 100)
    @Size(min = 6, max = 100)
    private String password;

    @Column(name = "first_name", length = 25)
    @Size(min = 3, max = 25)
    private String firstName;

    @Column(name = "last_name", length = 25)
    @Size(min = 3, max = 25)
    private String lastName;

    @Column(name = "email_id", unique = true, nullable = false)
    @Size(min = 4)
    private String emailId;

    @Pattern(regexp = "[6-9]{1}[0-9]{9}", message = "Mobile number is invalid")
    @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    @Column(name = "is_email_verified")
    @ColumnDefault(value = "false")
    private Boolean isEmailVerified;

    @Column(name = "is_mobile_verified")
    private boolean isMobileNumberVerified;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @JsonIgnore
    @Column(name = "last_password_reset_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @Column(name = "slug")
    private String slug;

    @JsonIgnoreProperties("users")
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "qr_user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private Set<Authority> authorities;

    @OneToMany(mappedBy = "user")
    private Set<CommunityUser> communityJoinedList;

    @OneToMany(mappedBy = "user")
    private Set<CommunityInvitedUser> communityInvitedUsers;


    // Users following this user.
    @OneToMany(mappedBy = "user")
    private Set<UserFollower> thisBeingFollowedUserSet;


    // This user following to another users.
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "followingUser",
            orphanRemoval = true)
    private Set<UserFollower> thisFollowingUserSet;

    @JsonIgnoreProperties("userActor")
    @OneToMany(mappedBy = "userActor")
    private Set<PostAction> postActionSet;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

    @Column(name = "sign_up_source")
    @Enumerated(EnumType.STRING)
    private SignUpSourceType signUpSource = SignUpSourceType.WEB;

    @Column(name = "is_public", columnDefinition = "bool default true")
    private Boolean isPublic;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "terms_privacy_agree")
    private Boolean agree;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public boolean isMobileNumberVerified() {
        return isMobileNumberVerified;
    }

    public void setMobileNumberVerified(boolean mobileNumberVerified) {
        isMobileNumberVerified = mobileNumberVerified;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @JsonIgnore
    public Set<CommunityUser> getCommunityJoinedList() {
        return communityJoinedList;
    }

    @JsonIgnore
    public Set<CommunityInvitedUser> getCommunityInvitedUsers() {
        return communityInvitedUsers;
    }

    @JsonIgnore
    public Set<UserFollower> getThisBeingFollowedUserSet() {
        return thisBeingFollowedUserSet;
    }

    public void setThisBeingFollowedUserSet(Set<UserFollower> thisBeingFollowedUserSet) {
        this.thisBeingFollowedUserSet = thisBeingFollowedUserSet;
    }

    @JsonIgnore
    public Set<UserFollower> getThisFollowingUserSet() {
        return thisFollowingUserSet;
    }

    public void setThisFollowingUserSet(Set<UserFollower> thisFollowingUserSet) {
        this.thisFollowingUserSet = thisFollowingUserSet;
    }

    public Set<PostAction> getPostActionSet() {
        return postActionSet;
    }

    public void setPostActionSet(Set<PostAction> postActionSet) {
        this.postActionSet = postActionSet;
    }

    public SignUpSourceType getSignUpSource() {
        return signUpSource;
    }

    public void setSignUpSource(SignUpSourceType signUpSource) {
        this.signUpSource = signUpSource;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("equals called");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        System.out.println("hashCode called");
        return Objects.hash(userId);
    }
}
