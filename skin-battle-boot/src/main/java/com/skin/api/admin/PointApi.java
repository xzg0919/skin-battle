package com.skin.api.admin;

import com.skin.core.service.PointListService;
import com.skin.core.service.PointService;
import com.skin.params.PointBean;
import com.skin.params.UserBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 16:51
 * @Description:
 */
@ApiService
public class PointApi {


    @Autowired
    PointService pointService;

    @Autowired
    PointListService pointListService;



    @Api(name = "point.edit", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object editPoint(UserBean userBean){
        pointService.editPoint(userBean.getId(),userBean.getPoint());
        return "success";
    }

    @Api(name = "point.info", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getPointInfo(UserBean userBean){
        return pointService.getByUid(userBean.getId());
    }

    @Api(name = "point.listPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object editPoint(PointBean pointBean){
        return pointListService.getPointListPage(pointBean.getPageBean().getPageNum(), pointBean.getPageBean().getPageSize(),
                pointBean.getUserId(), pointBean.getOrderFrom(),pointBean.getOrderNo());
    }
}
