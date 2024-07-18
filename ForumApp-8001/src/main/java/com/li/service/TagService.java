package com.li.service;

import com.li.entity.Result;
import com.li.entity.pojo.AllTag;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface TagService {
    List<AllTag> selectAllList();

    Result selectById(Integer id);

    Result deleteTag(Integer id);

    Result getAllTag();

    Result updateTag(@RequestBody AllTag allTag);

    Result addTag(@RequestBody AllTag allTag);
}
