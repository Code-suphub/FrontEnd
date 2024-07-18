package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.vo.ResultListVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.enumE.ValueMapping;
import com.li.entity.pojo.Article;
import com.li.entity.pojo.ArticleClass;
import com.li.entity.pojo.User;
import com.li.entity.vo.ArticleVO;
import com.li.entity.vo.ResultPageListVO;
import com.li.mapper.ArticleClassMapper;
import com.li.mapper.ArticleMapper;
import com.li.mapper.UserMapper;
import com.li.service.ArticleCommentService;
import com.li.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

  @Resource
  private ArticleMapper articleMapper;

  @Resource private UserMapper userMapper;

  @Resource private ArticleCommentService articleCommentService;

  @Resource private ArticleClassMapper articleClassMapper;
  @Resource
  private ModelMapper modelMapper;
  @Resource
  private StringRedisTemplate stringRedisTemplate;

  @Override
  public Result getArticleWithLike(Integer page, Integer limit, String title) {
    List<ArticleVO> result = new ArrayList<>();

    Page<Article> articlePage = new Page<>(page, limit);

    QueryWrapper<Article> wrapper = new QueryWrapper<Article>();
    if (title.isEmpty()) { // 如果前端传入这个表示获取全部
      wrapper.orderByDesc("id");
    }else {
      wrapper.orderByDesc("id").like("title", title);
    }

    Page<Article> resultPage = this.articleMapper.selectPage(articlePage, wrapper);

    List<Article> articles = resultPage.getRecords();
    long total = resultPage.getTotal();
    for (Article article : articles) {

      // 根据作者名称查询对应的头像地址
      Integer authorId = article.getAuthorId();
      User users = userMapper.searchId(authorId);
      String profile = users.getAvatar();
      ArticleVO articleVO = new ArticleVO();
      articleVO.setProfile(profile);

      BeanUtils.copyProperties(article, articleVO);
      result.add(articleVO);
    }
    return Result.ok(new ResultPageListVO<>(result,(long)page,total));
  }

  @Override
  public Result getAllArticleByAuthor(Integer authorId) {
    QueryWrapper<Article> wrapper = new QueryWrapper<>();
    wrapper.select().eq("author_id", authorId);
    List<Article> articles = articleMapper.selectList(wrapper);
    return Result.ok(new ResultListVO<>(articles,(long)articles.size()));
  }

  @Override
  public ArticleVO getAdjacentArticleById(Integer id, boolean pre) {
    QueryWrapper<Article> wrapper = new QueryWrapper<>();
    if (pre){
      wrapper.lt("id",id).orderByDesc("id").last("limit 1");
    }else {
      wrapper.gt("id",id).orderByAsc("id").last("limit 1");
    }
    Article article = articleMapper.selectOne(wrapper);
    if (article==null){
      return null;
    }
    ArticleVO articleVO = new ArticleVO();
    modelMapper.map(article, articleVO);
    return articleVO;
  }

  @Override
  public Result countAllPublish() {
    QueryWrapper<Article> wrapper = new QueryWrapper<>();
    wrapper.select().eq("status", "published");
    return Result.ok(articleMapper.selectCount(wrapper));
  }

  @Override
  public Result updateArticleByAction(Integer id, String action) {
    // 1.获取登录用户
//    Long userId = UserHolder.getUser().getId();
    Long userId = 1L;
    // 2.判断当前登录用户是否已经点赞
    String key = ValueMapping.ACTION_2_KEY.get("article_"+action);
    Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
    if (score == null) {
      // 3.如果未点赞，可以点赞
      // 3.1.数据库点赞数 + 1
      boolean isSuccess = update().setSql(action+" = " + action+" + 1").eq("id", id).update();
      // 3.2.保存用户到Redis的set集合  zadd key value score
      if (isSuccess) {
        stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
      }
    } else {
      // 4.如果已点赞，取消点赞
      // 4.1.数据库点赞数 -1
      boolean isSuccess = update().setSql(action+" = " + action+" + 1").eq("id", id).update();
      // 4.2.把用户从Redis的set集合移除
      if (isSuccess) {
        stringRedisTemplate.opsForZSet().remove(key, userId.toString());
      }
    }
    return Result.success(action+"成功");
//    return articleMapper.articlesIncur(id,action);
  }

  @Override
  public List<Article> getArticleSortBy(Integer limit, String condition) {
    // wrapper限制查询数量
    QueryWrapper<Article> wrapper = new QueryWrapper<Article>().last("limit " + limit).orderByDesc(condition);
    return articleMapper.selectList(wrapper);
  }

  @Override
  public Article getArticleById(Integer articleId) {
    return articleMapper.selectById(articleId);
  }

  @Override
  public Result getArticlePage(Integer page, Integer limit, Integer click) {
    List<ArticleVO> result = new ArrayList<>();

    Page<Article> articlePage = new Page<>(page, limit);

    QueryWrapper<Article> wrapper = new QueryWrapper<>();
    if (click != 0) {
      wrapper.eq("sort_class", click);
    }
    wrapper.orderByDesc("id");

    Page<Article> resultPage = this.articleMapper.selectPage(articlePage, wrapper);

    List<Article> articles = resultPage.getRecords();
    for (Article article : articles) {

      // 根据作者名称查询对应的头像地址
      Integer authorId = article.getAuthorId();
      User users = userMapper.searchId(authorId);
      String profile = users.getAvatar();
      ArticleVO articleVO = new ArticleVO();
      articleVO.setProfile(profile);
      articleVO.setAuthorId(authorId);


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
    return Result.ok(new ResultListVO<>(result,resultPage.getTotal()));
  }
}
