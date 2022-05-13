package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;

/**
* @author wei
* @description 针对表【t_seckill_order】的数据库操作Service
* @createDate 2022-05-11 09:33:53
*/
public interface SeckillOrderService extends IService<SeckillOrder> {
    
    /**
     * 获取秒杀结果
     * orderId:成功 ，-1:秒杀失败，0:排队中
     */
    Long getResult(User user, Long goodsId);

}
