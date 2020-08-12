package com.questnr.services;

import com.questnr.model.entities.EntityTag;
import com.questnr.model.repositories.EntityTagRepository;
import com.questnr.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityTagService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EntityTagRepository entityTagRepository;

    public EntityTag saveEntityTag(String tagValue) {
        return this.saveEntityTag(new EntityTag(tagValue));
    }

    public EntityTag saveEntityTag(EntityTag entityTag) {
        try {
            return entityTagRepository.save(entityTag);
        } catch (Exception e) {
            LOGGER.error(EntityTagService.class.getName() + ": Error in saving EntityTag");
        }
        return null;
    }
}
