package com.tzj.collect.api.ali;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.ali.PiccOrderBean;
import com.tzj.collect.core.service.PiccOrderService;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.PiccOrder;
import static com.tzj.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@ApiService
public class PiccOrderApi {

    @Autowired
    private PiccOrderService piccOrderService;

    /**
     * 添加picc用户下的保险单
     *
     * @author 王灿
     * @param
     * @return List<Order>:未完成的订单列表
     */
    @Api(name = "piccOrder.insertPiccOrder", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String insertPiccOrder(PiccOrderBean piccOrderBean) throws Exception {
        Member member = MemberUtils.getMember();
        //查询该用户是否有保单
        List<PiccOrder> piccOrderList = piccOrderService.selectList(new EntityWrapper<PiccOrder>().eq("ali_user_id", member.getAliUserId()).eq("del_flag", 0).eq("insurance_id", piccOrderBean.getInsuranceId()));
        if (piccOrderList != null) {
            for (PiccOrder piccOrder : piccOrderList) {
                if (piccOrder.getStatus().getValue() != PiccOrder.PiccOrderType.NOOPEN.getValue()) {
                    return "您已存在保险或保险正在审核";
                }
            }
        }
        return piccOrderService.insertPiccOrder(member.getAliUserId(), piccOrderBean);
    }

    /**
     * 用户收取绿色能量
     *
     * @author 王灿
     * @param
     * @return List<Order>:未完成的订单列表
     */
    @Api(name = "piccOrder.updatePiccWater", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public String updatePiccWater(PiccOrderBean piccOrderBean) throws Exception {
        Member member = MemberUtils.getMember();
        String piccWaterId = piccOrderBean.getId();
        if (StringUtils.isBlank(piccWaterId)) {
            piccWaterId = "0";
        }
        return piccOrderService.updatePiccWater(member.getAliUserId(), Integer.parseInt(piccOrderBean.getId()));
    }

}
