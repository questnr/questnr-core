package com.questnr.model.repositories;

import com.questnr.common.enums.AuthorityName;
import com.questnr.model.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

  Authority findByAuthId(long authId);

  Authority findByName(AuthorityName name);
}
