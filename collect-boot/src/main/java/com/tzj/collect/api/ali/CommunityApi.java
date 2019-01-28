package com.tzj.collect.api.ali;

import com.tzj.collect.api.ali.param.AreaBean;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Order;
import com.tzj.collect.service.CommunityService;
import com.tzj.collect.service.OrderService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 地区相关api
 * @Author 王美霞20180305
 **/
@ApiService
public class CommunityApi {
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private CommunityService communityService;
	/**
     * 根据区域id，取得该区域下所有小区 ，仅支持上一级，不支持跨层
     * @param 
     * @return
     */
    @Api(name = "community.areaCommunity", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<Community> areaCommunity(AreaBean area){
    	return communityService.areaCommunity(area.getId());
    }
	
    /**
     * 根据一级分类id获取该分类服务的回收企业，并取出这些企业服务的小区，并且去除重复
     * @param 
     * @return
     */
    @Api(name = "community.listareaByCategory", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<Community> listareaByCategory(CategoryBean categoryBean){
    	return communityService.listareaByCategory(categoryBean.getId(),categoryBean.getAreaId());
    }
    
    /**
     * 根据最近一次订单的地址，取出该地址。并且判断传入分类是否在该地址支持服务
     * @param 
     * @return
     */
    @Api(name = "community.defaultAddress", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Community defaultAddress(CategoryBean categoryBean){
    	Member  member  = MemberUtils.getMember();
    	Order order= orderService.getLastestOrderByMember(Integer.parseInt(member.getId()+""));
    	if(order !=null ){
    		Community community=communityService.defaultAddress(order.getCommunityId(), categoryBean.getId());
    		if(community==null)
        		return new Community(false);
        	else
        		community.setIsExist(true);
    		return  community;
    	}
    	return  new Community(false);
    }
    /**
     * 根据小区Id查询小区定点信息
     * @author 王灿
     * @param 
     * @return
     */
    @Api(name = "community.getCommunity", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getCommunity(CategoryBean categoryBean){
    	Community community = communityService.selectById(categoryBean.getCommunityId());
    	Map<String,Object> resultMap = null;
    	if(community!=null) {
    		resultMap = new HashMap<String,Object>();
    		String fixedPointTime = community.getFixedPointTime();
    		if(StringUtils.isNotBlank(fixedPointTime)) {
    			String[] fixedPoints = fixedPointTime.split(",");
    			resultMap.put("fixedPointTime", fixedPoints);
    			resultMap.put("fixedPointAddress", community.getFixedPointAddress());
    		}
    	}
    	return resultMap;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
