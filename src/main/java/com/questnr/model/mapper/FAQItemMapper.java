package com.questnr.model.mapper;

import com.questnr.model.dto.faq.FAQItemAdminDTO;
import com.questnr.model.dto.faq.FAQItemDTO;
import com.questnr.model.dto.faq.search.FAQItemSearchDTO;
import com.questnr.model.entities.FAQItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {UserMapper.class, FAQClassificationMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class FAQItemMapper {
    public abstract FAQItemAdminDTO toDTO(final FAQItem FAQItem);

    public List<FAQItemAdminDTO> toDTOs(final List<FAQItem> FAQItemList) {
        List<FAQItemAdminDTO> faqItemDTOList = new ArrayList<>();
        for (FAQItem faqItem : FAQItemList) {
            faqItemDTOList.add(this.toDTO(faqItem));
        }
        return faqItemDTOList;
    }

    public abstract FAQItemSearchDTO toSearchDTO(final FAQItem FAQItem);

    public List<FAQItemSearchDTO> toSearchDTOs(final List<FAQItem> FAQItemList) {
        List<FAQItemSearchDTO> faqItemSearchDTOList = new ArrayList<>();
        for (FAQItem faqItem : FAQItemList) {
            faqItemSearchDTOList.add(this.toSearchDTO(faqItem));
        }
        return faqItemSearchDTOList;
    }

    public abstract FAQItemDTO toStandaloneDTO(final FAQItem FAQItem);

    public List<FAQItemDTO> toStandaloneDTOs(final List<FAQItem> FAQItemList) {
        List<FAQItemDTO> faqItemDTOList = new ArrayList<>();
        for (FAQItem faqItem : FAQItemList) {
            faqItemDTOList.add(this.toStandaloneDTO(faqItem));
        }
        return faqItemDTOList;
    }
}
