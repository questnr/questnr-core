package com.questnr.requests;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import org.springframework.web.multipart.MultipartFile;

public class PostActionRequest {

    private String slug;
    private String title;
    private String text;
    private PublishStatus status;
    private boolean featured;
    private boolean popular;
    private String tags;
    private String titleTag;
    private Community community;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitleTag() {
        return titleTag;
    }

    public void setTitleTag(String titleTag) {
        this.titleTag = titleTag;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public PostAction getPostAction(){
        PostAction postAction = new PostAction();
        postAction.setSlug(this.getSlug());
        postAction.setTitle(this.getTitle());
        postAction.setText(this.getText());
        postAction.setStatus(this.getStatus());
        postAction.setFeatured(this.isFeatured());
        postAction.setPopular(this.isPopular());
        postAction.setTags(this.getTags());
        postAction.setTitleTag(this.getTitleTag());
        postAction.setCommunity(this.getCommunity());
        return postAction;
    }
}
