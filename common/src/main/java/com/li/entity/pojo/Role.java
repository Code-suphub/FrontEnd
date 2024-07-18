package com.li.entity.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
public class Role implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;       // 主键id

  private String name;      // 名称

  private String sort;

  private Date createTime; // 创建时间

  private Date updateTime;  // 修改时间

  private String code;

  private Boolean status;  //状态 1 启用 0 停用

  private String remark;  // 注释

}
