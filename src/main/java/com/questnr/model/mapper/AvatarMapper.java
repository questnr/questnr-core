package com.questnr.model.mapper;

import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.exceptions.InvalidInputException;
import com.questnr.model.dto.AvatarDTO;
import com.questnr.model.entities.Avatar;
import com.questnr.services.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvatarMapper {

    @Autowired
    AmazonS3Client amazonS3Client;

    public AvatarDTO toAvatarDTO(Avatar avatar) {
        try{
            if(avatar == null || avatar.getAvatarKey().trim().isEmpty()) {
                throw new InvalidInputException(Avatar.class.getName(), null, null);
            }
            AvatarDTO avatarDTO = new AvatarDTO();
            avatarDTO.setAvatarLink(this.amazonS3Client.getS3BucketUrl(avatar.getAvatarKey(), PostActionPrivacy.public_post));
            return avatarDTO;
        }catch (Exception e) {
            return new AvatarDTO();
        }
    }

    public List<AvatarDTO> toAvatarDTOList(List<Avatar> avatarList) {
        return avatarList.stream().map((Avatar avatar) ->
            this.toAvatarDTO(avatar)
        ).collect(Collectors.toList());
    }
}
