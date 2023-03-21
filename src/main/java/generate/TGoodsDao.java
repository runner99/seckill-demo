package generate;

import generate.TGoods;

public interface TGoodsDao {
    int deleteByPrimaryKey(Long id);

    int insert(TGoods record);

    int insertSelective(TGoods record);

    TGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TGoods record);

    int updateByPrimaryKey(TGoods record);
}