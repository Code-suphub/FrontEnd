package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.pojo.OrderInfo;
import com.li.entity.pojo.User;
import com.li.mapper.UserMapper;
import com.li.mapper.VipOrderInfoMapper;
import com.li.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private VipOrderInfoMapper vipOrderInfoMapper;
    @Override
    public OrderInfo getOrderByOrderNo(String orderNo) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return vipOrderInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public int updateOrderInfo(OrderInfo orderInfo) {
        return vipOrderInfoMapper.updateById(orderInfo);
    }
}
