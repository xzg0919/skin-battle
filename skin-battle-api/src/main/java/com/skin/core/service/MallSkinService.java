package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.MallSkin;
import com.skin.entity.Skin;

public interface MallSkinService extends IService<MallSkin> {


    Page<MallSkin> getSkinPage(Integer pageNum, Integer pageSize, String skinName );


    Page<MallSkin> getSkinPage(Integer pageNum, Integer pageSize, String skinName, Integer isDesc );


    void exchangeSkin(Long userId, Long skinId);
}
