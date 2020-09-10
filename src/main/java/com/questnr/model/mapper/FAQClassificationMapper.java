package com.questnr.model.mapper;

import com.questnr.model.dto.faq.FAQClassificationDTO;
import com.questnr.model.dto.faq.search.FAQClassificationSearchDTO;
import com.questnr.model.entities.FAQClassification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class FAQClassificationMapper {
    public abstract FAQClassificationDTO toDTO(final FAQClassification faqClassification);

    public abstract FAQClassificationSearchDTO toClassificationDTO(final FAQClassification faqClassification);
}
