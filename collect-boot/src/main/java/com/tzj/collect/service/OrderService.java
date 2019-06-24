package com.tzj.collect.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.app.param.ScoreAppBean;
import com.tzj.collect.api.app.param.TimeBean;
import com.tzj.collect.api.app.result.AppOrderResult;
import com.tzj.collect.api.app.result.AppScoreResult;
import com.tzj.collect.api.business.param.BOrderBean;
import com.tzj.collect.api.business.param.CompanyBean;
import com.tzj.collect.api.iot.param.IotParamBean;
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

	@DS("slave")
	Order getLastestOrderByMember(Integer memberId);
	
	/**
     * 获取会员的订单列表 分页
     * @author 王灿
     * @param memberId :会员表主键
     * @param num : 第几页
     * @param size : 共多少条
     * @return
     */
	@DS("slave")
	Map<String,Object> getOrderlist(long memberId,Integer status,int num,int size );
	
	 /**
     * 下单接口
     * 该接口参数复杂 待定中...
     * @param  orderbean : 订单参数实体
     * @return long
     */
	 Map<String,Object> saveOrder(OrderBean orderbean);
	
	boolean saveByRecy(OrderBean orderbean);

	/**
	 * @author sgmark@aliyun.com
	 * @param status 状态, page 分页对象
	 * @return Page
	 */
	@DS("slave")
	Page<Order> getOrderPage(String status, PageBean page);
	
	 /**
     * 根据会员Id 获取未完成的订单列表
     * @author 王灿
     * @param  memberId : 会员Id（主键）
     * @return List<Order>
     */
	 @DS("slave")
	List<Order> getUncompleteList(long memberId);
	
	 /**
     * 根据订单id获取订单详情
     * @author 王灿
     * @param orderId : 订单主键Id 
     * @return
     */
	 @DS("slave")
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
	@DS("slave")
	Page<Order> getSearchOrder(BOrderBean orderBean, PageBean pageBean);
	
	/**
	 * 根据各种查询条件获取订单 列表
	 * @author 王灿
	 * @param orderBean:查询订单的条件
	 * @param pageBean : 分页的条件
	 * @return 
	*/
	@DS("slave")
	Map<String, Object> getOrderLists(BOrderBean orderBean,PageBean pageBean);
	
	
	/**
	 * 根据各种状态查询相订单表相关的条数
	 * @author 王灿
	 * @param status:订单的条件
	 * @param companyId:企业Id
	 * @return 
	*/
	@DS("slave")
	Map<String,Object> selectCountByStatus(String status, Integer companyId, Order.TitleType titleType);

	/**
	 * 根据订单状态获得订单列表
	 * @param orderbean
	 * @return
	 */
	@DS("slave")
	Map<String,Object> getAppOrderList(OrderBean orderbean);
	/**
	 * 根据订单id或者订单单号获得订单详情
	 * @param orderbean
	 * @return
	 */
	@DS("slave")
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
	@DS("slave")
	AppOrderResult getRecord(TimeBean timeBean);
	/**
	 * 得到当前回收人员的评价列表,好评比率
	 * @author sgmark@aliyun.com
	 * @param scoreAppBean
	 * @return
	 */
	@DS("slave")
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
	@DS("slave")
	Map<String,Object> selectDetail(OrderBean orderbean);
	/**
	 * 订单详情(企业)
	 * @author 王灿
	 * @param order:订单
	 * @return 
	*/
	@DS("slave")
	Map<String,Object> selectOrderByBusiness(Integer orderId);
	/**
	 * 获取用户提交生活垃圾订单id详情
	 * @author 王灿
	 * @param order:订单
	 * @return 
	*/
	@DS("slave")
	Map<String,Object> getInitDetail(Integer orderId);
	/**
	 * 派单、驳回、接单 接口(企业)
	 * @author 王灿
	 * @param
	 * @return
	 */
	String updateOrderByBusiness(Integer orderId,String status,String cancelReason,Integer recyclerId);

	boolean modifyOrderSta(OrderBean orderBean);
	/** 回调修改状态
	  * @author sgmark@aliyun.com
	  * @date 2019/6/11 0011
	  * @param
	  * @return
	  */
	String callbackForGround(OrderBean orderBean);
	/**
	 * 订单数据看板折线图
	 * @author 王灿
	 * @param orderBean
	 * @return 
	 */
	@DS("slave")
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
	@DS("slave")
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
	@DS("slave")
	List<Map<String,Object>> outOrderExcel(Integer companyId,String type,String startTime,String endTime);

	Object savefiveKgOrder(OrderBean orderBean);

	/**
	 * 小程序大家具下单接口
	 * @param orderbean
	 * @return
	 * @throws ApiException
	 */
	Map<String,Object> saveBigThingOrder(OrderBean orderbean) throws ApiException;

	 void updateMemberPoint(Integer memberId, String OrderNo, double amount,String descrb);

	@DS("slave")
	Map<String, Object> getOrderListsDistribute(BOrderBean orderBean, PageBean pageBean);
	/**
	 * 根据订单传来的状态获取订单列表
	 */
	@DS("slave")
	Map<String, Object> getBigOrderList(String status,Long recycleId , PageBean pagebean);
	/**
	 * 根据订单id查询订单的详细信息
	 */
	@DS("slave")
	Map<String, Object> getBigOrderDetails(Integer orderId);

	String setAchOrder(OrderBean orderBean);

	String saveBigOrderPrice(OrderBean orderBean);

	Map<String, Object> iotCreatOrder(IotParamBean iotParamBean);

	Map<String, Object> iotUpdateOrderItemAch(IotParamBean iotParamBean);

	void deleteBigOrderRemarks(Integer orderId);
	@DS("slave")
	List<Map<String,Object>> sevenDayorderNum(String streetId);
	@DS("slave")
	List<Map<String,Object>> oneDayorderNum(String streetId);
	@DS("slave")
	public String isUserOrder(String memberId);

	//解析该订单的数据成蚂蚁森林结构MyslData
	OrderBean myslOrderData (String orderId);
	@DS("slave")
    Map<String, Object> getOrderListByPhone(OrderBean orderbean);
	@DS("slave")
    Map<String, Object> getBigOrderListByPhone(OrderBean orderbean);

	Object orderUpdateStatus(String companyId,String orderId);

	/**
	 * 根据订单id自动派单给具体的回收人员
	 * @param orderId
	 * @return
	 */
	String orderSendRecycleByOrderId(Integer orderId);

	Object tosendfiveKgOrder(Integer orderId);
}
