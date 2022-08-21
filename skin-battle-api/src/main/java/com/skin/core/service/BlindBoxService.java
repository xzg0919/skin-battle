package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.BlindBox;
import com.skin.entity.BlindBoxSkin;
import com.skin.entity.TakeOrder;

import java.util.List;

public interface BlindBoxService extends IService<BlindBox> {


    Page<BlindBox> getPage(Integer pageNo, Integer pageSize,String boxName,Long boxType);


    void changeStatus(Long id );


    Page<BlindBoxSkin> getSkinPage(Integer pageNo, Integer pageSize, String skinName);

    void delBoxSkin(Long id );


    BlindBoxSkin getSkinById(Long id );

    void updateSkin(BlindBoxSkin blindBoxSkin);

    void insertSkin(Long boxId,Long skinId,double probability);


    Page<BlindBox> getBoxByType(Integer pageNo, Integer pageSize,Long boxType);


    List<TakeOrder> openBox(Long userId,Long boxId,Integer num);

    BlindBox getBoxById(Long id);


    List<BlindBoxSkin> getSkinByBoxId(Long boxId);


}
