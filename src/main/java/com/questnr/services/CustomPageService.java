package com.questnr.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomPageService<T> {

    public Page<T> customPage(List<T> list, Pageable pageable) {
        return new PageImpl<T>(list.subList(0, list.size() > pageable.getPageSize()
                ? pageable.getPageSize() :
                list.size()),
                pageable,
                list.size());
    }
}
