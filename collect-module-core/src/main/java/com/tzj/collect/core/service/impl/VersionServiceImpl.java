package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.VersionMapper;
import com.tzj.collect.core.service.VersionService;
import com.tzj.collect.entity.ApkVersion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class VersionServiceImpl extends ServiceImpl<VersionMapper, ApkVersion> implements VersionService {
    
	/**
	 * 根据版本类型获取版本信息
	 */
	@Transactional(readOnly = false)
	public ApkVersion getAppVersionByApkType(String apkType) {
		 EntityWrapper<ApkVersion> wrapper = new EntityWrapper<ApkVersion>();
		wrapper.eq("apk_type", apkType);
		 wrapper.orderBy("id",false);
		return this.selectOne(wrapper);
	}



}
