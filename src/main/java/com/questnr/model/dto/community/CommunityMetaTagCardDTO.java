package com.questnr.model.dto.community;

import com.questnr.model.entities.CommunityMetaInformation;

import java.util.ArrayList;
import java.util.List;

public class CommunityMetaTagCardDTO {
    String title;
    List<CommunityMetaInformation> metaList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommunityMetaInformation> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<CommunityMetaInformation> metaList) {
        this.metaList = metaList;
    }
}
