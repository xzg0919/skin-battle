package com.skin.core.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.BoxBattle;
import com.skin.entity.BoxBattleInfo;
import com.skin.entity.BoxBattleSkin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BoxBattleService extends IService<BoxBattle> {


    Page<BoxBattle> getPage(Integer pageNo, Integer pageSize, String boxName);


    void changeStatus(Long id );


    Page<BoxBattleSkin> getSkinPage(Integer pageNo, Integer pageSize, String skinName);

    void delBoxSkin(Long id );


    BoxBattleSkin getSkinById(Long id );

    void updateSkin(BoxBattleSkin boxBattleSkin);

    void insertSkin(Long boxId,Long skinId,double probability);

    void  createBattleBox(Long userId,String boxIds,Integer userCount);


    boolean join(Long userId,Long boxBattleId);

    List<BoxBattleInfo> getUnfinishList();


    void finishBattle(Long boxBattleId);


    Page<BoxBattleInfo> getBattleInfoPage(Integer pageNum,Integer pageSize,Long userId);


    Page<BoxBattle> getBoxPage(Integer pageNum,Integer pageSize);


    HashMap<String,Object> getBattleInfo(Long battleInfoId);


    Map<String,Object> getSkinList(Long boxId);

    Page<BoxBattleInfo> getHistoryPage(Long userId,Integer pageNum,Integer pageSize );
}
