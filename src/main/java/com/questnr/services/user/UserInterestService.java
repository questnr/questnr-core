package com.questnr.services.user;

import com.questnr.model.dto.StaticInterestDTO;
import com.questnr.model.entities.StaticInterest;
import com.questnr.model.repositories.StaticInterestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInterestService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StaticInterestRepository staticInterestRepository;

    public List<StaticInterestDTO> searchUserInterest(String interestString) {
        Pageable pageable = PageRequest.of(0, 6);
        try {
            Page<StaticInterest> staticInterestPage = staticInterestRepository.findByInterestContaining(interestString, pageable);
            return staticInterestPage.stream().map(StaticInterestDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(UserInterestService.class.getName() + ": Error in saving EntityTag");
        }
        return null;
    }
}
