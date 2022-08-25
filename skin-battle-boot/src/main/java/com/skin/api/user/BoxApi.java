package com.skin.api.user;

import com.skin.common.util.AssertUtil;
import com.skin.core.service.BlindBoxService;
import com.skin.core.service.PullBoxService;
import com.skin.core.service.TakeOrderService;
import com.skin.entity.PullBox;
import com.skin.params.BlindBoxBean;
import com.skin.params.PullBoxBean;
import com.tzj.module.api.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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
    public Object pullOpenBox(PullBoxBean pullBoxBean) {
        AssertUtil.isTrue(pullBoxBean.getProbability()>75.00,"最大概率不能超过75%");
        return pullBoxService.openBox(UserApi.getMember().getId(),pullBoxBean.getId(),pullBoxBean.getProbability());
    }
    @Api(name = "pull.getPrice", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object pullGetPrice(PullBoxBean pullBoxBean) {
        AssertUtil.isTrue(pullBoxBean.getProbability()>75.00,"最大概率不能超过75%");
        PullBox pullBox = pullBoxService.getById(pullBoxBean.getId());
        BigDecimal totalPrice = pullBox.getPrice().multiply(new BigDecimal(String.valueOf(pullBoxBean.getProbability()))).setScale(2, BigDecimal.ROUND_HALF_UP);
        return totalPrice;
    }


    @Api(name = "pull.getSkinList", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object pullGetSkinList(PullBoxBean pullBoxBean) {
        return pullBoxService.getSkinList( pullBoxBean.getId());
    }


    @Api(name = "pull.getPage", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object pullGetPage(PullBoxBean pullBoxBean) {
        return pullBoxService.getPage(pullBoxBean.getPageBean().getPageNum(),pullBoxBean.getPageBean().getPageSize(),pullBoxBean.getSkinName()
        ,pullBoxBean.getBeginPrice(),pullBoxBean.getEndPrice());
    }


    @Api(name = "pull.pullLog", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object pullLog(PullBoxBean pullBoxBean) {
        return pullBoxService.getPullBoxLogPage(UserApi.getMember().getId(),pullBoxBean.getPageBean().getPageNum(),pullBoxBean.getPageBean().getPageSize());
    }
}
