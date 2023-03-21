package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.service.OrderService;
import org.springframework.stereotype.Service;

/**
* @author wei
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2023-03-15 23:33:56
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




