package com.li.controller;

import com.li.entity.pojo.DispositionCarousel;
import com.li.entity.pojo.Setting;
import com.li.mapper.DispositionCarouselMapper;
import com.li.mapper.SettingMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@io.swagger.annotations.Api(tags = "Web设置接口")
@RestController
@RequestMapping("/setting")
public class WebSettingController {

  @Autowired private DispositionCarouselMapper disposition_carouselMapper;

  @Autowired private SettingMapper settingMapper;

  @ApiOperation(value = "获取首页轮播图")
  @GetMapping("/getCarousel")
  public List<DispositionCarousel> getCarousel() {

    return this.disposition_carouselMapper.selectAll();
  }

  @ApiOperation(value = "获取所有设置")
  @GetMapping("/getSetting")
  public Setting getSetting() {
    return settingMapper.selectOne(null);
  }
}
