package com.tzj.collect.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.VoucherCode;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [券码映射类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
public interface VoucherCodeMapper extends BaseMapper<VoucherCode>
{

    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return VoucherCode
     */
    VoucherCode getByCode(String entityNum);

    /**
     * <p>Created on 2019年10月26日</p>
     * <p>Description:[券码绑定会员]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    void updateMemberId(@Param("id")Long id,@Param("memberId") Long memberId);

    /**
     * <p>Created on 2019年10月28日</p>
     * <p>Description:[导出券码分页查询]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<VoucherCode>
     */
    List<VoucherCode> getExpoPageList(@Param("pageStart")Integer pageStart, @Param("pageSize")Integer pageSize,@Param("voucherId")Integer voucherId);

}