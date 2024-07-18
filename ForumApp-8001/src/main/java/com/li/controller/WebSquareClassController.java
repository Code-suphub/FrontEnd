package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.pojo.Square;
import com.li.entity.pojo.SquareClass;
import com.li.mapper.SquareClassMapper;
import com.li.mapper.SquareMapper;
import com.li.service.SquareClassService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@io.swagger.annotations.Api(tags = "Web圈子分类接口")
@RestController
@RequestMapping("/webSquareClass")
public class WebSquareClassController {

  @Resource private SquareMapper squareMapper;

  @Resource private SquareClassMapper squareClassMapper;
  @Resource
  private SquareClassService squareClassService;

  @ApiOperation(value = "根据分类id获取文章数量")
  @ApiImplicitParam(name = "id", value = "id", required = true)
  @GetMapping("/getArticleClassNum/{id}")
  public int getArticleClassNum(@PathVariable("id") Integer id) {
    QueryWrapper<Square> wrapper = new QueryWrapper<>();
    wrapper.eq("sort_class", id);
    return squareMapper.selectCount(wrapper);
  }

  @ApiOperation(value = "根据分类id获取分类信息")
  @ApiImplicitParam(name = "classId", value = "classId", required = true)
  @GetMapping("/getArticleClassByOtherName/{classId}")
  public SquareClass getArticleClassByOtherName(@PathVariable("classId") Integer classId) {
    return squareClassService.getById(classId);
//    QueryWrapper<SquareClass> wrapper = new QueryWrapper<>();
//    wrapper.eq("id", classId);
//     squareClassMapper.selectOne(wrapper);
  }

  @ApiOperation(value = "获取全部分类列表")
  @GetMapping("/getSquareClassList")
  public List<SquareClass> getSquareClasslist() {
    return squareClassMapper.selectList(null);
  }
}
