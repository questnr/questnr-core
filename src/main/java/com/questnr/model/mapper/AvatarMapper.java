package com.questnr.model.mapper;

import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.exceptions.InvalidInputException;
import com.questnr.model.dto.AvatarDTO;
import com.questnr.model.entities.Avatar;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvatarMapper {

    @Autowired
    AmazonS3Client amazonS3Client;

    @Value("${app.icon-prefix}")
    private String ICON_PREFIX;

    @Value("${app.small-prefix}")
    private String SMALL_PREFIX;

    @Value("${app.medium-prefix}")
    private String MEDIUM_PREFIX;

    @Value("${app.large-prefix}")
    private String LARGE_PREFIX;

    @Value("${amazonProperties.publicAssetPath}")
    private String publicAssetPath;

    public AvatarDTO toAvatarDTO(Avatar avatar) {
        try{
            if(avatar == null || !(CommonService.isNull(avatar.getFileName()) || CommonService.isNull(avatar.getAvatarKey()))) {
                throw new InvalidInputException(Avatar.class.getName(), null, null);
            }
            AvatarDTO avatarDTO = new AvatarDTO();
            String fileName;
            String pathToDir;
            if(CommonService.isNull(avatar.getFileName())){
//                Path ps = Paths.get(avatar.getAvatarKey());
//                fileName = ps.getFileName().toString();
//                Iterator<Path> pathIterator = ps.iterator();
//                pathToDir = "";
//                int i = 0;
//                while(pathIterator.hasNext()){
//                    if(i != 0)
//                        pathToDir += "/";
//                    pathToDir += pathIterator.next();
//                }
                avatarDTO.setAvatarLink(
                        this.amazonS3Client.getS3BucketUrl(
                                Paths.get(publicAssetPath, avatar.getAvatarKey()).toString(),
                                PostActionPrivacy.public_post));
            }else{
                fileName = avatar.getFileName();
                pathToDir = avatar.getPathToDir();
                avatarDTO.setAvatarLink(
                        this.amazonS3Client.getS3BucketUrl(
                                Paths.get(publicAssetPath, pathToDir, fileName).toString(),
                                PostActionPrivacy.public_post));
                avatarDTO.setIconLink(this.amazonS3Client.getS3BucketUrl(
                        Paths.get(publicAssetPath, pathToDir, ICON_PREFIX+fileName).toString(),
                        PostActionPrivacy.public_post));
                avatarDTO.setSmallLink(this.amazonS3Client.getS3BucketUrl(
                        Paths.get(publicAssetPath, pathToDir, SMALL_PREFIX+fileName).toString(),
                        PostActionPrivacy.public_post));
                avatarDTO.setMediumLink(this.amazonS3Client.getS3BucketUrl(
                        Paths.get(publicAssetPath, pathToDir, MEDIUM_PREFIX+fileName).toString(),
                        PostActionPrivacy.public_post));
            }
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
