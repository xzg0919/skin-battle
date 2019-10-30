package com.tzj.collect.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.VoucherNofity;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [优惠券通知映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
public interface VoucherNofityMapper extends BaseMapper<VoucherNofity>
{

    /**
     * <p>Created on 2019年10月30日</p>
     * <p>Description:[阿里的id获取通知列表]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<VoucherNofity>
     */
    List<VoucherNofity> getListByAliId(String aliUserId);

    /**
     * <p>Created on 2019年10月30日</p>
     * <p>Description:[更新通知状态]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    void updateStatus(@Param("voucherNofity")VoucherNofity voucherNofity);

}