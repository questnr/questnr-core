package com.questnr.services;

import org.springframework.stereotype.Service;

@Service
public class CommonService {
    public boolean isNull(String string) {
        return string == null || string.trim().isEmpty();
    }

}
