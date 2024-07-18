package com.li.controller;

import com.li.entity.Result;
import com.li.entity.pojo.CosInfo;
import com.li.entity.pojo.DispositionCarousel;
import com.li.entity.pojo.Setting;
import com.li.mapper.CosInfoMapper;
import com.li.mapper.DispositionCarouselMapper;
import com.li.mapper.SettingMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@io.swagger.annotations.Api(tags = "后台设置接口")
@RestController
@RequestMapping("/Sitting")
public class SettingController {

  @Autowired private SettingMapper settingMapper;

  @Autowired private CosInfoMapper cosInfoMapper;

  @Autowired private DispositionCarouselMapper dispositionCarouselMapper;

  @ApiOperation(value = "获取设置")
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiImplicitParam(name = "setting", value = "设置", required = true)
  @PostMapping("/getSetting")
  public Result getSetting() {
    return Result.success(settingMapper.selectOne(null));
  }

  @ApiOperation(value = "修改设置")
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiImplicitParam(name = "setting", value = "设置", required = true)
  @PostMapping("/setSetting")
  public int setSetting(@RequestBody Setting setting) {
    return settingMapper.updateById(setting);
  }

  @ApiOperation(value = "获取oss配置")
  @RequiresAuthentication // 需要登陆认证的接口
  @GetMapping("/getCosSetting")
  public Result getCosSetting() {
    return Result.success(cosInfoMapper.selectOne(null));
  }

  @ApiOperation(value = "修改oss设置")
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiImplicitParam(name = "setting", value = "设置", required = true)
  @PostMapping("/setCosInfo")
  public Result setSettingCos(@RequestBody CosInfo cosInfo) {
    return Result.success(cosInfoMapper.update(cosInfo, null));
  }

  @ApiOperation(value = "获取全部轮播图")
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiImplicitParam(name = "轮播", value = "设置", required = true)
  @GetMapping("/getAllDispositionCarousel")
  public Result getAllDispositionCarousel() {
    return Result.success(dispositionCarouselMapper.selectList(null));
  }

  @ApiOperation(value = "获取全部轮播图")
  @RequiresAuthentication // 需要登陆认证的接口
  @ApiImplicitParam(name = "轮播", value = "设置", required = true)
  @GetMapping("/setAllDispositionCarousel")
  public Result setAllDispositionCarousel(DispositionCarousel dispositionCarousel) {
    return Result.success(dispositionCarouselMapper.update(dispositionCarousel, null));
  }


}
