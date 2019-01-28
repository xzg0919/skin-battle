package com.tzj.collect.api.app;

import org.springframework.beans.factory.annotation.Autowired;

import com.tzj.collect.api.app.param.MessageBean;
import com.tzj.collect.service.MessageService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;

@ApiService
public class AppMessageApi {
	
	@Autowired
	private MessageService MessageService;
	/**
	 * 得到code
	 * @return
	 */
	@Api(name = "app.message.getcode", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public String getMessageCode(MessageBean messageBean) {
		return MessageService.getMessageCode(messageBean.getTel());
	}
}
