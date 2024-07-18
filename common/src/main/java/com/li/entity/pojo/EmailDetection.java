package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
public class EmailDetection implements Serializable {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private String email;

  private String code;

  private Date time;
}