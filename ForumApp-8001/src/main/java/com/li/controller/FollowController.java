package com.li.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Result;
import com.li.entity.pojo.Follows;
import com.li.entity.pojo.User;
import com.li.mapper.FollowsMapper;
import com.li.mapper.UserMapper;
import com.li.service.FollowService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@io.swagger.annotations.Api(tags = "Web关注接口")
@RestController
@RequestMapping("/follow")
public class FollowController {

  @Autowired private FollowsMapper followsMapper;

  @Resource private FollowService followService;

  @ApiOperation(value = "根据关注者和被关注者id关注")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "followerId", value = "关注者", required = true),
    @ApiImplicitParam(name = "followingId", value = "被关注者", required = true)
  })
  @GetMapping("/follow/{followerId}/{followingId}")
  public Result follow(
      @PathVariable("followerId") Integer followerId,
      @PathVariable("followingId") Integer followingId) {
    return followService.follow(followerId, followingId);
  }

  @ApiOperation(value = "取消关注")
  @GetMapping("/unfollow/{followerId}/{followingId}")
  public Result unfollowUser(
      @PathVariable("followerId") Integer followerId,
      @PathVariable("followingId") Integer followeeId) {
    return followService.unfollowUser(followerId, followeeId);
  }

  @ApiOperation(value = "关注者")
  @GetMapping("/followers/{followerIds}")
  public Result getFollowers(@PathVariable("followerIds") Integer followerIds) {
    return followService.getFollowers(followerIds);
  }

  @GetMapping("/isFollower/{followerId}/{followeeId}")
  public Result isFollower(@PathVariable("followerId") Integer followerId,@PathVariable("followeeId") Integer followeeId) {
      return followService.isFollower(followerId,followeeId);
  }

}
