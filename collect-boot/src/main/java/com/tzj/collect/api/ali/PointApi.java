package com.tzj.collect.api.ali;

import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.service.PointListService;
import com.tzj.collect.core.service.PointService;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Point;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 积分相关api
 * @Author 王美霞20180305
 **/
@ApiService
public class PointApi {
	
	@Autowired
	private PointService pointService;
	@Autowired
	private PointListService pointListService;
	
	/** 
     * 根据类型获取积分流水列表 分页
     * @author 王灿
     * @param 
     * @return
     */
    @Api(name = "point.getPointListByType", version = "1.0")
    //@AuthIgnore //这个api忽略token验证
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<Object> getPointListByType(PageBean pageBean){
        //接口里面获取  Member 的例子
        Member member= MemberUtils.getMember();
        //获取用户积分流水表
        List<Object> pointLists = pointListService.getPointListByType(member.getAliUserId(),pageBean);
    	return pointLists; 
    }
	
	
	/**
     * 获取积分值接口
     * @author 王灿
     * @param 
     * @return
     */
    @Api(name = "point.getPoint", version = "1.0")
    //@AuthIgnore //这个api忽略token验证
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Point getPoint(){
        //接口里面获取  Member 的例子
        Member member= MemberUtils.getMember();
    	Point points = pointService.getPoint(member.getAliUserId());
    	return points;
    }

    /**
     * 获取环保能量及其流水接口
     * @author 王灿
     * @param
     * @return
     */
    @Api(name = "point.getPointLists", version = "1.0")
    //@AuthIgnore //这个api忽略token验证
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getPointLists(){
        //接口里面获取  Member 的例子
        Member member= MemberUtils.getMember();
        return pointService.getPointLists(member.getAliUserId());
    }
	
	
    
}
