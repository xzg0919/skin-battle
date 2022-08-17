package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.core.mapper.UserPackageMapper;
import com.skin.core.service.UserPackageService;
import com.skin.entity.UserPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/17 16:58
 * @Description:
 */
@Service
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class UserPackageServiceImpl extends ServiceImpl<UserPackageMapper, UserPackage> implements UserPackageService {


    @Override
    public List<UserPackage> getLastPageList() {
        QueryWrapper<UserPackage> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("skin_name,box_id,pic_url,level_,nick_name,avatar");
        queryWrapper.orderByDesc("create_date");
        queryWrapper.last("limit 9");
        return baseMapper.selectList(queryWrapper);
    }
}
