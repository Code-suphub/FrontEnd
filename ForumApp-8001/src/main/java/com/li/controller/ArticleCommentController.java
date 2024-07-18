package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.pojo.Article;
import com.li.entity.pojo.ArticleComment;
import com.li.entity.vo.ArticleCommentVO;
import com.li.entity.vo.ResultPageListVO;
import com.li.mapper.ArticleCommentMapper;
import com.li.service.ArticleCommentService;
import com.li.service.ArticleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@io.swagger.annotations.Api(tags = "后台评论管理接口")
@RestController
@RequestMapping("/articleComment")
public class ArticleCommentController {

  @Autowired
  ArticleCommentMapper articleCommentMapper;

  @Autowired
  ArticleCommentService articleCommentService;
  @Resource
  private ModelMapper modelMapper;
  @Resource
  private ArticleService articleService;


  @ApiOperation(value = "获取全部评论(分页)")
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", value = "页数", required = true),
          @ApiImplicitParam(name = "limit", value = "总量", required = true)
  })
  @GetMapping("/getallArticleComments/{page}/{limit}")
  public Result getallArticleComments(
          @PathVariable("page") Integer page, @PathVariable("limit") Integer limit) {
    return Result.success(articleCommentService.VoList(page, limit, 0));
  }

  @ApiOperation(value = "增加评论")
  @RequiresAuthentication // 需要登陆认证的接口
  @PostMapping("/addArticleComment")
  @ApiImplicitParam(name = "articleComment", value = "文章分类对象", required = true)
  public Result addArticleComment(@RequestBody ArticleComment articleComment) {
    return Result.success(articleCommentMapper.insert(articleComment));
  }

  @ApiOperation(value = "查看文章对应评论数")
  @ApiImplicitParam(name = "articleId", value = "文章id", required = true)
  @GetMapping("/getArticleCommentNum/{articleId}")
  public Result getArticleCommentNum(@PathVariable("articleId") Integer articleId) {
    QueryWrapper<ArticleComment> wrapper = new QueryWrapper<>();
    wrapper.eq("article_id", articleId);
    return Result.success(articleCommentMapper.selectCount(wrapper));
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id删除评论")
  @ApiImplicitParam(name = "id", value = "评论id", required = true)
  @GetMapping("/DeleteArticleCommentById/{id}")
  public Result DeleteArticleCommentById(@PathVariable("id") Integer id) {
    // 根据文章id删除评论
    return Result.success(articleCommentMapper.deleteById(id));
  }
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id修改文章评论")
  @ApiImplicitParam(name = "id", value = "文章id", required = true)
  @PostMapping("/ReviseArticleCommentById/{id}")
  public Result ReviseArticleCommentById(@RequestBody ArticleComment articleComment) {
    return Result.success(articleCommentService.updateById(articleComment));
  }


  @ApiOperation(value = "根据文章id查询对应的评论")
  @ApiImplicitParam(name = "articleId", value = "文章id", required = true)
  @GetMapping("/getAllArticleComment/{articleId}")
  public Result getAllArticleComment(@PathVariable("articleId") Integer articleId) {
    return articleCommentService.getAllArticleComment(articleId);
  }

  @ApiOperation(value = "最新评论")
  @ApiImplicitParam(name = "num", value = "数量", required = true)
  @GetMapping("/getNewArticleComment/{limit}")
  public Result getNewArticleComment(@PathVariable("limit") Integer limit) {
    List<ArticleCommentVO> result = new ArrayList<>();
    List<ArticleComment> articleComments =
            articleCommentService.getCommentByDesc("add_time",1,limit);
    for (ArticleComment articleComment : articleComments) {
      ArticleCommentVO articleCommentVO = new ArticleCommentVO();
      // 根据文章id查询文章名称
      Article articleById = articleService.getArticleById(articleComment.getArticleId());
      if (articleById!=null) { // 防止数据库有脏数据，匹配不到文章的评论
//        articleCommentVO.setArticleName(articleById.getTitle());
      }
      modelMapper.map(articleComment,articleCommentVO);
      result.add(articleCommentVO);
    }
    return Result.success(new ResultPageListVO<>(result, 1L,(long)result.size()));
  }
}
