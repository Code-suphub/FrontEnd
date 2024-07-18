package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
public class UserRole implements Serializable {

  @TableId // 指定主键
  private Integer userId;

  private Integer roleId;
}
