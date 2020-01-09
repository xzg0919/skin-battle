package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;

import com.tzj.green.entity.CompanyRecycler;
import com.tzj.green.entity.Goods;
import com.tzj.green.mapper.GoodsMapper;
import com.tzj.green.param.GoodsBean;
import com.tzj.green.param.PageBean;
import com.tzj.green.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [商品表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService
{
    @Resource
    private GoodsMapper goodsMapper;

    @Override
    @Transactional
    public Object saveOrUpdateGoods(GoodsBean goodsBean, Long companyId) {

        Goods goods = this.selectById(goodsBean.getId());
        if (null == goods){
            goods = new Goods();
        }
        goods.setCompanyId(companyId);
        goods.setType(goodsBean.getType());
        goods.setGoodsName(goodsBean.getGoodsName());
        goods.setMarketPrice(new BigDecimal(goodsBean.getMarketPrice()));
        goods.setGoodsNum(Long.parseLong(goodsBean.getGoodsNum()));
        goods.setGoodsFrozenNum((long)0);
        goods.setPoints((Long.parseLong(goodsBean.getPoints())));
        goods.setDetail(goodsBean.getDetail());
        goods.setIcon(goodsBean.getIcon());
        goods.setBigIcon(goodsBean.getBigIcon());
        this.insertOrUpdate(goods);
        return "操作成功";
    }

    @Override
    public Object getGoodsList(GoodsBean goodsBean,Long companyId) {
        PageBean pageBean = goodsBean.getPageBean();
        if (null == pageBean){
            pageBean = new PageBean();
        }
        Integer startPage = (pageBean.getPageNum() - 1)*pageBean.getPageSize();
        EntityWrapper<Goods> wrapper = new EntityWrapper<>();
        wrapper.eq("company_id",companyId);
        if (StringUtils.isNotBlank(goodsBean.getGoodsNo())){
            wrapper.like("goods_no",goodsBean.getGoodsNo());
        }
        if (StringUtils.isNotBlank(goodsBean.getGoodsName())){
            wrapper.like("goods_name",goodsBean.getGoodsName());
        }
        int count = this.selectCount(wrapper);
        wrapper.orderBy("create_date",false);
        wrapper.last("limit "+startPage+","+pageBean.getPageSize());
        List<Goods> goodsList = this.selectList(wrapper);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("count",count);
        resultMap.put("goodsList",goodsList);
        resultMap.put("pageNum",pageBean.getPageNum());
        return resultMap;
    }

    @Override
    public Object getGoodsListByActivityId(String activityCode) {
        return goodsMapper.getGoodsListByActivityId(activityCode);
    }

    @Override
    public Object getGoodsDetail(String goodsNo) {
        return this.selectOne(new EntityWrapper<Goods>().eq("goods_no", goodsNo));
    }
}