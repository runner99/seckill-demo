package generate;

import generate.TSeckillOrder;

public interface TSeckillOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(TSeckillOrder record);

    int insertSelective(TSeckillOrder record);

    TSeckillOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TSeckillOrder record);

    int updateByPrimaryKey(TSeckillOrder record);
}