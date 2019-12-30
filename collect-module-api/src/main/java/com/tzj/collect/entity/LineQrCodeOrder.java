package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 分享码订单
 * @author: sgmark@aliyun.com
 * @Date: 2019/12/19 0019
 * @Param: 
 * @return: 
 */
@TableName("sb_line_qr_code_order")
@Data
public class LineQrCodeOrder extends DataEntity<Long> {
    /**
     *  id
     */
    private Long id;

    private String shareCode;//唯一code

    private String orderNo;//订单编号
}
