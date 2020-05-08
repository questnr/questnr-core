package com.questnr.access;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.springframework.stereotype.Service;

@Service
public class PostActionAccessService {
    public boolean isUserOwnerOfPost(User user, PostAction postAction) {
        return user.equals(postAction.getUserActor());
    }
}
