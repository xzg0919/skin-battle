package com.skin.api.user;

import com.skin.core.service.BlindBoxService;
import com.skin.core.service.TakeOrderService;
import com.skin.params.BlindBoxBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/17 16:33
 * @Description:
 */

@ApiService
public class BoxApi {

    @Autowired
    TakeOrderService takeOrderService;
    @Autowired
    BlindBoxService     blindBoxService;

    @Api(name = "box.getBoxByType", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getBox(BlindBoxBean blindBoxBean) {
        return blindBoxService.getBoxByType(blindBoxBean.getPageBean().getPageNum(), blindBoxBean.getPageBean().getPageSize(), blindBoxBean.getBoxType());
    }


    @Api(name = "box.getLastPageList", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getLastPageList() {
        return takeOrderService.getLastPageList( );
    }
}
