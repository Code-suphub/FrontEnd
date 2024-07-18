package com.li.service;

import com.li.entity.pojo.OrderInfo;

public interface OrderService {
    OrderInfo getOrderByOrderNo(String orderNo);

    int updateOrderInfo(OrderInfo orderInfo);
}
