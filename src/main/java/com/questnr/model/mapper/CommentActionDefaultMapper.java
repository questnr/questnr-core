package com.questnr.model.mapper;

import com.questnr.model.dto.CommentActionDTO;
import com.questnr.model.entities.CommentAction;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class CommentActionDefaultMapper {

    @Autowired
    CommentActionMapper commentActionMapper;

    CommentActionDefaultMapper() {
        commentActionMapper = Mappers.getMapper(CommentActionMapper.class);
    }

    List<CommentActionDTO> toDefaultCommentDTOList(Set<CommentAction> commentActionSet) {
        List<CommentAction> commentActionList = new ArrayList<>(commentActionSet);
        if (commentActionList.size() > 0) {
            Predicate<CommentAction> commentActionPredicate = commentAction -> !commentAction.isChildComment();
            List<CommentAction> filteredCommentActionList = commentActionList.stream().filter(commentActionPredicate).collect(Collectors.toList());

            Comparator<CommentAction> createdAtComparator
                    = Comparator.comparing(CommentAction::getCreatedAt);
            filteredCommentActionList.sort(createdAtComparator.reversed());
            return commentActionMapper.toDTOs(filteredCommentActionList.subList(0, filteredCommentActionList.size() > 3 ? 3 : filteredCommentActionList.size()));
        }
        return new ArrayList<>();
    }
}
