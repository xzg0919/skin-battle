package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.PullBox;
import com.skin.entity.PullBoxSkin;

public interface PullBoxService extends IService<PullBox> {


    Page<PullBox> getPage(Integer pageNo, Integer pageSize,String skinName);


    void changeStatus(Long id);


    Page<PullBoxSkin> getSkinPage(Integer pageNo, Integer pageSize,String skinName);

    void delSkin(Long skinId);

    PullBoxSkin getSkinById(Long skinId);

    void updateSkin(PullBoxSkin pullBoxSkin);


    void insertSkin(Long skinId,Long pullBoxId,double probability);
}
