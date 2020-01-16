package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;
import com.tzj.green.entity.ProductOrder;
import com.tzj.green.mapper.ProductOrderMapper;
import com.tzj.green.param.PageBean;
import com.tzj.green.param.ProductBean;
import com.tzj.green.service.ProductOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [用户兑换订单表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements ProductOrderService
{
    @Resource
    private ProductOrderMapper productOrderMapper;


    @Override
    public Object getProductOrderList(ProductBean productBean, Long companyId) {

        PageBean pageBean = productBean.getPageBean();
        if (null == pageBean){
            pageBean = new PageBean();
        }
        Integer pageStart = (pageBean.getPageNum()-1)*pageBean.getPageSize();
        List<Map<String, Object>> productOrderList = productOrderMapper.getProductOrderList(companyId, productBean.getStartTime(), productBean.getEndTime(), productBean.getName(), productBean.getTel(), productBean.getType(), pageStart, pageBean.getPageSize());
        Integer count = productOrderMapper.getProductOrderCount(companyId, productBean.getStartTime(), productBean.getEndTime(), productBean.getName(), productBean.getTel(), productBean.getType());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("productOrderList",productOrderList);
        resultMap.put("count",count);
        resultMap.put("pageNum",pageBean.getPageNum());
        return resultMap;
    }

    @Override
    public Object getProductOrderDetail(String productOrderId) {

        return productOrderMapper.getProductOrderDetail(productOrderId);
    }
}