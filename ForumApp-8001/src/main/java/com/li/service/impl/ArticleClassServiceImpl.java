package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.Result;
import com.li.entity.pojo.Article;
import com.li.entity.pojo.ArticleClass;
import com.li.entity.vo.ArticleClassVO;
import com.li.entity.vo.ClassNameVO;
import com.li.entity.vo.ResultListVO;
import com.li.entity.vo.ResultPageListVO;
import com.li.mapper.ArticleClassMapper;
import com.li.mapper.ArticleMapper;
import com.li.service.ArticleClassService;
import com.li.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleClassServiceImpl extends ServiceImpl<ArticleClassMapper, ArticleClass>
    implements ArticleClassService {

  @Resource
  private ArticleClassMapper articleClassMapper;

  @Resource private ArticleMapper articleMapper;
  @Resource private ArticleService articleService;

  // 获取文章类别列表
  @Override
  public Result getArticleClassList(Integer page, Integer limit) {
    Page<ArticleClass> ArticleClassPage = new Page<>(page, limit);
    List<ArticleClassVO> result = new ArrayList<>();
    QueryWrapper<ArticleClass> wrapper = new QueryWrapper<>();
    wrapper.orderByDesc("id");

    Page<ArticleClass> resultPage = this.articleClassMapper.selectPage(ArticleClassPage, wrapper);
    List<ArticleClass> articleClasses = resultPage.getRecords();

    // 每种类别查询文章数量
    for (ArticleClass articleClass : articleClasses) {
      ArticleClassVO articleClassVO = new ArticleClassVO();
      BeanUtils.copyProperties(articleClass, articleClassVO);
      Integer id = articleClass.getId();
      QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("sort_class", id);
      Integer count = articleMapper.selectCount(queryWrapper);
      articleClassVO.setNum(count);
      result.add(articleClassVO);
    }
    long total = resultPage.getTotal();

    return Result.ok(new ResultListVO<>(result,total));
  }

  @Override
  public ArticleClass getArticleClassById(Integer id) {
    return articleClassMapper.selectById(id);
  }

  @Override
  public Result newArticleClass(ArticleClass articleClass) {
    QueryWrapper<ArticleClass> wrapper = new QueryWrapper<ArticleClass>();
    wrapper.eq("name", articleClass.getName());
    ArticleClass articleClassFound = this.getOne(wrapper);
    if (articleClassFound != null) {
      // 该分类已存在
      return Result.fail("该分类已存在");
    }
    int insert = articleClassMapper.insert(articleClass);
    if (insert <= 0) {
      return Result.fail("添加失败");
    }
    return Result.success();
  }

  @Override
  public Result deleteArticleClass(Integer id) {
    // 根据分类id删除文章
    QueryWrapper<Article> wrapper = new QueryWrapper<>();
    wrapper.eq("sort_class", id);
    boolean remove = articleService.remove(wrapper);
    if (!remove) {
      return Result.fail("删除失败");
    }
    int count = articleClassMapper.deleteById(id);
    if (count > 0){
      return Result.success();
    }
    return  Result.fail("删除失败");
  }

  @Override
  public Result getAllClassName() {
    List<ClassNameVO> result = new ArrayList<>();

    QueryWrapper<ArticleClass> wrapper = new QueryWrapper<ArticleClass>();
    wrapper.select("name", "id");
    List<ArticleClass> articleClasses = articleClassMapper.selectList(wrapper);
    for (ArticleClass articleClass : articleClasses) {
      ClassNameVO classNameVO = new ClassNameVO();
      BeanUtils.copyProperties(articleClass, classNameVO);
      result.add(classNameVO);
    }
    return Result.success(new ResultListVO<>(result, (long)articleClasses.size()));
  }

  @Override
  public Result ReviseArticleClassById(ArticleClass articleClass) {
    boolean ok = this.updateById(articleClass);
    if (ok){
      return Result.success();
    }
    return Result.fail("修改失败");
  }

  @Override
  public Result getClassNameById(Integer id) {
    ArticleClass articleClass = this.getById(id);
    if (articleClass != null){
      return Result.success(articleClass.getName());
    }
    return Result.fail("不存在");
  }

  @Override
  public Result topArticleClass(Integer id) {
    ArticleClass articleClass = this.getById(id);
    if (articleClass == null){
      return Result.fail("该类别不存在");
    }
    // 设置为这个字段在数据库里面相反的
    articleClass.setTop(!articleClass.getTop());
    return Result.success(this.updateById(articleClass));
  }

  @Override
  public Result getArticleClassNum(Integer id) {
    QueryWrapper<Article> wrapper = new QueryWrapper<Article>();
    wrapper.eq("sort_class", id);
    Integer count = articleMapper.selectCount(wrapper);
    if (count != null){
      return Result.success(count);
    }
    return Result.fail("获取失败");
  }

  @Override
  public Result getArticleClassList() {
    List<ArticleClass> articleClasses = articleClassMapper.selectList(null);
    if (articleClasses != null){
      return Result.success(new ResultListVO<>(articleClasses,(long)articleClasses.size()));
    }
    return Result.fail("获取失败");
  }
}
