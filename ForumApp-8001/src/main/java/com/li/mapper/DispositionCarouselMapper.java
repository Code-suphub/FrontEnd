package com.li.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.li.entity.pojo.DispositionCarousel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispositionCarouselMapper extends BaseMapper<DispositionCarousel> {
  @Select("select * from disposition_carousel  order by id")
  List<DispositionCarousel> selectAll();
}
