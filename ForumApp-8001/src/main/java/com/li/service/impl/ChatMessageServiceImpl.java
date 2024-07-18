package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.pojo.ChatMessages;
import com.li.entity.pojo.User;
import com.li.entity.vo.ChatMessagesVO;
import com.li.entity.vo.ResultListVO;
import com.li.entity.vo.UserVO;
import com.li.mapper.ChatMessagesMapper;
import com.li.service.ChatMessageService;
import com.li.service.UserService;
import com.li.util.DatetimeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Resource
    ChatMessagesMapper chatMessagesMapper;
    @Resource
    UserService userService;

    @Override
    public Result getChatMessages(Integer sender, Integer receiver) {
        QueryWrapper<ChatMessages> wrapper = new QueryWrapper<>();
        wrapper
                .and(i -> i.eq("from_user_id", sender).eq("to_user_id", receiver))
                .or(j -> j.eq("from_user_id", receiver).eq("to_user_id", sender));
        ArrayList<ChatMessages> res = new ArrayList<>(chatMessagesMapper.selectList(wrapper));
        ArrayList<ChatMessagesVO> result = new ArrayList<>();
        for(ChatMessages chatMessages : res){
            ChatMessagesVO chatMessagesVO = new ChatMessagesVO();
            BeanUtils.copyProperties(chatMessages,chatMessagesVO);
            chatMessagesVO.setTime(DatetimeUtils.timestampToDate(chatMessages.getCreateTime().getTime()));
            result.add(chatMessagesVO);
        }
        return Result.success(new ResultListVO<>(result,(long)result.size()));
    }

    @Override
    public Result sendChatMessage(ChatMessages chatMessage) {
        if(chatMessagesMapper.insert(chatMessage)==1){
            return Result.ok();
        }else {
            return Result.fail("发送失败, 请重试");
        }
    }

    @Override
    public Result getMessageList(Integer userid) {
        ArrayList<UserVO> result = new ArrayList<>();

        // 获取所有发送给自己的消息
        QueryWrapper<ChatMessages> wrapper = new QueryWrapper<>();
        wrapper.and(i -> i.eq("to_user_id", userid)).or(j -> j.eq("from_user_id",userid));
        List<ChatMessages> chatMessages = chatMessagesMapper.selectList(wrapper);
        HashSet<Integer> set = new HashSet<>();

        // 添加非本人的 id 信息
        for (ChatMessages chatMessage : chatMessages) {
            if (Objects.equals(chatMessage.getToUserId(), userid)){
                set.add(chatMessage.getFromUserId());
            }
            if (Objects.equals(chatMessage.getFromUserId(), userid)){
                set.add(chatMessage.getToUserId());
            }
        }
        for (Integer id : set){
            // 获取每一位 "好友" 的名称和头像 id 信息
            User user = userService.getById(id);
            if(user == null){
                continue;
            }
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            result.add(userVO);
        }
        return Result.ok(new ResultListVO<>(result,(long)result.size()));
    }
}
