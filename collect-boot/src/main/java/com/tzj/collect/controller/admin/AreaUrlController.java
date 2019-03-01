package com.tzj.collect.controller.admin;


import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.RegionCity;
import com.tzj.collect.service.AreaService;
import com.tzj.collect.service.CommunityService;
import com.tzj.collect.service.RegionCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("area/url")
public class AreaUrlController {
    @Autowired
    private RegionCityService regionCityService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CommunityService communityService;


    @RequestMapping("/toXcxIndex")
    public  String toXcxIndex(String areaId,String companyId,String communityId){

        String url = "http://open.mayishoubei.com/area/url/toXcxIndex?areaId="+areaId+"&companyId="+companyId;
        Community community = communityService.selectById(communityId);
        Area area = areaService.selectById(areaId);
        System.out.println("进入城市入口了 : "+area.getAreaName());
        Area city = areaService.selectById(area.getParentId());
        Area province = areaService.selectById(city.getParentId());
        RegionCity regionCity = new RegionCity();
        regionCity.setCompanyId(Integer.parseInt(companyId));
        regionCity.setAreaId(area.getId().intValue());
        regionCity.setAreaName(area.getAreaName());
        if(community!=null){
            url += "&communityId="+communityId;
            regionCity.setCommunityId(community.getId().intValue());
            regionCity.setCommunityName(community.getName());
        }
        regionCity.setCityId(city.getId().intValue());
        regionCity.setCityName(city.getAreaName());
        regionCity.setProvinceId(province.getId().intValue());
        regionCity.setProvinceName(province.getAreaName());
        regionCity.setToUrl(url);
        regionCityService.insert(regionCity);
        return "admin/xcxIndex";
    }

}
