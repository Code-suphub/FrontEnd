package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.pojo.SquareClass;
import com.li.entity.vo.SquareClassPageVO;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

@Repository
public interface SquareClassService extends IService<SquareClass> {

  SquareClassPageVO GetList(Integer page, Integer limit);

  SquareClass getByName(String sortName);

  SquareClass getSquareClassById(Integer id);
}
