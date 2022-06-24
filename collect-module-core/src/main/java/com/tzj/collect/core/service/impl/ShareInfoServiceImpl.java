package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.ShareInfoMapper;
import com.tzj.collect.core.service.ShareInfoService;
import com.tzj.collect.core.service.SharerService;
import com.tzj.collect.entity.ShareInfo;
import com.tzj.collect.entity.Sharer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ShareInfoServiceImpl extends ServiceImpl<ShareInfoMapper, ShareInfo> implements ShareInfoService {

    @Autowired
    SharerService sharerService;

    @Autowired
    ShareInfoMapper shareInfoMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void share(String shareId, String aliUserId,String name,String tel) {
        Sharer sharer = sharerService.getByAliUserId(shareId);
        if (sharer == null) {
            return;
        }
        ShareInfo shareInfo = new ShareInfo();
        shareInfo.setShareAliUserId(shareId);
        shareInfo.setAliUserId(aliUserId);
        shareInfo.setName(name);
        shareInfo.setTel(tel);
        sharer.setLastShareTime(new Date());
        sharer.setTotalShareNum(sharer.getTotalShareNum() + 1);
        if (StringUtils.isNotBlank(shareInfo.getAliUserId())) {
            sharer.setSuccessShareNum(sharer.getSuccessShareNum() + 1);
            sharer.setTotalBonus(sharer.getTotalBonus().add(new BigDecimal("0.05")));
            shareInfo.setBonus(new BigDecimal("0.05"));
        }
        sharerService.updateById(sharer);
        this.insert(shareInfo);
    }

    @Override
    public Map<String, Object> getShareData(String shareId,String date) {
        return shareInfoMapper.getShareInfoData(shareId,date+"%");
    }

}
