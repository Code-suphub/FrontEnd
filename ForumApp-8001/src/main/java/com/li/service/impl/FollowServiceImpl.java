package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.li.entity.Result;
import com.li.entity.pojo.Follows;
import com.li.entity.pojo.User;
import com.li.entity.vo.FollowVO;
import com.li.entity.vo.ResultListVO;
import com.li.mapper.FollowsMapper;
import com.li.mapper.UserMapper;
import com.li.service.FollowService;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    @Resource
    UserMapper userMapper;
    @Resource
    FollowsMapper followsMapper;

    // 获取当前用户的关注者，即是谁的粉丝
    @Override
    public Result getFollowers(Integer followerIds) {
        ArrayList<FollowVO> result = new ArrayList<>();

        // 查找所有的关注者
        QueryWrapper<Follows> wrapper = new QueryWrapper<>();
        wrapper.eq("follower_id", followerIds);
        List<Follows> list = followsMapper.selectList(wrapper);
        if(list.isEmpty()){
            return Result.ok(new ResultListVO<>(result,(long) 0));
        }

        // 获取当前用户的信息
        QueryWrapper<User> wrapperUser = new QueryWrapper<>();
        System.out.println(list.get(0).getFollowerId());
        wrapperUser.eq("user_id", list.get(0).getFollowerId());
        User follower = userMapper.selectOne(wrapperUser);
        for (Follows follows : list) {
            wrapperUser = new QueryWrapper<>();
            wrapperUser.eq("user_id", follows.getFolloweeId());
            User followee = userMapper.selectOne(wrapperUser);
            if(followee==null){
                continue;
            }
            result.add(new FollowVO().setFollowerNickname(follower.getNickName())
                    .setFolloweeAvatar(followee.getAvatar())
                    .setFolloweeNickname(followee.getNickName())
                    .setFolloweeId(followee.getUserId())
                    .setFollowerId(follower.getUserId())); // 为了可以取消关注使用
        }
        return Result.ok(new ResultListVO<>(result,(long) result.size()));
    }

    @Override
    public Result unfollowUser(Integer followerId, Integer followeeId) {
        QueryWrapper<Follows> wrapper = new QueryWrapper<>();
        wrapper.eq("follower_id", followerId);
        wrapper.eq("followee_id", followeeId);
        int delete = followsMapper.delete(wrapper);
        if(delete==1) {
            return Result.ok("取消关注成功");
        }
        return Result.fail("取消关注失败");
    }

    @Override
    public Result follow(Integer followerId, Integer followeeId) {
        Follows follow = new Follows();
        follow.setFollowerId(followerId);
        follow.setFolloweeId(followeeId);
        if (followsMapper.selectOne(
                new QueryWrapper<Follows>().
                        eq("follower_id", followerId).
                        eq("followee_id", followeeId))!=null
            || followsMapper.insert(follow)==0
        ){
            return Result.fail("关注失败");
        }
        return Result.ok("关注成功");
    }

    @Override
    public Result isFollower(Integer followerId, Integer followeeId) {
        if (followsMapper.selectOne(
                new QueryWrapper<Follows>().
                        eq("follower_id", followerId).
                        eq("followee_id", followeeId)) == null){
            return Result.fail("未关注");
        }
        return Result.ok("已关注");
    }
}
