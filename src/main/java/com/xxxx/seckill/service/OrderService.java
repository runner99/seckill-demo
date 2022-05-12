package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;

/**
* @author wei
* @description 针对表【t_order】的数据库操作Service
* @createDate 2022-05-11 09:33:53
*/
public interface OrderService extends IService<Order> {

    /**
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    Order seckill(User user, GoodsVo goods);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Long orderId);
}
