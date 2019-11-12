package com.tzj.collect.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.VoucherCodeMapper;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.service.VoucherCodeService;
import com.tzj.collect.entity.VoucherCode;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [券码service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class VoucherCodeServiceImpl extends ServiceImpl<VoucherCodeMapper, VoucherCode> implements VoucherCodeService
{
    @Resource
    private VoucherCodeMapper voucherCodeMapper;

    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[根据券码找]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    public VoucherCode getByCode(String entityNum)
    {
        return voucherCodeMapper.getByCode(entityNum);
    }

    /**
     * <p>Created on 2019年10月26日</p>
     * <p>Description:[券码绑定会员]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    @Transactional
    public void updateMemberId(Long id, Long memberId)
    {
        voucherCodeMapper.updateMemberId(id,memberId);
    }

    /**
     * <p>Created on 2019年10月28日</p>
     * <p>Description:[导出券码分页查询]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    public List<VoucherCode> getExpoPageList(PageBean pageBean,Integer voucherId)
    {
        int pageStart = (pageBean.getPageNumber() - 1) * pageBean.getPageSize();
        return voucherCodeMapper.getExpoPageList(pageStart,pageBean.getPageSize(),voucherId);
    }

}