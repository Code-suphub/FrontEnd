package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // 链式赋值
public class ChatMessages implements Serializable {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private String msg;         // 聊天内容
  private Integer fromUserId; // 聊天的发起者id

  private Integer toUserId;   // 聊天的接收者id

  //    @ApiModelProperty(value = "创建时间")
  //    @TableField(fill = FieldFill.INSERT)//创建注解::自动填充 -DEFAULT没有时，INSERT插入时
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;

  public ChatMessages(String msg, Integer fromUserId, Integer toUserId) {
    this.msg = msg;
    this.fromUserId = fromUserId;
    this.toUserId = toUserId;
  }
}
