package com.questnr.common.message.helper.messages;

import com.questnr.common.message.helper.Message;
import com.questnr.common.message.helper.MessageType;

public class PostActionMessages {
    public final static Message PA100 = new Message(
            "Post not found!",
            MessageType.Error);
}
