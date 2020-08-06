package com.tzj.collect.api.ali;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.CategoryBean;
import com.tzj.collect.core.service.CommunityService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.Community;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
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
     * 根据小区Id查询小区定点信息
     * @author 王灿
     * @param 
     * @return
     */
    @Api(name = "community.getCommunity", version = "1.0")
	@SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	@DS("slave")
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
