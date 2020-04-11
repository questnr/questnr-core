package com.questnr.model.mapper;

import com.questnr.model.dto.MetaDataDTO;
import com.questnr.responses.TimeData;
import com.questnr.services.CommonService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MetaDataMapper {

    public MetaDataDTO toMetaDataDTO(Date createdAt) {
        Date now = new Date();

        Long elapsed = (now.getTime() - createdAt.getTime()) / 1000;

        TimeData timeData = CommonService.calculateTimeFromSeconds(elapsed);
        MetaDataDTO metaDataDTO = new MetaDataDTO();
        metaDataDTO.setTimeString(timeData.getMaxTimePart());
        return metaDataDTO;
    }
}
