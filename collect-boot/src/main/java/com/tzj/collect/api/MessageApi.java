package com.tzj.collect.api;

import com.tzj.collect.api.ali.param.MessageBean;

/**
 * 短信接口
 * @author Michael_Wang
 *
 */
public class MessageApi {
	
	/**
	 * 保存到数据库中(因为没有短信服务器 所以先往数据库插一条数据 返回给前台测试)
	 * 发送短信
	 */
	public String sentMessage(MessageBean message){
		
		return null;
		
	}

}
