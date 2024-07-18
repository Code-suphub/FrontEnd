package com.li.controller;

import com.li.entity.Result;
import com.li.entity.pojo.AllTag;
import com.li.mapper.AllTagMapper;
import com.li.service.TagService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@io.swagger.annotations.Api(tags = "后台标签接口")
@RestController
@RequestMapping("/tag")
public class AllTagController {

  @Resource
  private TagService tagService;

  @ApiOperation(value = "获取全部tag")
  @RequiresAuthentication // 需要登陆认证的接口
  @GetMapping("/getAllTag")
  public Result getAllTag() {
    return tagService.getAllTag();
  }

  @ApiOperation(value = "修改标签")
  @ApiImplicitParam(name = "allTag", value = "allTag", required = true)
  @RequiresAuthentication // 需要登陆认证的接口
  @PostMapping("/updateTag")
  public Result updateTag(@RequestBody AllTag allTag) {
    return tagService.updateTag(allTag);
  }

  @ApiOperation(value = "新增单个标签")
  @ApiImplicitParam(name = "allTag", value = "allTag", required = true)
  @RequiresAuthentication // 需要登陆认证的接口
  @PostMapping("/addTag")
  public Result addTag(@RequestBody AllTag tag) {
    return tagService.addTag(tag);
  }

  @ApiOperation(value = "删除单个标签")
  @ApiImplicitParam(name = "allTag", value = "allTag", required = true)
  @RequiresAuthentication // 需要登陆认证的接口
  @GetMapping("/deleteTag/{id}")
  public Result deleteTag(@PathVariable("id") Integer id) {
    return tagService.deleteTag(id);
  }

  @ApiOperation(value = "根据id获取tag名称")
  @GetMapping("/getTagByList/{id}")
  public Result getTagByList(@PathVariable("id") Integer id) {
    return tagService.selectById(id);
  }
}
