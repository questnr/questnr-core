package com.questnr.model.dto.post.normal;

import com.questnr.model.entities.PostActionMetaInformation;

import java.util.ArrayList;
import java.util.List;

public class PostActionMetaTagCardDTO {
    String title;
    private List<PostActionMetaInformation> metaList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PostActionMetaInformation> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<PostActionMetaInformation> metaList) {
        this.metaList = metaList;
    }
}
