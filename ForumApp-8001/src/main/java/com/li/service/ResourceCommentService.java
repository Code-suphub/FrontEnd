package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.pojo.ResourceComment;
import com.li.entity.vo.ResourceCommentPageVO;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceCommentService extends IService<ResourceComment> {
  int GetCommentNum(Integer resourceId);

  ResourceCommentPageVO VoList(Integer page, Integer limit, Integer click);

}
