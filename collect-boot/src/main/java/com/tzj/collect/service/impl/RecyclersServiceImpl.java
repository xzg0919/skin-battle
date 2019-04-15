
/**
* @Title: SbRecyclersServiceImpl.java
* @Package com.tzj.collect.service.impl
* @Description: 【】
* @date 2018年3月5日 下午1:38:50
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.service.impl;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.api.admin.param.RecyclersBean;
import com.tzj.collect.api.ali.param.AreaBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.param.BusinessRecyclerBean;
import com.tzj.collect.api.business.param.CompanyAccountBean;
import com.tzj.collect.api.business.param.RecyclersServiceRangeBean;
import com.tzj.collect.api.business.param.TitleBean;
import com.tzj.collect.api.common.websocket.AppWebSocketServer;
import com.tzj.collect.api.common.websocket.WebSocketServer;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.entity.*;
import com.tzj.collect.mapper.RecyclersMapper;
import com.tzj.collect.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @ClassName: SbRecyclersServiceImpl
* @Description: 【】
* @date 2018年3月5日 下午1:38:50
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@Service
@Transactional(readOnly = true)
public class RecyclersServiceImpl extends ServiceImpl<RecyclersMapper,Recyclers> implements RecyclersService{


	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private RecyclersMapper  recyclersMapper;
	@Autowired
	private CompanyRecyclerService companyRecyclerService;
	@Autowired
	private RecyclersService recyclerService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private RecyclersServiceRangeService recyclersServiceRangeService;
	@Autowired
	private AppWebSocketServer appWebSocketServer;
	@Autowired
	private RecyclersTitleService recyclersTitleService;
    /**
     * 根据手机号查询回收人员
     * @param mobile
     * @return
     */
    @Override
    public Recyclers selectByMobile(String mobile) {
        return selectOne(new EntityWrapper<Recyclers>().eq("tel", mobile));
    }

	@Override
	public Page<Recyclers> selectByRecyclersPage(Recyclers recyclers, PageBean page) {
		Page<Recyclers> pages = new Page<Recyclers>(page.getPageNumber(), page.getPageSize());
		EntityWrapper<Recyclers> wrapper = new EntityWrapper<Recyclers>();
		wrapper.eq("id", recyclers.getId());
		return this.selectPage(pages, wrapper);
	}

	/**
	 * 
	 * @Title: getRecyclerPage 
	 * @Description:回收人员分页 
	 * @author: 向忠国
	 * @param @param bean
	 * @param @return    设定文件  
	 * @throw
	 */
	@Override
	public List<Recyclers> getRecyclerPage(RecyclersBean bean) {
		return recyclersMapper.getRecyclerPage(bean);
	}

	/**
	 * 
	 * @Title: getRecyclerPageSize 
	 * @Description:获取回收人员总数量
	 * @author: 向忠国
	 * @param @param bean
	 * @param @return    设定文件  
	 * @throw
	 */
	@Override
	public Integer getRecyclerPageSize(RecyclersBean bean) {
		Integer record=recyclersMapper.getRecyclerPageSize(bean);
		return record==null?0:record;
	}


	@Override
	public RecyclersBean getRecEvaById(String recyclerId) {
		return recyclersMapper.getRecEvaById(recyclerId);
	}

	@Override
	public Page<Recyclers> selectRecPageByIds(String ids, CompanyAccountBean companyAccountBean) {
		Page<Recyclers> pages = new Page<Recyclers>(companyAccountBean.getPageBean().getPageNumber(), companyAccountBean.getPageBean().getPageSize());
		EntityWrapper<Recyclers> wrapper = new EntityWrapper<Recyclers>();
		wrapper.in("id", ids);
		RecyclersBean recyclersBean = companyAccountBean.getRecBean();
		if (recyclersBean.getRecyclerIdCard() != null && !"".equals(recyclersBean.getRecyclerIdCard())) {
			wrapper.eq("id_card", recyclersBean.getRecyclerIdCard());
		}
		if (recyclersBean.getRecyclerName() != null && !"".equals(recyclersBean.getRecyclerName())) {
			wrapper.eq("name_", recyclersBean.getRecyclerName());
		}
		return this.selectPage(pages, wrapper);
	}

	@Override
	public List<AdminCommunityBean> getRecSerCommById(String recyclerId) {
		return recyclersMapper.getRecSerCommById(recyclerId);
	}

	@Override
	public Integer getCommNumByRecId(String recyclerId) {
		return recyclersMapper.getCommNumByRecId(recyclerId);
	}

	
	/**
	 * 根据企业Id和分类Id 获取回收人员列表
	 * @author 王灿
	 * @param companyId :  企业Id
	 * @param categoryId : 分类Id
	 * @return
	 */
	@Override
	public List<Recyclers> getRecyclersList(Integer companyId, Integer categoryId) {		
		return recyclersMapper.getRecyclersList(companyId,categoryId);
	}


	/**
	 * 根据回收人员id获取回收人员的详情
	 */
	@Override
	public Recyclers getRecyclersById(BusinessRecyclerBean recyclerBean) {
		return recyclersMapper.getRecyclersById(recyclerBean);
	}

	/**
	 * 查询回收人员的申请列表
	 */
	@Override
	public List<Recyclers> getRecyclersApply(BusinessRecyclerBean recyclerBean) {
		return recyclersMapper.getRecyclersApply(recyclerBean.getCompanyId(),recyclerBean.getIsBigRecycle());
	}


	@Override
	public List<Recyclers> getRecyclersList2(Integer companyId,Integer orderId) {
		return recyclersMapper.getRecyclersLists(companyId,orderId);
	}
	/**
	 * 获取该企业的所有业务经理信息
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
	public List<Map<String,Object>> getRecyclers(Integer companyId){
		return recyclersMapper.getRecyclers(companyId);
	}

	/**
	 * 保存业务经理，和下属回收人员的信息
	 * @author wangcan
	 * @param recyclersServiceRangeBean
	 * @param companyId : 企业Id
	 * @return
	 */
	@Transactional
	@Override
	public Object saveRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean, Integer companyId){
		//判断回收人员入住状态
		if(StringUtils.isBlank(recyclersServiceRangeBean.getIsEnable())||Integer.parseInt(recyclersServiceRangeBean.getIsEnable())>2){
			return "请确定回收人员入住状态";
		}
		//根据回收人员Id查询回收人员信息
		Recyclers recyclers = recyclerService.selectById(recyclersServiceRangeBean.getRecycleId());
		//根据企业Id和回收人员Id获取关联信息表
		CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclersServiceRangeBean.getRecycleId()).eq("company_id", companyId).eq("status_","0"));
		if (companyRecycler==null||recyclers==null){
			return "查询不到该回收人员申请信息";
		}
		//拒绝该回收人员的申请
		if("2".equals(recyclersServiceRangeBean.getIsEnable())){
			companyRecycler.setStatus("2");
			companyRecyclerService.updateById(companyRecycler);
			return "操作成功";
		}
		//同意该回收人员是下级回收人员
		if("1".equals(recyclersServiceRangeBean.getIsEnable())){
			//根据经理Id查询经理信息
			Recyclers ManagerRecyclers = recyclerService.selectById(recyclersServiceRangeBean.getManagerId());
			if (ManagerRecyclers==null){
				return "查询不到传入的经理信息";
			}
			recyclers.setIsManager("0");
			recyclers.setCity(ManagerRecyclers.getCity());
			recyclers.setProvince(ManagerRecyclers.getProvince());
			recyclers.setParentsId(ManagerRecyclers.getId().intValue());
			recyclerService.updateById(recyclers);
			companyRecycler.setStatus("1");
			companyRecyclerService.updateById(companyRecycler);
			try {
				appWebSocketServer.sendInfo(recyclers.getId().toString(), "你是下属回收人员");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "操作成功";
		}
		//同意该回收人员是回收经理
		if("0".equals(recyclersServiceRangeBean.getIsEnable())){
			//根据市级id查询具体信息
			Area area = areaService.selectById(recyclersServiceRangeBean.getCityId());
			if(area==null){
				return "传入的市级id不对";
			}
			recyclers.setIsManager("1");
			recyclers.setCity(area.getId().intValue());
			recyclers.setProvince(area.getParentId());
			recyclerService.updateById(recyclers);
			companyRecycler.setStatus("1");
			companyRecyclerService.updateById(companyRecycler);
			//获取所有的区域Id
			List<AreaBean> areaList = recyclersServiceRangeBean.getAreaList();
			for (AreaBean areaBean: areaList) {
				//储存经理和区域的关联关系
				RecyclersServiceRange recyclersServiceRange = new RecyclersServiceRange();
				recyclersServiceRange.setAreaId(Integer.parseInt(areaBean.getStreeId()));
				recyclersServiceRange.setAreaParentsId(areaBean.getAreaId());
				recyclersServiceRange.setRecyclersId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
				recyclersServiceRangeService.insert(recyclersServiceRange);
			}
			//获取回收类型信息
			List<TitleBean> titleList = recyclersServiceRangeBean.getTitleList();
			for (TitleBean titleBean: titleList) {
				RecyclersTitle recyclersTitle = new RecyclersTitle();
				recyclersTitle.setRecycleId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
				recyclersTitle.setTitleId(Integer.parseInt(titleBean.getTitleId()));
				recyclersTitle.setTitleName(titleBean.getTitleName());
				recyclersTitleService.insert(recyclersTitle);
			}

			try {
				appWebSocketServer.sendInfo(recyclers.getId().toString(), "你是回收经理");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "操作成功";
		}
		return "您的操作有误";
	}
	/**
	 * 保存业务经理更改区域信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Transactional
	@Override
	public Object updateRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean, Integer companyId){
		//根据回收人员Id查询回收人员信息
		Recyclers recyclers = recyclerService.selectById(recyclersServiceRangeBean.getRecycleId());
		//根据市级id查询具体信息
		Area area = areaService.selectById(recyclersServiceRangeBean.getCityId());
		if(area==null){
			return "传入的市级id不对";
		}
		recyclers.setIsManager("1");
		recyclers.setCity(area.getId().intValue());
		recyclers.setProvince(area.getParentId());
		recyclerService.updateById(recyclers);
		//清除与该回收人员绑定的区域
		RecyclersServiceRange recyclersServiceRange = new RecyclersServiceRange();
		recyclersServiceRange.setDelFlag("1");
		recyclersServiceRangeService.update(recyclersServiceRange,new EntityWrapper<RecyclersServiceRange>().eq("recyclers_id",recyclersServiceRangeBean.getRecycleId()).eq("del_flag",0));
		//获取所有的区域Id
		List<AreaBean> areaList = recyclersServiceRangeBean.getAreaList();
		for (AreaBean areaBean: areaList) {
			//储存经理和区域的关联关系
			if(areaBean !=null ){
				RecyclersServiceRange ServiceRange = new RecyclersServiceRange();
				ServiceRange.setAreaId(Integer.parseInt(areaBean.getStreeId()));
				ServiceRange.setAreaParentsId(areaBean.getAreaId());
				ServiceRange.setRecyclersId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
				recyclersServiceRangeService.insert(ServiceRange);
			}
		}
		//清除回收人员与回收类型的关联关系
		recyclersTitleService.delete(new EntityWrapper<RecyclersTitle>().eq("recycle_id",recyclersServiceRangeBean.getRecycleId()));
		//获取回收类型信息
		List<TitleBean> titleList = recyclersServiceRangeBean.getTitleList();
		for (TitleBean titleBean: titleList) {
			if(titleBean != null){
				//储存经理和回收类型的关联关系
				RecyclersTitle recyclersTitle = new RecyclersTitle();
				recyclersTitle.setRecycleId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
				recyclersTitle.setTitleId(Integer.parseInt(titleBean.getTitleId()));
				recyclersTitle.setTitleName(titleBean.getTitleName());
				recyclersTitleService.insert(recyclersTitle);
			}
		}
		return "操作成功";
	}
	/**
	 * 根据市级Id和回收人员id获取区域信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Override
	public Object getAreaRecyclersRange(String cityId,String recycleId,Integer companyId){
		return recyclersMapper.getAreaRecyclersRange(cityId,recycleId);
	}
	/**
	 * 根据市级Id和回收人员id获取街道信息
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Override
	public Object getStreeRecyclersRange(String areaId,String recycleId,Integer companyId){
		return recyclersMapper.getStreeRecyclersRange(areaId,recycleId);
	}
	/**
	 * 获取回收经理人员列表
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
	@Override
	public Object getRangeRecyclersList(Integer companyId,String recycleName,String cityId ,Integer pageNum,Integer pageSize,String isBigRecycle){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> recycleList = recyclersMapper.getRangeRecyclersList(companyId.toString(),recycleName,cityId,(pageNum-1)*pageSize,pageSize,isBigRecycle);
		Integer count = recyclersMapper.getRangeRecyclersListCount(companyId.toString(),recycleName,cityId,isBigRecycle);
		if(recycleList.isEmpty()){
			return resultMap;
		}
		resultMap.put("count",count);
		resultMap.put("pageNum",pageNum);
		resultMap.put("recycleList",recycleList);
		return resultMap;
	}
	/**
	 * 获取回收经理的详细信息
	 * @author wangcan
	 * @param recyclerId : 经理Id
	 * @return
	 */
	public List<Map<String,Object>> getRecycleDetails(Integer recyclerId){
		return recyclersMapper.getRecycleDetails(recyclerId);
	}

	@Transactional
	@Override
	public String getAuthCode(String authCode,Long recyclersId) throws ApiException{

		Recyclers recyclers = this.selectById(recyclersId);
		//根据用户授权的具体authCode查询是用户的userid和token
		AlipaySystemOauthTokenResponse response = aliPayService.selectUserToken(authCode, AlipayConst.appId);
		if(!response.isSuccess()){
			throw new ApiException("授权失败，请重新授权");
		}
		String accessToken = response.getAccessToken();
		String userId = response.getUserId();
		recyclers.setAliUserId(userId);
		this.updateById(recyclers);
		return "操作成功";
	}

	@Override
	public List<Recyclers> getRecyclersListByParentId(Integer companyId, String recycleId) {
		return recyclersMapper.getRecyclersListByParentId(companyId, recycleId);
	}
}
