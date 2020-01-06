package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.green.entity.Area;
import com.tzj.green.mapper.AreaMapper;
import com.tzj.green.service.AreaService;
import org.springframework.stereotype.Service;


@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {


    @Override
    public Object getProvinceList() {
        return this.selectList(new EntityWrapper<Area>().eq("type","0"));
    }
    @Override
    public Object getAreaList(String parentId){
        return this.selectList(new EntityWrapper<Area>().eq("parent_id",parentId));
    }
}
