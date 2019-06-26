package com.tzj.collect.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.ApkVersion;
import com.tzj.collect.mapper.VersionMapper;
import com.tzj.collect.service.VersionService;

@Service
@Transactional(readOnly=true)
public class VersionServiceImpl extends ServiceImpl<VersionMapper, ApkVersion> implements VersionService{
    
	@Autowired
	private  VersionMapper versionMapper;
	/**
	 * 根据版本类型获取版本信息
	 */
	@Override
	public ApkVersion getAppVersionByApkType(String apkType) {
		 EntityWrapper<ApkVersion> wrapper = new EntityWrapper<ApkVersion>();
		wrapper.eq("apk_type", apkType);
		 wrapper.orderBy("id",false);
		return this.selectOne(wrapper);
	}

}
