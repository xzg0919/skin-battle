package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import com.tzj.collect.core.param.admin.LjAdminBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.app.ScoreAppBean;
import com.tzj.collect.core.param.app.TimeBean;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.core.result.app.AppOrderResult;
import com.tzj.collect.core.result.app.AppScoreResult;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.collect.core.param.business.CompanyBean;
import com.tzj.collect.core.param.iot.IotParamBean;
import com.tzj.collect.core.result.third.ThirdOrderResult;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.entity.VoucherMember;
import org.eclipse.paho.client.mqttv3.MqttClient;


import java.util.List;
import java.util.Map;


/**
 * @Author 王灿
 **/
public interface OrderService extends IService<Order> {


	
	/**
     * 获取会员的订单列表 分页
     * @author 王灿
     * @param num : 第几页
     * @param size : 共多少条
     * @return
     */
	@DS("slave")
	Map<String,Object> getOrderlist(String aliUserId, Integer status, int num, int size);

	 /**
     * 下单接口
     * 该接口参数复杂 待定中...
     * @param  orderbean : 订单参数实体
     * @return long
     */
	 Map<String,Object> saveOrder(OrderBean orderbean,MqttClient mqtt4PushOrder);

	/**
	 * app保存订单信息
	 * @param orderbean
	 * @return
	 */
	boolean saveByRecy(OrderBean orderbean);

	Object getPriceByOrderId(OrderBean orderbean,MqttClient mqttClient);

	Object getCommissionsPriceByOrderId(OrderBean orderbean,Integer count);
	/**
	 * 回收员无需重量提交订单其他信息
	 * @param orderbean
	 * @return
	 */
	Object editByAdminOrder(OrderBean orderbean);

	/**
	 * 后台重新编辑订单重量
	 * @param orderBean
	 * @return
	 */
	boolean updateOrderAchItem(OrderBean orderBean);

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
     * @return List<Order>
     */
	 @DS("slave")
	List<Order> getUncompleteList(String aliUserId);

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
	Map<String, Object> getOrderLists(BOrderBean orderBean, PageBean pageBean);


	/**
	 * 根据各种状态查询相订单表相关的条数
	 * @author 王灿
	 * @param status:订单的条件
	 * @param companyId:企业Id
	 * @return
	*/
	@DS("slave")
	Map<String,Object> selectCountByStatus(BOrderBean orderBean);

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
	 * @return
	 */
	boolean modifyOrder(OrderBean orderBean);
	/**
	 * 获得当前回收人员数据
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
	String completeOrder(Order order, String orderInitStatus);
	/**
	 * 取消订单
	 * @author 王灿
	 * @param order:订单
	 * @return
	*/
	String  orderCancel(Order order, String orderInitStatus,MqttClient mqttClient);
	/**
	 * 订单详情(用户)
	 * @author 王灿
	 * @return
	*/
	@DS("slave")
	Map<String,Object> selectDetail(OrderBean orderbean);
	/**
	 * 订单详情(企业)
	 * @author 王灿
	 * @return
	*/
	@DS("slave")
	Map<String,Object> selectOrderByBusiness(Integer orderId);

	@DS("slave")
	Map<String,Object> selectOrderByBusiness1(Integer orderId);
	/**
	 * 获取用户提交生活垃圾订单id详情
	 * @author 王灿
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
	String updateOrderByBusiness(Integer orderId, String status, String cancelReason, Integer recyclerId,MqttClient mqttClient);

	boolean modifyOrderSta(OrderBean orderBean,MqttClient mqttClient);

	boolean modifyOrderByPayment(OrderBean orderBean, VoucherMember voucherMember);
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
    public Object saveOrderByCardNo(OrderBean orderBean, Recyclers recyclers);
	/**
	 * 回收经理转派订单
	 * @author 王灿
	 * @param orderId : 订单id
	 * @param recyclerId : 需要转派的回收人员id
	 * @return
	 */
	public Object distributeOrder(Integer orderId, Integer recyclerId);
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
	public Object XcxSaveOrder(OrderBean orderbean, Member member,MqttClient mqtt4PushOrder);

	/**
	 * 导出企业的完成订单的Excel
	 * @param companyId
	 * @return
	 */
	@DS("slave")
	List<Map<String,Object>> outOrderExcel(Integer companyId, String type, String startTime, String endTime,String recyclerName);

	Object savefiveKgOrder(OrderBean orderBean);

	Object saveSmallAppOrder(OrderBean orderBean);

	/**
	 * 小程序大家具下单接口
	 * @param orderbean
	 * @return
	 */
	Map<String,Object> saveBigThingOrder(OrderBean orderbean, MqttClient mqtt4PushOrder) throws Exception;

	 void updateMemberPoint(String aliUserId, String OrderNo, double amount, String descrb);

	@DS("slave")
	Map<String, Object> getOrderListsDistribute(BOrderBean orderBean, PageBean pageBean);
	/**
	 * 根据订单传来的状态获取订单列表
	 */
	@DS("slave")
	Map<String, Object> getBigOrderList(String status, Long recycleId, PageBean pagebean);
	/**
	 * 根据订单id查询订单的详细信息
	 */
	@DS("slave")
	Map<String, Object> getBigOrderDetails(Integer orderId);

	String setAchOrder(OrderBean orderBean);

	String saveBigOrderPrice(OrderBean orderBean,MqttClient mqttClient);

	Map<String, Object> iotCreatOrder(IotParamBean iotParamBean);

	Map<String, Object> iotUpdateOrderItemAch(IotParamBean iotParamBean);

	void deleteBigOrderRemarks(Integer orderId);
	@DS("slave")
	List<Map<String,Object>> sevenDayorderNum(String streetId);
	@DS("slave")
	List<Map<String,Object>> oneDayorderNum(String streetId);

	//解析该订单的数据成蚂蚁森林结构MyslData
	OrderBean myslOrderData(String orderId);
	@DS("slave")
    Map<String, Object> getOrderListByPhone(OrderBean orderbean);
	@DS("slave")
    Map<String, Object> getBigOrderListByPhone(OrderBean orderbean);

	Object orderUpdateStatus(String companyId, String orderId);

	/**
	 * 根据订单id自动派单给具体的回收人员
	 * @param orderId
	 * @return
	 */
	String orderSendRecycleByOrderId(Integer orderId);

	Object tosendfiveKgOrder(Integer orderId);
	@DS("slave")
	Boolean selectOrderByImprisonRule(String aliUserId, String title, Integer orderNum, Integer dateNum);

	Object recallOrder(Integer orderId, Long recyclerId);

	/**
	 * 根据区域id 提供第三方的订单数据 目前仅给合肥市
	 * @param areaId
	 * @return
	 */
	@DS("slave")
	List<ThirdOrderResult> orderStatistics4Third(String areaId,String startTime,String endTime,Integer pageNumber,Integer pageSize);

	@DS("slave")
	Object getOrderListByAdmin(OrderBean orderBean);
	@DS("slave")
	Object getOrderListByAdminReception(OrderBean orderBean);
	@DS("slave")
	Object getXyOrderListByAdminReception(OrderBean orderBean);
	@DS("slave")
	List<Map<String, Object>> getOutComplaintOrderList(OrderBean orderBean);
	@DS("slave")
	Object getOrderDetailByIdByAdmin(String orderId);
	@DS("slave")
	Integer getOrderCountByLj(LjAdminBean ljAdminBean);
	@DS("slave")
	Integer getInitCountByLj(LjAdminBean ljAdminBean);
	@DS("slave")
	Integer getTosendCountByLj(LjAdminBean ljAdminBean);
	@DS("slave")
	Integer getReadyCountByLj(LjAdminBean ljAdminBean);
	@DS("slave")
	Integer getOrderCountBytitle(LjAdminBean ljAdminBean,String title,String isGreen);
	@DS("slave")
	Double getGreenBigPaymentOrderPrice(LjAdminBean ljAdminBean);
	@DS("slave")
	List<Map<String,Object>> getOrderCategoryByLj(LjAdminBean ljAdminBean);
	@DS("slave")
	List<Map<String,Object>> getHouseOrderCategoryByLj(LjAdminBean ljAdminBean,String isCash);
	@DS("slave")
	Double avgOrMaxDateByOrder(LjAdminBean ljAdminBean,String status,String avgOrMax,String title);
	@DS("slave")
	Integer getSumOrderBylj(LjAdminBean ljAdminBean);
	@DS("slave")
	Integer getOrderLjByStatus(LjAdminBean ljAdminBean,String status);
	@DS("slave")
	Object getNewOrderDetail(Integer orderId);
	@DS("slave")
	Object getAdminOrderDetail(Integer orderId);
	@DS("slave")
	Object getOrderDetailByOrderId(Integer orderId);
	@DS("slave")
	Map<String, Object> getOrderComplaint(String orderNo);
	@DS("slave")
	List<Map<String,Object>>selectHouseAmount();
	/**
	 *根据订单信息，查询该订单是否是10倍积分
	 * @return
	 */
	@DS("slave")
	boolean isTenGreen(Order order);

	/**
	 * 导出企业的五废完成订单的Excel
	 * @param companyId
	 * @param startTime
	 * @param endTime
	 * @return
	 * added by michael_wang
	 */
	@DS("slave")
	List<Map<String,Object>> orderDetail4HorseHold(Integer companyId,String startTime, String endTime,String recyclerName,String title);
	@DS("slave")
	List<Order> getOrderListss(Integer companyId,String orderNo,String linkMan,String recyclerName,String startTime, String endTime,String title);
	@DS("slave")
	Map<String, Object> select1Or4Map(Long id);
	@DS("slave")
	List<Map<String,Object>>select2Or3Map(Long id);
	@DS("slave")
	Object getReyclersServiceAbility(OrderBean orderBean,Integer companyId);
	@DS("slave")
	Object overTimeOrderListByReyclersId(OrderBean orderBean,Integer companyId);
	@DS("slave")
	List<Map<String,Object>> getRecyclerOrderList(OrderBean orderBean);

	Object cancleOrderExamine(OrderBean orderBean);
	@DS("slave")
	Map<String,Object> getOrderCancleExamineList(OrderBean orderBean);

	String agreeExamineOdrerStatus(OrderBean orderbean,MqttClient mqttClient);

	@DS("slave")
    Map<String, Object> getAllOrderMapOverview(BOrderBean orderBean);
    @DS("slave")
    List<Map<String, Object>> outAchOrderListOverview(BOrderBean bOrderBean);
    @DS("slave")
    List<Map<String, Object>> outOtherOrderListOverview(BOrderBean bOrderBean);

	void  updateOrderCompany(String streetId,String companyId,String title);

	Map<String, Object> receivingMoney(OrderBean orderbean);

	Object saveOrderReceptionByOrderNo(OrderBean orderbean);
	@DS("slave")
	Object getOrderComplaintDetail(String orderNo);

	Object addOrderComplaintBack(String orderNo,String type,String complaintBack);
	@DS("slave")
	Map<String, Object> getOrderAchItemDatail(OrderBean orderBean);
	@DS("slave")
	List<Long> getOrderListByTitleStatus(String startTime,String endTime,String title,String status);
	@DS("slave")
	List<Long> getOrderListByCity(String startTime,String endTime,String title);
	@DS("slave")
	List<Map<String,Object>> getOrderCancelByCompany(String startTime,String endTime);
	@DS("slave")
	List<Long> outOrderListByRecycler(String startTime,String endTime,String isOverTime);
	@DS("slave")
	List<Long> outOrderListGroupCompany(String startTime,String endTime);
	@DS("slave")
	Integer getManyOrderListByDate(String startTime,String endTime,String title,String status);
	@DS("slave")
	Integer getOrderListByDate(String startTime,String endTime,String title,String status);
	@DS("slave")
	Map<String, Object>  selectIotRecList(Long recId, String status, PageBean pageBean);

	Object saveXyOrder(String message);
	@DS("slave")
	Object sendXyOrderByCompanyId(OrderBean orderBean);
	@DS("slave")
	Map<String,Object> dsddOrderDetail(Integer orderId);
	@DS("slave")
	Object getOrderDetailPrice(Integer id);
	@DS("slave")
    Object uploadCategoryByAoTu(String aliUserId, String equipmentCode, String categoryName, Double rubbishWeight);

	Order getByOrderNo(String orderNo);

	List<Order> getOrders();


	void cancelOrderBatch(String companyId, String endDate);

	@Override
	boolean updateById(Order order);


	Map<String,Object> saveElectroMobileOrder(OrderBean orderbean);

	Map<String,Object> savePashmOrder(OrderBean orderbean);
}
