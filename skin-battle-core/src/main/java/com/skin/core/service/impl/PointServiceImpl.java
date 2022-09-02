package com.skin.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.handler.VipHandler;
import com.skin.common.security.MD5Util;
import com.skin.core.mapper.PointMapper;
import com.skin.core.service.InvitationService;
import com.skin.core.service.PointListService;
import com.skin.core.service.PointService;
import com.skin.core.service.UserService;
import com.skin.entity.Invitation;
import com.skin.entity.PointInfo;
import com.skin.entity.PointList;
import com.skin.entity.User;
import com.tzj.module.easyopen.exception.ApiException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 16:55
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class PointServiceImpl extends ServiceImpl<PointMapper, PointInfo> implements PointService {


    @Autowired
    UserService userService;

    @Autowired
    PointListService pointListService;

    @Autowired
    InvitationService   invitationService;
    @Autowired
    PointService pointService;






    @SneakyThrows
    @Transactional
    @Override
    public void editPoint(Long id, BigDecimal point) {
        //判断当前余额是否被修改
        PointInfo pointInfo = this.getByUid(id);
        if (!MD5Util.md5(pointInfo.getPoint().setScale(2).toString() + pointInfo.getTotalPoint().setScale(2).toString() + MD5Util.SIGN_KEY).equals(pointInfo.getMd5Code())) {
            throw new ApiException("数据已被篡改，请重新操作");
        }
        pointInfo.setPoint(point);
        pointInfo.setConsumePoint(BigDecimal.ZERO);
        pointInfo.setTotalPoint(point);
        pointInfo.setMd5Code(MD5Util.md5(pointInfo.getPoint().toString() + pointInfo.getTotalPoint().toString() + MD5Util.SIGN_KEY));
        baseMapper.updateById(pointInfo);

    }

    @Override
    public PointInfo getByUid(Long id) {
        return getOne(new QueryWrapper<PointInfo>().eq("user_id", id));
    }

    @Override
    public BigDecimal userPointSum() {
        return baseMapper.selectOne(new QueryWrapper<PointInfo>().select("sum(total_Point) as totalPoint")).getTotalPoint();
    }

    @Override
    public BigDecimal getUserValidPoint(Long userId) {
        return  getOne(new QueryWrapper<PointInfo>().eq("user_id", userId)).getPoint();
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editPoint(Long userId, BigDecimal point, Integer from,String fromChn,String orderNo,Integer type) {

        pointService.editPoint(userId, point, from, fromChn, orderNo, type,null);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editPoint(Long userId, BigDecimal point, Integer from, String fromChn, String orderNo, Integer type, Long beInvitedUserId) {

        PointInfo pointInfo =this.getByUid(userId);
        PointList pointList = new PointList();
        pointList.setUserId(userId);
        pointList.setOrderNo(orderNo);
        pointList.setType(type);
        pointList.setPoint(point);
        pointList.setOrderFromChn(fromChn);
        pointList.setOrderFrom(from);
        pointList.setAfterPoint(pointInfo.getPoint().add(point));

        //判断当前余额是否被修改
        if (!MD5Util.md5(pointInfo.getPoint().setScale(2).toString() + pointInfo.getTotalPoint().setScale(2).toString() + MD5Util.SIGN_KEY).equals(pointInfo.getMd5Code())) {
            throw new ApiException("数据已被篡改，请重新操作");
        }
        pointInfo.setPoint(pointInfo.getPoint().add(point));
        if(type == 1){
            pointInfo.setTotalPoint(pointInfo.getTotalPoint().add(point));
        }else{
            pointInfo.setConsumePoint(pointInfo.getConsumePoint().add(point));
        }

        pointInfo.setMd5Code(MD5Util.md5(pointInfo.getPoint().toString() + pointInfo.getTotalPoint().toString() + MD5Util.SIGN_KEY));

        //计算vip等级  只算充值的 并且不算充值赠送的
        if(from == 2){
            pointInfo.setRechargePoint(pointInfo.getRechargePoint().add(point));
            userService.updateVIP(userId, VipHandler.getVip(pointInfo.getRechargePoint()));
            //用户充值后判断 是不是被邀请的 如果是 则赠送积分 并记录
            Invitation invitationByUserId = invitationService.getInvitationByUserId(userId);
            if(invitationByUserId != null){
                invitationByUserId.setRechargeAmount(invitationByUserId.getRechargeAmount().add(point));
                invitationService.updateById(invitationByUserId);
                pointList.setInviteUserId(invitationByUserId.getUserId());
                pointService.editPoint(invitationByUserId.getUserId(),
                        point.multiply(new BigDecimal(invitationService.invitationPercentage(invitationByUserId.getUserId())))
                                .divide(new BigDecimal(100))
                                .setScale(2, BigDecimal.ROUND_DOWN),
                        5,"邀请",orderNo,1,invitationByUserId.getInviteUserId());

            }

        }

        if(from == 5){
            pointList.setBeInviteUserId(beInvitedUserId);
        }
        baseMapper.updateById(pointInfo);
        pointListService.save(pointList);
    }


}
