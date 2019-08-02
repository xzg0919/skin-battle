package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.CompanyRecyclerMapper;
import com.tzj.collect.core.param.app.RecyclersBean;
import com.tzj.collect.core.param.business.BusinessRecyclerBean;
import com.tzj.collect.core.result.app.AppCompany;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private RecyclersRangeBigService recyclersRangeBigService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	@Autowired
	private RecyclersRangeHouseService recyclersRangeHouseService;
	@Autowired
	private CompanyRecyclerService companyRecyclerService;

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
	public List<AppCompany> getRecyclerCompanyStatus(String recId,String isBigRecycle) {
		return mapper.getRecyclerCompanyStatus(recId,isBigRecycle);
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
	public Map<String,Object> getBigCurrcomList(RecyclersBean recyclersBean) {
		List<AppCompany> appComList = mapper.getBigCurrcomList(recyclersBean, (recyclersBean.getPageBean().getPageNumber()-1)*recyclersBean.getPageBean().getPageSize(), recyclersBean.getPageBean().getPageSize());
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
		
		String comIds = company.getComIds();
		EntityWrapper<CompanyRecycler> wrapper = null;
		CompanyRecycler companyRecycler = null;
		List<CompanyRecycler> list = new ArrayList<>();
		List<CompanyRecycler> companyRecyclers = null;
		Wrapper<CompanyRecycler> wrapper1 = new EntityWrapper<CompanyRecycler>().eq("recycler_id", recId).in("status_", "0,1");
		Wrapper<CompanyRecycler> wrapper2 = new EntityWrapper<CompanyRecycler>().eq("recycler_id", recId).eq("company_id", comIds).eq("status_", "2");
		if("Y".equals((company.getIsBigRecycle()))){
			wrapper1.eq("type_","4");
			wrapper2.eq("type_","4");
			companyRecyclers = this.selectList(wrapper1);
			if (null != companyRecyclers &&!companyRecyclers.isEmpty()){
				throw new ApiException("你已申请或入住公司，不能再申请公司");
			}
			List<CompanyStreetBig> companyStreetBigList = companyStreetBigService.selectList(new EntityWrapper<CompanyStreetBig>().eq("company_id", comIds).eq("del_flag",0));
			if (companyStreetBigList.isEmpty()){
				throw new ApiException("您申请的公司，不回收大件类型垃圾");
			}
			companyRecycler = this.selectOne(wrapper2);
			if(null == companyRecycler){
				companyRecycler = new CompanyRecycler();
			}
			companyRecycler.setType("4");
		}else {
			wrapper1.eq("type_","1");
			wrapper2.eq("type_","1");
			companyRecyclers = this.selectList(wrapper1);
			if (null != companyRecyclers &&!companyRecyclers.isEmpty()){
				throw new ApiException("你已申请或入住公司，不能再申请公司");
			}
			companyRecycler = this.selectOne(wrapper2);
			if(null == companyRecycler){
				companyRecycler = new CompanyRecycler();
			}
			companyRecycler.setType("1");
		}
		companyRecycler.setRecyclerId(Integer.parseInt(recId + ""));
		companyRecycler.setCompanyId(Integer.parseInt(comIds));
		companyRecycler.setStatus("0");
		this.insertOrUpdate(companyRecycler);
		return true;
	}

	@Override
	public Object deleteCompanyRecycle(AppCompany appCompanys, long recId){

		Wrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>().eq("recycler_id", recId).eq("company_id", appCompanys.getId()).eq("status_", 0);
		if ("Y".equals(appCompanys.getIsBigRecycle())){
			wrapper.eq("type_","4");
		}else {
			wrapper.eq("type_","1");
		}
		this.delete(wrapper);
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
		Wrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>().eq("recycler_id", recycleId).eq("company_id", companyId).eq("status_", "1");
		if("4".equals(title)){
			wrapper.eq("type_","4");
		}else {
			wrapper.eq("type_","1");
		}
		CompanyRecycler companyRecycler1 = companyRecyclerService.selectOne(wrapper);
		List<Order> orderList = orderService.selectList(new EntityWrapper<Order>().eq("recycler_id", recycleId).in("status_", "1,2"));
		if(!orderList.isEmpty()){
			throw  new ApiException("该回收人员还有订单，无法删除");
		}
		if("1".equals(companyRecycler1.getIsManager())){
			List<CompanyRecycler> companyRecyclerList = null;
			Wrapper<CompanyRecycler> wrapper1 = new EntityWrapper<CompanyRecycler>().eq("parents_id", recycleId);
			if("4".equals(title)){
				wrapper1.eq("type_","4");
				companyRecyclerList = companyRecyclerService.selectList(wrapper1);
				if(!companyRecyclerList.isEmpty()){
					throw  new ApiException("还有下属回收人员，无法删除");
				}
				recyclersTitleService.delete(new EntityWrapper<RecyclersTitle>().eq("recycle_id",recycleId).eq("title_id","4"));
				recyclersRangeBigService.delete(new EntityWrapper<RecyclersRangeBig>().eq("recyclers_id",recycleId).eq("company_id",companyId));
			}else {
				wrapper1.eq("type_","1");
				companyRecyclerList = companyRecyclerService.selectList(wrapper1);
				if(!companyRecyclerList.isEmpty()){
					throw  new ApiException("还有下属回收人员，无法删除");
				}
				recyclersTitleService.delete(new EntityWrapper<RecyclersTitle>().eq("recycle_id",recycleId).in("title_id","1,2"));
				recyclersRangeApplianceService.delete(new EntityWrapper<RecyclersRangeAppliance>().eq("recyclers_id",recycleId).eq("company_id",companyId));
				recyclersRangeHouseService.delete(new EntityWrapper<RecyclersRangeHouse>().eq("recyclers_id",recycleId).eq("company_id",companyId));
			}
		}
		companyRecycler1.setIsManager("0");
		companyRecycler1.setCity(0);
		companyRecycler1.setProvince(0);
		companyRecycler1.setParentsId(0);
		companyRecycler1.setStatus("2");
		companyRecycler1.setDelFlag("0");
		this.updateById(companyRecycler1);
		return "操作成功";
	}

	@Override
	public Object recycleIsDelete(Integer companyId, String recycleId,String title) {
		Map<String,Object> map = new HashMap<>();
		Recyclers recyclers = recyclersService.selectById(recycleId);
		if(null == recyclers){
			throw  new ApiException("找不到该回收人员");
		}
		Wrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>().eq("recycler_id", recycleId).eq("company_id", companyId).eq("status_", "1");
		if("4".equals(title)){
			wrapper.eq("type_","4");
		}else {
			wrapper.eq("type_","1");
		}
		CompanyRecycler companyRecycler1 = companyRecyclerService.selectOne(wrapper);
		String isDelete = "";
		List<Order> orderList = orderService.selectList(new EntityWrapper<Order>().eq("recycler_id", recycleId).in("status_", "1,2"));
		if(!orderList.isEmpty()){
			map.put("orderNum",orderList.size());
			isDelete = "no";
		}else {
			map.put("orderNum",0);
			isDelete = "yes";
		}
		if("1".equals(companyRecycler1.getIsManager())) {
			List<CompanyRecycler> companyRecyclerList = null;
			Wrapper<CompanyRecycler> wrapper1 = new EntityWrapper<CompanyRecycler>().eq("parents_id", recycleId);
			if("4".equals(title)) {
				wrapper1.eq("type_", "4");
			}else {
				wrapper1.eq("type_", "1");
			}
			companyRecyclerList = companyRecyclerService.selectList(wrapper1);
			if (!companyRecyclerList.isEmpty()) {
				map.put("recycleNum",companyRecyclerList.size());
				isDelete = "no";
			}else {
				map.put("recycleNum",companyRecyclerList.size());
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
//			areaNum = recyclersRangeHouseholdService.selectAreaRangeCount(companyId, recyclerId);
//			List<RecyclersRangeHousehold> recyclersRangeHouseholds = recyclersRangeHouseholdService.selectList(new EntityWrapper<RecyclersRangeHousehold>().eq("company_id", companyId).eq("recyclers_id", recyclerId).groupBy("street_id"));
//			if (!recyclersRangeHouseholds.isEmpty()){
//				streetNum = recyclersRangeHouseholds.size();
//			}
//			List<RecyclersRangeHousehold> recyclersRangeHouseholdsCommunity = recyclersRangeHouseholdService.selectList(new EntityWrapper<RecyclersRangeHousehold>().eq("company_id", companyId).eq("recyclers_id", recyclerId));
//			if (!recyclersRangeHouseholdsCommunity.isEmpty()){
//				communityNum = recyclersRangeHouseholdsCommunity.size();
//			}
			List<RecyclersRangeHouse> recyclersRangeHouseliances = recyclersRangeHouseService.selectList(new EntityWrapper<RecyclersRangeHouse>().eq("company_id", companyId).eq("recyclers_id", recyclerId).groupBy("area_id"));
			if (!recyclersRangeHouseliances.isEmpty()){
				areaNum = recyclersRangeHouseliances.size();
			}
			List<RecyclersRangeHouse> recyclersRangeAppliancesStreet = recyclersRangeHouseService.selectList(new EntityWrapper<RecyclersRangeHouse>().eq("company_id", companyId).eq("recyclers_id", recyclerId));
			if (!recyclersRangeAppliancesStreet.isEmpty()){
				streetNum = recyclersRangeAppliancesStreet.size();
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
