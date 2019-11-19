package com.tzj.collect.core.service;


import com.tzj.collect.common.constant.XingeMessageCode;

public interface XingeMessageService {
	

	
	/**
	 * 腾讯信鸽推送消息
	 * @param title
	 * @param device_token
	 * @return
	 */
	public void sendPostMessage(String title, String content, String device_token, XingeMessageCode code);
	
}
