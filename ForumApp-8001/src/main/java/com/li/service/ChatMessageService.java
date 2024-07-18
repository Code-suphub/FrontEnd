package com.li.service;

import com.li.entity.Result;
import com.li.entity.pojo.ChatMessages;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageService {
    Result getChatMessages(Integer sender, Integer receiver);

    Result sendChatMessage(ChatMessages chatMessages);
    Result getMessageList(Integer userid);
}
