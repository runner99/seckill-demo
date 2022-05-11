package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author wei
* @description 针对表【t_goods】的数据库操作Service
* @createDate 2022-05-11 09:33:53
*/
public interface GoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
