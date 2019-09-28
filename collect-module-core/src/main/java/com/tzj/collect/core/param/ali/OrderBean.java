package com.tzj.collect.core.param.ali;


import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.OrderPic;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 前台订单对象
 * @author Michael_Wang
 *
 */
@Data
public class OrderBean {
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
     * 行政区Id
     */
    private String cityId;
    
	private List<Integer> status2;

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
     * 区域id
     */
    private  Integer areaId;
	/**
	 *
	 */
	private String orderRemarks;
    /**
     * 街道Id
     */
    private  Integer streetId;
    /**
     * 小区id
     */
    private  Integer communityId;
    
   
    private Community community;
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
     * 是否评价过
     */
    private  String isEvaluated = "0";
    /**
     * 取消原因
     */
    private String cancelReason;
    /**
     * 取消时间
     */
    private Date cancelTime;
    /**
     * 海域 0为私海
     */
    private String level;
    /**
     * 上门时间
     */
    private String arrivalTime;
    /**
     * 上午am 下午pm
     */
    private String arrivalPeriod;
    /**
     * 绿账号
     */
    private  String greenCode;
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
    private  double amount;
    /**
     * 是否是免费 0是不免费，1是免费
     */
    private  String isCash;
	/**
	 * 类型
	 */
	private  String type;
    
    //page对象
    private PageBean  pagebean;
    private String dingDingUrl;   //钉钉通知的连接

	private String startTime;
	private String endTime;
   
    private OrderItemBean  orderItemBean;
    
   
    private OrderPic  OrderPic;
    
    private String title;//回收无类型
    
    private List<IdAmountListBean> idAndListList;//构造参数
    
    private List<OrderItemBean> orderItemList;

    private String resultStatus;//交易是否成功
    
    /**
	 * 订单完成时,用户的签名的图片链接
	 */
	private String signUrl;

	private String enterpriseCode;//以旧换新码

	private String picUrl;

	/**
	 * 快递总重量
	 */
	private String expressAmount;
	/**
	 * 快递单号
	 */
	private String expressNo;
	/**
	 * 快递员姓名
	 */
	private String expressName;
	/**
	 * 快递员电话
	 */
	private String expressTel;
	/**
	 * 第三方物流公司ID
	 */
	private String logisticsId;

	/**
	 * 是否授权蚂蚁森林能量仅仅用于页面展示
	 */
	private String isMysl;

	private String isRisk;

	private String myslParam;
	
	private String formId;

	private String OrderNo;

	private String linkName;
    /**
     * 经纬度
     */
    private String location;

    private String isOverTime;//是否超时0，不超时  1超时

    private String recyclerName;//

    private String mobile;//

    private String isBig;//"Y"是大件 其他情况不是

    private String isDelivery;//马上回收（每笔订单是否派送红包）

    private String isComplaint; //是否客诉

    private String reason;

    private String complaintType;//客诉类型  0催派 1催接 2催收  3形成客诉

}
