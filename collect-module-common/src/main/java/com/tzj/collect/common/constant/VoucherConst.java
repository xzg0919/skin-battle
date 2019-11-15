package com.tzj.collect.common.constant;

/**
 * 
 * <p>Created on 2019年10月30日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [优惠券常量]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
 */
public class VoucherConst
{
    /**
     * 券状态-已领取
     */
    public static String VOUCHER_STATUS_CREATE = "CREATE";

    /**
     * 券状态-已绑定
     */
    public static String VOUCHER_STATUS_USEING = "USEING";

    /**
     * 券状态-已使用
     */
    public static String VOUCHER_STATUS_USED = "USED";

    /**
     * 券领取通知状态-领取成功
     */
    public static String VOUCHER_NOTIFY_OK = "OK";
    /**
     * 券领取通知状态-先领取再授权的发送成功
     */
    public static String VOUCHER_NOTIFY_REOK = "REOK";
    /**
     * 券领取通知状态-券码不存在
     */
    public static String VOUCHER_NOTIFY_NO = "NO";
    
    /**
     * 券领取通知状态-已被领取
     */
    public static String VOUCHER_NOTIFY_HAD = "HAD";
    
    /**
     * 券领取通知状态-会员不存在
     */
    public static String VOUCHER_NOTIFY_MEMBER = "MEMBER";
    
    /**
     * 券领取通知状态-发生错误
     */
    public static String VOUCHER_NOTIFY_ERROR = "ERROR";
    
    /**
     * 券领有效期类型--绝对日期
     */
    public static String VOUCHER_VALIDTYPE_ABSOLUTE = "ABSOLUTE";
    /**
     * 券领有效期类型--相对日期
     */
    public static String VOUCHER_VALIDTYPE_RELATIVE = "RELATIVE";
    
    
}
