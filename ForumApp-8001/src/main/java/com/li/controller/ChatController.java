package com.li.controller;

import com.li.entity.Result;
import com.li.entity.pojo.ChatMessages;
import com.li.service.ChatMessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@io.swagger.annotations.Api(tags = "Web信息接口")
@RestController
@RequestMapping("/chat")
public class ChatController {
  @Resource private ChatMessageService chatMessagesService;

  @ApiOperation(value = "获取聊天记录")
  @GetMapping("/getChatMessages/{sender}/{receiver}")
  public Result getChatMessages(
      @PathVariable("sender") Integer sender, @PathVariable("receiver") Integer receiver) {
    return chatMessagesService.getChatMessages(sender, receiver);
  }

  @ApiOperation(value = "发送聊天消息")
  @PostMapping("/sendChatMessage")
  public Result sendChatMessage(@RequestBody ChatMessages chatMessages) {
    return chatMessagesService.sendChatMessage(chatMessages);
  }

  @ApiOperation(value = "获取聊天好友列表")
  @GetMapping("/getMessageList/{userid}")
  public Result getMessageList(@PathVariable("userid") Integer userid) {
    return chatMessagesService.getMessageList(userid);
  }
}
