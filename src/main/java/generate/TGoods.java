package generate;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * t_goods
 * @author 
 */
@Data
public class TGoods implements Serializable {
    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品标题
     */
    private String goodsTitle;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品详情
     */
    private String goodsDetail;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品库存，-1表示没有限制
     */
    private Integer goodsStock;

    private static final long serialVersionUID = 1L;
}