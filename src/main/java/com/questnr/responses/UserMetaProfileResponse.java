package com.questnr.responses;

public class UserMetaProfileResponse {
    private int followers;
    private int followingTo;
    private int postsOnProfile;
    private int postsOnCommunities;
    private int posts;
    private int ownsCommunities;
    private int followsCommunities;

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowingTo() {
        return followingTo;
    }

    public void setFollowingTo(int followingTo) {
        this.followingTo = followingTo;
    }

    public int getPostsOnProfile() {
        return postsOnProfile;
    }

    public void setPostsOnProfile(int postsOnProfile) {
        this.postsOnProfile = postsOnProfile;
    }

    public int getPostsOnCommunities() {
        return postsOnCommunities;
    }

    public void setPostsOnCommunities(int postsOnCommunities) {
        this.postsOnCommunities = postsOnCommunities;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getOwnsCommunities() {
        return ownsCommunities;
    }

    public void setOwnsCommunities(int ownsCommunities) {
        this.ownsCommunities = ownsCommunities;
    }

    public int getFollowsCommunities() {
        return followsCommunities;
    }

    public void setFollowsCommunities(int followsCommunities) {
        this.followsCommunities = followsCommunities;
    }
}
