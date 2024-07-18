package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
public class Follows implements Serializable {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private Integer followerId;

  private Integer followeeId;
}
