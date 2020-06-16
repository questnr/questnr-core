package com.questnr.model.mapper;

import com.questnr.model.entities.PostReport;
import com.questnr.requests.PostReportRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {
        UserMapper.class,
        PostActionMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class PostReportMapper {

    abstract public PostReport fromPostReportRequest(final PostReportRequest postReportRequest);
}
