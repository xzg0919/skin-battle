package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Message;

/**
 * @Author 王灿
 **/
public interface MessageService extends IService<Message> {
	
	//判断手机号和验证码是否有效
	public boolean validMessage(String tel,String messageCode); 
	
	String getMessageCode(String tel);
}
