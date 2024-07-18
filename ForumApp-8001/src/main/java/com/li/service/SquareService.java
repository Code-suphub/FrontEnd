package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.pojo.Square;
import com.li.entity.vo.SquarePageVO;
import org.springframework.stereotype.Repository;

@Repository
public interface SquareService extends IService<Square> {

  SquarePageVO VoList(Integer id, Integer page, Integer limit);
}
