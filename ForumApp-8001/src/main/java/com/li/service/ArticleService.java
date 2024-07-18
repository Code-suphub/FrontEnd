package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.Result;
import com.li.entity.pojo.Article;
import com.li.entity.vo.ArticleVO;
import com.li.entity.vo.ResultPageListVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleService extends IService<Article> {

  Result getArticlePage(Integer page, Integer limit, Integer click);

  Result getArticleWithLike(Integer page, Integer limit, String title);

  Result getAllArticleByAuthor(Integer authorId);

  ArticleVO getAdjacentArticleById(Integer id, boolean pre);

  Result countAllPublish();

  Result updateArticleByAction(Integer id, String action);

  List<Article> getArticleSortBy(Integer limit, String condition);

  Article getArticleById(Integer articleId);
}
