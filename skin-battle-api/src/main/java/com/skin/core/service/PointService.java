package com.skin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.PointInfo;

import java.math.BigDecimal;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/10 16:55
 * @Description:
 */
public interface PointService extends IService<PointInfo> {

    void  editPoint(Long id , BigDecimal point);


    PointInfo  getByUid(Long id );


    BigDecimal userPointSum();

}
