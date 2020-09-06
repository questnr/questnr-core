package com.questnr.model.mapper;

import com.questnr.model.dto.faq.FAQItemDTO;
import com.questnr.model.dto.faq.FAQItemPageDTO;
import com.questnr.model.entities.FAQItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class FAQItemMapper {
    public abstract FAQItemDTO toDTO(final FAQItem FAQItem);

    public List<FAQItemDTO> toDTOs(final List<FAQItem> FAQItemList){
        List<FAQItemDTO> faqItemDTOList = new ArrayList<>();
        for(FAQItem faqItem: FAQItemList){
            faqItemDTOList.add(this.toDTO(faqItem));
        }
        return faqItemDTOList;
    }

    public abstract FAQItemPageDTO toPageDTO(final FAQItem FAQItem);

    public List<FAQItemPageDTO> toPageDTOs(final List<FAQItem> FAQItemList){
        List<FAQItemPageDTO> faqItemPageDTOList = new ArrayList<>();
        for(FAQItem faqItem: FAQItemList){
            faqItemPageDTOList.add(this.toPageDTO(faqItem));
        }
        return faqItemPageDTOList;
    }
}
