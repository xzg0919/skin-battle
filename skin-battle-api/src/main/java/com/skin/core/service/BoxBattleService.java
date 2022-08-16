package com.skin.core.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.BoxBattle;
import com.skin.entity.BoxBattleSkin;

public interface BoxBattleService extends IService<BoxBattle> {


    Page<BoxBattle> getPage(Integer pageNo, Integer pageSize, String boxName);


    void changeStatus(Long id );


    Page<BoxBattleSkin> getSkinPage(Integer pageNo, Integer pageSize, String skinName);

    void delBoxSkin(Long id );


    BoxBattleSkin getSkinById(Long id );

    void updateSkin(BoxBattleSkin boxBattleSkin);

    void insertSkin(Long boxId,Long skinId,double probability);
}
