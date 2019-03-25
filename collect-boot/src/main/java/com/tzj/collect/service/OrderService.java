package com.tzj.collect.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.app.param.ScoreAppBean;
import com.tzj.collect.api.app.param.TimeBean;
import com.tzj.collect.api.app.result.AppOrderResult;
import com.tzj.collect.api.app.result.AppScoreResult;
import com.tzj.collect.api.business.param.BOrderBean;
import com.tzj.collect.api.business.param.CompanyBean;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Recyclers;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;


/**
 * @Author 王灿
 **/
public interface OrderService extends IService<Order> {
             
	Order getLastestOrderByMember(Integer memberId);
	
	/**
     * 获取会员的订单列表 分页
     * @author 王灿
     * @param memberId :会员表主键
     * @param page : 分页对象
     * @param num : 第几页
     * @param size : 共多少条
     * @return
     */
	Map<String,Object> getOrderlist(long memberId,Integer status,int num,int size );
	
	 /**
     * 下单接口
     * 该接口参数复杂 待定中...
     * @param  orderbean : 订单参数实体
     * @return long
     */
	String saveOrder(OrderBean orderbean);
	
	boolean saveByRecy(OrderBean orderbean);

	/**
	 * @author sgmark@aliyun.com
	 * @param status 状态, page 分页对象
	 * @return Page
	 */
	Page<Order> getOrderPage(String status, PageBean page);
	
	 /**
     * 根据会员Id 获取未完成的订单列表
     * @author 王灿
     * @param  memberId : 会员Id（主键）
     * @return List<Order>
     */
	List<Order> getUncompleteList(long memberId);
	
	 /**
     * 根据订单id获取订单详情
     * @author 王灿
     * @param orderId : 订单主键Id 
     * @return
     */
	Order selectOrder(int orderId);

	
	/**根据查询条件获取订单
	* @Title: getSearchOrder
	* @Description: 【】
	* @date 2018年3月19日 上午11:09:03
	* @param @param orderBean 订单查询条件
	* @param @param pageBean 分页对象
	* @param @return    参数
	* @return Page<Order>    返回类型
	* @throws
	*/
	
	Page<Order> getSearchOrder(BOrderBean orderBean, PageBean pageBean);
	
	/**
	 * 根据各种查询条件获取订单 列表
	 * @author 王灿
	 * @param orderBean:查询订单的条件
	 * @param pageBean : 分页的条件
	 * @return 
	*/
	Map<String, Object> getOrderLists(BOrderBean orderBean,PageBean pageBean);
	
	
	/**
	 * 根据各种状态查询相订单表相关的条数
	 * @author 王灿
	 * @param status:订单的条件
	 * @param companyId:企业Id
	 * @return 
	*/
	Map<String,Object> selectCountByStatus(String status, Integer companyId, Category.CategoryType categoryType);

	/**
	 * 根据订单状态获得订单列表
	 * @param orderbean
	 * @return
	 */
	Map<String,Object> getAppOrderList(OrderBean orderbean);
	/**
	 * 根据订单id或者订单单号获得订单详情
	 * @param orderbean
	 * @return
	 */
	AppOrderResult getOrderDetails(OrderBean orderbean);
	/**
	 * 主要是修改上门时间能通用但必须传订单单号或者订单id 及回收人员id,否则返回false
	 * @param order
	 * @return
	 */
	boolean modifyOrder(OrderBean orderBean);
	/**
	 * 获得当前回收人员数据
	 * @param orderBean
	 * @return
	 */
	AppOrderResult getRecord(TimeBean timeBean);
	/**
	 * 得到当前回收人员的评价列表,好评比率
	 * @author sgmark@aliyun.com
	 * @param scoreAppBean
	 * @return
	 */
	AppScoreResult getScoreEvaRate(ScoreAppBean scoreAppBean);
	

	/**
	 * 完成订单
	 * @author 王灿
	 * @param order:订单
	 * @return 
	*/
	String completeOrder(Order order,String orderInitStatus);
	/**
	 * 取消订单
	 * @author 王灿
	 * @param order:订单
	 * @return 
	*/
	String  orderCancel(Order order,String orderInitStatus);
	/**
	 * 订单详情(用户)
	 * @author 王灿
	 * @param order:订单
	 * @return 
	*/
	Map<String,Object> selectDetail(OrderBean orderbean);
	/**
	 * 订单详情(企业)
	 * @author 王灿
	 * @param order:订单
	 * @return 
	*/
	Map<String,Object> selectOrderByBusiness(Integer orderId);
	/**
	 * 获取用户提交生活垃圾订单id详情
	 * @author 王灿
	 * @param order:订单
	 * @return 
	*/
	Map<String,Object> getInitDetail(Integer orderId);
	/**
	 * 派单、驳回、接单 接口(企业)
	 * @author 王灿
	 * @param
	 * @return
	 */
	String updateOrderByBusiness(Integer orderId,String status,String cancelReason,Integer recyclerId);

	boolean modifyOrderSta(OrderBean orderBean);
	/**
	 * 订单数据看板折线图
	 * @author 王灿
	 * @param orderBean
	 * @return 
	 */
	List<Map<String,Object>> getBrokenLineData(CompanyBean companyBean);
	 /**
	 * 扫描用户会员卡卡号完成订单
	 * @author 王灿
	 * @param orderBean
	 * @return
	 */
    public Object saveOrderByCardNo(OrderBean orderBean,Recyclers recyclers);
	/**
	 * 回收经理转派订单
	 * @author 王灿
	 * @param orderId : 订单id
	 * @param recyclerId : 需要转派的回收人员id
	 * @return
	 */
	public Object distributeOrder(Integer orderId,Integer recyclerId);
	/**
	 * 转派订单列表
	 * @author 王灿
	 * @param recyclerId : 需要转派的回收人员id
	 * @return
	 */
	public Object distributeOrderList(Integer recyclerId);
	/**
	 * 小程序保存六废订单
	 * @author 王灿
	 * @param
	 * @return
	 */
	public Object XcxSaveOrder(OrderBean orderbean,Member member);

	/**
	 * 导出企业的完成订单的Excel
	 * @param companyId
	 * @return
	 */
	List<Map<String,Object>> outOrderExcel(Integer companyId,String type,String startTime,String endTime);

	Object savefiveKgOrder(OrderBean orderBean);

	 void updateMemberPoint(Integer memberId, String OrderNo, double amount,String descrb);


}
