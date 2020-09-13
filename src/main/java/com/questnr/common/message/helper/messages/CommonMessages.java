package com.questnr.common.message.helper.messages;

import com.questnr.common.message.helper.Message;
import com.questnr.common.message.helper.MessageType;

public class CommonMessages {
    public final static Message C100 = new Message(
            "You don't have access for the particular operation",
            MessageType.Error);
}
