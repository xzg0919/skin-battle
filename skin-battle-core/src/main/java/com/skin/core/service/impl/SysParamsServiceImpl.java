package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.SysParamsMapper;
import com.skin.core.service.SysParamsService;
import com.skin.entity.SysParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 14:52
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class SysParamsServiceImpl extends ServiceImpl<SysParamsMapper, SysParams> implements SysParamsService {


    @Override
    public SysParams getSysParams(String param) {
        return baseMapper.selectOne(new QueryWrapper<SysParams>().eq("param", param));
    }

    @Override
    public List<SysParams> getSysParamsList(String param) {
        QueryWrapper<SysParams> queryWrapper = new QueryWrapper<SysParams>().eq("param", param);
        queryWrapper.select("desc_","param","val");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public SysParams getSysParams(String param, String val) {
        return baseMapper.selectOne(new QueryWrapper<SysParams>().eq("param", param).eq("val", val));
    }
}
