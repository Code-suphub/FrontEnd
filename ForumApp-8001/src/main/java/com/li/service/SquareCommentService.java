package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.pojo.SquareComment;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SquareCommentService extends IService<SquareComment> {

  Integer GetCommentNum(Integer planetId);

  List<SquareComment> VoList(Integer page, Integer limit);

    List<SquareComment> getCommentByIdOrder(Integer postId, Integer parentId, String orderBy);
}
