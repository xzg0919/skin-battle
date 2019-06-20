package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.common.constant.Const;
import com.tzj.collect.entity.Message;
import com.tzj.collect.mapper.MessageMapper;
import com.tzj.collect.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 评价类ServiceImpl
 * @Author 王灿
 **/
@Service

public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService{
	
	@Autowired
	private MessageService messageService;
	
	
	@Override
	@DS("slave")
	public boolean validMessage(String tel, String messageCode) {
		if (tel != null && messageCode != null && !"".equals(tel) && !"".equals(messageCode)) {
			EntityWrapper<Message> wrapper = new EntityWrapper<>();
			wrapper.eq("tel", tel);
			wrapper.eq("message_code", messageCode);
			Message message= messageService.selectOne(wrapper);
			if (message != null) {
				long codeTime = System.currentTimeMillis() - Long.parseLong(String.valueOf(message.getUpdateDate().getTime()));
				if (message.getValidity() >= codeTime/1000) {
					return true;
				}
			}
		}
		return false;
	}


	@Override
	@DS("slave")
	public String getMessageCode(String tel) {
		EntityWrapper<Message> wrapper = new EntityWrapper<>();
		wrapper.eq("tel", tel);
		Message message = messageService.selectOne(wrapper);
		Const const2 = new Const();
		if (message == null ) {
			message = new Message();
		}
		message.setUpdateDate(new Date());
		message.setTel(tel);
		message.setMessageCode(MessageServiceImpl.getCode());
		message.setValidity(const2.getExctime());
		boolean flag = this.insertOrUpdate(message);
		if (flag) {
			AlidayuService.sendMessage("蚂蚁收呗", tel, "SMS_59045026", message.getMessageCode());
		}
		return "操作成功";
	}
	public static String getCode() {
		return (int)((Math.random()*9+1)*100000) + "";
	}
	
	/*  
     * 将时间转换为时间戳
         
    public static String dateToStamp(String s) throws java.text.ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }*/
}
