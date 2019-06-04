package com.tzj.collect.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.api.ali.param.OrderBean;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.api.ali.result.CommToken;
import com.tzj.collect.api.app.param.ScoreAppBean;
import com.tzj.collect.api.app.param.TimeBean;
import com.tzj.collect.api.app.result.AppOrderResult;
import com.tzj.collect.api.app.result.AppScoreResult;
import com.tzj.collect.api.app.result.AttrItem;
import com.tzj.collect.api.app.result.EvaluationResult;
import com.tzj.collect.entity.Order;
/**
 * @Author 王灿
 **/
public interface OrderMapper extends BaseMapper<Order> {
	/**
     * 根据会员Id 获取未完成的订单列表
     * @author 王灿
     * @param  memberId : 会员Id（主键）
     * @return List<Order>
     */
	List<Order> getUncompleteList(long memberId);
	
	/**
     * 获取会员的订单列表 分页
     * @author 王灿
     * @param memberId :会员表主键
     * @param page : 分页对象
     * @param sizeStart : 第几条开始
     * @param size : 共多少条
     * @return
     */
	List<Order> getOrderlist(@Param("memberId") int memberId,@Param("status") int status,@Param("sizeStart") int sizeStart,@Param("size") int size );
	
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
	 * @param orderBean:查询订单的条件
	 * @param pageBean : 分页的条件
	 * @return 
	*/
	List<Order> getOrderLists(@Param("companyId") Integer companyId,@Param("status") List<String> status,@Param("orderNo") String orderNo,@Param("linkMan") String linkMan,@Param("recyclersName") String recyclersName,@Param("pageSize") int pageSize,@Param("startSize") int startSize,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("isScan")String isScan, @Param("title")String title);
	/**
	 * 根据各种查询条件获取订单的条数
	 * @author 王灿
	 * @return 
	*/
	Integer getOrderListsCount(@Param("companyId") Integer companyId,@Param("status") List<String> status,@Param("orderNo") String orderNo,@Param("linkMan") String linkMan,@Param("recyclersName") String recyclersName,@Param("pageSize") int pageSize,@Param("startSize") int startSize,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("isScan")String isScan, @Param("title")String title);

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

	List<EvaluationResult> getScore(@Param("scoreAppBean")  ScoreAppBean scoreAppBean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);

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
	
	Order getDataBoard(@Param("companyId") String companyId,@Param("status")String status);
	
	List<Map<String,Object>> getBrokenData(@Param("companyId") String companyId,@Param("startTime")String startTime);
	List<Map<String,Object>> getBrokenWeek(@Param("companyId") String companyId,@Param("startTime")String startTime,@Param("endTime")String endTime);
	List<Map<String,Object>> getBrokenMath(@Param("companyId") String companyId,@Param("startTime")String startTime,@Param("endTime")String endTime);

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
	List<Map<String,Object>> outOrderExcel(@Param("companyId") Integer companyId,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);

	/**
	 * 大件已转派订单列表
	 * @param recycleId
	 * @param startPage
	 * @param endPage
	 * @return
	 */
	List<Map<String,Object>> getBigOrderTransferList(@Param("recycleId")Integer recycleId,@Param("startPage")Integer startPage,@Param("endPage")Integer endPage);
	Integer getBigOrderTransferCount(@Param("recycleId")Integer recycleId);
	/**
	 * 大件订单列表
	 * @param recycleId
	 * @param startPage
	 * @param endPage
	 * @return
	 */
	List<Map<String,Object>> getBigOrderList(@Param("status")Integer status,@Param("recycleId")Integer recycleId,@Param("startPage")Integer startPage,@Param("endPage")Integer endPage);
	Integer getBigOrderCount(@Param("status")Integer status,@Param("recycleId")Integer recycleId);

	AppOrderResult getBigOrderDetails(Integer orderId);

	void deleteBigOrderRemarks(Integer orderId);

	List<Map<String,Object>> sevenDayorderNum(String streetId);
	List<Map<String,Object>> oneDayorderNum(String streetId);

	Integer getOrderCountByPhone(@Param("orderBean")OrderBean orderbean);

	List<AppOrderResult> getOrderListByPhone(@Param("orderBean") OrderBean orderbean, @Param("startSize") int startSize, @Param("pageSize") int pageSize);
	List<Map<String,Object>> getBigOrderListByPhone(@Param("recycleId")Integer recycleId, @Param("tel")String tel,@Param("startPage")Integer startPage,@Param("endPage")Integer endPage);
	Integer getBigOrderCountByPhone(@Param("recycleId")Integer recycleId, @Param("tel")String tel);
}

