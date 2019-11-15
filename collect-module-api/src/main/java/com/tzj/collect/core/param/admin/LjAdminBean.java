package com.tzj.collect.core.param.admin;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class LjAdminBean {

    private String startDate;

    private String endDate;

    private String cityName;

    private String areaName;

    private String streetName;

    private String companyName;

    private String cityId;

    private String areaId;

    private String streetId;

    private String companyId;

    public String getCityId() {
        if (StringUtils.isNotBlank(cityId)){
            return cityId.replace(",","");
        }
        return cityId;
    }

    public String getAreaId() {
        if (StringUtils.isNotBlank(areaId)){
            return areaId.replace(",","");
        }
        return areaId;
    }

    public String getStreetId() {
        if (StringUtils.isNotBlank(streetId)){
            return streetId.replace(",","");
        }
        return streetId;
    }

    public String getCompanyId() {
        if (StringUtils.isNotBlank(companyId)){
            return companyId.replace(",","");
        }
        return companyId;
    }
}
