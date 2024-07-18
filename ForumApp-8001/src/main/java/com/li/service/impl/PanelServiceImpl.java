package com.li.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.pojo.Panel;
import com.li.mapper.ArticleCommentMapper;
import com.li.mapper.PanelMapper;
import com.li.mapper.UserMapper;
import com.li.service.PanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PanelServiceImpl extends ServiceImpl<PanelMapper, Panel> implements PanelService {
  @Resource
  private UserMapper userMapper;
  @Resource private ArticleCommentMapper commentMapper;

  @Override
  public Panel SearchPanelInfo() {
    Panel panel = new Panel();
    Integer userNum = userMapper.selectCount(null);
    Integer commentNum = commentMapper.selectCount(null);

    panel.setCommentNum(commentNum);
    panel.setCustomerNum(userNum);

    return panel;
  }
}
