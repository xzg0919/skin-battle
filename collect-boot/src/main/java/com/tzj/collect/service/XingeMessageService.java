package com.tzj.collect.service;

import com.tzj.collect.service.impl.XingeMessageServiceImp.XingeMessageCode;

public interface XingeMessageService {
	

	
	/**
	 * 腾讯信鸽推送消息
	 * @param title
	 * @param message
	 * @param device_token
	 * @return
	 */
	public void sendPostMessage(String title,String content,String device_token,XingeMessageCode code);
	
}
