package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import javax.annotation.Resource;

import com.tzj.green.entity.Goods;
import com.tzj.green.entity.Product;
import com.tzj.green.entity.ProductGoods;
import com.tzj.green.entity.ProductRecycler;
import com.tzj.green.mapper.ProductMapper;
import com.tzj.green.param.PageBean;
import com.tzj.green.param.ProductBean;
import com.tzj.green.param.ProductGoodsBean;
import com.tzj.green.service.GoodsService;
import com.tzj.green.service.ProductGoodsService;
import com.tzj.green.service.ProductRecyclerService;
import com.tzj.green.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [活动表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 *
 * @author [王灿]
 * @version 1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Resource
    private ProductMapper productMapper;
    @Autowired
    private ProductRecyclerService productRecyclerService;
    @Autowired
    private ProductGoodsService productGoodsService;
    @Autowired
    private GoodsService goodsService;

    @Override
    @Transactional
    public Object saveOrUpdateProduct(ProductBean productBean, Long companyId) {
        Product product = this.selectById(productBean.getId());
        if (null == product) {
            product = new Product();
        }
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        product.setCompanyId(companyId);
        product.setName(productBean.getName());
        try {
            product.setPickStartDate(sim.parse(productBean.getPickStartDate()));
            product.setPickEndDate(sim.parse(productBean.getPickEndDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        product.setHouseNameId(Long.parseLong(productBean.getHouseNameId()));
        product.setDetail(productBean.getDetail());
        product.setExchangeNum((long) 0);
        product.setExchangePoints((long) 0);
        product.setIsLower("0");
        this.insertOrUpdate(product);
        Long productId = product.getId();
        List<String> recyclerIds = productBean.getRecyclerIds();
        recyclerIds.stream().forEach(s -> {
            ProductRecycler productRecycler = productRecyclerService.selectOne(new EntityWrapper<ProductRecycler>().eq("product_id", productId).eq("recyclers_id", s));
            if (null == productRecycler) {
                productRecycler = new ProductRecycler();
            }
            productRecycler.setProductId(productId);
            productRecycler.setRecyclersId(Long.parseLong(s));
            productRecyclerService.insertOrUpdate(productRecycler);
        });
        List<ProductGoodsBean> productGoodsBeanList = productBean.getProductGoodsBeanList();
        productGoodsBeanList.stream().forEach(productGoodsBean -> {
            ProductGoods productGoods = productGoodsService.selectOne(new EntityWrapper<ProductGoods>().eq("goods_id", productGoodsBean.getGoodsId()).eq("product_id", productId));
            if (null == productGoods) {
                productGoods = new ProductGoods();
            }
            productGoods.setProductId(productId);
            productGoods.setGoodsId(Long.parseLong(productGoodsBean.getGoodsId()));
            productGoods.setTotalNum(Long.parseLong(productGoodsBean.getTotalNum()));
            productGoodsService.insertOrUpdate(productGoods);
            Goods goods = goodsService.selectById(productGoodsBean.getGoodsId());
            goods.setGoodsFrozenNum(goods.getGoodsFrozenNum() + Long.parseLong(productGoodsBean.getTotalNum()));
            goodsService.updateById(goods);
        });
        return "操作成功";
    }

    @Override
    public Object getProductList(ProductBean productBean, Long companyId) {
        PageBean pageBean = productBean.getPageBean();
        if (null == pageBean) {
            pageBean = new PageBean();
        }
        Integer pageStart = (pageBean.getPageNum() - 1) * pageBean.getPageSize();
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.eq("company_id", companyId);
        if (StringUtils.isNotBlank(productBean.getPickStartDate())) {
            wrapper.ge("pick_end_date", productBean.getPickStartDate());
        }
        if (StringUtils.isNotBlank(productBean.getPickEndDate())) {
            wrapper.le("pick_start_date", productBean.getPickEndDate());
        }
        if (StringUtils.isNotBlank(productBean.getName())) {
            wrapper.like("name_", productBean.getName());
        }
        if (StringUtils.isNotBlank(productBean.getProductNo())) {
            wrapper.like("product_no", productBean.getProductNo());
        }
        if (StringUtils.isNotBlank(productBean.getIsLower())) {
            wrapper.eq("is_lower", productBean.getIsLower());
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String now = df.format(date);
            if ("0".equals(productBean.getIsLower())) {
                wrapper.addFilter(" pick_end_date >= '" + now + "' AND pick_start_date <= '" + now + "'");
            } else if ("1".equals(productBean.getIsLower())) {
                wrapper.addFilter(" (pick_end_date >= '" + now + "' OR pick_start_date <= '" + now + "') ");
            }
        }
        int count = this.selectCount(wrapper);
        wrapper.orderBy("create_date", false);
        wrapper.last(" limit " + pageStart + " , " + pageBean.getPageSize());
        List<Product> productList = this.selectList(wrapper);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("count", count);
        resultMap.put("productList", productList);
        resultMap.put("pageNum", pageBean.getPageNum());
        return resultMap;
    }

    @Override
    public Object getProductDetailById(ProductBean productBean) {
        Long productId = Long.parseLong(productBean.getId());
        PageBean pageBean = productBean.getPageBean();
        if (null == pageBean) {
            pageBean = new PageBean();
        }
        Integer pageStart = (pageBean.getPageNum() - 1) * pageBean.getPageSize();
        Map<String, Object> product = productMapper.getProductDetailById(productId);
        List<Map<String, Object>> ProductGoods = productMapper.getProductGoodsList(productId, pageStart, pageBean.getPageSize());
        Integer count = productMapper.getProductGoodsCount(productId);
        List<Map<String, Object>> recyclerList = productMapper.getRecyclerList(productId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("product", product);
        resultMap.put("ProductGoods", ProductGoods);
        resultMap.put("recyclerList", recyclerList);
        resultMap.put("count", count);
        resultMap.put("pageNum", pageBean.getPageNum());
        return resultMap;
    }

    @Override
    @Transactional
    public Object updateProductIsLowerById(String productId, String isLower) {

        Product product = this.selectById(productId);
        product.setIsLower(isLower);
        this.updateById(product);
        List<ProductGoods> productGoods = productGoodsService.selectList(new EntityWrapper<ProductGoods>().eq("product_id", productId));
        productGoods.stream().forEach(productGoods1 -> {
            Goods goods = goodsService.selectById(productGoods1.getGoodsId());
            if ("0".equals(isLower)) {
                goods.setGoodsFrozenNum(goods.getGoodsFrozenNum() + productGoods1.getTotalNum() - productGoods1.getExchangeNum());
            } else {
                goods.setGoodsFrozenNum(goods.getGoodsFrozenNum() - productGoods1.getTotalNum() + productGoods1.getExchangeNum());
            }
            goodsService.updateById(goods);
        });
        return "操作成功";
    }


    /**
     * 根据经纬度和公司id获取用户附近三公里的活动
     *
     * @param lat
     * @param lng
     * @param companyId
     * @return
     */
    @Override
    public Object nearActivitys(Double lat, Double lng, Long companyId) {
        return  productMapper.nearActivitys(companyId, lat, lng);
    }

}