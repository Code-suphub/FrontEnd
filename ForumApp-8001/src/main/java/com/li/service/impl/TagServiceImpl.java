package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.pojo.AllTag;
import com.li.entity.vo.ResultListVO;
import com.li.entity.vo.ResultPageListVO;
import com.li.mapper.AllTagMapper;
import com.li.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Resource
    AllTagMapper allTagMapper;

    @Override
    public List<AllTag> selectAllList() {
        return allTagMapper.selectList(null);
    }

    @Override
    public Result selectById(Integer id) {
        AllTag allTag = allTagMapper.selectById(id);
        if (allTag!=null){
            return Result.ok(allTag);
        }else{
            return Result.fail("标签不存在，请核对后再查询");
        }
    }

    @Override
    public Result deleteTag(Integer id){
        int i = allTagMapper.deleteById(id);
        if(i!=0){
            return Result.ok();
        }else{
            return Result.fail("删除失败，请核对标签id");
        }
    }

    @Override
    public Result getAllTag(){
        List<AllTag> allTags = allTagMapper.selectList(null);
        return Result.ok(new ResultListVO<>(allTags,(long)allTags.size()));
    }

    @Override
    public Result updateTag(AllTag allTag) {
        int affectRow = allTagMapper.updateById(allTag);
        if(affectRow==0){
            return Result.fail("该标签不存在，请核验标签id");
        }else{
            return Result.ok();
        }
    }

    @Override
    public Result addTag(AllTag allTag) {
        int affectRow = allTagMapper.insert(allTag);
        if(affectRow==0){
            return Result.fail("插入失败，请重试");
        }else{
            return Result.ok();
        }
    }
}
