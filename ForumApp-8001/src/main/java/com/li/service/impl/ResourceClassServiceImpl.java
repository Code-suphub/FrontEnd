package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.pojo.ForumResource;
import com.li.entity.pojo.ResourceClass;
import com.li.entity.vo.ResourceClassPageVO;
import com.li.mapper.ResourceClassMapper;
import com.li.mapper.ResourceMapper;
import com.li.service.ResourceClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceClassServiceImpl extends ServiceImpl<ResourceClassMapper, ResourceClass>
    implements ResourceClassService {

  @Resource private ResourceClassMapper resourceClassMapper;

  @Resource
  private ResourceMapper resourceMapper;

  @Override
  public ResourceClassPageVO GetList(Integer page, Integer limit) {
    Page<ResourceClass> ResourceClassPage = new Page<>(page, limit);
    ResourceClassPageVO classPageVO = new ResourceClassPageVO();
    List<ResourceClass> result = new ArrayList<>();

    QueryWrapper<ResourceClass> wrapper = new QueryWrapper<ResourceClass>();
    wrapper.orderByDesc("id");

    Page<ResourceClass> resultPage =
        this.resourceClassMapper.selectPage(ResourceClassPage, wrapper);
    List<ResourceClass> resourceClasses = resultPage.getRecords();
    for (ResourceClass resourceClasse : resourceClasses) {
      Integer id = resourceClasse.getId();
      QueryWrapper<ForumResource> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("sort_class", id);
      Integer count = resourceMapper.selectCount(queryWrapper);
      resourceClasse.setNum(count);
      result.add(resourceClasse);
    }

    long total = resultPage.getTotal();
    classPageVO.setData(result);
    classPageVO.setTotal(total);
    return classPageVO;
  }
}
