package com.questnr.model.mapper;

import com.questnr.common.enums.PostEditorType;
import com.questnr.model.dto.post.normal.NormalPostDTO;
import com.questnr.model.entities.PostAction;
import org.springframework.stereotype.Component;

@Component
public class NormalPostMapper {

    public static NormalPostDTO getMetaMapper(final PostAction postAction, final boolean toSetText) {
       NormalPostDTO normalPostDTO = new NormalPostDTO();
       try{
           normalPostDTO.setPostEditorType(postAction.getPostEditorType());
           if(postAction.getPostEditorType() == PostEditorType.blog){
               normalPostDTO.setBlogTitle(postAction.getBlogTitle());
               if(toSetText){
                   normalPostDTO.setText(postAction.getText());
               }
           }else{
               normalPostDTO.setText(postAction.getText());
           }
           return normalPostDTO;
       }catch (Exception e){
           return normalPostDTO;
       }
    }
}
