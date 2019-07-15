package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.GoodsProductOrderMapper;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.collect.entity.GoodsProductOrder.GoodsState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
@Transactional(readOnly=true)
public class GoodsProductOrderServiceImpl extends ServiceImpl<GoodsProductOrderMapper, GoodsProductOrder> implements GoodsProductOrderService {
	@Autowired
	private ProductService productService;
	@Autowired
	private PointService pointService;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private PointListService pointListService;
	@Autowired
	private MemberAddressService memberAddressService;
	
	/**
     * 给用户发放实物
     * @author 王灿
     * @param product : 商品信息
     * @param member : 会员信息
     * @param point : 积分信息
     */
	@Override
	@Transactional
	public Object sendGoodsProduct(Product product, Member member, Point point, MemberAddress memberAddress) {
		//增加实物兑换记录订单
		GoodsProductOrder goodsProductOrder = new GoodsProductOrder();
		goodsProductOrder.setUserName(memberAddress.getName());
		goodsProductOrder.setMobile(memberAddress.getTel());
		goodsProductOrder.setAddress(memberAddress.getAddress());
		goodsProductOrder.setProductId(product.getId());
		goodsProductOrder.setMemberId(Integer.parseInt(member.getId().toString()));
		goodsProductOrder.setAliUserId(member.getAliUserId());
		goodsProductOrder.setProductName(product.getBrand());
		goodsProductOrder.setProductUrl(product.getImg());
		goodsProductOrder.setGoodsState(GoodsState.INIT);
		this.insert(goodsProductOrder);
		
		//发实物成功时......更新已兑换数量+1
		product.setBindingQuantity(product.getBindingQuantity()+1);
		productService.updateById(product);
		//用户剩余积分
		double remainPoint=0;
		if(point!=null) {
			remainPoint = point.getRemainPoint()-((double)product.getBindingPoint());
			//扣除用户本地积分
			point.setRemainPoint(remainPoint);
			pointService.updateById(point);
		}
		System.out.println("给用户发券扣除的积分是 ："+product.getBindingPoint() + "----剩余point是 : "+remainPoint+"");
		try {
		//给用户会员卡扣除相应积分
		aliPayService.updatePoint(member.getAliCardNo(), member.getOpenCardDate(), remainPoint+"", null,member.getAppId());
		}catch(Exception e) {
			System.out.println("扣除用户积分失败---------------");
		}
		//增加相应的积分记录
		if(product.getBindingPoint()!=0) {
			PointList pointList =new  PointList();
			pointList.setMemberId(Integer.parseInt(member.getId().toString()));
			pointList.setPoint("-"+product.getBindingPoint());
			pointList.setType("1");
			pointList.setDocumentNo(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000));
			pointList.setDescrb(product.getBrand());
			pointListService.insert(pointList);
		}
		return "兑换成功";
	}

}
