package com.li.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true) // 链式赋值
public class FollowVO {
    Integer followeeId;
    Integer followerId;
    String followerNickname; // 自己昵称（粉丝）
    String followeeAvatar; // 被关注头像（大V）
    String followeeNickname; // 被关注者昵称
}
