package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
public class ArticleClass implements Serializable {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private String name;        // 分类名
  private Integer father;     // 父级分类id，顶级类别为0
  private String otherName;   // 别名
  private String describes;   // 描述
  private String imgClass;    // 分类图片
  private Boolean top;        // 是否置顶，0不置顶，1置顶
}
