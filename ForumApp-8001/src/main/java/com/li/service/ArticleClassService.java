package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.Result;
import com.li.entity.pojo.ArticleClass;
import com.li.entity.vo.ArticleClassVO;
import com.li.entity.vo.ResultListVO;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface ArticleClassService extends IService<ArticleClass> {

  Result getArticleClassList(Integer page, Integer limit);

  ArticleClass getArticleClassById(Integer id);

  Result newArticleClass(ArticleClass articleClass);

  Result deleteArticleClass(Integer id);

  Result getAllClassName();

  Result ReviseArticleClassById(ArticleClass articleClass);

  Result getClassNameById(Integer id);

  Result topArticleClass(Integer id);

  Result getArticleClassNum(Integer id);

  Result getArticleClassList();
}
