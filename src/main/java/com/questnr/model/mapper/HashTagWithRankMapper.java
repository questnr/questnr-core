package com.questnr.model.mapper;

import com.questnr.model.dto.HashTagWithRankDTO;
import com.questnr.model.entities.HashTag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface HashTagWithRankMapper {

    HashTagWithRankDTO toHashTagWithRankDTO(HashTag hashTag);
}
