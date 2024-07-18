package com.li.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.entity.pojo.Square;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SquareMapper extends BaseMapper<Square> {

  @Select("update square set love_num=IFNULL(love_num,0)+1 where id = ${id}")
  Boolean resourceLoveBrowse(@Param("id") Integer id);
}
