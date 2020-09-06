package com.questnr.model.dto.post.normal;

import com.questnr.model.dto.community.CommunityDTO;
import com.questnr.model.dto.post.PostBaseDTO;

public class PostNotAccessibleDTO extends PostBaseDTO {
    private final boolean hasError = true;
    private String errorMessage = "Not Accessible";
    private CommunityDTO communityDTO;

    public boolean isHasError() {
        return hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CommunityDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityDTO communityDTO) {
        this.communityDTO = communityDTO;
    }
}
