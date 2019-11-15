package com.tzj.iot.api.equipment.app;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.utils.MemberUtils;
import com.tzj.collect.core.param.app.EquipmentParamBean;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.collect.entity.EquipmentAdvert;
import com.tzj.collect.param.TokenBean;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.doc.DataType;
import com.tzj.module.easyopen.doc.annotation.ApiDoc;
import com.tzj.module.easyopen.doc.annotation.ApiDocField;
import com.tzj.module.easyopen.doc.annotation.ApiDocMethod;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * iot 设备 api
 * @author: sgmark@aliyun.com
 * @Date: 2019/11/13 0013
 * @Param: 
 * @return: 
 */
@ApiService
@ApiDoc(value = "APP iot 设备端api模块",appModule = "equipment")
public class EquipmentAppApi {

    @Resource
    private EquipmentAdvertService advertService;
    @Resource
    private EquipmentErrorListService equipmentErrorListService;
    @Resource
    private EquipmentErrorCodeService equipmentErrorCodeService;
    /**
     * 广告位
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/13 0013
     * @Param: 
     * @return: 
     */
    @Api(name = "equipment.advert", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public List<Map<String, Object>> advertList() {
        return advertService.iotEquipmentAdvertList();
    }

    /**
     * iot错误信息列表
     * @author: sgmark@aliyun.com
     * @Date: 2019/10/16 0016
     * @Param:
     * @return:
     */
    @Api(name = "equipment.error.code", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public List<Map<String, Object>> iotErrorCode(IotErrorParamBean iotErrorParamBean){
        return equipmentErrorCodeService.iotErrorCode(iotErrorParamBean);
    }
    /**
     * 用户上传iot错误信息
     * @author: sgmark@aliyun.com
     * @Date: 2019/10/16 0016
     * @Param:
     * @return:
     */
    @Api(name = "equipment.error.list.member", version = "1.0")
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Map<String, Object>  iotErrorListByMember(IotErrorParamBean iotErrorParamBean){
        iotErrorParamBean.setMember(MemberUtils.getMember());
        return equipmentErrorListService.iotErrorListByMember(iotErrorParamBean);
    }

}
