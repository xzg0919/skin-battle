package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;

import com.tzj.green.entity.*;
import com.tzj.green.mapper.ProductGoodsMapper;
import com.tzj.green.param.MemberGoodsBean;
import com.tzj.green.service.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [商品活动关联表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ProductGoodsServiceImpl extends ServiceImpl<ProductGoodsMapper, ProductGoods> implements ProductGoodsService
{
    @Resource
    private ProductGoodsMapper productGoodsMapper;
    @Resource
    private PointsListService pointsListService;
    @Resource
    private MemberService memberService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private ProductOrderService productOrderService;
    @Autowired
    private CompanyRecyclerService companyRecyclerService;
    @Autowired
    private ProductService productService;

    /**
     * 根据回收员小区地址查找当前小区活动商品列表
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/15 0015
     * @Param: 
     * @return: 
     */
    @Override
    public Set<Map<String, Object>> appGoodsList(Long recId) {
        List<Map<String, Object>> mapLists = productGoodsMapper.appProductList(recId);
        Set<Map<String, Object>> goodsLists = new HashSet<>();
        mapLists.stream().forEach(mapList -> {
            Long proId = Long.parseLong(mapList.get("id").toString());//根据活动id查找活动
            goodsLists.addAll(productGoodsMapper.appGoodsList(proId));
        });
        return goodsLists;
    }
    /**
     * 当前活动下所有商品列表
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/15 0015
     * @Param:
     * @return:
     */
    @Override
    public List<Map<String, Object>> appGoodsListByProId(String proId) {
        return productGoodsMapper.appGoodsList(Long.parseLong(proId));
    }
    /**
     * 当前小区下所有活动
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/15 0015
     * @Param:
     * @return:
     */
    @Override
    public List<Map<String, Object>> appProductList(Long recId) {
        return productGoodsMapper.appProductList(recId);
    }

    /**
     * app礼品兑换
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/15 0015
     * @Param: 
     * @return:  todo 异常做回滚处理
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> appGoodsChange(MemberGoodsBean memberGoodsBean) {
        Map<String, Object> returnMap = new HashMap<>();
        //检查数量是否足够兑换
        ProductGoods productGoods1 = this.selectOne(new EntityWrapper<ProductGoods>().eq("goods_id", memberGoodsBean.getGoodsId()).eq("product_id", memberGoodsBean.getProductId()));
        CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", memberGoodsBean.getRecId()).eq("status_", "1"));
        if (productGoods1.getTotalNum()-productGoods1.getExchangeNum() < memberGoodsBean.getAmount()){
            throw new ApiException("所兑换礼品剩余数量不足");
        }
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("del_flag", 0).eq("real_no", memberGoodsBean.getRealNo()).last(" limit 1"));
        Product product = productService.selectById(memberGoodsBean.getProductId());
        Goods goods = goodsService.selectById(memberGoodsBean.getGoodsId());
        if (null == member || null == goods){
            throw new ApiException("兑换用户不存在");
        }
        //扣除积分
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pointType", 1);
        paramMap.put("source", 1);
        //数据库查询所需积分
        paramMap.put("points", Math.abs(goods.getPoints() * memberGoodsBean.getAmount()));
        paramMap.put("recId", memberGoodsBean.getRecId());
        paramMap.put("companyId", companyRecycler.getCompanyId());
        paramMap.put("userNo", member.getRealNo());
        paramMap.put("userName", member.getName());
        if (StringUtils.isNotEmpty(member.getAliUserId())){
            paramMap.put("aliUserId", member.getAliUserId());
        }
        if (pointsListService.changePoint(memberGoodsBean.getRealNo(), paramMap)){
            //积分扣除成功
                //保存礼品兑换信息
                ProductOrder productOrder = new ProductOrder();
                productOrder.setGoodsId(memberGoodsBean.getGoodsId());
                productOrder.setProductId(memberGoodsBean.getProductId());
                productOrder.setProductName(product.getName());
                productOrder.setGoodsName(goods.getGoodsName());
                productOrder.setGoodsNum(memberGoodsBean.getAmount());
                Map<String, Object> map = goodsService.selectAddressByProId(memberGoodsBean.getProductId());
                if (!CollectionUtils.isEmpty(map)){
                    productOrder.setAddress(map.get("address_")+"");
                }
                productOrder.setUserName(member.getName());
                productOrder.setUserNo(member.getRealNo());
                productOrder.setTel(member.getMobile());
                productOrder.setPoints(Math.abs(goods.getPoints() * memberGoodsBean.getAmount()));
                productOrder.setRecyclerId(memberGoodsBean.getRecId());
                productOrder.setCompanyId(companyRecycler.getCompanyId());
                productOrderService.insert(productOrder);
                //更新剩余数量
                ProductGoods productGoods = this.selectOne(new EntityWrapper<ProductGoods>().eq("del_flag", 0).eq("goods_id",memberGoodsBean.getGoodsId()).eq("product_id", memberGoodsBean.getProductId()));
                productGoods.setExchangeNum(productGoods.getExchangeNum() + Math.abs(memberGoodsBean.getAmount()));
                this.updateById(productGoods);
                product.setExchangeNum(product.getExchangeNum()+memberGoodsBean.getAmount());
                product.setExchangePoints(product.getExchangePoints()+(goods.getPoints()*memberGoodsBean.getAmount()));
                productService.updateById(product);
                returnMap.put("msg", "兑换成功");
                returnMap.put("code", 200);
        }else {
            returnMap.put("msg", "兑换失败");
            returnMap.put("code", -9);
        }
        return returnMap;
    }
}