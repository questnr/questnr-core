package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    boolean existsByCommunityAndUser(Community community, User user);

    Page<CommunityUser> findAllByCommunityOrderByCreatedAtDesc(Community community, Pageable pageable);

    @Query("select cu from CommunityUser cu where " +
            " cu.user.username like :userString% " +
            " or cu.user.firstName like :userString% " +
            " or cu.user.lastName like :userString%  and cu.community=:community")
    Page<CommunityUser> findAllByUserContainingString(Community community, @Param("userString") String userString, Pageable pageable);

    Page<CommunityUser> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<CommunityUser> findAllByCommunityAndCreatedAtBetween(Community community, Date startingDate, Date endingDate);

    CommunityUser findByCommunityAndUser(Community community, User user);

    int countByUser(User user);

    int countByCommunity(Community community);
}
