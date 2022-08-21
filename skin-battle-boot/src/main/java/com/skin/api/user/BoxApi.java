package com.skin.api.user;

import com.skin.core.service.BlindBoxService;
import com.skin.core.service.PullBoxService;
import com.skin.core.service.TakeOrderService;
import com.skin.params.BlindBoxBean;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static com.skin.common.constant.TokenConst.USER_API_COMMON_AUTHORITY;

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
    BlindBoxService blindBoxService;
    @Autowired
    PullBoxService pullBoxService;

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
        return takeOrderService.getLastPageList();
    }


    @Api(name = "box.openBox", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object openBox(BlindBoxBean blindBoxBean) {
        return blindBoxService.openBox(UserApi.getMember().getId(), blindBoxBean.getBoxId(), blindBoxBean.getNum());
    }

    @Api(name = "box.getBoxInfo", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getBoxInfo(BlindBoxBean blindBoxBean) {
        HashMap<String,Object> resultMap =new HashMap<>();
        resultMap.put("boxInfo",blindBoxService.getBoxById(blindBoxBean.getBoxId()));
        resultMap.put("skinList",blindBoxService.getSkinByBoxId(blindBoxBean.getBoxId()));
        return resultMap;
    }


    @Api(name = "box.recentOpen", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object recentOpen(BlindBoxBean blindBoxBean) {
        return takeOrderService.recentTakeOrder(blindBoxBean.getBoxId(),blindBoxBean.getPageBean().getPageNum(),blindBoxBean.getPageBean().getPageSize());
    }


    @Api(name = "pull.openBox", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = USER_API_COMMON_AUTHORITY)
    public Object pullOpenBox(BlindBoxBean blindBoxBean) {

        return null;
    }
}
