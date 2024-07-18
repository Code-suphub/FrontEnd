package com.li.service;

import com.li.entity.Result;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowService {
    Result getFollowers(Integer followerIds);

    Result unfollowUser(Integer followerId, Integer followeeId);

    Result follow(Integer followerId, Integer followingId);

    Result isFollower(Integer followerId, Integer followeeId);
}
