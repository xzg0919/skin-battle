package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;

import com.tzj.green.entity.Area;
import com.tzj.green.entity.CommunityHouseName;
import com.tzj.green.entity.CompanyCommunity;
import com.tzj.green.mapper.CompanyCommunityMapper;
import com.tzj.green.param.CompanyCommunityBean;
import com.tzj.green.param.PageBean;
import com.tzj.green.service.AreaService;
import com.tzj.green.service.CommunityHouseNameService;
import com.tzj.green.service.CompanyCommunityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [社区表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CompanyCommunityServiceImpl extends ServiceImpl<CompanyCommunityMapper, CompanyCommunity> implements CompanyCommunityService
{
    @Resource
    private CompanyCommunityMapper companyCommunityMapper;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CommunityHouseNameService communityHouseNameService;


    @Override
    @Transactional
    public Object saveCompanyCommunity(CompanyCommunityBean companyCommunityBean,Long companyId) {
        CompanyCommunity companyCommunity = this.selectById(companyCommunityBean.getId());
        if (null==companyCommunity){
            companyCommunity = new CompanyCommunity();
        }
        Area province = areaService.selectById(companyCommunityBean.getProvinceId());
        Area city = areaService.selectById(companyCommunityBean.getCityId());
        Area area = areaService.selectById(companyCommunityBean.getAreaId());
        Area street = areaService.selectById(companyCommunityBean.getStreetId());
        companyCommunity.setCompanyId(companyId);
        companyCommunity.setProvinceId(province.getId());
        companyCommunity.setProvinceName(province.getAreaName());
        companyCommunity.setCityId(city.getId());
        companyCommunity.setCityName(city.getAreaName());
        companyCommunity.setAreaId(area.getId());
        companyCommunity.setAreaName(area.getAreaName());
        companyCommunity.setStreetId(street.getId());
        companyCommunity.setStreetName(street.getAreaName());
        companyCommunity.setCommunityName(companyCommunityBean.getCommunityName());
        companyCommunity.setHouseNum(companyCommunityBean.getHouseNum());
        companyCommunity.setPointsNum(Long.parseLong(companyCommunityBean.getPointsNum()));
        companyCommunity.setPutType(companyCommunityBean.getPutType());
        if ("0".equals(companyCommunityBean.getIsDry())){
            companyCommunity.setDryTime(companyCommunityBean.getDryTime());
        }else if ("1".equals(companyCommunityBean.getIsDry())){
            companyCommunity.setDryTime(" ");
        }
        if ("0".equals(companyCommunityBean.getIsWet())){
            companyCommunity.setWetTime(companyCommunityBean.getWetTime());
        }else if ("1".equals(companyCommunityBean.getIsWet())){
            companyCommunity.setWetTime(" ");
        }
        if ("0".equals(companyCommunityBean.getIsHarmful())){
            companyCommunity.setHarmfulTime(companyCommunityBean.getHarmfulTime());
        }else if ("1".equals(companyCommunityBean.getIsHarmful())){
            companyCommunity.setHarmfulTime(" ");
        }
        if ("0".equals(companyCommunityBean.getIsRecovery())){
            companyCommunity.setRecoveryTime(companyCommunityBean.getRecoveryTime());
            companyCommunity.setRecoveryWeek(companyCommunityBean.getRecoveryWeek());
        }else if ("1".equals(companyCommunityBean.getIsRecovery())){
            companyCommunity.setRecoveryTime(" ");
            companyCommunity.setRecoveryWeek(" ");
        }
        this.insertOrUpdate(companyCommunity);
        Long communityId = companyCommunity.getId();
        List<CommunityHouseName> houseNameList = companyCommunityBean.getHouseNameList();
        CompanyCommunity finalCompanyCommunity = companyCommunity;
        if (null != houseNameList&&!houseNameList.isEmpty()){
            communityHouseNameService.delete(new EntityWrapper<CommunityHouseName>().eq("community_id",communityId));
        }
        houseNameList.stream().forEach(communityHouseName -> {
            CommunityHouseName communityHouseName1 = new CommunityHouseName();
            communityHouseName1.setCommunityId(finalCompanyCommunity.getId());
            communityHouseName1.setHouseName(communityHouseName.getHouseName());
            communityHouseName1.setAddress(communityHouseName.getAddress());
            communityHouseName1.setLat(communityHouseName.getLat());
            communityHouseName1.setLng(communityHouseName.getLng());
            communityHouseNameService.insertOrUpdate(communityHouseName1);
        });
        return "操作成功";
    }

    @Override
    public Object getCompanyCommunityById(String id) {
        CompanyCommunity companyCommunity = this.selectById(id);
        List<CommunityHouseName> communityHouseNameList = communityHouseNameService.selectList(new EntityWrapper<CommunityHouseName>().eq("community_id", id));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("companyCommunity",companyCommunity);
        resultMap.put("communityHouseNameList",communityHouseNameList);
        List<Map<String,Object>> communityTimeList = new ArrayList<>();
        if (StringUtils.isNotBlank(companyCommunity.getDryTime())){
            Map<String,Object> dryMap = new HashMap<>();
            dryMap.put("time",companyCommunity.getDryTime());
            dryMap.put("name","干垃圾");
            communityTimeList.add(dryMap);
        }
        if (StringUtils.isNotBlank(companyCommunity.getWetTime())){
            Map<String,Object> wetMap = new HashMap<>();
            wetMap.put("time",companyCommunity.getWetTime());
            wetMap.put("name","湿垃圾");
            communityTimeList.add(wetMap);
        }
        if (StringUtils.isNotBlank(companyCommunity.getHarmfulTime())){
            Map<String,Object> harmfulMap = new HashMap<>();
            harmfulMap.put("time",companyCommunity.getHarmfulTime());
            harmfulMap.put("name","有害垃圾");
            communityTimeList.add(harmfulMap);
        }
        if (StringUtils.isNotBlank(companyCommunity.getRecoveryTime())){
            Map<String,Object> recoveryMap = new HashMap<>();
            recoveryMap.put("time",companyCommunity.getRecoveryTime());
            recoveryMap.put("name","可回收垃圾");
            communityTimeList.add(recoveryMap);
        }
        resultMap.put("communityTimeList",communityTimeList);
        return resultMap;
    }

    @Override
    public Object getCompanyCommunityList(CompanyCommunityBean companyCommunityBean,Long companyId) {
        PageBean pageBean = companyCommunityBean.getPageBean();
        if (null == pageBean){
            pageBean = new PageBean();
        }
        Integer pageStartNum = (pageBean.getPageNum()-1)*pageBean.getPageSize();

        List<Map<String, Object>> companyCommunityList = companyCommunityMapper.getCompanyCommunityList(companyCommunityBean.getCommunityNo(),companyId, companyCommunityBean.getProvinceId(), companyCommunityBean.getCityId(), companyCommunityBean.getAreaId(), companyCommunityBean.getStreetId(), companyCommunityBean.getCommunityName(),  companyCommunityBean.getCommunityHouseName(), pageStartNum, pageBean.getPageSize());

        Integer count = companyCommunityMapper.getCompanyCommunityCount(companyCommunityBean.getCommunityNo(),companyId, companyCommunityBean.getProvinceId(), companyCommunityBean.getCityId(), companyCommunityBean.getAreaId(), companyCommunityBean.getStreetId(), companyCommunityBean.getCommunityName(), companyCommunityBean.getCommunityHouseName());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("companyCommunityList",companyCommunityList);
        resultMap.put("count",count);
        resultMap.put("pageNum",pageBean.getPageNum());
        return resultMap;
    }

    @Override
    public Object getCompanyCommunityListByStreetId(CompanyCommunityBean companyCommunityBean, Long companyId) {
        return this.selectList(new EntityWrapper<CompanyCommunity>().eq("company_id", companyId).eq("street_id", companyCommunityBean.getStreetId()));
    }
    @Override
    public Object getCompanyHouseListByCommunityId(CompanyCommunityBean companyCommunityBean, Long companyId) {
        return communityHouseNameService.selectList(new EntityWrapper<CommunityHouseName>().eq("community_id", companyCommunityBean.getCommunityId()));
    }

    @Override
    public Object getRecyclerListByHouseId(CompanyCommunityBean companyCommunityBean, Long companyId) {
        return companyCommunityMapper.getRecyclerListByHouseId(companyCommunityBean.getCommunityHouseId(), companyId);
    }
}