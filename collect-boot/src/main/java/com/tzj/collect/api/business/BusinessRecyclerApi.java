package com.tzj.collect.api.business;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.param.BusinessRecyclerBean;
import com.tzj.collect.api.business.param.RecyclerServiceBean;
import com.tzj.collect.api.business.param.RecyclersServiceRangeBean;
import com.tzj.collect.api.common.websocket.AppWebSocketServer;
import com.tzj.collect.common.util.BusinessUtils;
import com.tzj.collect.entity.CompanyAccount;
import com.tzj.collect.entity.CompanyRecycler;
import com.tzj.collect.entity.Recyclers;
import com.tzj.collect.service.*;
import com.tzj.module.api.annotation.*;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_COMMON_AUTHORITY;

@ApiService
public class BusinessRecyclerApi {

	@Autowired
	private RecyclersService recycleService;
	@Autowired
	private RecyclerCommunityService recyclerCommunityService;
	@Autowired
	private OrderEvaluationService orderEvaluationService;
	@Autowired
	private CompanyRecyclerService companyRecyclerService;
	@Autowired
	private AppWebSocketServer appWebSocketServer;
	@Autowired
	private RecyclersTitleService recyclersTitleService;
	/**
	 * 通过回收员姓名,id返回某公司回收人员列表
	* @Title: getRecyclerList
	* @Description: 【】
	* @date 2018年3月27日 上午10:12:16
	* @author:[王池][wjc2013481273@163.com]
	* @param @param recyclerBean
	* @param @return    参数
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	 @Api(name = "business.search.getRecyclerList", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getRecyclerList(BusinessRecyclerBean recyclerBean){
		
		List< BusinessRecyclerBean> BusinessRecyclerBeanList  = companyRecyclerService.getgetSearchCompanyRecyclerList(recyclerBean);
		
		// 刚开始的页面为第一页
		if(recyclerBean.getPage()==null){
			PageBean page = new PageBean();
			page.setPageNumber(1);
			page.setPageSize(10);
			recyclerBean.setPage(page);			
		}
		// list的大小
		int count = BusinessRecyclerBeanList.size();
		// 每页的开始数
		int starCount = (recyclerBean.getPage().getPageNumber()-1)*recyclerBean.getPage().getPageSize();
		// 设置总页数
		int totalPage = (count % recyclerBean.getPage().getPageSize() == 0 ? count / recyclerBean.getPage().getPageSize() : count / recyclerBean.getPage().getPageSize() + 1);
		//每页显示的list数据
		recyclerBean.setDataList(BusinessRecyclerBeanList.subList(starCount,count-starCount > recyclerBean.getPage().getPageSize() ? starCount+recyclerBean.getPage().getPageSize() : count));
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("dataList", recyclerBean.getDataList());
		result.put("current", recyclerBean.getPage().getPageNumber());
		result.put("totalPage", totalPage);
		return result;
		
	}
	/**
	 * 查看回收人员的回收范围
	* @Title: recyclerServiceList
	* @date 2018年3月27日 下午2:18:36
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数
	* @return List<RecyclerServiceBean>    返回类型
	 */
	 @Api(name = "business.search.recyclerServiceList", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<RecyclerServiceBean> recyclerServiceList(BusinessRecyclerBean recyclerBean){
		
		return recyclerCommunityService.recyclerServiceList(recyclerBean);
		
	}
	/**
	 * 据回收人员id获取回收范围个数
	* @Title: getRecyclerServiceListCount
	* @date 2018年3月27日 下午4:55:16
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数
	* @return int    返回类型
	 */
	 @Api(name = "business.search.getRecyclerServiceListCount", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public int getRecyclerServiceListCount(BusinessRecyclerBean recyclerBean){
	
	    int	count = recyclerCommunityService.getRecyclerServiceListCount(recyclerBean);
		return count;
	}
	/**
	 * 根据回收人员id获取该公司的回收人员详情
	* @Title: getRecyclersById
	* @date 2018年3月27日 下午3:47:09
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数
	* @return Map<String,Object>    返回类型
	 */
	 @Api(name = "business.search.getRecyclersById", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Map<String, Object> getRecyclersById(BusinessRecyclerBean recyclerBean){
		Recyclers recycler = recycleService.getRecyclersById(recyclerBean);
		//查询回收人员的评价数(好评，中评，差评)
		Map<String, Object> result = orderEvaluationService.getScoreCount(recycler.getId());
		result.put("recycler", recycler);
		return result;
		
	}
	/**
	 * 修改回收人员的状态(启用，禁用)
	* @Title: editorDelflag
	* @date 2018年3月27日 下午5:15:05
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数
	* @return String    返回类型
	 */
	 @Api(name = "business.search.editorDelflag", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public String editorDelflag(BusinessRecyclerBean recyclerBean){		
		 CompanyRecycler companyRecycler = companyRecyclerService.getCompanyRecyclerByRecyclerId(recyclerBean.getId());
		String delFlag = recyclerBean.getDelFlag();
		String msg = "操作成功";
		switch(delFlag){
		case "0" :
			companyRecycler.setDelFlag(delFlag);//启用回收人员
			companyRecyclerService.update(companyRecycler, new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclerBean.getId()));
		 break;
		case "1" :
			companyRecycler.setDelFlag(delFlag);//禁用回收人员
			companyRecyclerService.update(companyRecycler, new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclerBean.getId()));
			break;
		default : msg="请传入正确的状态" ;
		}
		return msg;
	}
	
	/**
	 * 查询回收人员申请列表
	* @Title: getRecyclersApply
	* @date 2018年3月27日 下午5:55:01
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数(公司id,状态)
	* @return List<Recyclers>    返回类型
	 */
	 @Api(name = "business.search.getRecyclersApply", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<Recyclers> getRecyclersApply(BusinessRecyclerBean recyclerBean){
		
		return recycleService.getRecyclersApply(recyclerBean);
	}
	/**
	 * 操作申请回收人员的申请状态(同意，拒绝)
	* @Title: editorStatus
	* @date 2018年3月28日 上午9:53:08
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数(公司id,回收人员id)
	* @return String    返回类型
	 */
	 @Api(name = "business.search.editorStatus", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public String editorStatus(BusinessRecyclerBean recyclerBean){
		CompanyRecycler companyRecycler = companyRecyclerService.getCompanyRecycler(recyclerBean);
		String applyStatus = recyclerBean.getApplyStatus();
		String msg = "操作成功";
		switch (applyStatus){
		case "1" :
			companyRecycler.setStatus(applyStatus);//同意
			companyRecycler.setUpdateDate(Calendar.getInstance().getTime());
			companyRecyclerService.update(companyRecycler, new EntityWrapper<CompanyRecycler>().eq("company_id", recyclerBean.getCompanyId()).eq("recycler_id", recyclerBean.getId()));
		   break;
		case "2" :
			companyRecycler.setStatus(applyStatus);//拒绝
			companyRecycler.setUpdateDate(Calendar.getInstance().getTime());
			companyRecyclerService.update(companyRecycler, new EntityWrapper<CompanyRecycler>().eq("company_id", recyclerBean.getCompanyId()).eq("recycler_id", recyclerBean.getId()));
		   break;
		default :
			msg="请传入正确的状态";
		}
		return msg;
	 }
	
	/**
	 * 查看该公司下的回收人员的身份证信息
	* @Title: findIdCard
	* @date 2018年3月28日 上午10:28:16
	* @author:[王池]
	* @param @param recyclerBean
	* @param @return    参数
	* @return Recyclers    返回类型
	 */
	 @Api(name = "business.search.findIdCard", version = "1.0")
	 @SignIgnore
	 @RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Recyclers findIdCard(BusinessRecyclerBean recyclerBean){
		
		Recyclers recyclers = companyRecyclerService.findIdCard(recyclerBean);
		return recyclers;
	}
	/**
	 * 获取该企业的所有业务经理信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.getRecyclers", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getRecyclers(BusinessRecyclerBean recyclerBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		String isBigRecycle = "N";
		if (recyclerBean != null){
			isBigRecycle = recyclerBean.getIsBigRecycle();
		}
		return recycleService.getRecyclers(companyAccount.getCompanyId(), isBigRecycle);
	}
	/**
	 * 保存业务经理，和下属回收人员的信息(最新)
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.updateOrSaveRecyclersRange", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object updateOrSaveRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean) throws Exception{
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return recycleService.updateOrSaveRecyclersRange(recyclersServiceRangeBean,companyAccount.getCompanyId());
	}

	/**
	 * 保存业务经理，和下属回收人员的信息（）
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.saveRecyclersRange", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object saveRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean) throws Exception{
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		String result = recycleService.saveRecyclersRange(recyclersServiceRangeBean,companyAccount.getCompanyId()).toString();
			String api="http://172.19.182.58:9090/business/api";
			try{
				if("172.19.182.58".equals(InetAddress.getLocalHost().getHostAddress())){
					api = "http://172.19.182.59:9090/business/api";
				}
				HashMap<String,Object> param=new HashMap<>();
				param.put("name","business.recycle.sendMessage");
				param.put("version","1.0");
				param.put("format","json");
				param.put("app_key","app_id_3");
				param.put("timestamp", Calendar.getInstance().getTimeInMillis());
				param.put("nonce", UUID.randomUUID().toString());
				param.put("data",recyclersServiceRangeBean);
				String jsonStr = JSON.toJSONString(param);
				String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_99aabbcc");
				param.put("sign", sign);
				System.out.println("请求的参数是 ："+JSON.toJSONString(param));
				Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
				String resultJson=response.body().string();
				System.out.println("返回的参数是 ："+resultJson);
			}catch (Exception e){
				System.out.println("同意回收人员时，给另一台推送消息失败");
			}
			return result;
	}
	/**
	 * 保存业务经理，和下属回收人员的信息()
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.sendMessage", version = "1.0")
	@SignIgnore
	@AuthIgnore
	public void sendMessage(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		try {
			appWebSocketServer.sendInfo(recyclersServiceRangeBean.getRecycleId().toString(), "你是回收经理");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存业务经理更改区域信息(废弃)
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.updateRecyclersRange", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object updateRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return recycleService.updateRecyclersRange(recyclersServiceRangeBean,companyAccount.getCompanyId());
	}
	/**
	 * 根据市级Id和回收人员id获取区域信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.getAreaRecyclersRange", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getAreaRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return recycleService.getAreaRecyclersRangeList(recyclersServiceRangeBean, companyAccount.getCompanyId().toString());
	}
	/**
	 * 根据区域Id和回收人员id获取街道，小区信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.getStreeRecyclersRange", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getStreeRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return recycleService.getStreeRecyclersRange(recyclersServiceRangeBean.getAreaId(),recyclersServiceRangeBean.getRecycleId(),companyAccount.getCompanyId(),recyclersServiceRangeBean.getTitle());
	}
	/**
	 * 获取回收经理人员列表
	 * @author wangcan
	 * @updateBy sgmark 增加电话号码搜索
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.getRecyclersList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getRecyclersList(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return recycleService.getRangeRecyclersList(companyAccount.getCompanyId(),recyclersServiceRangeBean.getRecycleName(),recyclersServiceRangeBean.getCityId(),recyclersServiceRangeBean.getPageNum(),recyclersServiceRangeBean.getPageSize(),recyclersServiceRangeBean.getIsBigRecycle(), recyclersServiceRangeBean.getTel());
	}
	/**
	 * 根据回收经理Id获取下属回收人员列表
	 * @author wangcan
	 * @param
	 * @return
	 * @updateBy sgmark@aliyun.com(根据公司找当前下属回收人员)
	 */
	@Api(name = "business.recycle.getSunRecyclersList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public List<Recyclers> getSunRecyclersList(RecyclersServiceRangeBean recyclerBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
			String isBigRecycle = "N";
			if (recyclerBean != null){
				isBigRecycle = recyclerBean.getIsBigRecycle();
			}
		return recycleService.getRecyclersListByParentId(companyAccount.getCompanyId(), recyclerBean.getRecycleId(), isBigRecycle);
	}
	/**
	 * 根据回收人员Id查询详细信息列表
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.getRecyclersDetails", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	@DS("slave")
	public Object getRecyclersDetails(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return recycleService.selectById(recyclersServiceRangeBean.getRecycleId());
	}

	/**
	 * 删除公司下面所对应的回收员
	  * @author sgmark@aliyun.com
	  * @date 2019/4/12 0012
	  * @param
	  * @return
	  */
	@Api(name = "business.recycle.del", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object recyclersDel(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return companyRecyclerService.recyclersDel(companyAccount.getCompanyId(), recyclersServiceRangeBean.getRecycleId());
	}
	/**
	 * 删除公司下面所对应的回收员
	 * @author wang
	 * @date 2019/4/12 0012
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.isDelete", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object recycleIsDelete(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return companyRecyclerService.recycleIsDelete(companyAccount.getCompanyId(), recyclersServiceRangeBean.getRecycleId(),recyclersServiceRangeBean.getTitle());
	}

	/**
	 * 删除公司下面所对应的回收员
	 * @author wang
	 * @date 2019/4/12 0012
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.delete", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object recycleDelete(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return companyRecyclerService.recycleDelete(companyAccount.getCompanyId(), recyclersServiceRangeBean.getRecycleId(),recyclersServiceRangeBean.getTitle());
	}
	/**
	 * 查询选中小区数量信息
	 * @author wang
	 * @date 2019/4/12 0012
	 * @param
	 * @return
	 */
	@Api(name = "business.recycle.getRecycleRangeByTitle", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = BUSINESS_API_COMMON_AUTHORITY)
	public Object getRecycleRangeByTitle(RecyclersServiceRangeBean recyclersServiceRangeBean) {
		CompanyAccount companyAccount = BusinessUtils.getCompanyAccount();
		return companyRecyclerService.getRecycleRangeByTitle(companyAccount.getCompanyId().toString(),recyclersServiceRangeBean.getRecycleId(),recyclersServiceRangeBean.getTitle());
	}


}
