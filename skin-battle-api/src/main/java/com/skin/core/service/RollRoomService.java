package com.skin.core.service;

import com.alipay.api.domain.Room;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.RollRoom;
import com.skin.entity.RollRoomSkinInfo;
import com.skin.entity.RollRoomUser;

import java.math.BigDecimal;

public interface RollRoomService extends IService<RollRoom> {



    Page<RollRoom> getRoomPage(Integer pageNo, Integer pageSize,Integer roomType,String roomName);


    Page<RollRoomSkinInfo> getRoomSkinPage(Integer pageNo, Integer pageSize, String skinName,Long roomId);


    RollRoomSkinInfo getRoomSkinInfo(Long id);


    void delRoomSkinInfo(Long id );



    void addToRoom(Long SkinId,Long roomId);

    BigDecimal sumRoomPrice(Long roomId);



    Page<RollRoomUser> getRoomUserPage(Integer pageNo, Integer pageSize,Long roomId);

    void updateRoomSkinInfo(RollRoomSkinInfo rollRoomSkinInfo);
}
