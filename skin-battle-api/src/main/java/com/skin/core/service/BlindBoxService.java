package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.BlindBox;

public interface BlindBoxService extends IService<BlindBox> {


    Page<BlindBox> getPage(Integer pageNo, Integer pageSize,String boxName,String boxType);
}
