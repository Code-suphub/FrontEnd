package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.pojo.ResourceClass;
import com.li.entity.vo.ResourceClassPageVO;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceClassService extends IService<ResourceClass> {

  ResourceClassPageVO GetList(Integer page, Integer limit);
}
