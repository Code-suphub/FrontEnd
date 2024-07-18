package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.Result;
import com.li.entity.pojo.Article;
import com.li.entity.pojo.ArticleComment;
import com.li.entity.pojo.User;
import com.li.entity.vo.ArticleCommentPageVO;
import com.li.entity.vo.ArticleCommentVO;
import com.li.entity.vo.ResultListVO;
import com.li.mapper.ArticleCommentMapper;
import com.li.mapper.ArticleMapper;
import com.li.mapper.UserMapper;
import com.li.service.ArticleCommentService;
import com.li.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
    implements ArticleCommentService {

  @Resource
  private ArticleCommentMapper articleCommentMapper;

  @Resource private UserMapper userMapper;

  @Resource private ArticleMapper articleMapper;
  @Resource
  private UserService userService;

  @Override
  public int GetCommentNumByArticleId(Integer articleId) {
    QueryWrapper<ArticleComment> wrapper = new QueryWrapper<>();
    wrapper.eq("article_id", articleId);
    return articleCommentMapper.selectCount(wrapper);
  }

  @Override
  public ArticleCommentPageVO VoList(Integer page, Integer limit, Integer click) {
    List<ArticleCommentVO> result = new ArrayList<>();

    ArticleCommentVO articleCommentVO = null;

    Page<ArticleComment> articleCommentPage = new Page<>(page, limit);

    QueryWrapper<ArticleComment> wrapper = new QueryWrapper<ArticleComment>();
    wrapper.orderByDesc("id");

    Page<ArticleComment> resultPage = this.articleCommentMapper.selectPage(articleCommentPage, wrapper);

    List<ArticleComment> articleComments = resultPage.getRecords();
    for (ArticleComment articleComment : articleComments) {

      // 根据作者id查询对应的头像地址
      Integer author = articleComment.getUserId();
      User users = userMapper.searchId(author);
      String profile = users.getAvatar();
      articleCommentVO = new ArticleCommentVO();
      // 根据作者id查询对应的名称
      String name = users.getNickName();
      // 获取对应文章名称
      Integer articleId = articleComment.getArticleId();
      Article article = articleMapper.selectById(articleId);
      String articleName = article.getTitle();
//      articleCommentVO.setArticleName(articleName);

      BeanUtils.copyProperties(articleComment, articleCommentVO);
//      articleCommentVO.setUsername(name);

//      articleCommentVO.setProfile(profile);
      result.add(articleCommentVO);
    }
    ArticleCommentPageVO articleCommentPageVO = new ArticleCommentPageVO();
    articleCommentPageVO.setData(result);
    articleCommentPageVO.setTotal(resultPage.getTotal());
    articleCommentPageVO.setPages(resultPage.getPages());
    return articleCommentPageVO;
  }

  @Override
  public List<ArticleComment> getCommentByDesc(String order, Integer p, Integer limit) {
    Page<ArticleComment> page = new Page<>(p,limit);
    page.addOrder(OrderItem.desc(order));
    // 使用 page 对象进行查询
    Page<ArticleComment> articleCommentPage = articleCommentMapper.selectPage(page,null);
    System.out.println(articleCommentPage);
    return articleCommentPage.getRecords();
  }

  @Override
  public Integer addOne(ArticleComment articleComment) {
    if(articleComment.getContent().length()>255){ // 评论最大长度
      return -1;
    }
    return articleCommentMapper.insert(articleComment);
  }

  @Override
  public List<ArticleComment> selectCommentByArticleId(Integer articleId) {
    QueryWrapper<ArticleComment> wrapper = new QueryWrapper<>();
    wrapper.eq("article_id", articleId);
    return articleCommentMapper.selectList(wrapper);
  }

  @Override
  public Result getAllArticleComment(Integer articleId) {
    List<ArticleComment> articleComments = selectCommentByArticleId(articleId);
    List<ArticleCommentVO> articleCommentVOList = new ArrayList<>();
    for(ArticleComment articleComment:articleComments){
      ArticleCommentVO articleCommentVO = new ArticleCommentVO();
      BeanUtils.copyProperties(articleComment,articleCommentVO);
      User user = userService.getById(articleComment.getUserId());
      articleCommentVO.setNickname(user.getNickName());
      articleCommentVO.setAvatar(user.getNickName());
      articleCommentVOList.add(articleCommentVO);
    }
    return Result.success(new ResultListVO<>(articleCommentVOList,(long)articleCommentVOList.size()));
  }
}
