package com.tzj.green.param;


import lombok.Data;

@Data
public class PointsListBean {

    private String startTime;

    private String endTime;

    private String name;

    private String tel;

    private String cityId;

    private String areaId;

    private String streetId;

    private String communityId;

    private String communityHouseId;

    private PageBean pageBean;

    private String pointsType;//0加分  1减分


}
