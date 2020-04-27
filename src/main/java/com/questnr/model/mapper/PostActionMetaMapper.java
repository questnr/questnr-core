package com.questnr.model.mapper;

import com.questnr.model.dto.PostActionMetaDTO;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostActionMetaMapper {

    public static PostActionMetaDTO getMetaMapper(final PostAction postAction, final User user) {
        PostActionMetaDTO postActionMetaDTO = new PostActionMetaDTO();
        List<LikeAction> likeActionList = postAction.getLikeActionSet().stream().filter(likeAction ->
                user.equals(likeAction.getUserActor())
        ).collect(Collectors.toList());
        if(likeActionList.size() == 1){
            postActionMetaDTO.setLiked(true);
        }else{
            postActionMetaDTO.setLiked(false);
        }
        return postActionMetaDTO;
    }

    // This will create meta data from createdAt attribute only.
//    public MetaDataDTO toMetaDataDTO(Date createdAt) {
//        Date now = new Date();
//
//        Long elapsed = (now.getTime() - createdAt.getTime()) / 1000;
//
//        TimeData timeData = CommonService.calculateTimeFromSeconds(elapsed);
//        MetaDataDTO metaDataDTO = new MetaDataDTO();
//        metaDataDTO.setTimeString(timeData.getMaxTimePart());
//        return metaDataDTO;
//    }
}
