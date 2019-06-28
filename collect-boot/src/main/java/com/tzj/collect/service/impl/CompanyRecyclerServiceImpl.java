package com.tzj.collect.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tzj.collect.api.app.AppTokenApi;
import com.tzj.collect.entity.*;
import com.tzj.collect.service.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.app.param.RecyclersBean;
import com.tzj.collect.api.app.result.AppCompany;
import com.tzj.collect.api.business.param.BusinessRecyclerBean;
import com.tzj.collect.mapper.CompanyRecyclerMapper;

@Service
@Transactional
public class CompanyRecyclerServiceImpl extends ServiceImpl<CompanyRecyclerMapper, CompanyRecycler>
		implements CompanyRecyclerService {

	@Resource
	private CompanyRecyclerMapper mapper;
	@Autowired
	private RecyclersTitleService recyclersTitleService;
	@Autowired
	private RecyclerCompanyService recyclerCompanyService;
	@Autowired
	private RecyclersService recyclersService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RecyclersRangeApplianceService recyclersRangeApplianceService;
	@Autowired
	private RecyclersRangeHouseholdService recyclersRangeHouseholdService;
	@Autowired
	private RecyclersRangeBigService recyclersRangeBigService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;

	@Override
	public List<Company> selectCompanyByRecyclerId(String recyclerId) {
		return mapper.selectCompanyByRecyclerId(recyclerId);
	}

	/**
	 * 根据公司id查询绑定回收人员个数
	 */

	@Override
	public int selectRecycleByCompanyId(long id) {

		return mapper.selectRecycleByCompanyId(id);
	}

	@Override
	public List<CompanyRecycler> selectRecByComId(String id) {
		return selectList(new EntityWrapper<CompanyRecycler>().eq("company_id", id));
	}

	@Override
	public List<AppCompany> getRecyclerCompanyStatus(String recId) {
		return mapper.getRecyclerCompanyStatus(recId);
	}

	/**
	 * 根据公司id和回收人员ida查找关联的回收人员
	 */
	@Override
	public CompanyRecycler getCompanyRecycler(BusinessRecyclerBean recyclerBean) {

		return selectOne(new EntityWrapper<CompanyRecycler>().eq("company_id", recyclerBean.getCompanyId())
				.eq("recycler_id", recyclerBean.getId()));
	}

	/*
	 * 查看该公司下的回收人员的身份证信息
	 * 
	 * @return
	 * 
	 * @see
	 * com.tzj.collect.service.CompanyRecyclerService#findIdCard(com.tzj.collect
	 * .api.business.param.BusinessRecyclerBean)
	 */
	@Override
	public Recyclers findIdCard(BusinessRecyclerBean recyclerBean) {
		return mapper.findIdCard(recyclerBean);
	}

	@Override
	public Map<String,Object> getCurrComList(RecyclersBean recyclersBean) {
		List<AppCompany> appComList = mapper.getCurrComList(recyclersBean, (recyclersBean.getPageBean().getPageNumber()-1)*recyclersBean.getPageBean().getPageSize(), recyclersBean.getPageBean().getPageSize());
		int count = mapper.getCurrComCount(recyclersBean);
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> catagList = null;
		List<String> ids = new ArrayList<>();
		if (appComList.size() > 0) {
			for (AppCompany appCompany : appComList) {
				ids.add(appCompany.getId());
			}
		}
		if (ids.size() > 0) {
			recyclersBean.setComIdsList(ids);
			List<AppCompany> cateCom =  mapper.getComCateList(recyclersBean);
			for (AppCompany appCompany : appComList) {
				catagList = new ArrayList<>();
				for (int i = 0; i < cateCom.size(); i++) {
					if (cateCom.get(i).getId().equals(appCompany.getId())) {
						catagList.add(cateCom.get(i).getCateName());
					}
				}
				appCompany.setCategList(catagList);
			}
		}
		int pageNum = count % recyclersBean.getPageBean().getPageSize() == 0 ? count/recyclersBean.getPageBean().getPageSize() : count / recyclersBean.getPageBean().getPageSize() + 1;
		int currentpage = recyclersBean.getPageBean().getPageNumber();
		map.put("pageNum", pageNum);
		map.put("count", count);
		map.put("listCom", appComList);
		map.put("currentPage", currentpage > pageNum ? pageNum : currentpage);
		return map;
	}

	@Override
	public Map<String,Object> getNotEnterComList(RecyclersBean recyclersBean) {
		int count = mapper.getNotEnterComCount(recyclersBean);
		Map<String,Object> map = new HashMap<String,Object>();
		int pageNum = count % recyclersBean.getPageBean().getPageSize() == 0 ? count/recyclersBean.getPageBean().getPageSize() : count / recyclersBean.getPageBean().getPageSize() + 1;
		int currentpage = recyclersBean.getPageBean().getPageNumber();
		List<AppCompany> appNotEnterComList	= mapper.getNotEnterComList(recyclersBean, (recyclersBean.getPageBean().getPageNumber()-1)*recyclersBean.getPageBean().getPageSize(), recyclersBean.getPageBean().getPageSize());
		map.put("pageNum", pageNum);
		map.put("count", count);
		map.put("listCom", appNotEnterComList);
		map.put("currentPage", currentpage > pageNum ? pageNum : currentpage);
		return map;
	}

	@Override
	public boolean insertComRecByComIds(AppCompany company, long recId) {
		
		String[] comIds = company.getComIds().split(",");
		EntityWrapper<CompanyRecycler> wrapper = null;
		CompanyRecycler companyRecycler = null;
		List<CompanyRecycler> list = new ArrayList<>();
		List<CompanyRecycler> companyRecyclers = this.selectList(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recId).in("status_", "0,1"));
		if (null != companyRecyclers &&!companyRecyclers.isEmpty()){
			throw new ApiException("你已申请或入住公司，不能再申请公司");
		}

		if (comIds.length >= 0) {
			for (String id : comIds) {
				if("Y".equals((company.getIsBigRecycle()))){
					List<CompanyStreetBig> companyStreetBigList = companyStreetBigService.selectList(new EntityWrapper<CompanyStreetBig>().eq("company_id", id).eq("del_flag",0));
					if (companyStreetBigList.isEmpty()){
						throw new ApiException("您申请的公司，不回收大件类型垃圾");
					}
				}
				companyRecycler = this.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recId).eq("company_id", id).eq("status_", "2"));
				if(null == companyRecycler){
					companyRecycler = new CompanyRecycler();
				}
					companyRecycler.setRecyclerId(Integer.parseInt(recId + ""));
					companyRecycler.setCompanyId(Integer.parseInt(id));
					companyRecycler.setStatus("0");
					this.insertOrUpdate(companyRecycler);
			}
			if("Y".equals((company.getIsBigRecycle()))){
				this.saveRecycle(recId+"");
			}
			return true;
		}
		return false;
	}
	public void saveRecycle(String recyclerId){
		RecyclersTitle recyclersTitle = recyclersTitleService.selectOne(new EntityWrapper<RecyclersTitle>().eq("recycle_id", recyclerId).eq("title_id", 4).eq("del_flag", 0));
		if(recyclersTitle!=null){
			return;
		}else {
			recyclersTitle = new RecyclersTitle();
			recyclersTitle.setRecycleId(Integer.parseInt(recyclerId));
			recyclersTitle.setTitleId(4);
			recyclersTitle.setTitleName("大件垃圾");
			recyclersTitleService.insert(recyclersTitle);
		}
	}
	@Override
	public Object deleteCompanyRecycle(AppCompany appCompanys, long recId){
		this.delete(new EntityWrapper<CompanyRecycler>().eq("recycler_id",recId).eq("company_id",appCompanys.getId()).eq("status_",0));
		return "操作成功";
	}
	/**
	 * 修改回收人员的启用状态
	 */
	@Override
	public CompanyRecycler getCompanyRecyclerByRecyclerId(Long recyclerId) {
		EntityWrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>();
		wrapper.eq("recycler_id", recyclerId);
		return this.selectOne(wrapper);
	}
/**
 *  通过回收员姓名,id返回某公司回收人员列表
 */
	@Override
	public List<BusinessRecyclerBean> getgetSearchCompanyRecyclerList(BusinessRecyclerBean recyclerBean) {
		return mapper.getgetSearchCompanyRecyclerList(recyclerBean);
	}
	/**
	 * 获取企业服务范围（例南京市，苏州市等）
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Override
	public Object getCompanyRange(Integer companyId){
		return mapper.getCompanyRange(companyId);
	}

	@Override
	public Object getBigCompanyRange(Integer companyId){
		return mapper.getBigCompanyRange(companyId);
	}

	@Override
	public Object recyclersDel(Integer companyId, String recycleId) {
		EntityWrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>();
		wrapper.eq("recycler_id", recycleId);
		wrapper.eq("company_id", companyId);
		wrapper.eq("del_flag", 0);
		CompanyRecycler companyRecycler = this.selectOne(wrapper);
		companyRecycler.setDelFlag("1");
		return this.updateById(companyRecycler);
	}

	@Override
	public Object recycleDelete(Integer companyId, String recycleId,String title) {

		Recyclers recyclers = recyclersService.selectById(recycleId);
		if(null == recyclers){
			throw  new ApiException("找不到该回收人员");
		}
		List<Order> orderList = orderService.selectList(new EntityWrapper<Order>().eq("recycler_id", recycleId).in("status_", "1,2"));
		if(!orderList.isEmpty()){
			throw  new ApiException("该回收人员还有订单，无法删除");
		}
		if("1".equals(recyclers.getIsManager())){
			List<Recyclers> parentsList = recyclersService.selectList(new EntityWrapper<Recyclers>().eq("parents_id", recycleId));
			if(!parentsList.isEmpty()){
				throw  new ApiException("还有下属回收人员，无法删除");
			}
			if("4".equals(title)){
				recyclersTitleService.delete(new EntityWrapper<RecyclersTitle>().eq("recycle_id",recycleId).eq("title_id",4));
				recyclersRangeBigService.delete(new EntityWrapper<RecyclersRangeBig>().eq("recyclers_id",recycleId).eq("company_id",companyId));
			}else {
				recyclersTitleService.delete(new EntityWrapper<RecyclersTitle>().eq("recycle_id",recycleId).in("title_id","1,2"));
				recyclersRangeApplianceService.delete(new EntityWrapper<RecyclersRangeAppliance>().eq("recyclers_id",recycleId).eq("company_id",companyId));
				recyclersRangeHouseholdService.delete(new EntityWrapper<RecyclersRangeHousehold>().eq("recyclers_id",recycleId).eq("company_id",companyId));
			}
		}
		recyclers.setIsManager("0");
		recyclers.setCity(0);
		recyclers.setProvince(0);
		recyclers.setParentsId(0);
		recyclersService.updateById(recyclers);
		CompanyRecycler companyRecycler = this.selectOne(new EntityWrapper<CompanyRecycler>().eq("company_id", companyId).eq("recycler_id", recycleId).eq("status_", 1));
		companyRecycler.setStatus("2");
		companyRecycler.setDelFlag("0");
		this.updateById(companyRecycler);
		return "操作成功";
	}

	@Override
	public Object recycleIsDelete(Integer companyId, String recycleId,String title) {
		Map<String,Object> map = new HashMap<>();
		Recyclers recyclers = recyclersService.selectById(recycleId);
		if(null == recyclers){
			throw  new ApiException("找不到该回收人员");
		}
		String isDelete = "";
		List<Order> orderList = orderService.selectList(new EntityWrapper<Order>().eq("recycler_id", recycleId).in("status_", "1,2"));
		if(!orderList.isEmpty()){
			map.put("orderNum",orderList.size());
			isDelete = "no";
		}else {
			map.put("orderNum",0);
			isDelete = "yes";
		}
		if("1".equals(recyclers.getIsManager())) {
			List<Recyclers> parentsList = recyclersService.selectList(new EntityWrapper<Recyclers>().eq("parents_id", recycleId));
			if (!parentsList.isEmpty()) {
				map.put("recycleNum",parentsList.size());
				isDelete = "no";
			}else {
				map.put("recycleNum",0);
				isDelete = "yes";
			}
		}
			map.put("isDelete",isDelete);
		return map;
	}

	@Override
	public Object getRecycleRangeByTitle(String companyId,String recyclerId,String title){
		Map<String,Object> resultMap = new HashMap<>();
		Integer areaNum = 0;
		Integer streetNum = 0;
		Integer communityNum = 0;
		if("4".equals(title)){
			List<RecyclersRangeBig> recyclersRangeBigs = recyclersRangeBigService.selectList(new EntityWrapper<RecyclersRangeBig>().eq("company_id", companyId).eq("recyclers_id", recyclerId).groupBy("area_id"));
			if (!recyclersRangeBigs.isEmpty()){
				areaNum = recyclersRangeBigs.size();
			}
			List<RecyclersRangeBig> recyclersRangeBigStreet = recyclersRangeBigService.selectList(new EntityWrapper<RecyclersRangeBig>().eq("company_id", companyId).eq("recyclers_id", recyclerId));
			if (!recyclersRangeBigStreet.isEmpty()){
				streetNum = recyclersRangeBigStreet.size();
			}
		}else if("2".equals(title)){
			areaNum = recyclersRangeHouseholdService.selectAreaRangeCount(companyId, recyclerId);
			List<RecyclersRangeHousehold> recyclersRangeHouseholds = recyclersRangeHouseholdService.selectList(new EntityWrapper<RecyclersRangeHousehold>().eq("company_id", companyId).eq("recyclers_id", recyclerId).groupBy("street_id"));
			if (!recyclersRangeHouseholds.isEmpty()){
				streetNum = recyclersRangeHouseholds.size();
			}
			List<RecyclersRangeHousehold> recyclersRangeHouseholdsCommunity = recyclersRangeHouseholdService.selectList(new EntityWrapper<RecyclersRangeHousehold>().eq("company_id", companyId).eq("recyclers_id", recyclerId));
			if (!recyclersRangeHouseholdsCommunity.isEmpty()){
				communityNum = recyclersRangeHouseholdsCommunity.size();
			}
		}else if("1".equals(title)){
			List<RecyclersRangeAppliance> recyclersRangeAppliances = recyclersRangeApplianceService.selectList(new EntityWrapper<RecyclersRangeAppliance>().eq("company_id", companyId).eq("recyclers_id", recyclerId).groupBy("area_id"));
			if (!recyclersRangeAppliances.isEmpty()){
				areaNum = recyclersRangeAppliances.size();
			}
			List<RecyclersRangeAppliance> recyclersRangeAppliancesStreet = recyclersRangeApplianceService.selectList(new EntityWrapper<RecyclersRangeAppliance>().eq("company_id", companyId).eq("recyclers_id", recyclerId));
			if (!recyclersRangeAppliancesStreet.isEmpty()){
				streetNum = recyclersRangeAppliancesStreet.size();
			}
		}
		resultMap.put("areaNum",areaNum);
		resultMap.put("streetNum",streetNum);
		resultMap.put("communityNum",communityNum);
		return resultMap;
	}
}
