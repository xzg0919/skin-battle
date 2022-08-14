package com.skin.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.BlindBoxMapper;
import com.skin.core.service.BlindBoxService;
import com.skin.entity.BlindBox;

/**
 * @Auther: xiang
 * @Date: 2022/8/14 16:04
 * @Description:
 */
public class BlindBoxServiceImpl extends ServiceImpl<BlindBoxMapper, BlindBox> implements BlindBoxService {


    @Override
    public Page<BlindBox> getPage(Integer pageNo, Integer pageSize, String boxName, String boxType) {
        return null;
    }
}
