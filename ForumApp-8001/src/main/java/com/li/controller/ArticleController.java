package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.enumE.CheckSet;
import com.li.entity.pojo.Article;
import com.li.entity.pojo.ArticleClass;
import com.li.entity.pojo.ArticleComment;
import com.li.entity.pojo.User;
import com.li.entity.vo.ArticleVO;
import com.li.entity.vo.ResultListVO;
import com.li.entity.vo.ResultPageListVO;
import com.li.mapper.ArticleClassMapper;
import com.li.mapper.ArticleCommentMapper;
import com.li.mapper.UserMapper;
import com.li.service.ArticleClassService;
import com.li.service.ArticleCommentService;
import com.li.service.ArticleService;
import com.li.service.UserService;
import com.li.util.MathUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@io.swagger.annotations.Api(tags = "后台文章管理接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

  @Resource
  private ArticleService articleService;

  @Resource
  private ArticleClassMapper articleClassMapper;

  @Resource
  private ArticleCommentMapper articleCommentMapper;

  @Resource
  private UserMapper userMapper;

  @Resource
  private ArticleCommentService articleCommentService;
  @Resource private ArticleClassService articleClassService;
  @Resource
  private UserService userService;

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "新增文章")
  @ApiImplicitParam(name = "article", value = "文章", required = true)
  @PostMapping("/create")
  public Result add(@RequestBody Article article) throws ParseException {
    // 生成随机数注入
    int number = MathUtils.randomDigitNumber(7);
    article.setArticleStatus(number);
    // 查询分类名称对应的id值
    QueryWrapper<Article> wrapper = new QueryWrapper<>();
    wrapper.eq("title", article.getTitle());
    Article articleDB = articleService.getOne(wrapper);
    if (articleDB == null) {
      articleService.save(article);
    } else {
      articleService.update(article, wrapper);
    }
    return Result.success(article.getId());
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id删除文章")
  @ApiImplicitParam(name = "id", value = "文章id", required = true)
  @GetMapping("/delete/{id}")
  public Result DeleteArticleById(@PathVariable("id") Integer id) {
    // 根据文章id删除评论
    QueryWrapper<ArticleComment> wrapper = new QueryWrapper<ArticleComment>();
    wrapper.eq("article_id", id);
    articleCommentMapper.delete(wrapper);
    return Result.success(this.articleService.removeById(id));
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "批量删除文章")
  @ApiImplicitParam(name = "arr", value = "文章id", required = true)
  @PostMapping("/DeleteArticleBatch")
  public Result DeleteArticleBatch(@RequestBody int[] arr) {
    System.out.println(arr);
    List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
    articleService.removeByIds(list);

    // 根据文章id删除评论
    QueryWrapper<ArticleComment> wrapper = new QueryWrapper<ArticleComment>();
    wrapper.in("article_id", list);
    articleCommentMapper.delete(wrapper);

    return Result.success(null);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "根据id修改文章")
  @ApiImplicitParam(name = "id", value = "文章id", required = true)
  @PostMapping("/update/{id}")
  public Result ReviseArticleById(@RequestBody Article article) {
    return Result.success(articleService.updateById(article));
  }

  @ApiOperation(value = "根据id获取文章")
  @ApiImplicitParam(name = "id", value = "文章id", required = true)
  @GetMapping("/get/{id}")
  public Result getArticleById(@PathVariable("id") Integer id) {
    Article article = articleService.getById(id);
    ArticleVO articleVO = new ArticleVO();
    BeanUtils.copyProperties(article, articleVO);
    articleVO.setAuthorName(userService.getById(article.getAuthorId()).getUsername());
    return Result.success(articleVO);
  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "获取全部文章(分页)")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "页数", required = true),
    @ApiImplicitParam(name = "limit", value = "总量", required = true)
  })
  @GetMapping("/getAllArticle/{page}/{limit}")
  public Result getAllArticle(
      @PathVariable("page") Integer page, @PathVariable("limit") Integer limit) {
    return this.articleService.getArticlePage(page, limit, 0);
  }

//  @RequiresAuthentication // 需要登陆认证的接口
//  @ApiOperation(value = "根据name获取文章")
//  @ApiImplicitParam(name = "id", value = "文章name", required = true)
//  @GetMapping("/getArticleByName/{name}/{page}/{limit}")
//  public Result getArticleByName(
//      @PathVariable("name") String name,
//      @PathVariable("page") Integer page,
//      @PathVariable("limit") Integer limit) {
//    QueryWrapper<Article> wrapper = new QueryWrapper<>();
//    ArticlePageVO articlePageVO = new ArticlePageVO();
//    wrapper.like("title", name);
//    Page<Article> articlePage = new Page<>(page, limit);
//
//    Page<Article> resultPage = this.articleService.page(articlePage, wrapper);
//    List<ArticleVO> articleVOList = ArticleToArticleVo(resultPage.getRecords());
//
//    articlePageVO.setData(articleVOList);
//    articlePageVO.setTotal(articlePage.getTotal());
//    articlePageVO.setPages(articlePageVO.getPages());
//
//    return Result.success(articlePageVO);
//  }

  @RequiresAuthentication // 需要登陆认证的接口
  @ApiOperation(value = "获取全部文章名称")
  @GetMapping("/getAllArticleName")
  public Result getAllArticleName() {
    // 拼装一个id还有一个name对应一个值的json
    List<Article> articleList = articleService.list();
    List<ArticleVO> result = ArticleToArticleVo(articleList);
    return Result.success(result);
  }

  public List<ArticleVO> ArticleToArticleVo(List<Article> articleList) {
    List<ArticleVO> result = new ArrayList<>();

    for (Article article : articleList) {

      ArticleVO articleVO = null;
      // 根据作者名称查询对应的头像地址
      Integer authorId = article.getAuthorId();
      User users = userMapper.searchId(authorId);
      String profile = users.getAvatar();
      articleVO = new ArticleVO();
      articleVO.setProfile(profile);
      // 根据文章id获取对应的评论数
      Integer aid = article.getId();
      int acnum = articleCommentService.GetCommentNumByArticleId(aid);
      articleVO.setCommentNum(acnum);
      // 获取对应分类
      String sortClass = String.valueOf(article.getSortClass());
      ArticleClass articleClass = articleClassMapper.selectById(sortClass);
      String classname = articleClass.getName();
      articleVO.setClassName(classname);

      BeanUtils.copyProperties(article, articleVO);
      result.add(articleVO);
    }

    return result;
  }

  @ApiOperation(value = "根据author获取文章")
  @GetMapping("/getAllArticleByAuthor/{author}")
  public Result getAllArticleByAuthor(@PathVariable("author") Integer authorId) {
    return articleService.getAllArticleByAuthor(authorId);
  }

  @ApiOperation(value = "根据类别获取获取全部文章列表(分页)")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "page", value = "页数", required = true),
          @ApiImplicitParam(name = "limit", value = "总量", required = true),
          @ApiImplicitParam(name = "click", value = "类别", required = true)
  })
  @GetMapping("/getAllArticle/{page}/{limit}/{click}")
  public Result getAllArticle(
          @PathVariable("page") Integer page,
          @PathVariable("limit") Integer limit,
          @PathVariable("click") Integer click) {
    return this.articleService.getArticlePage(page, limit, click);
  }

  @ApiOperation(value = "文章排序获取")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "limit", value = "数量", required = true),
          @ApiImplicitParam(name = "condition", value = "条件", required = true)
  })
  @GetMapping("/getArticleSortByCondition/{limit}/{condition}")
  public Result getArticleSortByCondition(
          @PathVariable("limit") Integer limit, @PathVariable("condition") String condition) {
    List<ArticleVO> result = new ArrayList<>();

    List<Article> articles = articleService.getArticleSortBy(limit,condition);
    for (Article article : articles) {
      ArticleVO articleVO = new ArticleVO();
      BeanUtils.copyProperties(article, articleVO);
      // 拼接额外数据
      ArticleClass articleClass = articleClassService.getArticleClassById(article.getSortClass());
      articleVO.setClassName(articleClass.getName());
      articleVO.setCommentNum(articleCommentService.GetCommentNumByArticleId(articleVO.getId()));
      articleVO.setAuthorName((userService.getById(articleVO.getAuthorId())).getNickName());
      result.add(articleVO);
    }
    return Result.ok(new ResultListVO<>(result,(long)result.size()));
  }

  @ApiOperation(value = "获取所有文章数量")
  @GetMapping("/getAllArticleNumber")
  public Result getAllArticleNumber() {
    return articleService.countAllPublish();
  }

  @ApiOperation(value = "点赞或浏览文章量+1")
  @ApiImplicitParam(name = "id", value = "文章id", required = true)
  @GetMapping("/articles/{id}/{action}")
  public Result articlesLoveBrowse(@PathVariable("id") Integer id, @PathVariable("action") String action) {
    if (!CheckSet.ArticleActionSet.contains(action)){// 因为列名通过动态绑定，需要进行验证防止sql注入
      return Result.fail("不可执行的操作");
    }
    return articleService.updateArticleByAction(id,action);
//    if (){
//      return Result.success((action == "hits"? "浏览":"点赞")+ "成功");
//    }
//    return Result.fail("点赞失败，请重试");
  }

  @ApiOperation(value = "模糊查询文章(分页)")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "title", value = "内容", required = true),
          @ApiImplicitParam(name = "page", value = "页数", required = true),
          @ApiImplicitParam(name = "limit", value = "总量", required = true)
  })
  @GetMapping("/findAllArticle")
  public Result findAllArticle(
          @RequestParam(value = "title",defaultValue = "") String title,
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
    return this.articleService.getArticleWithLike(page, limit, title);
  }

  @ApiOperation(value = "根据作者username查询对应头像")
  @ApiImplicitParam(name = "username", value = "作者名称", required = true)
  @GetMapping("/findAvatarByName/{username}")
  public Result findAvatarByName(@PathVariable("username") String username) {
    return Result.success(userService.getUserInfoByName(username).getAvatar());
  }

  @ApiOperation(value = "获取文章上一页(标题)")
  @GetMapping("/getPreNewsArticle/{id}")
  public Result getPreNewsArticle(@PathVariable("id") Integer id) {
    ArticleVO article = articleService.getAdjacentArticleById(id,true);
    if (article == null) {
      return Result.fail("没有上一页");
    }
    return Result.success(article);
  }

  @ApiOperation(value = "获取文章下一页(标题)")
  @GetMapping("/getLastNewsArticle/{id}")
  public Result getLastNewsArticle(@PathVariable("id") Integer id) {
    ArticleVO article = articleService.getAdjacentArticleById(id,false);
    if (article == null) {
      return Result.fail("没有下一页");
    }
    return Result.success(article);
  }
}
