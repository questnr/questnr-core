package com.questnr.model.repositories;

import com.questnr.common.enums.PostType;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostActionTrendLinearData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostActionTrendLinearDataRepository extends JpaRepository<PostActionTrendLinearData, Long> {
    Page<PostActionTrendLinearData> findAll(Pageable pageable);

    PostActionTrendLinearData findByPostAction(PostAction postAction);

//    @Query("select pld from PostActionTrendLinearData pld join PostAction pa on pa=pld.postAction where p")
//    Page<PostActionTrendLinearData> findAllByPostPollQuestion(PostAction postAction, Pageable pageable);
Page<PostActionTrendLinearData> findAllByPostType(PostType postType, Pageable pageable);
}
