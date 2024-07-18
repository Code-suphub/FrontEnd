package com.li.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.li.entity.pojo.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer userId;       // Id

    private Integer userAge;      // 年龄

    private String intro;         // 简介

    private String gender;        // 性别

    private String nickName;      // 昵称

    private String username;      // 用户名

    private String email;         // 邮箱

    private Integer role;          // 角色

    private String avatar;        // 头像

    private Date vipExpireDate;    //vip失效时间

    private Integer integral;      // 积分

    private Boolean vipDisableTip; // 会员是否禁用

    private String height;         //身高

    private String birthday;       //生日

    private String academic;       //学历

    private String monthly;        //月薪

    private String permanent;     // 常住地

    private String token;         // 登陆状态

    public UserVO(User user, String token){
        this.userId = user.getUserId();
        this.userAge = user.getUserAge();
        this.intro = user.getIntro();
        this.gender = user.getGender();
        this.nickName = user.getNickName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.avatar = user.getAvatar();
        this.vipExpireDate = user.getVipExpireDate();
        this.integral = user.getIntegral();
        this.vipDisableTip = user.getVipDisableTip();
        this.height = user.getHeight();
        this.birthday = user.getBirthday();
        this.academic = user.getAcademic();
        this.monthly = user.getMonthly();
        this.permanent = user.getPermanent();
        this.token = token;
    }
}

