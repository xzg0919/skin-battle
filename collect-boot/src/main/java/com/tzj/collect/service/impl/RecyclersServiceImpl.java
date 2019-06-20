
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
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.api.admin.param.RecyclersBean;
import com.tzj.collect.api.ali.param.AreaBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.param.*;
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
	@Autowired
	private  RecyclersRangeApplianceService recyclersRangeApplianceService;
	@Autowired
	private  RecyclersRangeBigService recyclersRangeBigService;
	@Autowired
	private  RecyclersRangeHouseholdService recyclersRangeHouseholdService;
	@Autowired
	private RecyclerCompanyService recyclerCompanyService;
	@Autowired
	private OrderService orderService;
    /**
     * 根据手机号查询回收人员
     * @param mobile
     * @return
     */
    @Override
	@DS("slave")
    public Recyclers selectByMobile(String mobile) {
        return selectOne(new EntityWrapper<Recyclers>().eq("tel", mobile));
    }

	@Override
	@DS("slave")
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
	@DS("slave")
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
	@DS("slave")
	public Integer getRecyclerPageSize(RecyclersBean bean) {
		Integer record=recyclersMapper.getRecyclerPageSize(bean);
		return record==null?0:record;
	}


	@Override
	@DS("slave")
	public RecyclersBean getRecEvaById(String recyclerId) {
		return recyclersMapper.getRecEvaById(recyclerId);
	}

	@Override
	@DS("slave")
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
	@DS("slave")
	public List<AdminCommunityBean> getRecSerCommById(String recyclerId) {
		return recyclersMapper.getRecSerCommById(recyclerId);
	}

	@Override
	@DS("slave")
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
	@DS("slave")
	public List<Recyclers> getRecyclersList(Integer companyId, Integer categoryId) {		
		return recyclersMapper.getRecyclersList(companyId,categoryId);
	}


	/**
	 * 根据回收人员id获取回收人员的详情
	 */
	@Override
	@DS("slave")
	public Recyclers getRecyclersById(BusinessRecyclerBean recyclerBean) {
		return recyclersMapper.getRecyclersById(recyclerBean);
	}

	/**
	 * 查询回收人员的申请列表
	 */
	@Override
	@DS("slave")
	public List<Recyclers> getRecyclersApply(BusinessRecyclerBean recyclerBean) {
		return recyclersMapper.getRecyclersApply(recyclerBean.getCompanyId(),recyclerBean.getIsBigRecycle());
	}


	@Override
	@DS("slave")
	public List<Recyclers> getRecyclersList2(Integer companyId,Integer orderId) {
		Order order = orderService.selectById(orderId);
		return recyclersMapper.getRecyclersLists(companyId,orderId,Integer.parseInt(order.getTitle().getValue()+""));
	}

	/**
	 * 获取该企业的所有业务经理信息
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
	@DS("slave")
	public List<Map<String,Object>> getRecyclers(Integer companyId, String isBigRecycle){
		return recyclersMapper.getRecyclers(companyId, isBigRecycle);
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
			recyclerCompanyService.selectOne(new EntityWrapper<RecyclerCompany>().eq("recycler_id", recyclersServiceRangeBean.getRecycleId()).eq("company_id", companyId).eq("status_","0").eq("title",recyclersServiceRangeBean.getTitle()));
			//根据市级id查询具体信息
//			Area area = areaService.selectById(recyclersServiceRangeBean.getCityId());
//			if(area==null){
//				return "传入的市级id不对";
//			}
			recyclers.setIsManager("1");
//			recyclers.setCity(area.getId().intValue());
//			recyclers.setProvince(area.getParentId());
			recyclerService.updateById(recyclers);
			companyRecycler.setStatus("1");
			companyRecyclerService.updateById(companyRecycler);

			RecyclersTitle recyclersTitle = new RecyclersTitle();
			recyclersTitle.setRecycleId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
			recyclersTitle.setTitleId(Integer.parseInt(recyclersServiceRangeBean.getTitle()));
			recyclersTitleService.insert(recyclersTitle);

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
		return "此接口作废";
	}
	@Transactional
	@Override
	public Object updateOrSaveRecyclersRange(RecyclersServiceRangeBean recyclersServiceRangeBean,Integer companyId){
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
		//获取所有的区域Id
		List<AreaBean> areaList = recyclersServiceRangeBean.getAreaList();
		for (AreaBean areaBean: areaList) {
            if(null != areaBean){
                if("1".equals(recyclersServiceRangeBean.getTitle())){
                    if("0".equals(areaBean.getSaveOrDelete())){
                        RecyclersRangeAppliance recyclersRangeAppliances = recyclersRangeApplianceService.selectOne(new EntityWrapper<RecyclersRangeAppliance>().eq("recyclers_id", recyclersServiceRangeBean.getRecycleId()).eq("company_id", companyId).eq("street_id", areaBean.getStreeId()).eq("area_id", areaBean.getAreaId()));
                        if(null == recyclersRangeAppliances){
                            RecyclersRangeAppliance recyclersRangeAppliance = new RecyclersRangeAppliance();
                            recyclersRangeAppliance.setCompanyId(companyId);
                            recyclersRangeAppliance.setRecyclersId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
                            recyclersRangeAppliance.setStreetId(Integer.parseInt(areaBean.getStreeId()));
                            recyclersRangeAppliance.setAreaId(areaBean.getAreaId());
                            recyclersRangeApplianceService.insert(recyclersRangeAppliance);
                        }
                    }else {
                        recyclersRangeApplianceService.delete(new EntityWrapper<RecyclersRangeAppliance>().eq("recyclers_id",recyclersServiceRangeBean.getRecycleId()).eq("company_id",companyId).eq("street_id",areaBean.getStreeId()).eq("area_id",areaBean.getAreaId()));
                    }
                }else if("2".equals(recyclersServiceRangeBean.getTitle())){
                    List<CommunityBean> communityIdList = areaBean.getCommunityIdList();
                    for (CommunityBean communityBean:communityIdList){
                        if(null != communityBean){
                            if("0".equals(communityBean.getSaveOrDelete())){
                                RecyclersRangeHousehold recyclersRangeHouseholds = recyclersRangeHouseholdService.selectOne(new EntityWrapper<RecyclersRangeHousehold>().eq("recyclers_id", recyclersServiceRangeBean.getRecycleId()).eq("company_id", companyId).eq("community_id", communityBean.getCommunityId()).eq("street_id", areaBean.getStreeId()));
                                if(null == recyclersRangeHouseholds){
                                    RecyclersRangeHousehold recyclersRangeHousehold = new RecyclersRangeHousehold();
                                    recyclersRangeHousehold.setCompanyId(companyId);
                                    recyclersRangeHousehold.setRecyclersId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
                                    recyclersRangeHousehold.setStreetId(Integer.parseInt(areaBean.getStreeId()));
                                    recyclersRangeHousehold.setCommunityId(Integer.parseInt(communityBean.getCommunityId()));
                                    recyclersRangeHouseholdService.insert(recyclersRangeHousehold);
                                }
                            }else {
                                recyclersRangeHouseholdService.delete(new EntityWrapper<RecyclersRangeHousehold>().eq("recyclers_id",recyclersServiceRangeBean.getRecycleId()).eq("company_id",companyId).eq("community_id",communityBean.getCommunityId()).eq("street_id",areaBean.getStreeId()));
                            }
                        }
                    }
                }else if("4".equals(recyclersServiceRangeBean.getTitle())){
                    if("0".equals(areaBean.getSaveOrDelete())){
                        RecyclersRangeBig recyclersRangeBigs = recyclersRangeBigService.selectOne(new EntityWrapper<RecyclersRangeBig>().eq("recyclers_id", recyclersServiceRangeBean.getRecycleId()).eq("company_id", companyId).eq("street_id", areaBean.getStreeId()).eq("area_id", areaBean.getAreaId()));
                        if(null == recyclersRangeBigs){
                            RecyclersRangeBig recyclersRangeBig = new RecyclersRangeBig();
                            recyclersRangeBig.setCompanyId(companyId);
                            recyclersRangeBig.setRecyclersId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
                            recyclersRangeBig.setStreetId(Integer.parseInt(areaBean.getStreeId()));
                            recyclersRangeBig.setAreaId(areaBean.getAreaId());
                            recyclersRangeBigService.insert(recyclersRangeBig);
                        }
                    }else {
                        recyclersRangeBigService.delete(new EntityWrapper<RecyclersRangeBig>().eq("recyclers_id",recyclersServiceRangeBean.getRecycleId()).eq("company_id",companyId).eq("street_id",areaBean.getStreeId()).eq("area_id",areaBean.getAreaId()));
                    }
                }
            }
		}
		RecyclersTitle recyclersTitle = null;
		//查询是否存在回收人员与回收类型的关联关系
		recyclersTitle = recyclersTitleService.selectOne(new EntityWrapper<RecyclersTitle>().eq("recycle_id", recyclersServiceRangeBean.getRecycleId()).eq("title_id", recyclersServiceRangeBean.getTitle()).eq("del_flag", 0));
		if(null == recyclersTitle){
			recyclersTitle = new RecyclersTitle();
			recyclersTitle.setRecycleId(Integer.parseInt(recyclersServiceRangeBean.getRecycleId()));
			recyclersTitle.setTitleId(Integer.parseInt(recyclersServiceRangeBean.getTitle()));
			recyclersTitleService.insert(recyclersTitle);
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
	@DS("slave")
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
	@DS("slave")
	public Object getStreeRecyclersRange(String areaId,String recycleId,Integer companyId,String title){
		Map<String,Object> resultMap = new HashMap<>();
		List<Map<String,Object>> areaList = null ;
		Object communityList = null;
		if("4".equals(title)){
			areaList = ( List<Map<String,Object>>)recyclersRangeBigService.getStreeRecyclersRange(areaId, recycleId, companyId.toString());
			for (Map<String, Object> map:areaList){
				communityList = recyclersRangeBigService.getCommunityRecyclersRange(map.get("id").toString(), recycleId, companyId.toString());
				map.put("communityList",communityList);
			}
			resultMap.put("areaList",areaList);
			return resultMap;
		}else if("2".equals(title)){
			areaList = ( List<Map<String,Object>>)recyclersRangeHouseholdService.getStreeRecyclersRange(areaId, recycleId, companyId.toString());
			for (Map<String, Object> map:areaList){
				communityList = recyclersRangeHouseholdService.getCommunityRecyclersRange(map.get("id").toString(), recycleId, companyId.toString());
				map.put("communityList",communityList);
			}
			resultMap.put("areaList",areaList);
			return resultMap;
		}else if ("1".equals(title)){
			areaList = ( List<Map<String,Object>>)recyclersRangeApplianceService.getStreeRecyclersRange(areaId, recycleId, companyId.toString());
			for (Map<String, Object> map:areaList){
				communityList = recyclersRangeApplianceService.getCommunityRecyclersRange(map.get("id").toString(), recycleId, companyId.toString());
				map.put("communityList",communityList);
			}
			resultMap.put("areaList",areaList);
			return resultMap;
		}
		return recyclersMapper.getStreeRecyclersRange(areaId,recycleId);
	}
	/**
	 * 获取回收经理人员列表
	 * @author wangcan
	 * @param companyId : 企业Id
	 * @return
	 */
	@Override
	@DS("slave")
	public Object getRangeRecyclersList(Integer companyId,String recycleName,String cityId ,Integer pageNum,Integer pageSize,String isBigRecycle, String tel){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> recycleList = recyclersMapper.getRangeRecyclersList(companyId.toString(),recycleName,cityId,(pageNum-1)*pageSize,pageSize,isBigRecycle, tel);
		Integer count = recyclersMapper.getRangeRecyclersListCount(companyId.toString(),recycleName,cityId,isBigRecycle, tel);
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
	@Override
	@DS("slave")
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
	@DS("slave")
	public List<Recyclers> getRecyclersListByParentId(Integer companyId, String recycleId, String isBigRecycle) {
		return recyclersMapper.getRecyclersListByParentId(companyId, recycleId, isBigRecycle);
	}

	@Override
	@DS("slave")
	public Object getAreaRecyclersRangeList(RecyclersServiceRangeBean recyclersServiceRangeBean, String companyId) {
		RecyclerCompany recyclerCompany = recyclerCompanyService.selectOne(new EntityWrapper<RecyclerCompany>().eq("recycler_id", recyclersServiceRangeBean.getRecycleId()).eq("company_id", companyId).eq("status_", 1));
		Map<String,Object> resultMap = new HashMap<>();
		List<Map<String,Object>> areaList = null ;
		if("4".equals(recyclersServiceRangeBean.getTitle())){
			areaList = ( List<Map<String,Object>>)recyclersRangeBigService.getAreaRecyclersRange(recyclersServiceRangeBean.getCityId(), recyclersServiceRangeBean.getRecycleId(), companyId);
		}else if("2".equals(recyclersServiceRangeBean.getTitle())){
			areaList = ( List<Map<String,Object>>)recyclersRangeHouseholdService.getAreaRecyclersRange(recyclersServiceRangeBean.getCityId(), recyclersServiceRangeBean.getRecycleId(), companyId);
		}else if ("1".equals(recyclersServiceRangeBean.getTitle())){
			areaList = ( List<Map<String,Object>>)recyclersRangeApplianceService.getAreaRecyclersRange(recyclersServiceRangeBean.getCityId(), recyclersServiceRangeBean.getRecycleId(), companyId);
		}

		List<Map<String, Object>> recyclerTitleList = recyclersTitleService.getRecyclerTitleList(recyclersServiceRangeBean.getRecycleId());
		resultMap.put("recyclerTitleList",recyclerTitleList);
		resultMap.put("areaList",areaList);
		return resultMap;
	}
}
