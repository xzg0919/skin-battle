package com.tzj.collect.api.app.result;

public class AttrItem {
	
	private String cateAttrName;//分类属性名称
	
	private String cateAttrOpp;//分类选项名称
	
	private String 	picUrl; // 订单图片url
	
	private String smallPic;//小图url
	
	private String origPic;//原图url

	public String getCateAttrName() {
		return cateAttrName;
	}

	public void setCateAttrName(String cateAttrName) {
		this.cateAttrName = cateAttrName;
	}

	public String getCateAttrOpp() {
		return cateAttrOpp;
	}

	public void setCateAttrOpp(String cateAttrOpp) {
		this.cateAttrOpp = cateAttrOpp;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getSmallPic() {
		return smallPic;
	}

	public void setSmallPic(String smallPic) {
		this.smallPic = smallPic;
	}

	public String getOrigPic() {
		return origPic;
	}

	public void setOrigPic(String origPic) {
		this.origPic = origPic;
	}
	
}
