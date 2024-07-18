package com.li.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.entity.pojo.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {
  @Update("update article set ${action}=IFNULL(${action},0)+1 where id = ${id}")
  Boolean articlesIncur(@Param("id") Integer id, @Param("action")String action);
}
