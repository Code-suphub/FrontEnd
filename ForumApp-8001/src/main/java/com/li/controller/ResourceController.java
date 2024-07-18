package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.pojo.ForumResource;
import com.li.entity.pojo.ResourceClass;
import com.li.entity.pojo.ResourceComment;
import com.li.entity.pojo.User;
import com.li.entity.vo.ResourceVO;
import com.li.mapper.ResourceClassMapper;
import com.li.mapper.ResourceCommentMapper;
import com.li.mapper.UserMapper;
import com.li.service.ResourceCommentService;
import com.li.service.ResourceService;
import com.li.util.MathUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@io.swagger.annotations.Api(tags = "后台资源管理接口")
@RestController
@RequestMapping("/resource")
public class ResourceController {

  @Autowired private ResourceService resourceService;

  @Autowired private UserMapper userMapper;

  @Autowired private ResourceCommentService resourceCommentService;

  @Autowired private ResourceClassMapper resourceClassMapper;

  @Autowired private ResourceCommentMapper resourceCommentMapper;

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "新增资源")
  @ApiImplicitParam(name = "resource", value = "资源", required = true)
  @PostMapping("/create")
  public Result add(@RequestBody ForumResource resource) throws ParseException {
    // 生成随机数注入
    int number = MathUtils.randomDigitNumber(7);
    resource.setResourceStatus(number);
    // 设置资源状态为已发布
    resource.setStatus("published");
    // 查询分类名称对应的id值
    QueryWrapper<ForumResource> wrapper = new QueryWrapper<>();
    wrapper.eq("title", resource.getTitle());
    ForumResource resourceDB = resourceService.getOne(wrapper);
    if (resourceDB == null) {
      resourceService.save(resource);
    } else {
      resourceService.update(resource, wrapper);
    }
    return Result.success(resource.getId());
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "获取全部资源(分页)")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "页数", required = true),
    @ApiImplicitParam(name = "limit", value = "总量", required = true)
  })
  @GetMapping("/getAllResource/{page}/{limit}")
  public Result getAllResource(
      @PathVariable("page") Integer page, @PathVariable("limit") Integer limit) {
    return Result.success(resourceService.VoList(page, limit));
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id修改资源")
  @ApiImplicitParam(name = "id", value = "资源id", required = true)
  @PostMapping("/ReviseResourceById/{id}")
  public Result ReviseResourceById(@RequestBody ForumResource resource) {
    return Result.success(resourceService.updateById(resource));
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id删除资源")
  @ApiImplicitParam(name = "id", value = "资源id", required = true)
  @GetMapping("/DelectResourceById/{id}")
  public Result DelectResourceById(@PathVariable("id") Integer id) {
    // 根据资源id删除评论
    QueryWrapper<ResourceComment> wrapper = new QueryWrapper<ResourceComment>();
    wrapper.eq("resource_id", id);
    resourceCommentMapper.delete(wrapper);
    return Result.success(resourceService.removeById(id));
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id获取资源")
  @ApiImplicitParam(name = "id", value = "资源id", required = true)
  @GetMapping("/getResourceById/{id}")
  public Result getResourceById(@PathVariable("id") Integer id) {

    return Result.success(resourceService.getById(id));
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "获取资源名称")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "resourceName", value = "资源名称", required = true),
  })
  @GetMapping("/getResourceByName/{name}")
  public Result UpdateResourceClass(@PathVariable("name") String resourceName) {

    LambdaQueryWrapper<ForumResource> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(ForumResource::getTitle, resourceName);
    List<ForumResource> list = resourceService.list(wrapper);
    return Result.success(list);
  }
  
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "获取全部文章名称")
  @GetMapping("/getAllResourceName")
  public Result getAllResourceName() {
    // 拼装一个id还有一个name对应一个值的json
    List<ForumResource> resourceList = resourceService.list();
    List<ResourceVO> result = ResourceToResourceVo(resourceList);
    return Result.success(result);
  }
  
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id批量删除资源")
  @ApiImplicitParam(name = "arr", value = "资源id", required = true)
  @PostMapping("/DeletetResourceBatch")
  public Result DeletetResourceBatch(@RequestBody int[] arr) {

    List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());

    resourceService.removeByIds(list);

    return Result.success(null);
  }

  public List<ResourceVO> ResourceToResourceVo(List<ForumResource> resourceList) {
    List<ResourceVO> result = new ArrayList<>();

    for (ForumResource resource : resourceList) {

      ResourceVO resourceVO = null;
      // 根据作者名称查询对应的头像地址
      Integer authorId = resource.getAuthorId();
      User users = userMapper.searchId(authorId);
      String profile = users.getAvatar();
      resourceVO = new ResourceVO();
      resourceVO.setProfile(profile);
      // 根据文章id获取对应的评论数
      Integer aid = resource.getId();
      int acnum = resourceCommentService.GetCommentNum(aid);
      resourceVO.setCommentNum(acnum);
      // 获取对应分类
      String sortClass = String.valueOf(resource.getSortClass());
      ResourceClass resourceClass = resourceClassMapper.selectById(sortClass);
      String classname = resourceClass.getName();
      resourceVO.setClassName(classname);

      BeanUtils.copyProperties(resource, resourceVO);
      result.add(resourceVO);
    }

    return result;
  }
}

