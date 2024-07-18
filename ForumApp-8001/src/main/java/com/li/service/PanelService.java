package com.li.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.li.entity.pojo.Panel;

public interface PanelService extends IService<Panel> {
  Panel SearchPanelInfo();
}
