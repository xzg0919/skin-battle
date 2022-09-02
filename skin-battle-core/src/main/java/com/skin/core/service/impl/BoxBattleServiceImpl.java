package com.skin.core.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.lottery.LotteryBean;
import com.skin.common.lottery.LotteryUtils;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.*;
import com.skin.core.service.*;
import com.skin.entity.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: xiang
 * @Date: 2022/8/14 16:04
 * @Description:
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class BoxBattleServiceImpl extends ServiceImpl<BoxBattleMapper, BoxBattle> implements BoxBattleService {

    @Autowired
    BoxBattleSkinMapper boxBattleSkinMapper;
    @Autowired
    SkinService skinService;
    @Autowired
    BoxBattleListMapper boxBattleListMapper;
    @Autowired
    BoxBattleInfoMapper boxBattleInfoMapper;
    @Autowired
    PointService pointService;
    @Autowired
    BoxBattleUserMapper boxBattleUserMapper;
    @Autowired
    UserService userService;
    @Autowired
    SysParamsService sysParamsService;

    @Autowired
    TakeOrderService takeOrderService;


    @Override
    public Page<BoxBattle> getPage(Integer pageNo, Integer pageSize, String boxName) {

        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(boxName)) {
            queryWrapper.like("box_name", boxName);
        }
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional
    @Override
    public void changeStatus(Long id) {
        BoxBattle boxBattle = this.getById(id);
        if (boxBattle == null) {
            throw new ApiException("更改的数据不存在");
        }
        if (boxBattle.getStatus() == 1) {
            boxBattle.setStatus(0);
        } else {
            boxBattle.setStatus(1);
        }
        this.updateById(boxBattle);
    }

    @Override
    public Page<BoxBattleSkin> getSkinPage(Integer pageNo, Integer pageSize, String skinName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.like("skin_name", skinName);
        }
        queryWrapper.orderByDesc("create_date");
        return boxBattleSkinMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
    }

    @Transactional
    @Override
    public void delBoxSkin(Long id) {
        boxBattleSkinMapper.deleteById(id);
    }

    @Override
    public BoxBattleSkin getSkinById(Long id) {
        return boxBattleSkinMapper.selectById(id);
    }

    @Transactional
    @Override
    public void updateSkin(BoxBattleSkin boxBattleSkin) {
        boxBattleSkinMapper.updateById(boxBattleSkin);
    }

    @Transactional
    @Override
    public void insertSkin(Long boxId, Long skinId, double probability) {
        Optional.ofNullable(skinService.getById(skinId)).ifPresent(skin -> {
            BoxBattleSkin sameSkin = boxBattleSkinMapper.selectOne(new QueryWrapper<BoxBattleSkin>().eq("skin_name", skin.getName()).eq("box_battle_id", boxId));
            AssertUtil.isNotNull(sameSkin, "该皮肤已经存在");
            BoxBattleSkin boxBattleSkin = new BoxBattleSkin();
            boxBattleSkin.setPicUrl(skin.getPicUrl());
            boxBattleSkin.setBoxBattleId(boxId);
            boxBattleSkin.setSkinName(skin.getName());
            boxBattleSkin.setAttritionRate(skin.getAttritionRate());
            boxBattleSkin.setLevel(skin.getLevel());
            boxBattleSkin.setPrice(skin.getPrice());
            boxBattleSkin.setProbability(probability);
            boxBattleSkinMapper.insert(boxBattleSkin);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createBattleBox(Long userId, String boxIds, Integer userCount) {
        String[] split = boxIds.split(",");
        AssertUtil.isTrue(split.length == 0 || split.length > 6, "箱子数量为1-6个");
        AssertUtil.isFalse(userCount == 3 || userCount == 2, "只能选择2-3人进行拼箱");
        BigDecimal sumPrice = BigDecimal.ZERO;
        BoxBattleInfo boxBattleInfo = new BoxBattleInfo();

        boxBattleInfo.setBoxCount(split.length);
        boxBattleInfo.setUserCount(userCount);
        boxBattleInfo.setStatus(0);
        boxBattleInfo.setBoxPrice(sumPrice);
        boxBattleInfoMapper.insert(boxBattleInfo);

        User user = userService.getById(userId);
        AssertUtil.isNull(user, "用户不存在");

        for (String boxId : split) {
            BoxBattle boxBattle = baseMapper.selectById(boxId);
            BoxBattleList boxBattleList = new BoxBattleList();
            boxBattleList.setBoxName(boxBattle.getBoxName());
            boxBattleList.setBoxPicUrl(boxBattle.getBoxPic());
            boxBattleList.setBoxId(boxBattle.getId());
            boxBattleList.setBoxPrice(boxBattle.getPrice());
            boxBattleList.setBoxBattleId(boxBattleInfo.getId());
            BoxBattleUser battleUser = new BoxBattleUser();
            battleUser.setBoxBattleId(boxBattleInfo.getId());
            battleUser.setBoxId(boxBattle.getId());
            battleUser.setUserId(user.getId());
            battleUser.setNickName(user.getNickName());
            battleUser.setTel(user.getTel());
            battleUser.setEmail(user.getEmail());
            battleUser.setAvatar(user.getAvatar());
            boxBattleListMapper.insert(boxBattleList);
            boxBattleUserMapper.insert(battleUser);
            AssertUtil.isNull(boxBattle, "系统异常");
            sumPrice = sumPrice.add(boxBattle.getPrice());
        }

        PointInfo pointInfo = pointService.getByUid(userId);
        AssertUtil.isTrue(sumPrice.compareTo(pointInfo.getPoint()) > 0, "余额不足，请先充值");
        boxBattleInfo.setBoxPrice(sumPrice);
        boxBattleInfoMapper.updateById(boxBattleInfo);
        int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "拼箱", "4"));
        pointService.editPoint(userId, sumPrice, from, "拼箱","", 0);

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean join(Long userId, Long boxBattleId) {

        //判断是否存在
        BoxBattleInfo boxBattleInfo = boxBattleInfoMapper.selectById(boxBattleId);
        AssertUtil.isNull(boxBattleInfo, "加入的房间不存在");

        //判断用户是否已经加入过了
        AssertUtil.isTrue(boxBattleUserMapper.exists(new QueryWrapper<BoxBattleUser>().eq("box_battle_id", boxBattleId).eq("user_id",userId)), "不能重复加入该房间");
        User user = userService.getById(userId);
        AssertUtil.isNull(user, "用户不存在");
        AssertUtil.isBlank(user.getSteamUrl(), "您还没有绑定Steam交易链接，请先绑定链接以继续游戏");
        PointInfo pointInfo = pointService.getByUid(userId);
        AssertUtil.isTrue(boxBattleInfo.getBoxPrice().compareTo(pointInfo.getPoint()) > 0, "余额不足，请先充值");
        List<BoxBattleList> boxBattleLists = boxBattleListMapper.selectList(new QueryWrapper<BoxBattleList>().eq("box_battle_id", boxBattleId));
        for (BoxBattleList boxBattleList : boxBattleLists) {
            BoxBattleUser battleUser = new BoxBattleUser();
            battleUser.setBoxBattleId(boxBattleInfo.getId());
            battleUser.setBoxId(boxBattleList.getBoxId());
            battleUser.setUserId(user.getId());
            battleUser.setNickName(user.getNickName());
            battleUser.setTel(user.getTel());
            battleUser.setEmail(user.getEmail());
            battleUser.setAvatar(user.getAvatar());
            boxBattleUserMapper.insert(battleUser);
        }
        int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "拼箱", "4"));
        pointService.editPoint(userId, boxBattleInfo.getBoxPrice(), from, "拼箱", "", 0);
        //判断人数是否已经满足

        Long currentCount =boxBattleUserMapper.selectCount(
                new QueryWrapper<BoxBattleUser>().eq("box_battle_id", boxBattleId)
                        .select("distinct(user_id)"));
        if (currentCount.intValue() == boxBattleInfo.getUserCount()) {
            //开始开箱
            List<BoxBattleUser> boxBattleUsers = boxBattleUserMapper.selectList(new QueryWrapper<BoxBattleUser>().eq("box_battle_id", boxBattleId));
            List<TakeOrder> takeOrders = new ArrayList<>();
            for (BoxBattleUser boxBattleUser : boxBattleUsers) {
                List<LotteryBean> lotteryBeans = new ArrayList<>();
                User currentUser = userService.getById(boxBattleUser.getUserId());
                List<BoxBattleSkin> skins = boxBattleSkinMapper.selectList(new QueryWrapper<BoxBattleSkin>().eq("box_battle_id", boxBattleUser.getBoxId()));
                skins.forEach(blindBoxSkin -> {
                    LotteryBean lotteryBean = new LotteryBean();
                    lotteryBean.setGoodsId(blindBoxSkin.getId());
                    lotteryBean.setBili(blindBoxSkin.getProbability());
                    //判断是否设置了单独的概率
                    if (currentUser.getHighProbability() != null && blindBoxSkin.getLevel() == 2) {
                        lotteryBean.setBili(currentUser.getHighProbability());
                    }
                    if (currentUser.getMiddleProbability() != null && blindBoxSkin.getLevel() == 1) {
                        lotteryBean.setBili(currentUser.getMiddleProbability());
                    }
                    if (currentUser.getLowProbability() != null && blindBoxSkin.getLevel() == 0) {
                        lotteryBean.setBili(currentUser.getLowProbability());
                    }
                    lotteryBeans.add(lotteryBean);
                });
                List<HashMap<String, Object>> lotteryResult = LotteryUtils.lottery(lotteryBeans, 1);

                //存入背包
                lotteryResult.forEach(map -> {
                    BoxBattleSkin blindBoxSkin = boxBattleSkinMapper.selectById((Long) map.get("goodsId"));
                    AssertUtil.isNull(blindBoxSkin, "网络异常，请稍后再试");
                    boxBattleUser.setBoxId(boxBattleId);
                    boxBattleUser.setSkinName(blindBoxSkin.getSkinName());
                    boxBattleUser.setPrice(blindBoxSkin.getPrice());
                    boxBattleUser.setAttritionRate(blindBoxSkin.getAttritionRate());
                    boxBattleUser.setLevel(blindBoxSkin.getLevel());
                    boxBattleUserMapper.updateById(boxBattleUser);
                    TakeOrder takeOrder = new TakeOrder();
                    takeOrder.setBoxId(boxBattleId);
                    String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
                    takeOrder.setOrderNo(orderNo);
                    takeOrder.setSource(Integer.parseInt(sysParamsService.getSysParams("box_battle").getVal()));
                    takeOrder.setSkinName(blindBoxSkin.getSkinName());
                    takeOrder.setStatus(0);
                    takeOrder.setPrice(blindBoxSkin.getPrice());
                    takeOrder.setAttritionRate(blindBoxSkin.getAttritionRate());
                    takeOrder.setLevel(blindBoxSkin.getLevel());
                    takeOrder.setPicUrl(blindBoxSkin.getPicUrl());
                    takeOrders.add(takeOrder);
                });
            }

            //计算获胜者

            Map<Long, List<BoxBattleUser>> map = boxBattleUsers.stream().collect(Collectors.groupingBy(m -> m.getUserId()));
            Map<String, Object> winner = new HashMap<>();
            map.keySet().stream().forEach(key -> {
                BigDecimal totalPrice = map.get(key).stream().map(BoxBattleUser::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (winner.get("userId")==null) {
                    winner.put("userId", key);
                    winner.put("totalPrice", totalPrice);
                } else {
                    BigDecimal totalPrice1 = (BigDecimal) winner.get("totalPrice");
                    if (totalPrice1.compareTo(totalPrice) < 0) {
                        winner.put("userId", key);
                        winner.put("totalPrice", totalPrice);
                    } else if (totalPrice1.compareTo(totalPrice) == 0) {
                        throw new ApiException("系统异常");
                    }
                }
            });


            User winnerU = userService.getById(Long.parseLong(winner.get("userId") + ""));
            //获胜的人将所有奖励放入背包
            for (TakeOrder takeOrder : takeOrders) {
                takeOrder.setUserId(winnerU.getId());
                takeOrder.setNickName(winnerU.getNickName());
                takeOrder.setSteamUrl(winnerU.getSteamUrl());
                takeOrder.setTel(winnerU.getTel());
                takeOrder.setEmail(winnerU.getEmail());
                takeOrder.setAvatar(winnerU.getAvatar());
            }


            //发放失败的奖励
            map.keySet().stream().forEach(key -> {
                if (!key.equals(winnerU.getId())) {
                    TakeOrder takeOrder = new TakeOrder();
                    takeOrder.setBoxId(boxBattleId);
                    String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
                    takeOrder.setOrderNo(orderNo);
                    takeOrder.setSource(Integer.parseInt(sysParamsService.getSysParams("box_battle").getVal()));

                    User u = userService.getById(key);
                    Skin skin = skinService.getById(4);
                    takeOrder.setSkinName(skin.getName());
                    takeOrder.setStatus(0);
                    takeOrder.setPrice(skin.getPrice());
                    takeOrder.setAttritionRate(skin.getAttritionRate());
                    takeOrder.setLevel(skin.getLevel());
                    takeOrder.setPicUrl(skin.getPicUrl());
                    takeOrder.setUserId(u.getId());
                    takeOrder.setNickName(u.getNickName());
                    takeOrder.setSteamUrl(u.getSteamUrl());
                    takeOrder.setTel(u.getTel());
                    takeOrder.setEmail(u.getEmail());
                    takeOrder.setAvatar(u.getAvatar());
                    takeOrderService.save(takeOrder);
                }
            });

            boxBattleInfo.setWinner(winnerU.getId());
            takeOrderService.saveBatch(takeOrders);
            boxBattleInfo.setStatus(1);
            boxBattleInfoMapper.updateById(boxBattleInfo);
            return true;
        }
        return false;
    }

    @Override
    public List<BoxBattleInfo> getUnfinishList() {
        return boxBattleInfoMapper.selectList(new QueryWrapper<BoxBattleInfo>().eq("status_", 1));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void finishBattle(Long boxBattleId) {
        BoxBattleInfo boxBattleInfo = boxBattleInfoMapper.selectById(boxBattleId);
        boxBattleInfo.setStatus(2);
        boxBattleInfoMapper.updateById(boxBattleInfo);
    }

    @Override
    public Page<BoxBattleInfo> getBattleInfoPage(Integer pageNum, Integer pageSize, Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id,status_,box_count,box_price,user_count");
        queryWrapper.orderByDesc("create_date");
        Page<BoxBattleInfo> page = boxBattleInfoMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        List<BoxBattleInfo> records = new ArrayList<>();
        for (BoxBattleInfo record : page.getRecords()) {
            BoxBattleInfo newInfo = new BoxBattleInfo();
            BeanUtils.copyProperties(record, newInfo);
            List<BoxBattleUser> boxBattleUsers = boxBattleUserMapper.selectList(
                    new QueryWrapper<BoxBattleUser>().eq("box_battle_id", record.getId())
                            .select("user_id,avatar").groupBy("user_id,avatar"));
            List<BoxBattleList> boxBattleLists = boxBattleListMapper.selectList(
                    new QueryWrapper<BoxBattleList>().eq("box_battle_id", record.getId())
                            .select("box_pic_url"));
            newInfo.setBoxBattleUsers(boxBattleUsers);
            newInfo.setBoxBattleLists(boxBattleLists);
            newInfo.setIsJoin(2);
            if (userId != null && boxBattleUsers.stream().filter(boxBattleUser -> boxBattleUser.getUserId().equals(userId)).findAny().isPresent()) {
                newInfo.setIsJoin(1);
            }
            records.add(newInfo);
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public Page<BoxBattle> getBoxPage(Integer pageNum, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNum, pageSize),
                new QueryWrapper<BoxBattle>().orderByDesc("create_date")
                        .select("id,box_name,price,box_pic")
                        .eq("status_", 0));
    }

    @Override
    public HashMap<String, Object> getBattleInfo(Long battleInfoId) {
        HashMap<String, Object> result = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("box_id,box_name,box_price,box_pic_url");
        queryWrapper.eq("box_battle_id", battleInfoId);
        queryWrapper.orderByDesc("id");
        List<BoxBattleList> battleLists = boxBattleListMapper.selectList(queryWrapper);

        QueryWrapper userQuery = new QueryWrapper();
        userQuery.select("nick_name,user_id,avatar,ifnull(sum(price),0) as price");
        userQuery.eq("box_battle_id", battleInfoId);
        userQuery.orderByDesc("id");
        userQuery.groupBy("nick_name,user_id,avatar");
        BoxBattleInfo boxBattleInfo = boxBattleInfoMapper.selectById(battleInfoId);
        List<BoxBattleUser> battleUsers = boxBattleUserMapper.selectList(userQuery);
        for (BoxBattleUser battleUser : battleUsers) {
            User user = userService.getById(battleUser.getUserId());
            battleUser.setVipLevel(user.getVip());
            QueryWrapper rewardQuery = new QueryWrapper();
            rewardQuery.eq("box_battle_id", battleInfoId);
            rewardQuery.eq("user_id", battleUser.getUserId());
            rewardQuery.select("pic_url,skin_name,attrition_rate,price");
            battleUser.setUserReward(boxBattleUserMapper.selectList(rewardQuery));
            if (boxBattleInfo.getWinner() != null && user.getId().equals(boxBattleInfo.getWinner())) {
                battleUser.setWinner(1);
            } else {
                battleUser.setWinner(0);
            }
        }

        Skin skin = skinService.getById(4);
        result.put("failReward", skin);
        result.put("boxList", battleLists);
        result.put("userList", battleUsers);
        return result;
    }

    @Override
    public Map<String, Object> getSkinList(Long boxId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("pic_url,skin_name,attrition_rate");
        queryWrapper.eq("box_battle_id", boxId);
        queryWrapper.orderByDesc("level_");
        BoxBattle boxBattle = baseMapper.selectOne(new QueryWrapper<BoxBattle>().eq("id", boxId)
                .select("box_name,high_probability,middle_probability,low_probability"));
        HashMap<String, Object> result = new HashMap<>();
        result.put("boxInfo", boxBattle);
        result.put("skinList", boxBattleSkinMapper.selectList(queryWrapper));
        return result;
    }

    @Override
    public Page<BoxBattleInfo> getHistoryPage(Long userId, Integer pageNum, Integer pageSize) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("CREATE_DATE,id,box_price,winner");
        if (userId != null) {
            queryWrapper.exists("select 1 from box_battle_user where box_battle_info.id = box_battle_id and user_id ="+userId );
        }
        queryWrapper.eq("status_",2);
        queryWrapper.orderByDesc("create_date");
        Page<BoxBattleInfo> page = boxBattleInfoMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        List<BoxBattleInfo> records = new ArrayList<>();
        for (BoxBattleInfo record : page.getRecords()) {
            BoxBattleInfo newInfo = new BoxBattleInfo();
            BeanUtils.copyProperties(record, newInfo);
            List<BoxBattleUser> boxBattleUsers = boxBattleUserMapper.selectList(
                    new QueryWrapper<BoxBattleUser>().eq("box_battle_id", record.getId())
                            .select("user_id,nick_name,avatar").groupBy("user_id,nick_name,avatar"));
            List<BoxBattleList> boxBattleLists = boxBattleListMapper.selectList(
                    new QueryWrapper<BoxBattleList>().eq("box_battle_id", record.getId())
                            .select("box_pic_url"));
            newInfo.setBoxBattleUsers(boxBattleUsers);
            newInfo.setBoxBattleLists(boxBattleLists);
            if(record.getWinner().equals(userId)){
                newInfo.setWinner(1L);
            }else{
                newInfo.setWinner(0L);
            }
            records.add(newInfo);
        }
        page.setRecords(records);
        return page;
    }


}
