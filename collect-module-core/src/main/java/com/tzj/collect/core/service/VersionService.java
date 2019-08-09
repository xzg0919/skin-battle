package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.ApkVersion;

public interface VersionService extends IService<ApkVersion> {

	ApkVersion getAppVersionByApkType(String apkType);

}
