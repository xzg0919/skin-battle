package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.Area;

public interface AreaService extends IService<Area> {

    Object getProvinceList();

    Object getAreaList(String parentId);

    Object getCityList();
}
