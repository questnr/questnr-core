package com.questnr.model.dto.post.normal;

import com.questnr.model.dto.community.CommunityPublicWithoutMetaDTO;
import com.questnr.model.dto.post.PostBaseDTO;

public class PostNotAccessibleDTO extends PostBaseDTO {
    private final boolean hasError = true;
    private String errorMessage = "Not Accessible";
    private CommunityPublicWithoutMetaDTO communityDTO;

    public boolean isHasError() {
        return hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CommunityPublicWithoutMetaDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityPublicWithoutMetaDTO communityDTO) {
        this.communityDTO = communityDTO;
    }
}
