package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.service.GoodsService;
import com.xxxx.seckill.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

/**
* @author wei
* @description 针对表【t_goods】的数据库操作Service实现
* @createDate 2023-03-15 23:59:39
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}




