package com.li.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.entity.pojo.ForumResource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceMapper extends BaseMapper<ForumResource> {

  @Select("update forum_resource set hits=IFNULL(hits,0)+1 where id = ${id}")
  Boolean resourceBrowse(@Param("id") Integer id);

  @Select("update forum_resource set love_num=IFNULL(love_num,0)+1 where id = ${id}")
  Boolean resourceLoveBrowse(@Param("id") Integer id);
}