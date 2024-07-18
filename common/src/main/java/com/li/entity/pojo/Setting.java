package com.li.entity.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
public class Setting implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;

  private String beian;

  private String banquan;

  private boolean comment_show;

  private String sitTitle;

  private String sitLogo;

  private boolean imageFormat;
}
