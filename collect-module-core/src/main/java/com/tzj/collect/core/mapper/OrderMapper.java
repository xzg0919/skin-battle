package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.param.ali.BusinessOrderItemBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.app.ScoreAppBean;
import com.tzj.collect.core.param.app.TimeBean;
import com.tzj.collect.core.result.ali.ComCatePrice;
import com.tzj.collect.core.result.ali.CommToken;
import com.tzj.collect.core.result.app.AppOrderResult;
import com.tzj.collect.core.result.app.AppScoreResult;
import com.tzj.collect.core.result.app.AttrItem;
import com.tzj.collect.core.result.app.EvaluationResult;
import com.tzj.collect.core.result.third.ThirdOrderResult;
import com.tzj.collect.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author 王灿
 **/
public interface OrderMapper extends BaseMapper<Order> {
	/**
     * 根据会员Id 获取未完成的订单列表
     * @author 王灿
     * @return List<Order>
     */
	List<Order> getUncompleteList(@Param("aliUserId")String aliUserId);
	
	/**
     * 获取会员的订单列表 分页
     * @author 王灿
     * @param sizeStart : 第几条开始
     * @param size : 共多少条
     * @return
     */
	List<Order> getOrderlist(@Param("aliUserId")String aliUserId, @Param("status") int status, @Param("sizeStart") int sizeStart, @Param("size") int size);

	 /**
     * 根据订单id获取订单详情
     * @author 王灿
     * @param orderId : 订单主键Id
     * @return
     */
	Order selectOrder(int orderId);

	/**
	 * 根据各种查询条件获取订单 列表
	 * @author 王灿
	 * @return
	*/
	List<Order> getOrderLists(@Param("companyId") Integer companyId, @Param("status") List<String> status, @Param("orderNo") String orderNo, @Param("linkMan") String linkMan, @Param("recyclersName") String recyclersName, @Param("pageSize") int pageSize, @Param("startSize") int startSize, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("isScan") String isScan, @Param("title") String title,@Param("reInit") String reInit);
	/**
	 * 根据各种查询条件获取订单的条数
	 * @author 王灿
	 * @return
	*/
	Integer getOrderListsCount(@Param("companyId") Integer companyId, @Param("status") List<String> status, @Param("orderNo") String orderNo, @Param("linkMan") String linkMan, @Param("recyclersName") String recyclersName, @Param("pageSize") int pageSize, @Param("startSize") int startSize, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("isScan") String isScan, @Param("title") String title,@Param("reInit") String reInit);

	AppOrderResult getOrderDetails(@Param("orderBean") OrderBean orderbean);
	/**
	 * @author sgmark@aliyun.com
	 * @param orderbean
	 * @return
	 */
	List<AttrItem> getOrderItemList(@Param("orderBean") OrderBean orderbean);

	List<AttrItem> getOrderUrlList(@Param("orderBean") OrderBean orderbean);

	List<Map<String,Object>> getOrderAchUrlList(@Param("orderBean") OrderBean orderbean);

	List<AppOrderResult> getAppOrderList(@Param("orderBean") OrderBean orderbean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);

	Integer getAppOrderCount(@Param("orderBean") OrderBean orderbean);


	List<AppOrderResult> getAppOrderCancelTaskList(@Param("orderBean") OrderBean orderbean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);

	Integer getAppOrderCancelTaskCount(@Param("orderBean") OrderBean orderbean);

	AppOrderResult getRecord(@Param("timeBean") TimeBean orderBean);

	Integer getRecordNum(@Param("scoreAppBean") ScoreAppBean orderBean);

	List<EvaluationResult> getScore(@Param("scoreAppBean") ScoreAppBean scoreAppBean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);

	AppScoreResult getEvaRate(@Param("scoreAppBean") ScoreAppBean scoreAppBean);
	/**
	 * 取消订单发送消息弹框
	 * @param orderNo 订单id
	 * @return 小区名称，腾讯token
	 */
	CommToken getCommToken(@Param("orderNo") String orderNo);
	/**
	 * 获取父类名称去重
	 * @param orderId
	 * @return
	 */
	List<ComCatePrice> selectCateName(int orderId);
	/**
	 * 获取父类名称去重
	 * @param orderId
	 * @return
	 */
	List<ComCatePrice> selectCateAchName(int orderId);

	Order getDataBoard(@Param("companyId") String companyId, @Param("status") String status);

	List<Map<String,Object>> getBrokenData(@Param("companyId") String companyId, @Param("startTime") String startTime);
	List<Map<String,Object>> getBrokenWeek(@Param("companyId") String companyId, @Param("startTime") String startTime, @Param("endTime") String endTime);
	List<Map<String,Object>> getBrokenMath(@Param("companyId") String companyId, @Param("startTime") String startTime, @Param("endTime") String endTime);

	/**
	 * 转派订单列表
	 * @author 王灿
	 * @param recyclerId : 需要转派的回收人员id
	 * @return
	 */
	public List<Map<String,Object>> distributeOrderList(Integer recyclerId);
	//根据订单Id查询订单交易价格
	Object paymentPriceByOrderId(Integer orderId);

	/**
	 * 导出企业的完成订单的Excel
	 * @param companyId
	 * @return
	 */
	List<Map<String,Object>> outOrderExcel(@Param("companyId") Integer companyId, @Param("type") String type, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("recyclerName") String recyclerName);

	List<Map<String,Object>> outOrderExcelHouse(@Param("companyId") Integer companyId,@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("recyclerName") String recyclerName);

	/**
	 * 大件已转派订单列表
	 * @param recycleId
	 * @param startPage
	 * @param endPage
	 * @return
	 */
	List<Map<String,Object>> getBigOrderTransferList(@Param("recycleId") Integer recycleId, @Param("startPage") Integer startPage, @Param("endPage") Integer endPage);
	Integer getBigOrderTransferCount(@Param("recycleId") Integer recycleId);
	/**
	 * 大件订单列表
	 * @param recycleId
	 * @param startPage
	 * @param endPage
	 * @return
	 */
	List<Map<String,Object>> getBigOrderList(@Param("status") Integer status, @Param("recycleId") Integer recycleId, @Param("startPage") Integer startPage, @Param("endPage") Integer endPage);
	Integer getBigOrderCount(@Param("status") Integer status, @Param("recycleId") Integer recycleId);
	AppOrderResult getBigOrderDetails(Integer orderId);
	void deleteBigOrderRemarks(Integer orderId);
	List<Map<String,Object>> sevenDayorderNum(String streetId);

	List<Map<String,Object>> oneDayorderNum(String streetId);

	Integer getOrderCountByPhone(@Param("orderBean") OrderBean orderbean);

	List<AppOrderResult> getOrderListByPhone(@Param("orderBean") OrderBean orderbean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);

	List<Map<String,Object>> getBigOrderListByPhone(@Param("orderNo") String orderNo,@Param("recycleId") Integer recycleId, @Param("tel") String tel, @Param("startPage") Integer startPage, @Param("endPage") Integer endPage);

	Integer getBigOrderCountByPhone(@Param("orderNo") String orderNo,@Param("recycleId") Integer recycleId, @Param("tel") String tel);

	List<Map<String,Object>> getOrderListByAdmin(@Param("complaintType") String complaintType,@Param("companyId") String companyId, @Param("title") String title, @Param("status") String status, @Param("tel") String tel, @Param("orderNo")String orderNo, @Param("linkName")String linkName, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("startPage")Integer startPage, @Param("pageSize")Integer pageSize);

	Integer getOrderCountByAdmin(@Param("complaintType") String complaintType,@Param("companyId") String companyId,@Param("title") String title,@Param("status") String status,@Param("tel")String tel,@Param("orderNo")String orderNo,@Param("linkName")String linkName,@Param("startTime")String startTime,@Param("endTime")String endTime);

	List<Map<String,Object>> getOrderListByAdminReception(@Param("complaintType") String complaintType,@Param("companyId") String companyId, @Param("title") String title, @Param("status") String status, @Param("tel") String tel, @Param("orderNo")String orderNo, @Param("linkName")String linkName, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("startPage")Integer startPage, @Param("pageSize")Integer pageSize);

	Integer getOrderCountByAdminReception(@Param("complaintType") String complaintType,@Param("companyId") String companyId,@Param("title") String title,@Param("status") String status,@Param("tel")String tel,@Param("orderNo")String orderNo,@Param("linkName")String linkName,@Param("startTime")String startTime,@Param("endTime")String endTime);

	List<Map<String,Object>> getXyOrderListByAdminReception(@Param("isNormal")String isNormal,@Param("companyId") String companyId, @Param("title") String title, @Param("status") String status, @Param("tel") String tel, @Param("orderNo")String orderNo, @Param("linkName")String linkName, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("startPage")Integer startPage, @Param("pageSize")Integer pageSize);

	Integer getXyOrderCountByAdminReception(@Param("isNormal")String isNormal,@Param("companyId") String companyId,@Param("title") String title,@Param("status") String status,@Param("tel")String tel,@Param("orderNo")String orderNo,@Param("linkName")String linkName,@Param("startTime")String startTime,@Param("endTime")String endTime);

	List<Map<String,Object>> getOutComplaintOrderList(@Param("complaintType") String complaintType,@Param("companyId") String companyId,@Param("title") String title,@Param("status") String status,@Param("tel")String tel,@Param("orderNo")String orderNo,@Param("linkName")String linkName,@Param("startTime")String startTime,@Param("endTime")String endTime);

	Integer getOrderCountByLj(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate);
	Integer getInitCountByLj(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId);
	Integer getTosendCountByLj(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId);
	Integer getReadyCountByLj(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId);
	Integer getOrderCountBytitle(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate,@Param("title")String title,@Param("isGreen")String isGreen);
	Double getGreenBigPaymentOrderPrice(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate);
	List<Map<String,Object>> getOrderCategoryByLj(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate);
	List<Map<String,Object>> getHouseOrderCategoryByLjAsCash(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate);
	List<Map<String,Object>> getHouseOrderCategoryByLjAsGreen(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate);
	Double avgOrMaxDateByOrderAvg(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate,@Param("status")String status,@Param("title")String title);
	Double avgOrMaxDateByOrderMax(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate,@Param("status")String status,@Param("title")String title);
	Integer getSumOrderBylj(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate);
	Integer getOrderLjByStatus(@Param("cityId")String cityId,@Param("areaId")String areaId,@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("startDate")String startDate,@Param("endtDate")String endtDate,@Param("status")String status);

	/**
	 * 根据区域id 提供第三方的订单数据
	 * @param areaId
	 * @return
	 */
	public List<ThirdOrderResult> orderStatistics4Third(@Param("areaId") String areaId,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("startPage") Integer startPage, @Param("endPage") Integer endPage);

	List<Map<String,Object>> selectHouseAmount();

	/**
	 * 导出企业的完成订单的Excel
	 * @param companyId
	 * @param startTime
	 * @param endTime
	 * @return
	 * added by michael_wang
	 */
	List<Map<String,Object>> orderDetail4HorseHold(@Param("companyId") Integer companyId,@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("recyclerName")String recyclerName,@Param("title")String title);

	List<Map<String,Object>> getReyclersServiceAbility(@Param("companyId") Integer companyId,@Param("recyclerName") String recyclerName,@Param("mobile")String mobile,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("isBig")String isBig,@Param("isOverTime")String isOverTime,@Param("pageStart")Integer pageStart,@Param("pageSize")Integer pageSize);

	Integer getReyclersServiceAbilityCount(@Param("companyId") Integer companyId,@Param("recyclerName") String recyclerName,@Param("mobile")String mobile,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("isBig")String isBig,@Param("isOverTime")String isOverTime);

	List<Map<String,Object>> overTimeOrderListByReyclersId (@Param("companyId") Integer companyId,@Param("recyclerId")Integer recyclerId,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("isBig")String isBig,@Param("isOverTime")String isOverTime,@Param("pageStart")Integer pageStart,@Param("pageSize")Integer pageSize);

	Integer overTimeOrderListCount (@Param("companyId") Integer companyId,@Param("recyclerId")Integer recyclerId,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("isBig")String isBig,@Param("isOverTime")String isOverTime);

	List<Map<String,Object>> getRecyclerOrderList(@Param("companyId")Integer companyId,@Param("recyclerName") String recyclerName,@Param("mobile")String mobile,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("isBig")String isBig,@Param("isOverTime")String isOverTime);

	List<Map<String,Object>> getOrderCancleExamineList(@Param("companyId") Integer companyId,@Param("orderNo") String orderNo,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageStart") Integer pageStart,@Param("pageSize") Integer pageSize);

	Integer getOrderCancleExamineCount(@Param("companyId") Integer companyId,@Param("orderNo") String orderNo,@Param("startTime")String startTime,@Param("endTime")String endTime);

	void  updateOrderCompany(@Param("streetId")String streetId,@Param("companyId")String companyId,@Param("title")String title);

	List<Map<String, Object>> getAllOrderMapOverview(@Param("complaintType")String complaintType,@Param("status")String status, @Param("companyId")Integer companyId, @Param("categoryType")Serializable categoryType, @Param("pageStart")Integer pageStart, @Param("pageSize")Integer pageSize, @Param("startTime")String  startTime, @Param("endTime")String  endTime);

	Integer getAllOrderMapOverviewCount(@Param("complaintType")String complaintType,@Param("status")String status, @Param("companyId")Integer companyId, @Param("categoryType")Serializable categoryType, @Param("startTime")String  startTime, @Param("endTime")String  endTime);

    List<Map<String, Object>> outAchOrderListOverview(@Param("complaintType")String complaintType,@Param("companyId") String companyId, @Param("status")String status, @Param("categoryType")String categoryType, @Param("startTime")String  startTime, @Param("endTime")String  endTime);

    List<Map<String, Object>> outOtherOrderListOverview(@Param("complaintType")String complaintType,@Param("companyId") String companyId, @Param("status")String status, @Param("categoryType")String categoryType, @Param("startTime")String  startTime, @Param("endTime")String  endTime);
	/**
	 * 根据订单号查询订单是否存在客诉条件
	 */
	Map<String, Object> getOrderComplaint(@Param("orderNo") String orderNo);

	List<Long> getOrderListByTitleStatus(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("title")String title,@Param("status")String status);

	List<Long> getOrderListByCity(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("title")String title);

	List<Map<String,Object>> getOrderCancelByCompany(@Param("startTime")String startTime,@Param("endTime")String endTime);

	List<Long> outOrderListByRecycler(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("isOverTime")String isOverTime);

	List<Long> outOrderListGroupCompany(@Param("startTime")String startTime,@Param("endTime")String endTime);

	Integer getManyOrderListByDate(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("title")String title,@Param("status")String status);

    List<Map<String, Object>> selectIotRecList(@Param("recId")String recId, @Param("status")String status, @Param("pageStart") Integer pageStart,@Param("pageSize") Integer pageSize);

    Double getAmountByOrderId(@Param("orderId")Long orderId);

	List<BusinessOrderItemBean> getCategoryInfoByOrderId(@Param("orderId") String orderId,@Param("title") String title);

	List<Map<String, Object>>getCategoryPriceList(@Param("orderId")Integer id);
}

