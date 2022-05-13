package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.SeckillOrderService;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
* @author wei
* @description 针对表【t_seckill_order】的数据库操作Service实现
* @createDate 2022-05-11 09:33:53
*/
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder>
    implements SeckillOrderService{

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取秒杀结果
     * orderId:成功 ，-1:秒杀失败，0:排队中
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper
                .selectOne(new QueryWrapper<SeckillOrder>()
                        .eq("user_id", user.getId())
                        .eq("goods_id", goodsId));
        if (null!=seckillOrder){
            return seckillOrder.getOrderId();
        }else if (redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return -1L;
        }else {
            return 0L;
        }

    }
}




