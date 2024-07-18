package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
public class SquareClass implements Serializable {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private String name;
  private Boolean isFree;
  private Integer price;
  private Integer father;
  private String otherName;
  private String describes;
  private String imgclass;
  private Boolean top;
  private Integer num;
}
