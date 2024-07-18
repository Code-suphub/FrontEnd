package com.li.entity.vo;

import lombok.Data;

import java.util.Date;

/** 文章列表 */
@Data
public class ArticleVO {
  private Integer id;
  private Date addTime;
  private Date createTime;
  private Integer authorId;
  private String authorName;
  private String profile;
  private String title;
  private String ownerTag;
  private String status;
  private String thumb;
  private String sortClass;
  private Integer hits;
  private Integer CommentNum;
  private String className;
  private Integer postNum;
  private String content;
  private String intro;
  private Integer loveNum;
  private String keyword;
}
