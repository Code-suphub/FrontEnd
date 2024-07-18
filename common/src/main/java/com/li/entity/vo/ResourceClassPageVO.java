package com.li.entity.vo;

import com.li.entity.pojo.ResourceClass;
import lombok.Data;

import java.util.List;

@Data
public class ResourceClassPageVO {
  private List<ResourceClass> data;
  private Long total;
}
