 package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.CdkMapper;
import com.skin.core.service.CdkService;
import com.skin.entity.CdkInfo;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;



@Service
@Transactional(readOnly = true)
public class CdkInfoServiceImpl extends ServiceImpl<CdkMapper, CdkInfo> implements CdkService {


    @Override
    public Page<CdkInfo> getCdkPage(Integer pageNum, Integer pageSize) {
        Page<CdkInfo> page = new Page<>(pageNum, pageSize);
        return   baseMapper.selectPage(page,new QueryWrapper<CdkInfo>().orderByDesc("create_time")
                .select("id,cdk_val,code_,create_date,enable_,times"));
    }

    @Transactional
    @Override
    public void insertCdk(BigDecimal price) {
        CdkInfo cdkInfo = new CdkInfo();
        cdkInfo.setEnable(1);
        cdkInfo.setCdkVal(price);
        cdkInfo.setCode(UUID.randomUUID().toString()
                .substring(0, 8)
                .replace("-", "")
                .replace("o", "0")
                .replace("i", "1")
                .toUpperCase()
        );
        cdkInfo.setTimes(0);

        this.save(cdkInfo);
    }

    @Transactional
    @Override
    public void updateCdk(Long id, BigDecimal price) {
        CdkInfo cdkInfo = this.getById(id);
        if (cdkInfo == null) {
            throw new ApiException("更改的数据不存在");
        }
        cdkInfo.setCdkVal(price);

        this.updateById(cdkInfo);
    }

    @Transactional
    @Override
    public void changeStatus(Long id) {
        CdkInfo cdkInfo = this.getById(id);
        if (cdkInfo == null) {
            throw new ApiException("更改的数据不存在");
        }
        if (cdkInfo.getEnable() == 1) {
            cdkInfo.setEnable(0);
        } else {
            cdkInfo.setEnable(1);
        }
        this.updateById(cdkInfo);
    }

    @Transactional
    @Override
    public void deleteCdk(Long id) {
        this.removeById(id);
    }
}
