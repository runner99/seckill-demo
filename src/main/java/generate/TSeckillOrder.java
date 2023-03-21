package generate;

import java.io.Serializable;
import lombok.Data;

/**
 * t_seckill_order
 * @author 
 */
@Data
public class TSeckillOrder implements Serializable {
    /**
     * 秒杀订单ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long goodsId;

    private static final long serialVersionUID = 1L;
}