package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("sb_apk_version")
public class ApkVersion extends DataEntity<Long>{
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
//	public String getVersion() {
//		return version;
//	}
//	public void setVersion(String version) {
//		this.version = version;
//	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdaterDesc() {
		return updaterDesc;
	}
	public void setUpdaterDesc(String updaterDesc) {
		this.updaterDesc = updaterDesc;
	}
	public String getIsForce() {
		return isForce;
	}
	public void setIsForce(String isForce) {
		this.isForce = isForce;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	
//	private String version; //版本号
	private String versionCode;//版本编号
	private String versionNo; //版本名称
	
	private String downloadUrl; //下载地址
	private String updaterDesc; //更新内容
	private String isForce; // 是否强制更新1强制更新2普通更新
	private String appName; // app名称
	private String apkName; //安装包名称
	private String apkType; //版本类型1管理员版，2保洁员版，3市民版
	public String getApkType() {
		return apkType;
	}
	public void setApkType(String apkType) {
		this.apkType = apkType;
	}
}
