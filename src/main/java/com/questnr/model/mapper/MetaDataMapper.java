package com.questnr.model.mapper;

import com.questnr.model.dto.MetaDataDTO;
import com.questnr.responses.TimeData;
import com.questnr.services.CommonService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MetaDataMapper {

    public static MetaDataDTO getMetaDataMapper(Date createdAt, Date updatedAt) {
        Date now = new Date();

        Long elapsed = (now.getTime() - updatedAt.getTime()) / 1000;

        TimeData timeData = CommonService.calculateTimeFromSeconds(elapsed);
        MetaDataDTO metaDataDTO = new MetaDataDTO();
        if (createdAt.equals(updatedAt)) {
            metaDataDTO.setEdited(true);
        } else {
            metaDataDTO.setEdited(false);
        }

        // @Todo: Can only be added if required. For Example, this can be useful for post, not comment or like.
        // can be done with adding getMetaDataMapper(Date createdAt, Date updatedAt, boolean shouldIncludeDate) or new overloading function.
        if(CommonService.isElapsedGreaterThanMonth(elapsed)){
            metaDataDTO.setTimeString(CommonService.getDateStringForPublicUse(updatedAt));
        }

        metaDataDTO.setTimeString(timeData.getMaxTimePart());
        return metaDataDTO;
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
