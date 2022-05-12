package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.GoodsService;
import com.xxxx.seckill.service.OrderService;
import com.xxxx.seckill.service.SeckillOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/doSeckill2")
    public String doSeckill2(Model model, User user,Long goodsId){
        if (user ==null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存
        if (goods.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }

        //判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id",user.getId()).eq("goods_id",goodsId));

        if (seckillOrder!=null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }

        Order order = orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";

    }

    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId){
        if (user ==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存
        if (goods.getStockCount()<1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
//                .eq("user_id",user.getId()).eq("goods_id",goodsId));
        SeckillOrder seckillOrder =
                (SeckillOrder)redisTemplate.opsForValue().get("order:"+user.getId()+":"+goods.getId());
        if (seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        Order order = orderService.seckill(user,goods);
        return RespBean.success(order);

    }
}
