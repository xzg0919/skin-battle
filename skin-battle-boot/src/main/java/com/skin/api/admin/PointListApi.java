package com.skin.api.admin;

import com.skin.core.service.PointListService;
import com.skin.core.service.PointService;
import com.skin.core.service.SysParamsService;
import com.skin.core.service.TakeOrderService;
import com.skin.params.UserBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/15 16:19
 * @Description:
 */

@ApiService
public class PointListApi {

    @Autowired
    PointListService pointListService;
    @Autowired
    TakeOrderService takeOrderService;
    @Autowired
    PointService pointService;
    @Autowired
    SysParamsService sysParamsService;


    @Api(name = "pointList.dashboard", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pointListDashBoard() {
        String startTime = DateUtils.getDate() + " 00:00:00";
        String endTime = DateUtils.getDate() + " 23:59:59";

        HashMap<String, Object> resultMap = new HashMap<>();
        //平台总盈利
        resultMap.put("totalPoint", pointService.userPointSum());
        Integer addFlag = Integer.parseInt(sysParamsService.getSysParams("add").getVal());
        //平台今日盈利
        resultMap.put("totalPointToday", pointListService.sumPoint(addFlag, startTime, endTime));
        //已发货总价值
        resultMap.put("deliveredPoint", takeOrderService.totalPrice());
        //今日已发货价值
        resultMap.put("deliveredPointToday", takeOrderService.totalPrice(startTime, endTime));
        Integer blindBox = Integer.parseInt(sysParamsService.getSysParams("blind_box").getVal());
        //盲盒开盒次数
        resultMap.put("blindBoxOpenCount", pointListService.consumeCount(blindBox));
        //今日盲盒开盒次数
        resultMap.put("blindBoxOpenCountToday", pointListService.consumeCount(blindBox, startTime, endTime));
        //总盲盒开盒的价值
        resultMap.put("blindBoxPoint", pointListService.sumPoint(blindBox));
        //今日盲盒开盒的价值
        resultMap.put("blindBoxPointToday", pointListService.sumPoint(blindBox, startTime, endTime));
        //盲盒已发货的总价值
        resultMap.put("blindBoxDeliveredPoint", takeOrderService.totalPrice(1, blindBox));
        //今日盲盒已发货的价值
        resultMap.put("blindBoxDeliveredPointToday", takeOrderService.totalPrice(startTime, endTime, 1, blindBox));
        //已发货总价值
        resultMap.put("deliveredCount", takeOrderService.takeOrderCount());
        //今日已发货价值
        resultMap.put("deliveredCountToday", takeOrderService.takeOrderCount(startTime, endTime));
        return resultMap;
    }

}
