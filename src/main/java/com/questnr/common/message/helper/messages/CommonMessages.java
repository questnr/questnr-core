package com.questnr.common.message.helper.messages;

import com.questnr.common.message.helper.Message;
import com.questnr.common.message.helper.MessageType;

public class CommonMessages {
    public final static Message C100 = new Message(
            "You don't have access for the particular operation",
            MessageType.Error);

    public final static Message C101 = new Message(
            "Error occurred. Please, try again!",
            MessageType.Error);
}
