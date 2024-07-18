package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.Result;
import com.li.entity.pojo.ArticleComment;
import com.li.entity.vo.ArticleCommentPageVO;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ArticleCommentService extends IService<ArticleComment> {

  int GetCommentNumByArticleId(Integer articleId);

  ArticleCommentPageVO VoList(Integer page, Integer limit, Integer click);

  List<ArticleComment> getCommentByDesc(String addTime, Integer i, Integer limit);

  Integer addOne(ArticleComment articleComment);

  List<ArticleComment> selectCommentByArticleId(Integer articleId);

  Result getAllArticleComment(Integer articleId);
}
