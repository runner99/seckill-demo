package com.xxxx.seckill.mapper;

import com.xxxx.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.vo.GoodsVo;

import java.util.List;

/**
* @author wei
* @description 针对表【t_goods】的数据库操作Mapper
* @createDate 2022-05-11 09:33:53
* @Entity com.xxxx.seckill.pojo.Goods
*/
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 获取商品详情
     * @param goodsId
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}




