package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.pojo.ForumResource;
import com.li.entity.pojo.ResourceClass;
import com.li.mapper.ResourceClassMapper;
import com.li.mapper.ResourceMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@io.swagger.annotations.Api(tags = "Web资源分类接口")
@RestController
@RequestMapping("/WebResourceClass")
public class WebResourceClassController {

  @Autowired private ResourceClassMapper resourceClassMapper;

  @Autowired private ResourceMapper resourceMapper;

  @ApiOperation(value = "获取全部资源分类列表")
  @GetMapping("/getResourceClasslist")
  public List<ResourceClass> getResourceClasslist() {
    // 获取每个分类对应的资源数量
    List<ResourceClass> resourceClassList = resourceClassMapper.selectList(null);
    for (ResourceClass resourceClass : resourceClassList) {
      QueryWrapper<ForumResource> wrapper = new QueryWrapper<>();
      wrapper.eq("sort_class", resourceClass.getId());
      resourceClass.setNum(resourceMapper.selectCount(wrapper));
    }
    return resourceClassList;
  }

  @ApiOperation(value = "根据classid查询对应的资源分类名称")
  @ApiImplicitParam(name = "classId", value = "分类id", required = true)
  @GetMapping("/getResourceClassNameByid/{classId}")
  public String getResourceClassNameByid(@PathVariable Integer classId) {
    QueryWrapper<ResourceClass> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id", classId);
    ResourceClass resourceClass = resourceClassMapper.selectOne(queryWrapper);
    return resourceClass.getName();
  }
}
