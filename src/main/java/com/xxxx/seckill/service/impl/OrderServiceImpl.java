package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.GoodsService;
import com.xxxx.seckill.service.OrderService;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.service.SeckillGoodsService;
import com.xxxx.seckill.service.SeckillOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author wei
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2022-05-11 09:33:53
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goods) {
        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                .eq("goods_id",goods.getId()));

        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count=stock_count-1")
                .eq("goods_id", goods.getId()).gt("stock_count", 0));

        if (!result){
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());

        seckillOrderService.save(seckillOrder);

        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);

        return order;
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId==null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);
        return detail;
    }
}




