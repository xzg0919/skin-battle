package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.VoucherAli;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [杭州绿账]_[]_[]</p>
 * <p>Description: [支付宝券码券映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
public interface VoucherAliMapper extends BaseMapper<VoucherAli>
{

    /**
     * <p>Created on 2019年10月26日</p>
     * <p>Description:[更新领取数]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    void updatePickCount(Long voucherId);

}