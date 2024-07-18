package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.pojo.ForumResource;
import com.li.entity.vo.ResourcePageVO;
import com.li.entity.vo.ResourceVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceService extends IService<ForumResource> {

  ResourcePageVO VoList(Integer page, Integer limit);

  ResourcePageVO VoListFilter(Integer page, Integer limit, Integer rclass, String filter);

  List<ResourceVO> ClassVoList(Integer id);

  List<ResourceVO> GetNewResource(Integer num, String filter);

  ResourcePageVO FindVoList(Integer page, Integer limit, String content);
}
