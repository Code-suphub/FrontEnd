package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式赋值
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.AUTO)
  private Integer userId;       // Id

  private Integer userAge;      // 年龄

  private String intro;         // 简介

  private String gender;        // 性别

  private String nickName;      // 昵称

  private String username;      // 用户名

  private String password;      // 密码

  private String email;         // 邮箱

  private Integer role;          // 角色

  private String avatar;        // 头像

  private Boolean status;       // 状态，是否处于正常状态，false为禁用

  /** 生成时间 */
  @ApiModelProperty(value = "生成时间")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;      // 创建时间

  private Date lastLogin;       //上次登陆时间

  private Date vipValidDate;    //vip创建时间

  private Date vipExpireDate;    //vip失效时间

  private Integer integral;      // 积分

  private Boolean vipDisableTip; // 会员是否禁用

  private String height;         //身高

  private String birthday;       //生日

  private String academic;       //学历

  private String monthly;        // 月薪

  private String permanent;     // 常住地
}
