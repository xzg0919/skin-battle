package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.VoucherAli;

import java.math.BigDecimal;

/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [支付宝券码券service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
public interface VoucherAliService extends IService<VoucherAli>
{

    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[生成券码]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    String makeCode(String id);

    /**
     * <p>Created on 2019年10月26日</p>
     * <p>Description:[更新领取数]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    void updatePickCount(Long voucherId);

    /**
     * 根据券的Id和最初的价格计算券优惠后的金额
     * @param price
     * @param voucherId
     * @return
     */
    BigDecimal getDiscountPriceByVoucherId(BigDecimal price,String voucherId);

}