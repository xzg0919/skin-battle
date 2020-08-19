package com.tzj.collect.common.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ToolUtils {
	public static final String categoryName = "五废";
	//调用绿账的发券接口
	public static final String greenH5 = "http://106.15.157.147/green/sbProduct/sendVoucher.jhtml";

	 /**
     * 阿里短信url
     */
	public static final String url = "http://gw.api.taobao.com/router/rest";
    /**
     * 阿里短信appkey
     */
	public static final String appkey = "23725564";
    /**
     * 阿里短信secret
     */
	public static final String secret = "9fc0613e5f49e9b996b6f1b42bf9ed06";

	/**
	 * 某个时间加上多少天
	 * @param date
	 * @param dateNum
	 * @return
	 */
	public static Date addDateByNow(Date date,Integer dateNum){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + dateNum);
		return cal.getTime();
	}
	/**
	 * 某个时间加上多少分钟
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date addMinuteByNow(Date date,Integer minute){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minute);
		return cal.getTime();
	}

	/**
	 * 某个时间减去多少天
	 * @param date
	 * @param dateNum
	 * @return
	 */
	public static Date RmDateByNow(Date date,Integer dateNum){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - dateNum);
		return cal.getTime();
	}

	public static String getDateTimeToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	public static String getDateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static Date getDateTime(String date){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(date);
		}catch (Exception e){
			e.printStackTrace();
		}
		return new Date();
	}

	public static String getAliUserIdByOrderNo(String orderNo){
		long s = (Long.parseLong(orderNo.substring(4))+99)/2-99;
		return s+"";
	}
	public static String getIdCardByAliUserId(String AliUserId){
		Long s = (Long.parseLong(AliUserId)+(long)99)*2-99;
		return LocalDate.now().getYear()+""+s;
	}

	public static void main(String[] args) {
		System.out.println(getAliUserIdByOrderNo("2088522442306921"));
	}
}
