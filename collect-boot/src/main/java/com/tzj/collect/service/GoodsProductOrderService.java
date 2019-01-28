package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.GoodsProductOrder;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.MemberAddress;
import com.tzj.collect.entity.Point;
import com.tzj.collect.entity.Product;

public interface GoodsProductOrderService extends IService<GoodsProductOrder> {
	
	/**
     * 给用户发放实物
     * @author 王灿
     * @param product : 商品信息
     * @param member : 会员信息
     * @param point : 积分信息
     */
	public Object sendGoodsProduct(Product product,Member member,Point point,MemberAddress memberAddress);
}
