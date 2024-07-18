package com.li.entity.vo;

import com.li.entity.pojo.ArticleClass;
import lombok.Data;

import java.util.List;

@Data
public class ArticleClassVO {
  private Integer id;

  private String name;        // 分类名
  private Integer father;     // 父类id
  private String otherName;   // 别名
  private String describes;   // 描述
  private String imgClass;    // 分类图片
  private Boolean top;        // 是否置顶
  private Integer num;        // 类别文章数量
}
