package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleComment implements Serializable {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private String content;     // 当前评论内容
  private Integer parentId;   //当前评论的父级id

  /** 添加时间 */
  @ApiModelProperty(value = "创建时间")
  @TableField(fill = FieldFill.INSERT) // 创建注解::自动填充 -DEFAULT没有时，INSERT插入时
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  //      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  // 返回前端自动把Data类型转换为json类型
  private Date addTime;


  private Integer userId;     // 当前评论由谁创建
  private Integer articleId;  // 当前评论的文章id
}
