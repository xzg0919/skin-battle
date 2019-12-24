package com.tzj.iot.api.equipment.app;

import com.tzj.collect.common.util.CompanyEquipmentUtils;
import com.tzj.collect.common.util.MemberUtils;
import com.tzj.collect.core.param.iot.IotCompanyResult;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.core.service.impl.FileUploadServiceImpl;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.module.api.annotation.*;
import com.tzj.module.easyopen.doc.annotation.ApiDoc;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Resource
    private FileUploadServiceImpl fileUploadServiceImpl;
    @Resource
    private CompanyService companyService;
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
     *  获取动态二维码
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/10 0010
     * @Param: 
     * @return: 
     */
    @Api(name = "equipment.qrcode", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public Map<String, Object> qrCode() {
        Map<String, Object> returnMap = new HashMap<>();
        CompanyEquipment equipment = CompanyEquipmentUtils.getMember();
        IotCompanyResult iotCompanyResult = companyService.selectIotUrlByEquipmentCode(equipment.getEquipmentCode());
        String aliUrl = "alipays://platformapi/startapp?appId=2018060660292753&page=pages/view/index/index&query=";
        String encodeString = URLEncoder.encode("tranTime="+ System.currentTimeMillis()+ "&ecUuid="+UUID.randomUUID().toString().substring(0,15)+ "&cabinetNo="+equipment.getEquipmentCode());
        returnMap.put("qrCode", aliUrl+URLEncoder.encode("qrCode=Y&type=appliance&sourceId="+equipment.getEquipmentCode()+"&qrUrl="+iotCompanyResult.getIotUrl()+"?"+encodeString));
        return returnMap;
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
    /**
     * 上传文件0
     */
    @Api(name = "equipment.upload.image", version = "1.0")
    @RequiresPermissions(values = EQUIPMENT_APP_API_COMMON_AUTHORITY)
    public List<FileBean> uploadImage(List<FileBase64Param> fileBase64ParamLists){
        return fileUploadServiceImpl.uploadImageForIot(fileBase64ParamLists);
    }

}
