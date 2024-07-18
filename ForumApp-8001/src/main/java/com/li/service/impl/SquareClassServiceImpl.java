package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.pojo.Square;
import com.li.entity.pojo.SquareClass;
import com.li.entity.vo.SquareClassPageVO;
import com.li.mapper.SquareClassMapper;
import com.li.mapper.SquareMapper;
import com.li.service.SquareClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SquareClassServiceImpl extends ServiceImpl<SquareClassMapper, SquareClass>
    implements SquareClassService {

  @Resource private SquareClassMapper squareClassMapper;

  @Resource
  private SquareMapper squareMapper;

  @Override
  public SquareClassPageVO GetList(Integer page, Integer limit) {
    Page<SquareClass> SquareClassPage = new Page<>(page, limit);
    SquareClassPageVO classPageVO = new SquareClassPageVO();
    List<SquareClass> result = new ArrayList<>();
    QueryWrapper<SquareClass> wrapper = new QueryWrapper<>();
    wrapper.orderByDesc("id");

    Page<SquareClass> resultPage = this.squareClassMapper.selectPage(SquareClassPage, wrapper);
    List<SquareClass> squareClasses = resultPage.getRecords();
    for (SquareClass squareClasse : squareClasses) {
      Integer id = squareClasse.getId();
      QueryWrapper<Square> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("sort_class", id);
      Integer count = squareMapper.selectCount(queryWrapper);
      squareClasse.setNum(count);
      result.add(squareClasse);
    }
    long total = resultPage.getTotal();
    classPageVO.setData(result);
    classPageVO.setTotal(total);
    return classPageVO;
  }

  @Override
  public SquareClass getByName(String sortName) {
    QueryWrapper<SquareClass> wrapper = new QueryWrapper<>();
    wrapper.eq("other_name", sortName);
    return squareClassMapper.selectOne(wrapper);
  }

  @Override
  public SquareClass getSquareClassById(Integer id) {
    if (id==0){
      return squareClassMapper.selectById(1);
    }
    return squareClassMapper.selectById(id);
  }
}
