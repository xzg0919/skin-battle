package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.MallSkinMapper;
import com.skin.core.mapper.SkinMapper;
import com.skin.core.service.MallSkinService;
import com.skin.core.service.SkinService;
import com.skin.entity.MallSkin;
import com.skin.entity.Skin;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 13:48
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class MallSkinServiceImpl extends ServiceImpl<MallSkinMapper, MallSkin> implements MallSkinService {


    @Override
    public Page<MallSkin> getSkinPage(Integer pageNum, Integer pageSize, String skinName) {
        Page page = new Page<>(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(skinName)) {
            queryWrapper.eq("goods_name", skinName);
        }
        queryWrapper.orderByDesc("create_date");
        return baseMapper.selectPage(page, queryWrapper);
    }
}
