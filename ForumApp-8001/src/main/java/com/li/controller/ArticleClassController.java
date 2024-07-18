package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.pojo.Article;
import com.li.entity.pojo.ArticleClass;
import com.li.entity.vo.ClassNameVO;
import com.li.mapper.ArticleClassMapper;
import com.li.mapper.ArticleMapper;
import com.li.service.ArticleClassService;
import com.li.service.ArticleService;
import com.li.util.MathUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@io.swagger.annotations.Api(tags = "后台分类管理接口")
@RestController
@RequestMapping("/articleClass")
public class ArticleClassController {
  @Resource private ArticleClassService articleClassService;

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "新建文章分类")
  @ApiImplicitParam(name = "articleClass", value = "文章分类对象", required = true)
  @PostMapping("/new")
  public Result newArticleClass(@RequestBody ArticleClass articleClass) {
    if(articleClass==null){
      return Result.fail("参数错误");
    }
    return articleClassService.newArticleClass(articleClass);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "获取文章分类列表(分页)")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "页数", required = true),
    @ApiImplicitParam(name = "limit", value = "总量", required = true)
  })
  @GetMapping("/allArticleClass/{page}/{limit}")
  public Result allArticleClass(
      @PathVariable("page") Integer page, @PathVariable("limit") Integer limit) {
    return articleClassService.getArticleClassList(page, limit);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "删除文章分类")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/delete/{id}")
  public Result DeleteArticleClass(@PathVariable("id") Integer id) {
    return articleClassService.deleteArticleClass(id);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id修改文章分类")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @PostMapping("/update/{id}")
  public Result ReviseArticleClassById(@RequestBody ArticleClass articleClass) {
    return articleClassService.ReviseArticleClassById(articleClass);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id值查询对应的分类名称")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/get/{id}")
  public Result getClassNameById(@PathVariable("id") Integer id) {
    if (id==null || id<=0){
      return Result.fail("参数错误");
    }
    return articleClassService.getClassNameById(id);
  }

  // 顶置分类
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "顶置分类")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/topArticleClass/{id}")
  public Result topArticleClass(@PathVariable("id") Integer id) {
    if (id==null || id<=0){
      return Result.fail("参数错误");
    }
    return articleClassService.topArticleClass(id);
  }
  @ApiOperation(value = "根据分类id获取文章数量")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/getArticleNumByClass/{id}")
  public Result getArticleClassNum(@PathVariable("id") Integer id) {
    if (id==null || id<=0){
      return Result.fail("参数错误");
    }
    return articleClassService.getArticleClassNum(id);
  }

  @ApiOperation(value = "获取全部分类列表")
  @GetMapping("/getAll")
  public Result getArticleClassList() {
    return articleClassService.getArticleClassList();
  }
}
