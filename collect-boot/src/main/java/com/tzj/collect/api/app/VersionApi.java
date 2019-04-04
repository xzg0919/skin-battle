package com.tzj.collect.api.app;

import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tzj.collect.api.app.param.VersionBean;
import com.tzj.collect.entity.ApkVersion;
import com.tzj.collect.service.VersionService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;

@ApiService
public class VersionApi {
	
	@Autowired
	private VersionService versionService;
	
	@Api(name = "app.apk.appVersion", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> getAppVersion(VersionBean version){
		
		Map<String, Object> result = new HashMap<String, Object>();
		 ApkVersion  apkVersion = versionService.getAppVersionByApkType(version.getApkType());
		    result.put("versionNo", apkVersion.getVersion());
			result.put("versionCode", apkVersion.getVersionCode());
			result.put("downloadUrl", apkVersion.getDownloadUrl());
			result.put("isForce", apkVersion.getIsForce());
			result.put("updaterDesc", apkVersion.getUpdaterDesc());
		return result;
	}
}
