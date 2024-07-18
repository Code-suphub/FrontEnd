package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.pojo.ArticleClass;
import com.li.entity.pojo.SquareClass;
import com.li.entity.vo.ClassNameVO;
import com.li.mapper.ArticleClassMapper;
import com.li.mapper.SquareClassMapper;
import com.li.service.ArticleClassService;
import com.li.service.SquareClassService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@io.swagger.annotations.Api(tags = "后台圈子分类管理接口")
@RestController
@RequestMapping("/squareClass")
public class SquareClassController {

  @Resource
  private ArticleClassService articleClassService;

  @Resource private ArticleClassMapper articleClassMapper;

  @Resource private SquareClassService squareClassService;

  @Resource private SquareClassMapper squareClassMapper;

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "新建分类")
  @ApiImplicitParam(name = "articleClass", value = "文章分类对象", required = true)
  @PostMapping("/newSquareClass")
  public int newSquareClass(@RequestBody SquareClass squareClass) {
    // 默认不顶置
    squareClass.setTop(false);
    QueryWrapper<SquareClass> wrapper = new QueryWrapper<SquareClass>();
    wrapper.eq("name", squareClass.getName());
    SquareClass userjudje = squareClassService.getOne(wrapper);
    if (userjudje != null) {
      // 该分类已存在
      return 143;
    }
    return this.squareClassMapper.insert(squareClass);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "获取圈子分类列表(分页)")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "页数", required = true),
    @ApiImplicitParam(name = "limit", value = "总量", required = true)
  })
  @GetMapping("/allSquareClass/{page}/{limit}")
  public Result allSquareClass(
      @PathVariable("page") Integer page, @PathVariable("limit") Integer limit) {
    return Result.success(squareClassService.GetList(page, limit));
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "获取全部分类列表")
  @GetMapping("/getAllClassName")
  public Result getAllClassName() {
    List<ClassNameVO> result = new ArrayList<>();

    QueryWrapper<SquareClass> wrapper = new QueryWrapper<SquareClass>();
    wrapper.select("name", "id");
    ClassNameVO classNameVO = null;
    List<SquareClass> resourceClasses = squareClassMapper.selectList(wrapper);
    for (SquareClass squareClass : resourceClasses) {
      classNameVO = new ClassNameVO();
      BeanUtils.copyProperties(squareClass, classNameVO);
      result.add(classNameVO);
    }
    return Result.success(result);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id修改圈子分类")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @PostMapping("/ReviseSquareClassById/{id}")
  public Result ReviseSquareClassById(@RequestBody SquareClass squareClass) {
    return Result.success(squareClassService.updateById(squareClass));
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id删除圈子分类")
  @ApiImplicitParam(name = "id", value = "文章id", required = true)
  @GetMapping("/DelectSquareClassById/{id}")
  public boolean DelectSquareClassById(@PathVariable("id") Integer id) {
    // 根据id删除
    QueryWrapper<SquareClass> wrapper = new QueryWrapper<SquareClass>();
    wrapper.eq("id", id);
    squareClassMapper.delete(wrapper);
    return this.squareClassService.removeById(id);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id值查询对应的分类名称")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/getClassNameById/{id}")
  public String getClassNameById(@PathVariable("id") Integer id) {
    QueryWrapper<ArticleClass> wrapper = new QueryWrapper<ArticleClass>();
    wrapper.eq("id", id);

    ArticleClass articleClass = articleClassMapper.selectOne(wrapper);
    return articleClass.getName();
  }

  @RequiresAuthentication  //需要登陆认证的接口
  @ApiOperation(value = "根据id值对广场类进行更新")
  @ApiImplicitParam(name = "广场", value = "广场参数", required = true)
  @PostMapping("/updatePlantClass")
  public Result updatePlantClass(@RequestBody SquareClass squareClass) {
    squareClassService.updateById(squareClass);
    return Result.success(null);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "顶置分类")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/topSquareClass/{id}")
  public Result topSquareClass(@PathVariable("id") Integer id) {
    SquareClass squareClass = squareClassService.getById(id);
    // 设置为这个字段在数据库里面相反的
    squareClass.setTop(!squareClass.getTop());
    return Result.success(squareClassService.updateById(squareClass));
  }
}
