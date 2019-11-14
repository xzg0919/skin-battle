package com.tzj.collect.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 序号生成器
 * @author Michael_Wang
 *
 */
public class SnUtils {

	
	/**
	 * 年月日 + 3位随机数作为编号规则 暂定
	 * @author Michael_Wang
	 * @return
	 */
	public  static String generateSn()
	{
		
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String result = dateFormat.format(new Date()) + (int)((Math.random()*9+1)*1000);
		
		return result;
	}
	/**
	 * 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
	 *
	 * @param fullName
	 * @param  index 1 为第index位
	 * @return
	 */
	public static String leftOne(String fullName,int index) {
		if (StringUtils.isBlank(fullName)) {
			return "";
		}
		String name = StringUtils.left(fullName, index);
		return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
	}

	public static String telEncry(String tel){
		if (tel.length() == 11){
			return tel.substring(0,3)+ "****" + tel.substring(7);
		}
		return tel;
	}

	// 获取本月是哪一月
	public static String getNowYearMonth() {
		Date date = new Date();
		String dates = new SimpleDateFormat("yyyy-MM").format(date);
		return dates;
	}
	
}
