package com.tzj.collect.core.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBindModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.RoyaltyEntity;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationBindRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationUnbindRequest;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.common.push.PushUtils;
import com.tzj.collect.core.mapper.CompanyMapper;
import com.tzj.collect.core.mapper.CompanyRecyclerMapper;
import com.tzj.collect.core.param.admin.CompanyBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.iot.IotCompanyResult;
import com.tzj.collect.core.result.business.BusinessRecType;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.cert.Extension;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tzj.collect.common.constant.Const.*;

@Service
@Transactional(readOnly=true)
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
	@Resource
	private CompanyMapper companyMapper;
	@Resource
	private CompanyRecyclerMapper companyRecyclerMapper;
	@Autowired
	private CompanyAccountService companyAccountService;
	@Autowired
	private RecyclersRangeApplianceService recyclersRangeApplianceService;
	@Autowired
	private RecyclersRangeHouseholdService recyclersRangeHouseholdService;
	@Autowired
	private RecyclersRangeBigService recyclersRangeBigService;
	@Autowired
	private CompanyServiceService companyServiceService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;
	@Autowired
	private CompanyStreetApplianceService companyStreetApplianceService;
	@Autowired
	private CompanyStreetHouseService companyStreetHouseService;
	@Autowired
	private RecyclersRangeHouseService recyclersRangeHouseService;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	CompanyStreetElectroMobileService companyStreetElectroMobileService;

	@Autowired
	RecyclersRangeElectroService recyclersRangeElectroService;
	/**
	 * 返回企业信息列表
	 */
	@Override
	public Page<Company> getSearchCompany(CompanyBean companybean, PageBean pageBean) {
		
		Page<Company> page = new Page<Company>(pageBean.getPageNumber(), pageBean.getPageSize());
		EntityWrapper<Company> wrapper = new EntityWrapper<Company>();
		if(StringUtils.isNotBlank(companybean.getCompanyName())){			
			wrapper.eq("name_", companybean.getCompanyName());
		}
		
		return this.selectPage(page, wrapper);
	}
	
	@Override
	public Integer selectCompanyCountByCom(String comId) {
		return companyMapper.selectCompanyCountByCom(comId);
	}
	@Override
	public Integer selectCategoryCountByCom(String comId) {
		return companyMapper.selectCategoryCountByCom(comId);
	}

	@Override
	public List<Company> selectCompanyListByComm(String commId) {
		return companyMapper.selectCompanyListByComm(commId);
	}

	@Override
	public List<BusinessRecType> getTypeByComId(String comId) {
		return companyMapper.getTypeByComId(comId);
	}
	
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业Id
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	@Override
	public Integer getCompanyIdByIds(Integer CommunityId, Integer CategoryId) {
		
		return companyMapper.getCompanyIdByIds(CommunityId,CategoryId);
	}

	@Override
	public Company getCurrent(CompanyAccount companyAccount) {
		return this.selectById(companyAccount.getCompanyId());
	}
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	@Override
	public Company getCompanyByIds(Integer CommunityId, Integer CategoryId) {
		//私海没找到，找公海信息
		Company company = companyMapper.getCompanyByIds(CommunityId,CategoryId);
		if (company == null) {
			//若恒为null,根据小区 id找到所属区,再找到所属公海服务的公司
			company = companyMapper.getCompanyWithNotFound(CommunityId, CategoryId);
		}
		return company;
	}

	@Override
	public IotCompanyResult selectIotUrlByEquipmentCode(String cabinetNo) {

		return companyMapper.selectIotUrlByEquipmentCode(cabinetNo);
	}

	@Transactional
	@Override
	public String isOpenOrder(String isOpenOrder, Integer companyId) {

		Company company = this.selectById(companyId);
		company.setIsOpenOrder(isOpenOrder);
		this.updateById(company);
		return "操作成功";
	}


	@Override
	public  Object companyAreaRanges(String title,String companyId){
		Map<String,Object> resutMap = new HashMap<>();
		Map<String,Object> companyRange = new HashMap<>();
		Map<String,Object> companyRecycleRange = new HashMap<>();
		if ("1".equals(title)){
			companyRange = companyStreetApplianceService.companyAreaRanges(companyId);
			companyRecycleRange = recyclersRangeApplianceService.companyAreaRecyclerRanges(companyId);
		}else if("2".equals(title)){
			companyRange = companyStreetHouseService.companyAreaRanges(companyId);
			companyRecycleRange = recyclersRangeHouseService.companyAreaRecyclerRanges(companyId);
		}else if("4".equals(title)){
			companyRange = companyStreetBigService.companyAreaRanges(companyId);
			companyRecycleRange = recyclersRangeBigService.companyAreaRecyclerRanges(companyId);
		}
		else if("9".equals(title)){
			companyRange = companyStreetElectroMobileService.companyAreaRanges(companyId);
			companyRecycleRange = recyclersRangeElectroService.companyAreaRecyclerRanges(companyId);
		}
		if(companyRange.isEmpty()){
			companyRange.put("cityNum",0);
			companyRange.put("areaNum",0);
			companyRange.put("streetNum",0);
			companyRange.put("communityNum",0);
		}
		if(companyRecycleRange.isEmpty()){
			companyRecycleRange.put("cityNum",0);
			companyRecycleRange.put("areaNum",0);
			companyRecycleRange.put("streetNum",0);
			companyRecycleRange.put("communityNum",0);
		}
		resutMap.put("companyRange",companyRange);
		resutMap.put("companyRecycleRange",companyRecycleRange);
		return resutMap;
	}

	@Override
	public  Object getAdminCompanyList(String companyName,String title,PageBean pageBean){

		int pageStart = (pageBean.getPageNumber() - 1) * pageBean.getPageSize();

		Map<String,Object> resultMap = new HashMap<>();
		List<Map<String,Object>> companyTitleList = companyMapper.getCompanyTitleNum();
		String applianceNum = "0";
		String houseNum = "0";
		String bigNum = "0";
		String  electroMobileNum ="0";
		if(null != companyTitleList&&!companyTitleList.isEmpty()){
			for (Map map:companyTitleList ) {
				if("1".equals(map.get("title").toString())){
					applianceNum = map.get("count").toString();
				}else if("2".equals(map.get("title").toString())){
					houseNum = map.get("count").toString();
				}else if("4".equals(map.get("title").toString())){
					bigNum = map.get("count").toString();
				}
				else if("9".equals(map.get("title").toString())){
					electroMobileNum = map.get("count").toString();
				}
			}
		}
			resultMap.put("applianceNum",applianceNum);
			resultMap.put("houseNum",houseNum);
			resultMap.put("bigNum",bigNum);
		    resultMap.put("electroMobileNum",electroMobileNum);
		List<Map<String, Object>> adminCompanyList = companyMapper.getAdminCompanyList(companyName, title,pageStart,pageBean.getPageSize());
		Integer adminCompanyCount = companyMapper.getAdminCompanyCount(companyName, title);
		resultMap.put("adminCompanyList",adminCompanyList);
		resultMap.put("adminCompanyCount",adminCompanyCount);
		resultMap.put("pageNum",pageBean.getPageNumber());
		return resultMap;
	}

	@Transactional
	@Override
	public String deleteCompanyById(Integer companyId){

		companyAccountService.delete(new EntityWrapper<CompanyAccount>().eq("company_id",companyId));
		this.delete(new EntityWrapper<Company>().eq("id",companyId));
		companyServiceService.delete(new EntityWrapper<CompanyServiceRange>().eq("company_id",companyId));
		companyStreetApplianceService.delete(new EntityWrapper<CompanyStreetAppliance>().eq("company_id",companyId));
		companyStreetBigService.delete(new EntityWrapper<CompanyStreetBig>().eq("company_id",companyId));
		return  "操作成功";
	}
	@Override
	public Object getCompanyDetail(Integer companyId){
		Company company = this.selectById(companyId);
		CompanyAccount companyAccount = companyAccountService.selectOne(new EntityWrapper<CompanyAccount>().eq("company_id", companyId));
		company.setUserName(companyAccount.getUsername());
		company.setPassword(companyAccount.getPassword());
		return  company;
	}
	@Transactional
	@Override
	public Object updateCompanyDetail(CompanyBean companyBean){
		List<CompanyAccount> companyAccounts = companyAccountService.selectList(new EntityWrapper<CompanyAccount>().eq("username", companyBean.getUserName()).eq("del_flag", 0));
		Company company = this.selectById(companyBean.getId());
		CompanyAccount companyAccount = companyAccountService.selectOne(new EntityWrapper<CompanyAccount>().eq("company_id", companyBean.getId()));
		if (!companyAccounts.isEmpty() ){
			if(companyBean.getId() == null || !(companyBean.getId().toString()).equals(companyAccounts.get(0).getCompanyId().toString())){
				return "该用户名已存在，请更换";
			}
		}
		companyBean.setBlueTooth(null == companyBean.getBlueTooth() ? 0 : companyBean.getBlueTooth());
		if(null == company&&null == companyAccount){
			company = new Company();
			companyAccount = new CompanyAccount();
		}else {
			//修改启用关闭蓝牙电子称时给回收员通知
			if (!companyBean.getBlueTooth().equals(company.getBlueTooth())){
				//找到当前公司下所有生活垃圾回收人员，发送通知
				List<Map<String, Object>> recsInfo = companyRecyclerMapper.selectRecycleInfoByCompanyId(company.getId(), "2");
				recsInfo.stream().forEach(recInfo ->{
					if (null != recInfo.get("mobile")){
						String status = "No";
						if ("0".equals(companyBean.getBlueTooth().toString())){
							//改变状态为关闭
							status = "No";
						}else if ("1".equals(companyBean.getBlueTooth().toString())){
							//改变状态为开启
							status = "Yes";
						}
						PushUtils.getAcsResponse(recInfo.get("mobile").toString(),status,"2");
					}
				});
			}
		}
		company.setName(companyBean.getName());
		company.setBlueTooth(companyBean.getBlueTooth());
		company.setContacts(companyBean.getContacts());
		company.setTel(companyBean.getTel());
		company.setAddress(companyBean.getAddress());
		//新增直营和企业支付宝账号
		company.setDirectOperation(companyBean.getDirectOperation());
		company.setZhiFuBaoAccount(companyBean.getZhiFuBaoAccount());
		this.insertOrUpdate(company);
		companyAccount.setUsername(companyBean.getUserName());
		companyAccount.setPassword(companyBean.getPassword());
		companyAccount.setCompanyId(company.getId().intValue());
		companyAccountService.insertOrUpdate(companyAccount);

		return "操作成功";
	}

	@Override
	public Object adminCompanyRangeById(Integer companyId){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> applianceRange = new HashMap<>();
		Map<String,Object> houseRange = new HashMap<>();
		Map<String,Object> bigRange = new HashMap<>();
		Map<String,Object> electroMobileRange = new HashMap<>();
		String isOpen = "1";
		applianceRange = companyStreetApplianceService.adminCompanyAreaRanges(companyId.toString());
		bigRange = companyStreetBigService.companyAreaRanges(companyId.toString());
		houseRange = companyStreetHouseService.adminCompanyAreaRanges(companyId.toString());
		electroMobileRange = companyStreetElectroMobileService.adminCompanyAreaRanges(companyId.toString());

		applianceRange.put("isOpen", isOpen);
		houseRange.put("isOpen", isOpen);
		bigRange.put("isOpen", isOpen);
		electroMobileRange.put("isOpen", isOpen);

		List<Map<String, Object>> resuleList = categoryService.getIsOpenCategory(companyId.toString());
		if(null != resuleList && !resuleList.isEmpty()) {
			for (Map map : resuleList) {
				resultMap = new HashMap<>();
				if ("1".equals(map.get("title").toString())) {
					applianceRange.put("isOpen","0");
				} else if ("2".equals(map.get("title").toString())) {
					houseRange.put("isOpen", "0");
				} else if ("4".equals(map.get("title").toString())) {
					bigRange.put("isOpen", "0");
				}
				else if ("9".equals(map.get("title").toString())) {
					electroMobileRange.put("isOpen", "0");
				}
			}
		}

		resultMap.put("applianceRange",applianceRange);
		resultMap.put("houseRange",houseRange);
		resultMap.put("bigRange",bigRange);
		resultMap.put("electroMobileRange",electroMobileRange);
		return  resultMap;
	}

	@Override
	public List<Company> getCompanyList(String companyName) {
		Wrapper<Company> wrapper = new EntityWrapper<Company>().eq("del_flag", 0);
		if (StringUtils.isNotBlank(companyName)){
			wrapper.like("name_",companyName);
		}
		return this.selectList(wrapper);
	}

	@Override
	public List<Company> getCompanyNameList(){
		return this.selectList(new EntityWrapper<Company>().orderBy("name_",true));
	}
	@Override
	public List<Long> getStreetNumByTableName(String tableName){
		return companyMapper.getStreetNumByTableName(tableName);
	}

	@Override
	public List<Map<String, Object>> otherAreaLists(String tableName, String cityId) {
		return companyMapper.otherAreaLists(tableName, cityId);
	}

	@Override
	@Transactional
	public Object saveAliTokenByCode(String code, Integer companyId) {
		AlipayOpenAuthTokenAppResponse response = null;
		try{
			//用code换取token
			 response = aliPayService.aliPayOpenAuthToken("authorization_code", code, null);
			System.out.println(response.getBody());
		}catch (Exception e){
			e.printStackTrace();
		}
		Company company = this.selectById(companyId);
		System.out.println(response.getBody());
		if (response.isSuccess()){
			company.setAuthToken(response.getAppAuthToken());
			company.setRefreshToken(response.getAppRefreshToken());
			company.setAppId(response.getAuthAppId());
			company.setAliUserId(response.getUserId());
			this.updateById(company);
		}else {
			throw new ApiException("授权异常，请重新授权");
		}
		return "success";
	}
}
