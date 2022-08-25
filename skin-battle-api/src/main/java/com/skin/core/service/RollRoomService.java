package com.skin.core.service;

import com.alipay.api.domain.Room;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.RollRoom;
import com.skin.entity.RollRoomSkinInfo;
import com.skin.entity.RollRoomUser;
import com.skin.vo.RollVo;

import java.math.BigDecimal;
import java.util.List;

public interface RollRoomService extends IService<RollRoom> {



    Page<RollRoom> getRoomPage(Integer pageNo, Integer pageSize,Integer roomType,String roomName);


    Page<RollRoomSkinInfo> getRoomSkinPage(Integer pageNo, Integer pageSize, String skinName,Long roomId);


    RollRoomSkinInfo getRoomSkinInfo(Long id);


    void delRoomSkinInfo(Long id );



    void addToRoom(Long SkinId,Long roomId);

    BigDecimal sumRoomPrice(Long roomId);



    Page<RollRoomUser> getRoomUserPage(Integer pageNo, Integer pageSize,Long roomId);

    void updateRoomSkinInfo(RollRoomSkinInfo rollRoomSkinInfo);


    Page<RollVo> getRoomPage(Integer pageNo, Integer pageSize, Integer status, Long  userId);

    RollRoom getInfo(Long rollRoomId,Long userId);

    Page<RollRoomUser> getRoomUserById(Integer pageNo, Integer pageSize, Long roomId);

    Page<RollRoomSkinInfo> getRoomSkinById(Integer pageNo, Integer pageSize, Long roomId);


    void join(Long userId,Long roomId,String roomPwd );


    List<RollRoom> getUnfinishRoom();


    void roll(Long rollId);

    boolean isJoin(Long roomId,String userId);

    Page<RollRoomUser> getRewardUser(Long roomId,Integer pageNum,Integer pageSize);

    BigDecimal rewardSumPrice(Long roomId);

}
