package com.totality.model.repositories;

import com.totality.common.enums.AuthorityName;
import com.totality.model.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

  Authority findByAuthId(long authId);

  Authority findByName(AuthorityName name);
}
