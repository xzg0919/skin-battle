package com.tzj.collect.core.result.business;

import lombok.Data;

/**
 * Created on 2019/8/30
 * Title: TODO
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 上海铸乾信息科技有限公司
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@Data
public class OrderServiceabilityResult {
    //平均派单时间
    private Double avgTosendDate;
    //最长派单时间
    private Double maxTosendDate;
    //平均接单时间
    private Double avgAlreadyDate;
    //最长接单时间
    private Double maxAlreadyDate;
    //平均完成订单时间
    private Double avgCompleteDate;
    //最长完成订单时间
    private Double maxCompleteDate;
}
