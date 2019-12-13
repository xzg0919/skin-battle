package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyEquipmentMapper;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.core.service.CompanyEquipmentService;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.collect.entity.EquipmentErrorList;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sgmark
 * @create 2019-04-02 15:25
 **/
@Service
@Transactional(readOnly = true)
public class CompanyEquipmentServiceImpl extends ServiceImpl<CompanyEquipmentMapper, CompanyEquipment> implements CompanyEquipmentService {

    @Resource
    private CompanyEquipmentMapper companyEquipmentMapper;

    /**
     * 查询iot设备订单数人数统计
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/13 0013
     * @Param:
     * @return:
     */
    @Override
    public Page<Map<String, Object>> adminIotOrderPage(AdminIotErrorBean adminIotErrorBean) {
        EntityWrapper<CompanyEquipment> entityWrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(adminIotErrorBean.getEquipmentCode())){
            entityWrapper.eq("equipment_code", adminIotErrorBean.getEquipmentCode());
        }
        if (!StringUtils.isEmpty(adminIotErrorBean.getEndTime()) && !StringUtils.isEmpty(adminIotErrorBean.getStartTime())){
            adminIotErrorBean.setEndTime(adminIotErrorBean.getEndTime() + " 23:59:59");
        }
        Page<CompanyEquipment> equipmentErrorListPage = this.selectPage(new Page(adminIotErrorBean.getPageBean().getPageNumber(), adminIotErrorBean.getPageBean().getPageSize()), entityWrapper);
        Page<Map<String, Object>> returnPage = new Page(adminIotErrorBean.getPageBean().getPageNumber(), adminIotErrorBean.getPageBean().getPageSize());
        returnPage.setTotal(equipmentErrorListPage.getTotal());
        returnPage.setRecords(companyEquipmentMapper.adminIotOrderPage(adminIotErrorBean.getEquipmentCode(), adminIotErrorBean.getCompanyId(), adminIotErrorBean.getStartTime(), adminIotErrorBean.getEndTime(), (adminIotErrorBean.getPageBean().getPageNumber()-1)*adminIotErrorBean.getPageBean().getPageSize(), adminIotErrorBean.getPageBean().getPageSize()));
        return returnPage;
    }
    /**
     * 近15天iot转化率
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/13 0013
     * @Param:
     * @return:
     */
    @Override
//    @Cacheable(value = "iotConRate" , key = "iotConRate",  sync = true)
    public String iotConRate() {
        return companyEquipmentMapper.iotConRate("15");
    }

}
