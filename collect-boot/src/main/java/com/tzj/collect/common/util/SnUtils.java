package com.tzj.collect.common.util;

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
	
}
