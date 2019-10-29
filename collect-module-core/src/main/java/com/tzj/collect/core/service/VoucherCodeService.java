package com.tzj.collect.core.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.entity.VoucherCode;

/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [券码service]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
public interface VoucherCodeService extends IService<VoucherCode>
{

    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[根据券码找]</p>
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
    void updateMemberId(Long id, Long memberId);

    /**
     * <p>Created on 2019年10月28日</p>
     * <p>Description:[导出券码分页查询]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<VoucherCode>
     */
    List<VoucherCode> getExpoPageList(PageBean pageBean,Integer Integer);

}