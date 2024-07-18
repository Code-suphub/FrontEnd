package com.li.controller;

import com.li.entity.Result;
import com.li.entity.pojo.Panel;
import com.li.mapper.ArticleCommentMapper;
import com.li.mapper.ResourceCommentMapper;
import com.li.mapper.UserMapper;
import com.li.mapper.VipOrderInfoMapper;
import com.li.service.PanelService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("Panel")
public class PanelController {

    @Autowired
    private PanelService panelService;

    @GetMapping("getPanelInfo")
    public Result getPanelInfo() {
        Panel panel = panelService.SearchPanelInfo();
        return Result.success(panel);
    }

    @Data
    public class ChartData {
        private String icon;
        private String bgColor;
        private String color;
        private int duration;
        private String name;
        private int TheValue;
        private String percent;
        private int[] data;
    }

    @Resource
    UserMapper userMapper;

    @Resource
    ArticleCommentMapper articleCommentMapper;

    @Resource
    ResourceCommentMapper resourceCommentMapper;

    @Resource
    VipOrderInfoMapper vipOrderInfoMapper;

    @GetMapping("/chartData")
    public Result getChartData() {
        // 获取用户数量
        int userNum = userMapper.selectCount(null);

        //获取所有评论数量
        int commentNum = articleCommentMapper.selectCount(null) + resourceCommentMapper.selectCount(null);

        // 获取资源数量
        int resourceNum = resourceCommentMapper.selectCount(null);

        // 获取订单数量
        int orderNum = vipOrderInfoMapper.selectCount(null);

        String chartData = "[{\"icon\":\"GroupLine\",\"bgColor\":\"#effaff\",\"color\":\"#41b6ff\",\"duration\":2200,\"name\":\"用户\",\"TheValue\":"+ userNum +",\"percent\":\"+88%\",\"data\":[2101,5288,4239,4962,6752,5208,7450]}," +
                "{\"icon\":\"Question\",\"bgColor\":\"#fff5f4\",\"color\":\"#e85f33\",\"duration\":1600,\"name\":\"评论\",\"TheValue\": " + commentNum + ",\"percent\":\"+70%\",\"data\":[2216,1148,1255,788,4821,1973,4379]}," +
                "{\"icon\":\"CheckLine\",\"bgColor\":\"#eff8f4\",\"color\":\"#26ce83\",\"duration\":1500,\"name\":\"资源\",\"TheValue\": " + resourceNum + ",\"percent\":\"+99%\",\"data\":[861,1002,3195,1715,3666,2415,3645]}," +
                "{\"icon\":\"Smile\",\"bgColor\":\"#f6f4fe\",\"color\":\"#7846e5\",\"duration\":100,\"name\":\"销量\",\"TheValue\": " + orderNum + ",\"percent\":\"+100%\",\"data\":[100]}]";
        return Result.success(chartData);
    }
}
