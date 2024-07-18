package com.li.entity.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
public class DispositionCarousel implements Serializable {

  private Integer id;

  private String title;

  private String introduce;

  private String button;

  private String img;
}
