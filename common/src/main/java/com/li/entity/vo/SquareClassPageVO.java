package com.li.entity.vo;

import com.li.entity.pojo.SquareClass;
import lombok.Data;

import java.util.List;

@Data
public class SquareClassPageVO {
  private List<SquareClass> data;
  private Long total;
}
