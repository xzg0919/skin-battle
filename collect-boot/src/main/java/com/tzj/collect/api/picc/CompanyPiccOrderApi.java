package com.tzj.collect.api.picc;

import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.common.util.PiccCompanyUtils;
import com.tzj.collect.core.param.ali.PiccOrderBean;
import com.tzj.collect.core.service.PiccNumService;
import com.tzj.collect.core.service.PiccOrderService;
import com.tzj.collect.entity.PiccCompany;
import com.tzj.collect.entity.PiccOrder;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tzj.common.constant.TokenConst.PICC_API_COMMON_AUTHORITY;

/**
 * 保险订单表
 */
@ApiService
public class CompanyPiccOrderApi {

    @Autowired
    private PiccOrderService piccOrderService;
    @Autowired
    private PiccNumService piccNumService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * picc相关企业待处理保险单列表
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.selectPiccOrderList",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object selectPiccOrderList(PiccOrderBean piccOrderBean){
        PiccCompany piccCompany = PiccCompanyUtils.getPiccCompany();
        return piccOrderService.selectPiccOrderList(piccCompany.getId(),piccOrderBean);
    }

    /**
     * 批量删除/删除
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.deletePiccOrderList",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object deletePiccOrderList(PiccOrderBean piccOrderBean){
        return piccOrderService.deletePiccOrderList(piccOrderBean.getIds());
    }


    /**
     * picc企业审核未通过保险单列表
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.selectPiccErrorOrderList",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object selectPiccErrorOrderList(PiccOrderBean piccOrderBean){
        PiccCompany piccCompany = PiccCompanyUtils.getPiccCompany();
        return piccOrderService.selectPiccErrorOrderList(piccCompany.getId(), piccOrderBean);
    }

    /**
     * picc根据保单Id查询详细信息
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.selectPiccOrderDetial",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object selectPiccOrderDetial(PiccOrderBean piccOrderBean){
        PiccCompany piccCompany = PiccCompanyUtils.getPiccCompany();
        return piccOrderService.selectById(piccOrderBean.getId());
    }

    /**
     * picc企业审核通过的保险单列表
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.selectPiccSuccessOrderList",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object selectPiccSuccessOrderList(PiccOrderBean piccOrderBean){
        PiccCompany piccCompany = PiccCompanyUtils.getPiccCompany();
        return piccOrderService.selectPiccSuccessOrderList(piccCompany.getId(), piccOrderBean);
    }

    /**
     * picc企业确认导入保险订单详情
     * wangcan
     * @param
     * @return
     */
    @Api(name="picc.addPiccOrderExcel",version="1.0")
    @SignIgnore
    @RequiresPermissions(values = PICC_API_COMMON_AUTHORITY)
    public Object addPiccOrderExcel(){
        PiccCompany piccCompany = PiccCompanyUtils.getPiccCompany();
        List<PiccOrder> piccOrderList = ( List<PiccOrder>)redisUtil.get(piccCompany.getId()+"");
        if(null==piccOrderList){
            return "操作时间超时，请重新导入";
        }
        redisUtil.del(piccCompany.getId()+"");
        return piccOrderService.addPiccOrderExcel(piccCompany, piccOrderList);
    }


}
