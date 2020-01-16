package com.tzj.green.api.app;


import com.tzj.green.param.RecyclersLoginBean;
import com.tzj.green.service.MessageService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

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
	public String getMessageCode(RecyclersLoginBean recyclersLoginBean) {
		return MessageService.getMessageCode(recyclersLoginBean.getMobile());
	}
}
