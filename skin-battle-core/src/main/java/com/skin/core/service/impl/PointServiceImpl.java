package com.skin.core.service.impl;

import com.aliyun.mns.common.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.security.MD5Util;
import com.skin.core.mapper.PointMapper;
import com.skin.core.service.PointService;
import com.skin.core.service.UserService;
import com.skin.entity.PointInfo;
import com.taobao.api.ApiException;
import com.tzj.module.common.utils.DateUtils;
import lombok.SneakyThrows;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi;
import org.bouncycastle.jcajce.provider.digest.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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


    static Map<Integer, BigDecimal> pointMap = new TreeMap<>();

    static {
        pointMap.put(1, new BigDecimal("50"));
        pointMap.put(2, new BigDecimal("200"));
        pointMap.put(3, new BigDecimal("500"));
        pointMap.put(4, new BigDecimal("1000"));
        pointMap.put(5, new BigDecimal("2000"));
        pointMap.put(6, new BigDecimal("3500"));
        pointMap.put(7, new BigDecimal("5000"));
        pointMap.put(8, new BigDecimal("7500"));
        pointMap.put(9, new BigDecimal("10000"));
        pointMap.put(10, new BigDecimal("20000"));
    }


    @SneakyThrows
    @Transactional
    @Override
    public void editPoint(Long id, BigDecimal point) {
        //判断当前余额是否被修改
        PointInfo pointInfo = this.getByUid(id);
        if (!MD5Util.md5(pointInfo.getPoint().toString() + pointInfo.getTotalPoint().toString() + MD5Util.SIGN_KEY).equals(pointInfo.getMd5Code())) {
            throw new ApiException("数据已被篡改，请重新操作");
        }
        pointInfo.setPoint(point);
        pointInfo.setConsumePoint(BigDecimal.ZERO);
        pointInfo.setTotalPoint(point);
        pointInfo.setMd5Code(MD5Util.md5(pointInfo.getPoint().toString() + pointInfo.getTotalPoint().toString() + MD5Util.SIGN_KEY));
        baseMapper.updateById(pointInfo);
        //计算vip等级
        userService.updateVIP(id, this.getVip(pointInfo.getTotalPoint()));
    }

    @Override
    public PointInfo getByUid(Long id) {
        return getOne(new QueryWrapper<PointInfo>().eq("user_id", id));
    }

    @Override
    public BigDecimal userPointSum() {
        return baseMapper.selectOne(new QueryWrapper<PointInfo>().select("sum(total_Point) as totalPoint")).getTotalPoint();
    }




    public Integer getVip(BigDecimal point) {
        Integer vip = 0;
        for (Map.Entry<Integer, BigDecimal> entry : pointMap.entrySet()) {
            if (point.divide(entry.getValue()).compareTo(new BigDecimal("1")) == 1) {
                vip = entry.getKey();
            } else {
                break;
            }
        }
        return vip;
    }


}
