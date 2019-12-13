package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.EquipmentErrorListMapper;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.core.param.iot.IotErrorParamBean;
import com.tzj.collect.core.service.EquipmentErrorListService;
import com.tzj.collect.entity.EquipmentErrorList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author sgmark
 * @create 2019-04-02 15:25
 **/
@Service
@Transactional(readOnly = true)
public class EquipmentErrorListServiceImpl extends ServiceImpl<EquipmentErrorListMapper, EquipmentErrorList> implements EquipmentErrorListService {

    @Resource
    private EquipmentErrorListMapper equipmentErrorListMapper;

    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> iotErrorListByMember(IotErrorParamBean iotErrorParamBean) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            EquipmentErrorList equipmentErrorList = new EquipmentErrorList();
            equipmentErrorList.setMobile(iotErrorParamBean.getMember().getMobile());
            equipmentErrorList.setLinkName(iotErrorParamBean.getMember().getLinkName());
            StringBuilder stringBuilder = new StringBuilder("");
            StringBuilder finalStringBuilder = stringBuilder;
            iotErrorParamBean.getErrorCodeList().stream().forEach(errorCode -> {
                finalStringBuilder.append(errorCode);
                finalStringBuilder.append(",");
            });
            equipmentErrorList.setErrorCode(stringBuilder.toString().substring(0,stringBuilder.toString().lastIndexOf(",")));
            stringBuilder = new StringBuilder("");
            StringBuilder finalStringBuilder1 = stringBuilder;
            iotErrorParamBean.getErrorMessageList().stream().forEach(errorMessage -> {
                finalStringBuilder1.append(errorMessage);
                finalStringBuilder1.append(",");
            });
            if (!StringUtils.isEmpty(iotErrorParamBean.getOtherMessage())){
                finalStringBuilder1.append(iotErrorParamBean.getOtherMessage());
                equipmentErrorList.setErrorMessage(finalStringBuilder1.toString());
            }else {
                equipmentErrorList.setErrorMessage(finalStringBuilder1.toString().substring(0,finalStringBuilder1.toString().lastIndexOf(",")));
            }
            equipmentErrorList.setEquipmentCode(iotErrorParamBean.getEquipmentCode());
            equipmentErrorList.setCompanyId(2l);
            equipmentErrorListMapper.insert(equipmentErrorList);
            returnMap.put("msg", "Y");
        }catch (Exception e){
            e.printStackTrace();
            returnMap.put("msg", "N");
        }
        return returnMap;
    }
    
    /**
     * iot申诉报错查询
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/13 0013
     * @Param: 
     * @return: 
     */
    @Override
    public Page<Map<String, Object>> adminIotErrorPage(AdminIotErrorBean adminIotErrorBean) {
        EntityWrapper<EquipmentErrorList> entityWrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(adminIotErrorBean.getStartTime()) && !StringUtils.isEmpty(adminIotErrorBean.getEndTime())){
            entityWrapper.between("create_date", adminIotErrorBean.getStartTime(), adminIotErrorBean.getEndTime() + " 23:59:59");
        }
        if (!StringUtils.isEmpty(adminIotErrorBean.getMobile())){
            entityWrapper.eq("mobile", adminIotErrorBean.getMobile());
        }
        if (!StringUtils.isEmpty(adminIotErrorBean.getLinkName())){
            entityWrapper.eq("link_name", adminIotErrorBean.getLinkName());
        }
        if (!StringUtils.isEmpty(adminIotErrorBean.getEquipmentCode())){
            entityWrapper.eq("equipment_code", adminIotErrorBean.getEquipmentCode());
        }
        Page<EquipmentErrorList> equipmentErrorListPage = this.selectPage(new Page(adminIotErrorBean.getPageBean().getPageNumber(), adminIotErrorBean.getPageBean().getPageSize()), entityWrapper);
        Page<Map<String, Object>> returnPage = new Page(adminIotErrorBean.getPageBean().getPageNumber(), adminIotErrorBean.getPageBean().getPageSize());
        returnPage.setTotal(equipmentErrorListPage.getTotal());
        List<Map<String, Object>> mapList = new ArrayList<>();
        equipmentErrorListPage.getRecords().stream().forEach(equipmentErrorList -> {
            Map<String, Object> map = new HashMap<>();
            map.put("link_name", equipmentErrorList.getLinkName());
            map.put("mobile", equipmentErrorList.getMobile());
            map.put("equipmentCode", equipmentErrorList.getEquipmentCode());
            map.put("create_date", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(equipmentErrorList.getCreateDate().toInstant()
                    .atZone( ZoneId.systemDefault() )
                    .toLocalDateTime()));
            List<String> errorlist = Arrays.asList(equipmentErrorList.getErrorMessage().split(","));
            map.put("errorList", errorlist);
            if (errorlist.size() >= 1){
                if (errorlist.get(0).length() >= 8){
                    map.put("error", errorlist.get(0).substring(0,8)+"...");
                }else {
                    map.put("error", errorlist.get(0));
                }
            }
            mapList.add(map);
        });
        returnPage.setRecords(mapList);
        returnPage.setCurrent(equipmentErrorListPage.getCurrent());
        returnPage.setSize(equipmentErrorListPage.getSize());
        return returnPage;
    }
}
