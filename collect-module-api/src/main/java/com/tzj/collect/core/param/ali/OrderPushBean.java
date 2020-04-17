package com.tzj.collect.core.param.ali;


import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.OrderPic;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 前台订单对象
 * @author Michael_Wang
 *
 */
@Data
public class OrderPushBean {

	//用来储存订单描述
	private String remarks;
	
	private String achRemarks;//完成订单描述
	/**
	 * 分类父级Id
	 */
	private Integer categoryParentId;
	//订单id
	private Integer id;
	 /**
     * 会员id
     */
    private  Integer memberId;
    
    /**
     * 状态 默认是初始状态
     */
    private String status;
	/**
     * 回收企业id
     */
    private  Integer companyId;
    
    private String companyName;
    
    /**
     * 回收人员id
     */
    private  Integer recyclerId;
    
    /**
     * 订单编号
     */
    private  String orderNo; 

    /**
     * 地址
     */
    private  String address;
    
    private String fullAddress;
    /**
     * 电话
     */
    private  String tel;
    /**
     * 联系人
     */
    private  String linkMan;
    /**
     * 分类id
     */
    private  Integer categoryId;
    
   
    private Category category;
    /**
     * 分类id的parent_ids
     */
    private  String categoryParentIds;
    /**
     * 基准价
     */
    private BigDecimal price;
    /**
     * 计量单位
     */
    private  String unit;
    /**
     * 数量
     */
    private  Integer qty;
    /**
     * 完成时间
     */
    private Date completeDate;

    /**
     * 取消原因
     */
    private String cancelReason;
    /**
     * 取消时间
     */
    private String cancelTime;
    /**
     * 上门时间
     */
    private String arrivalTime;
    /**
     * 上午am 下午pm
     */
    private String arrivalPeriod;
    /**
     * 阿里userid
     */
    private  String aliUserId;
    /**
     * 完成价格
     */
    private  String achPrice;
    /**
     * 重量
     */
    private  Double amount;
    /**
     * 是否是免费 0是不免费，1是免费
     */
    private  String isCash;
	/**
	 * 类型
	 */
	private  String type;
    

	private String startTime;
	private String endTime;
   
    private String title;//回收无类型
    
    private String resultStatus;//交易是否成功
    /**
     * 是否授权蚂蚁森林能量 0 不需要 1需要
     */
    private String isMysl;
    
    /**
	 * 订单完成时,用户的签名的图片链接
	 */
	private String signUrl;

	private String enterpriseCode;//以旧换新码

	private String picUrl;

	private String special;

	private String templateId;

	private String OrderNo;

	private String linkName;

    private String isOverTime;//是否超时0，不超时  1超时

    private String recyclerName;//

    private String mobile;//

    private String isBig;//"Y"是大件 其他情况不是

    private String reason;

    private String receiveTime;  //回收人员接r单时间
    private String recyclerTel;  //回收人员电话
    private String completeTime;   //完成时间
    /**
     * 得分
     */
    private  Integer score;
    /**
     * 评论内容
     */
    private  String content;

    /**
     * 回收物类目（一二级）
     */
    private List<Map<String, String>> categoryList;
    /**
     * 实际回收物类目（一二级）
     */
    private List<Map<String, Object>> achCategoryList;
    /**
     * 实际回收物照片
     */
    private List<String> achPicList;
}
