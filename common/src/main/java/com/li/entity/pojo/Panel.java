package com.li.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Panel {
  @TableId // 指定主键
  private Integer CustomerNum;
  private Integer CommentNum;
  private Integer SaleNum;
  private Integer OrderNum;
}
