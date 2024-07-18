package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.entity.pojo.Square;
import com.li.entity.pojo.SquareClass;
import com.li.entity.pojo.User;
import com.li.entity.vo.SquarePageVO;
import com.li.entity.vo.SquareVO;
import com.li.mapper.SquareMapper;
import com.li.mapper.UserMapper;
import com.li.service.SquareClassService;
import com.li.service.SquareCommentService;
import com.li.service.SquareService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SquareServiceImpl extends ServiceImpl<SquareMapper, Square> implements SquareService {

  @Resource
  private UserMapper userMapper;

  @Resource private SquareCommentService squareCommentService;

  @Resource private SquareMapper squareMapper;

  @Resource private SquareClassService squareClassService;

  @Override
  public SquarePageVO VoList(Integer squareId, Integer page, Integer limit) {

    Page<Square> squarePage = new Page<>(page, limit);

    List<SquareVO> result = new ArrayList<>();
    QueryWrapper<Square> queryWrapper = new QueryWrapper<Square>();
    queryWrapper.select().orderByDesc("add_time");
    if(squareId!=0){
      queryWrapper.eq("sort_class", squareId);
    }
    Page<Square> resultPage = squareMapper.selectPage(squarePage, queryWrapper);

    List<Square> squares = resultPage.getRecords();
    for (Square square : squares) {

      // 根据用户id获取名称信息
      // id是内容发布者id
      Integer authors = square.getAuthor();
      User users = userMapper.searchId(authors);
      String username = users.getNickName();
      String authorImg = users.getAvatar();
      SquareVO squareVO = new SquareVO();
      squareVO.setAuthor(username);
      squareVO.setUserid(authors);
      squareVO.setImage(square.getImage());
      squareVO.setAuthorImg(authorImg);
      // 查询分类名称对应的id值
      SquareClass SquareClassIs = squareClassService.getById(square.getSortClass());
      squareVO.setSortName(SquareClassIs.getName());

      Integer planetCommentNum = squareCommentService.GetCommentNum(square.getId());
      squareVO.setCommentNum(planetCommentNum);

      BeanUtils.copyProperties(square, squareVO);
      result.add(squareVO);
    }
    SquarePageVO squarePageVO = new SquarePageVO();
    squarePageVO.setData(result);
    squarePageVO.setTotal(resultPage.getTotal());
    squarePageVO.setPages(resultPage.getPages());
    return squarePageVO;
  }
}
