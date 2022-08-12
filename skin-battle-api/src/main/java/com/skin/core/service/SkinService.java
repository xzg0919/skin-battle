package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.Skin;

public interface SkinService extends IService<Skin> {


    Page<Skin> getSkinPage(Integer pageNum, Integer pageSize, String skinName );
}
