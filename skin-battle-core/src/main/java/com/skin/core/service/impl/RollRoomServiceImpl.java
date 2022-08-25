package com.skin.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.RollRoomMapper;
import com.skin.core.mapper.RollRoomSkinInfoMapper;
import com.skin.core.mapper.RollRoomUserMapper;
import com.skin.core.service.*;
import com.skin.entity.*;
import com.skin.vo.RollVo;
import com.tzj.module.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: xiang
 * @Date: 2022/8/13 11:54
 * @Description:
 */

@Service
@Transactional(readOnly = true)
public class RollRoomServiceImpl extends ServiceImpl<RollRoomMapper, RollRoom> implements RollRoomService {


    @Autowired
    RollRoomSkinInfoMapper rollRoomSkinInfoMapper;
    @Autowired
    SkinService skinService;

    @Autowired
    RollRoomUserMapper rollRoomUserMapper;

    @Autowired
    PointListService pointListService;

    @Autowired
    UserService userService;

    @Autowired
    SysParamsService sysParamsService;

    @Autowired
    TakeOrderService takeOrderService;

    @Override
    public Page<RollRoom> getRoomPage(Integer pageNo, Integer pageSize, Integer roomType, String roomName) {
        QueryWrapper<RollRoom> queryWrapper = new QueryWrapper<RollRoom>().orderByDesc("create_date");
        if (roomType != null && roomType != 0) {
            queryWrapper.eq("room_type", roomType);
        }
        if (StringUtils.isNotBlank(roomName)) {
            queryWrapper.eq("name_", roomName);
        }
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Override
    public Page<RollRoomSkinInfo> getRoomSkinPage(Integer pageNo, Integer pageSize, String skinName, Long roomId) {
        QueryWrapper<RollRoomSkinInfo> queryWrapper = new QueryWrapper<RollRoomSkinInfo>().orderByDesc("create_date");
        queryWrapper.eq("room_id", roomId);
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.like("skin_name", skinName);
        }
        return rollRoomSkinInfoMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }


    @Override
    public RollRoomSkinInfo getRoomSkinInfo(Long id) {
        return rollRoomSkinInfoMapper.selectById(id);
    }

    @Transactional
    @Override
    public void delRoomSkinInfo(Long id) {
        rollRoomSkinInfoMapper.deleteById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addToRoom(Long SkinId, Long roomId) {
        RollRoom rollRoom = baseMapper.selectById(roomId);
        Optional.ofNullable(skinService.getById(SkinId)).ifPresent(skin -> {
            RollRoomSkinInfo sameInfo = rollRoomSkinInfoMapper.selectOne(new QueryWrapper<RollRoomSkinInfo>().eq("skin_name", skin.getName()).eq("room_id", roomId));
            AssertUtil.isNotNull(sameInfo, "该皮肤已经存在于该房间中");
            RollRoomSkinInfo rollRoomSkinInfo = new RollRoomSkinInfo();
            rollRoomSkinInfo.setSkinName(skin.getName());
            rollRoomSkinInfo.setPicUrl(skin.getPicUrl());
            rollRoomSkinInfo.setPrice(skin.getPrice());
            rollRoomSkinInfo.setAttritionRate(skin.getAttritionRate());
            rollRoomSkinInfo.setLevel(skin.getLevel());
            rollRoomSkinInfo.setRoomId(roomId);
            rollRoomSkinInfoMapper.insert(rollRoomSkinInfo);

            rollRoom.setAwardCount(rollRoom.getAwardCount() + 1);
            rollRoom.setAwardPrice(rollRoom.getAwardPrice().add(rollRoomSkinInfo.getPrice()));

            baseMapper.updateById(rollRoom);
        });
    }

    @Override
    public BigDecimal sumRoomPrice(Long roomId) {
        return rollRoomSkinInfoMapper.selectOne(new QueryWrapper<RollRoomSkinInfo>().eq("room_id", roomId).select("sum(price) as price")).getPrice();
    }

    @Override
    public Page<RollRoomUser> getRoomUserPage(Integer pageNo, Integer pageSize, Long roomId) {
        QueryWrapper<RollRoomUser> queryWrapper = new QueryWrapper<RollRoomUser>().orderByDesc("create_date");
        queryWrapper.eq("room_id", roomId);
        return rollRoomUserMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoomSkinInfo(RollRoomSkinInfo rollRoomSkinInfo) {
        rollRoomSkinInfoMapper.updateById(rollRoomSkinInfo);
    }

    @Override
    public Page<RollVo> getRoomPage(Integer pageNo, Integer pageSize, Integer status, Long userId) {
        return baseMapper.getRollPage(new Page<>(pageNo, pageSize), status, userId);
    }

    @Override
    public RollRoom getInfo(Long rollRoomId, Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("name_,room_type,lottery_time,condition_type,desc_,room_pic,room_status,user_count,award_count,award_price");
        queryWrapper.eq("id", rollRoomId);
        RollRoom rollRoom = baseMapper.selectOne(queryWrapper);
        rollRoom.setJoin(0);
        if (userId != null && userId != 0L) {
            QueryWrapper userWrapper = new QueryWrapper();
            userWrapper.eq("user_id", userId);
            userWrapper.eq("room_id", rollRoom.getId());
            if (rollRoomUserMapper.exists(userWrapper)) {
                rollRoom.setJoin(1);
            }
        }
        return rollRoom;
    }

    @Override
    public Page<RollRoomUser> getRoomUserById(Integer pageNo, Integer pageSize, Long roomId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("nick_name,avatar");
        queryWrapper.eq("room_id", roomId);
        queryWrapper.orderByDesc("create_date");
        return rollRoomUserMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Override
    public Page<RollRoomSkinInfo> getRoomSkinById(Integer pageNo, Integer pageSize, Long roomId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("pic_url,skin_name,price,attrition_rate,level_");
        queryWrapper.eq("room_id", roomId);
        queryWrapper.orderByDesc("price");
        return rollRoomUserMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void join(Long userId, Long roomId, String roomPwd) {
        RollRoom rollRoom = baseMapper.selectById(roomId);
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("user_id", userId);
        userWrapper.eq("room_id", rollRoom.getId());
        AssertUtil.isTrue(rollRoomUserMapper.exists(userWrapper), "不能重复加入");
        AssertUtil.isTrue(rollRoom.getRoomStatus() == 2, "活动已结束");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        AssertUtil.isTrue(calendar.getTime().getTime() > rollRoom.getLotteryTime().getTime(), "参与失败：开奖前五分钟停止参与");
        //判断参与条件
        AssertUtil.isTrue(rollRoom.getConditionType() == 1 && !rollRoom.getRoomPswd().equals(roomPwd), "参与失败：口令错误");
        if (rollRoom.getConditionType() == 0) {
            String startTime = DateUtils.formatDate(rollRoom.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
            String endTime = DateUtils.formatDate(rollRoom.getLotteryTime(), "yyyy-MM-dd HH:mm:ss");
            BigDecimal rechargeAmount = pointListService.sumPoint(2, startTime, endTime, userId);
            AssertUtil.isTrue(rechargeAmount.compareTo(rollRoom.getPrice()) < 0,
                    "参与失败：再充值" + rollRoom.getPrice().subtract(rechargeAmount) + "游戏币参与");
        }

        User user = userService.getById(userId);
        RollRoomUser rollRoomUser = new RollRoomUser();
        rollRoomUser.setRoomId(roomId);
        rollRoomUser.setUserId(userId);
        rollRoomUser.setNickName(user.getNickName());
        rollRoomUser.setTel(user.getTel());
        rollRoomUser.setAvatar(user.getAvatar());
        rollRoomUser.setEmail(user.getEmail());
        rollRoomUser.setUserIdStr(user.getUserId());
        rollRoom.setUserCount(rollRoom.getUserCount() + 1);
        baseMapper.updateById(rollRoom);
        rollRoomUserMapper.insert(rollRoomUser);
    }

    @Override
    public List<RollRoom> getUnfinishRoom() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("room_status", 1);
        return baseMapper.selectList(queryWrapper);
    }




    @Transactional(rollbackFor = Exception.class)
    @Override
    public void roll(Long rollId) {

        RollRoom rollRoom = baseMapper.selectById(rollId);
        System.out.println("正在执行roll房抽奖,ID:" + rollId);
        List<RollRoomSkinInfo> roomSkinInfos = rollRoomSkinInfoMapper.selectList
                (new QueryWrapper<RollRoomSkinInfo>().eq("room_id", rollId));
        System.out.println("皮肤：");
        roomSkinInfos.forEach(rollRoomSkinInfo -> {
            System.out.println("皮肤名称:"+rollRoomSkinInfo.getSkinName() + "指定人：" + rollRoomSkinInfo.getSpecifiedUser());
        });

        List<RollRoomUser> rollRoomUsers = rollRoomUserMapper.selectList
                (new QueryWrapper<RollRoomUser>().eq("room_id", rollId));
        rollRoomUsers.forEach(roomUser -> {
            System.out.println("userId:"+roomUser.getUserId() + "昵称：" + roomUser.getNickName());
        });

        Random random = new Random();

        List<RollRoomSkinInfo> roomSkinInfos1 = new ArrayList<>();

        //随机抽取与礼品数量一致的人

        HashMap<RollRoomUser, RollRoomSkinInfo> rollInfo = new HashMap<>();

        //先指定中奖
        roomSkinInfos.forEach(rollRoomSkinInfo -> {
            if (StringUtils.isNotBlank(rollRoomSkinInfo.getSpecifiedUser())) {
                RollRoomUser rollRoomUser1 = rollRoomUsers.stream()
                        .filter(rollRoomUser -> rollRoomUser.getUserIdStr()
                                .equals(rollRoomSkinInfo.getSpecifiedUser())).findAny().get();
                rollRoomUsers.remove(rollRoomUser1);
                rollInfo.put(rollRoomUser1, rollRoomSkinInfo);
            } else {
                roomSkinInfos1.add(rollRoomSkinInfo);
            }
        });


        //判断是人多奖少还是人少奖多情况

        if (roomSkinInfos1.size() > rollRoomUsers.size()) {
            for (RollRoomUser rollRoomUser : rollRoomUsers) {
                int index = random.nextInt(roomSkinInfos1.size());
                rollInfo.put(rollRoomUser, roomSkinInfos1.get(index));
                roomSkinInfos1.remove(roomSkinInfos1.get(index));
            }
        } else {
            for (RollRoomSkinInfo roomSkinInfo : roomSkinInfos) {
                int index = random.nextInt(rollRoomUsers.size());
                rollInfo.put(rollRoomUsers.get(index), roomSkinInfo);
                rollRoomUsers.remove(rollRoomUsers.get(index));
            }
        }
        List<TakeOrder> takeOrders = new ArrayList<>();
        System.out.println("中奖明细：");
        for (RollRoomUser rollRoomUser : rollInfo.keySet()) {
            System.out.println("昵称：" + rollRoomUser.getNickName() + "获得：" + rollInfo.get(rollRoomUser).getSkinName());
            RollRoomUser roomUser = new RollRoomUser();
            BeanUtils.copyProperties(rollRoomUser, roomUser);
            roomUser.setPrice(rollInfo.get(rollRoomUser).getPrice());
            roomUser.setSkinName(rollInfo.get(rollRoomUser).getSkinName());
            roomUser.setPicUrl(rollInfo.get(rollRoomUser).getPicUrl());
            roomUser.setAttritionRate(rollInfo.get(rollRoomUser).getAttritionRate());
            roomUser.setLevel(rollInfo.get(rollRoomUser).getLevel());
            roomUser.setRrsiId(rollInfo.get(rollRoomUser).getId());
            rollRoomUserMapper.updateById(roomUser);

            //存入背包

            User user = userService.getById(roomUser.getId());
            TakeOrder takeOrder = new TakeOrder();
            takeOrder.setBoxId(rollId);
            takeOrder.setUserId(roomUser.getUserId());
            String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
            takeOrder.setOrderNo(orderNo);
            takeOrder.setSource(Integer.parseInt(sysParamsService.getSysParams("roll_room").getVal()));
            takeOrder.setSkinName(roomUser.getSkinName());
            takeOrder.setNickName(roomUser.getNickName());
            takeOrder.setSteamUrl(user.getSteamUrl());
            takeOrder.setTel(user.getTel());
            takeOrder.setEmail(user.getEmail());
            takeOrder.setStatus(0);
            takeOrder.setPrice(roomUser.getPrice());
            takeOrder.setAttritionRate(roomUser.getAttritionRate());
            takeOrder.setLevel(roomUser.getLevel());
            takeOrder.setPicUrl(roomUser.getPicUrl());
            takeOrder.setAvatar(user.getAvatar());
            takeOrders.add(takeOrder);
        }
        takeOrderService.saveBatch(takeOrders);
        rollRoom.setRoomStatus(2);
        baseMapper.updateById(rollRoom);

    }

    @Override
    public boolean isJoin(Long roomId, String userId) {
        return rollRoomUserMapper.exists(new QueryWrapper<RollRoomUser>().eq("room_id", roomId).eq("user_id_str", userId));
    }

    @Override
    public Page<RollRoomUser> getRewardUser(Long roomId, Integer pageNum, Integer pageSize) {

        //判断是不是已经开奖
        RollRoom rollRoom = baseMapper.selectById(roomId);
        AssertUtil.isNull(rollRoom, "房间不存在");
        AssertUtil.isTrue(rollRoom.getRoomStatus() == 1, "暂未开奖");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.isNotNull("skin_name");
        queryWrapper.select("skin_name,level_,avatar,pic_url,attrition_rate,price");

        return rollRoomUserMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);

    }

    @Override
    public BigDecimal rewardSumPrice(Long roomId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("room_id", roomId);

        queryWrapper.select("ifnull(sum(price),0) as price");
        return rollRoomUserMapper.selectOne(queryWrapper).getPrice();
    }


}
