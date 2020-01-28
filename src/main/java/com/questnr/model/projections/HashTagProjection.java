package com.questnr.model.projections;

import com.questnr.model.entities.HashTag;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "hashTagProjection", types = HashTag.class)
public interface HashTagProjection {
    String getHashTagValue();
}
