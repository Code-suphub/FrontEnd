package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.pojo.ForumResource;
import com.li.entity.pojo.ResourceClass;
import com.li.entity.pojo.User;
import com.li.entity.vo.ResourcePageVO;
import com.li.entity.vo.ResourceVO;
import com.li.mapper.ResourceClassMapper;
import com.li.mapper.ResourceMapper;
import com.li.mapper.UserMapper;
import com.li.service.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ForumResource>
    implements ResourceService {

  @Resource
  private ResourceMapper resourceMapper;

  @Resource
  private UserMapper userMapper;

  @Resource
  private ResourceClassMapper resourceClassMapper;

  @Override
  public ResourcePageVO FindVoList(Integer page, Integer limit, String content) {
    List<ResourceVO> result = new ArrayList<>();

    ResourceVO resourceVO = null;

    Page<ForumResource> resourcePage = new Page<>(page, limit);

    QueryWrapper<ForumResource> wrapper = new QueryWrapper<ForumResource>();
    wrapper.orderByDesc("id").like("title", content);

    Page<ForumResource> resultPage = this.resourceMapper.selectPage(resourcePage, wrapper);

    List<ForumResource> forumResources = resultPage.getRecords();
    long total = resultPage.getTotal();
    for (ForumResource forumResource : forumResources) {

      // 根据作者名称查询对应的头像地址
      Integer authorId = forumResource.getAuthorId();
      User users = userMapper.searchId(authorId);
      String profile = users.getAvatar();
      resourceVO = new ResourceVO();
      resourceVO.setProfile(profile);

      BeanUtils.copyProperties(forumResource, resourceVO);
      result.add(resourceVO);
    }
    ResourcePageVO resourcePageVO = new ResourcePageVO();
    resourcePageVO.setData(result);
    resourcePageVO.setTotal(total);
    return resourcePageVO;
  }

  @Override
  public ResourcePageVO VoList(Integer page, Integer limit) {
    List<ResourceVO> result = new ArrayList<>();

    ResourceVO resourceVO = null;

    Page<ForumResource> resourcePage = new Page<>(page, limit);

    QueryWrapper<ForumResource> wrapper = new QueryWrapper<ForumResource>();
    wrapper.orderByDesc("id");

    Page<ForumResource> resultPage = this.resourceMapper.selectPage(resourcePage, wrapper);

    List<ForumResource> forumResources = resultPage.getRecords();

    for (ForumResource forumResource : forumResources) {
      // 根据作者id查询对应的头像地址
      Integer authorId = forumResource.getAuthorId();
      QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
      userQueryWrapper.eq("user_id", authorId);
      User user = userMapper.selectOne(userQueryWrapper);
      String profile = user.getAvatar();

      resourceVO = new ResourceVO();
      resourceVO.setAuthorThumb(profile);
      //设置用户名称
      String author = user.getUsername();
      resourceVO.setAuthor(author);
      // 获取对应分类
      String sortClass = String.valueOf(forumResource.getSortClass());
      ResourceClass resourceClass = resourceClassMapper.selectById(sortClass);
      String classname = resourceClass.getName();
      resourceVO.setClassName(classname);


      BeanUtils.copyProperties(forumResource, resourceVO);
      result.add(resourceVO);
    }
    ResourcePageVO resourcePageVO = new ResourcePageVO();
    resourcePageVO.setData(result);
    resourcePageVO.setTotal(resultPage.getTotal());
    return resourcePageVO;
  }

  @Override
  public ResourcePageVO VoListFilter(Integer page, Integer limit, Integer rclass, String filter) {
    List<ResourceVO> result = new ArrayList<>();

    ResourceVO resourceVO = null;

    Page<ForumResource> resourcePage = new Page<>(page, limit);

    QueryWrapper<ForumResource> wrapper = new QueryWrapper<ForumResource>();
    if (filter.equals("news")) {
      wrapper.orderByDesc("id");
    }
    if (filter.equals("love")) {
      wrapper.orderByDesc("love_num");
    }
    if (filter.equals("recommend")) {
      wrapper.orderByDesc("owner_tag");
    }
    if (filter.equals("download")) {
      wrapper.orderByDesc("hits");
    }
    if (filter.equals("discuss")) {
      wrapper.orderByDesc("post_num");
    }
    if (rclass != 0) {
      wrapper.eq("sort_class", rclass);
    }

    Page<ForumResource> resultPage = this.resourceMapper.selectPage(resourcePage, wrapper);

    List<ForumResource> forumResources = resultPage.getRecords();

    for (ForumResource forumResource : forumResources) {
      Integer authorId = forumResource.getAuthorId();
      QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
      userQueryWrapper.eq("user_id", authorId);
      User user = userMapper.selectOne(userQueryWrapper);
      String profile = user.getAvatar();
      resourceVO = new ResourceVO();
      resourceVO.setAuthorThumb(profile);
      BeanUtils.copyProperties(forumResource, resourceVO);
      result.add(resourceVO);
    }
    ResourcePageVO resourcePageVO = new ResourcePageVO();
    resourcePageVO.setData(result);
    resourcePageVO.setTotal(resultPage.getTotal());
    return resourcePageVO;
  }

  @Override
  public List<ResourceVO> ClassVoList(Integer id) {
    QueryWrapper<ForumResource> wrapper = new QueryWrapper<>();
    wrapper.like("sort_class", id);
    List<ForumResource> forumResources = resourceMapper.selectList(wrapper);

    ResourceVO resourceVO = null;

    List<ResourceVO> result = new ArrayList<>();

    for (ForumResource forumResource : forumResources) {
      resourceVO = new ResourceVO();
      BeanUtils.copyProperties(forumResource, resourceVO);
      result.add(resourceVO);
    }
    return result;
  }

  @Override
  public List<ResourceVO> GetNewResource(Integer num, String filter) {
    List<ResourceVO> result = new ArrayList<>();

    QueryWrapper<ForumResource> wrapper = new QueryWrapper<>();
    // wrapper限制查询数量
    wrapper.last("limit " + num);

    if (filter.equals("new")) { // 按照最新发布排序
      wrapper.orderByDesc("id");
    }
    if (filter.equals("download")) { // 按照点击量排序
      wrapper.orderByDesc("hits");
    }
    if (filter.equals("recommend")) { // 按照
      wrapper.orderByDesc("owner_tag");
    }
    if (filter.equals("discuss")) { // 按照回帖数量排序
      wrapper.orderByDesc("post_num");
    }

    List<ForumResource> forumResources = resourceMapper.selectList(wrapper);
    for (ForumResource forumResource : forumResources) {
      Integer authorId = forumResource.getAuthorId();
      QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
      userQueryWrapper.eq("user_id", authorId);
      User user = userMapper.selectOne(userQueryWrapper);
      String profile = user.getAvatar();
      ResourceVO resourceVO = new ResourceVO();
      resourceVO.setAuthorThumb(profile);
      BeanUtils.copyProperties(forumResource, resourceVO);
      result.add(resourceVO);
    }

    return result;
  }
}
