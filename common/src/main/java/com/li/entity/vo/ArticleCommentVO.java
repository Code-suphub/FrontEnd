package com.li.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleCommentVO implements Serializable {

  @TableId(type = IdType.AUTO)
  private Integer id;
  private String content;

  private Integer userId;
  private String nickname;    // 当前评论的创建者昵称
  private String avatar;      // 当前评论创建者的头像
  private Date addTime;
}
