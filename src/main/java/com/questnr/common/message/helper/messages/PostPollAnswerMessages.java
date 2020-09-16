package com.questnr.common.message.helper.messages;

import com.questnr.common.message.helper.Message;
import com.questnr.common.message.helper.MessageType;

public class PostPollAnswerMessages {
    public final static Message PPA100 = new Message(
            "You don't have permission to answer this question",
            MessageType.Error);
    public final static Message PPA101 = new Message(
            "Sorry, you can not answer your question!",
            MessageType.Error);
    public final static Message PPA102 = new Message(
            "Already submitted the answer",
            MessageType.Error
    );
}
