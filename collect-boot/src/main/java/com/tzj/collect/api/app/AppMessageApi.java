package com.tzj.collect.api.app;

import com.tzj.collect.core.param.app.MessageBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

@ApiService
public class AppMessageApi {
	
	@Autowired
	private com.tzj.collect.core.service.MessageService MessageService;
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
