package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.pojo.ForumResource;
import com.li.entity.pojo.ResourceComment;
import com.li.entity.pojo.User;
import com.li.entity.vo.ResourceCommentPageVO;
import com.li.entity.vo.ResourceCommentVO;
import com.li.mapper.ResourceCommentMapper;
import com.li.mapper.ResourceMapper;
import com.li.mapper.UserMapper;
import com.li.service.ResourceCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现类
 *
 * @author admin
 * @since 2022-03-28
 */
@Service
public class ResourceCommentServiceImpl extends ServiceImpl<ResourceCommentMapper, ResourceComment>
    implements ResourceCommentService {

  @Resource
  private ResourceCommentMapper resourceCommentMapper;

  @Resource private UserMapper userMapper;
  
  @Resource private ResourceMapper resourceMapper;

  @Override
  public int GetCommentNum(Integer resourceId) {
    QueryWrapper<ResourceComment> wrapper = new QueryWrapper<>();
    wrapper.eq("resource_id", resourceId);
    return resourceCommentMapper.selectCount(wrapper);
  }

  @Override
  public ResourceCommentPageVO VoList(Integer page, Integer limit, Integer click) {
    List<ResourceCommentVO> result = new ArrayList<>();

    ResourceCommentVO resourceCommentVO = null;

    Page<ResourceComment> resourceCommentPage = new Page<>(page, limit);

    QueryWrapper<ResourceComment> wrapper = new QueryWrapper<ResourceComment>();
    wrapper.orderByDesc("id");

    Page<ResourceComment> resultPage = this.resourceCommentMapper.selectPage(resourceCommentPage, wrapper);

    List<ResourceComment> resourceComments = resultPage.getRecords();
    for (ResourceComment resourceComment : resourceComments) {

      // 根据作者id查询对应的头像地址
      Integer author = resourceComment.getUserId();
      User users = userMapper.searchId(author);
      String profile = users.getAvatar();
      resourceCommentVO = new ResourceCommentVO();
      // 根据作者id查询对应的名称
      String name = users.getNickName();
      // 获取对应文章名称
      Integer articleId = resourceComment.getResourceId();
      ForumResource article = resourceMapper.selectById(articleId);
      String articleName = article.getTitle();
      resourceCommentVO.setResourceName(articleName);

      BeanUtils.copyProperties(resourceComment, resourceCommentVO);
      resourceCommentVO.setUsername(name);

      resourceCommentVO.setProfile(profile);
      result.add(resourceCommentVO);
    }
    ResourceCommentPageVO resourceCommentPageVO = new ResourceCommentPageVO();
    resourceCommentPageVO.setData(result);
    resourceCommentPageVO.setTotal(resultPage.getTotal());
    resourceCommentPageVO.setPages(resultPage.getPages());
    return resourceCommentPageVO;
  }
}


