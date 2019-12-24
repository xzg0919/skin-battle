package com.tzj.collect.api.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayUserCertifyOpenQueryRequest;
import com.alipay.api.response.*;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.common.security.MyX509TrustManager;
import com.tzj.collect.common.util.RecyclersUtils;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.app.RecyclersBean;
import com.tzj.collect.core.param.app.RecyclersLoginBean;
import com.tzj.collect.core.param.app.ScoreAppBean;
import com.tzj.collect.core.param.app.TimeBean;
import com.tzj.collect.core.result.app.AppCompany;
import com.tzj.collect.core.result.app.AppOrderResult;
import com.tzj.collect.core.result.app.AppScoreResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.core.service.impl.FileUploadServiceImpl;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.CompanyRecycler;
import com.tzj.collect.entity.OrderEvaluation;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.common.file.upload.FileUpload;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;
import com.tzj.module.easyopen.file.FileUploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.APP_API_COMMON_AUTHORITY;

/**
 * 回收人员api
 * 
 * @author Michael_Wang
 *
 */
@ApiService
public class AppRecyclersApi {

	@Autowired
	private RecyclersService recyclersService;
	@Autowired
	private OrderEvaluationService orderEvaluationService;
	@Autowired
	private CompanyRecyclerService companyRecyclerService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private FileUploadServiceImpl fileUploadServiceImpl;
	@Autowired
	private FileUpload fileUpload;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private BindAxnService bindAxnService;

	public Recyclers getRecycler() {
		Subject subject=ApiContext.getSubject();
		// 接口里面获取 Recyclers 的例子
		Recyclers recyclers = (Recyclers) subject.getUser();
		return recyclers;
	}

	/**
	 * 回收人员的开工和收工接口 修改回收人员的开工收工状态
	 */

	@Api(name = "app.recycler.changeStatus", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public String changeStatus(RecyclersBean recyclersBean) {
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		if (recyclersBean.getStatus() != null && !"".equals(recyclersBean.getStatus())) {
			recyclers.setStatus(recyclersBean.getStatus());
		} else {
			throw new ApiException("参数错误");
		}

		boolean flag = recyclersService.updateById(recyclers);
		if (flag) {
			// System.out.println("修改成功!!");
			return recyclersBean.getStatus();
		} else {
			return "false";
		}
	}

	/**
	 * 返回当前回收人员
	 */
	@Api(name = "app.recycler.current", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	@DS("slave")
	public Object getCurrenAppRecycler(RecyclersBean recyclersBean) {
		Recyclers recyclers = recyclersService.selectById(this.getRecycler().getId());
		Wrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>().eq("status_", "1").eq("recycler_id", recyclers.getId()).eq("is_manager",'1');
		if("Y".equals(recyclersBean.getIsBigRecycle())){
			wrapper.eq("type_","4");
		}else {
			wrapper.eq("type_","1");
		}
		List<CompanyRecycler> companyRecyclerList = companyRecyclerService.selectList(wrapper);
		if (!companyRecyclerList.isEmpty()){
			recyclers.setIsManager(companyRecyclerList.get(0).getIsManager());
		}else {
			recyclers.setIsManager("0");
		}
		return recyclers;
	}



	/**
	 * 回收人员的评价
	 */
	@Api(name = "app.recycler.evaluation", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Page<OrderEvaluation> getRecyclerOrderEvaluation(RecyclersBean recyclersBean) {
		Page<OrderEvaluation> page = orderEvaluationService.selectEvalByRecyclePage(this.getRecycler().getId(),
				recyclersBean.getPageBean());
		return page;
	}

	/**
	 * 保存第一步接口 姓名 性别 以及地址 必传
	 */
	@Api(name = "app.recycler.save1", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object save1(RecyclersBean recyclersBean) {
		System.out.println("回收人员的姓名是+："+recyclersBean.getName()+"性别是："+recyclersBean.getSex()+"省份正号是"+recyclersBean.getIdCard()+"地址是："+recyclersBean.getAddress());
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		Recyclers idCard = recyclersService.selectOne(new EntityWrapper<Recyclers>().eq("id_card", recyclersBean.getIdCard()).eq("is_enable_card","1"));
		if (!StringUtils.isBlank(recyclersBean.getName())&&
			!StringUtils.isBlank(recyclersBean.getSex())&& 
			!StringUtils.isBlank(recyclersBean.getIdCard())&&
			!StringUtils.isBlank(recyclersBean.getAddress())) {
			recycler.setName(recyclersBean.getName());
			recycler.setSex(recyclersBean.getSex());
			recycler.setIdCard(recyclersBean.getIdCard());
			recycler.setAddress(recyclersBean.getAddress());
			if (null != idCard){
				recycler.setIsEnableCard("1");
			}
			recyclersService.updateById(recycler);
		} else {
			return "个人信息不完整";
		}
		return "true";
	}

	/**
	 * 
	 * 保存第二步接口 身份证正面 url 和 反面url 以及身份证号必传
	 * 
	 */
	@Api(name = "app.recycler.save2", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object save2(RecyclersBean recyclersBean) {
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		System.out.println("回收人员的省份正连接+："+recyclersBean.getIdCardObv()+"和："+recyclersBean.getIdCardRev());
//		if (recyclersBean.getIdCardObv() != null && !"".equals(recyclersBean.getIdCardObv())
//				&& recyclersBean.getIdCardRev() != null && !"".equals(recyclersBean.getIdCardRev())) {
//			// 判断图片地址是否有效
//			if (testUrlWithTimeOut(recyclersBean.getIdCardObv(), 1000)) {
//				recyclers.setIdCardObv(recyclersBean.getIdCardObv());
//				if (testUrlWithTimeOut(recyclersBean.getIdCardRev(), 1000)) {
//					recyclers.setIdCardRev(recyclersBean.getIdCardRev());
//					recyclersService.updateById(recyclers);
//				} else {
//					throw new ApiException("不是有效的图片地址");
//				}
//			} else {
//				throw new ApiException("不是有效的图片地址");
//			}
//		} else {
//			throw new ApiException("证件信息不完整");
//		}
		if(StringUtils.isBlank(recyclersBean.getIdCardObv())||StringUtils.isBlank(recyclersBean.getIdCardRev())) {
			return "身份证连接不对";
		}else {
			recyclers.setIdCardObv(recyclersBean.getIdCardObv());
			recyclers.setIdCardRev(recyclersBean.getIdCardRev());
			recyclersService.updateById(recyclers);
		}
		return "true";
	}

	/**
	 * 回收人员入住的相关的回收企业
	 * 
	 */
	@Api(name = "app.recycler.companies", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public List<Company> getRecyclerCompany() {
		List<Company> list = companyRecyclerService.selectCompanyByRecyclerId(this.getRecycler().getId().toString());
		return list;
	}
	/**
	 * 根据回收员找公司是否开启蓝牙
	 * @author: sgmark@aliyun.com
	 * @Date: 2019/10/28 0028
	 * @Param: 
	 * @return: 
	 */
	@Api(name = "app.blue.tooth", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public String companyBlueTooth(){
		return companyRecyclerService.companyBlueTooth(this.getRecycler().getId().toString());
	}

	/**
	 * 返回回收人员入驻状态
	 * 
	 * @param
	 * @return
	 */
	@Api(name = "app.recycler.comstatus", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public String getRecyclerCompanyStatus(RecyclersBean recyclersBean) {
		Map<String, String> map = new HashMap<>();
		// 检查信息是否完整 不完整返回false
		Recyclers recyclers = recyclersService.selectById(this.getRecycler().getId());
		map.put("isReal",recyclers.getIsReal());
		if (recyclers.getId_card_obv() == null || "".equals(recyclers.getId_card_obv())
				|| recyclers.getIdCardRev() == null || "".equals(recyclers.getIdCardRev())) {
			map.put("sta", "false");// 资料不完整
			return JSON.toJSONString(map);
		}
		if("0".equals(recyclers.getIsReal())){
			map.put("sta", "isReal");// 资料未芝麻实名
			return JSON.toJSONString(map);
		}
		List<AppCompany> list = companyRecyclerService.getRecyclerCompanyStatus(recyclers.getId().toString(),recyclersBean.getIsBigRecycle());
		String fail = "";
		map.put("failName", "");
		if (list.size() > 0) {
			for (AppCompany appCompany : list) {
				switch (appCompany.getStatus()) {
				case "1":
					map.put("sta", "success");// 入驻成功
					return JSON.toJSONString(map);
				case "2":
					fail += appCompany.getComName() + ",";
				default:
					break;
				}
				;
			}
		} else {
			map.put("sta", "empty");// 未申请任何公司
			return JSON.toJSONString(map);
		}
		if (fail != null && !"".equals(fail)) {
			map.put("failName", fail.substring(0, fail.length() - 1));
		}
		map.put("sta", "failure");// 被所有公司拒绝
		return JSON.toJSONString(map);
	}

	/**
	 * 根据回收人员id得到个人信息，评价数目
	 * 
	 * @author sgmark@aliyun.com
	 * @param
	 * @return
	 */
	@Api(name = "recycler.getrecbyid", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public com.tzj.collect.core.param.admin.RecyclersBean getRecEvaById() {
		com.tzj.collect.core.param.admin.RecyclersBean recyclersBean = recyclersService.getRecEvaById(getRecycler().getId().toString());
		return recyclersBean;
	}

	/**
	 * 我的公司（状态申请中或者已入驻）
	 * 
	 * @author sgmark@aliyun.com
	 * @param recyclersBean
	 * @return
	 */
	@Api(name = "recycler.getcurrcom", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> getCurrComList(RecyclersBean recyclersBean) {
		recyclersBean.setId(this.getRecycler().getId());
		Map<String, Object> map = companyRecyclerService.getCurrComList(recyclersBean);
		return map;

	}
	/**
	 * 我的公司（状态申请中或者已入驻）
	 *
	 * @author sgmark@aliyun.com
	 * @param recyclersBean
	 * @return
	 */
	@Api(name = "recycler.getBigCurrcom", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> getBigCurrcomList(RecyclersBean recyclersBean) {
		recyclersBean.setId(this.getRecycler().getId());
		Map<String, Object> map = companyRecyclerService.getBigCurrcomList(recyclersBean);
		return map;

	}

	/**
	 * 添加公司列表（状态未入驻 或者 已拒绝）
	 * 
	 * @author sgmark@aliyun.com
	 * @param recyclersBean
	 * @return
	 */
	@Api(name = "recycler.getnotentry", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> getNotEnterComList(RecyclersBean recyclersBean) {
		recyclersBean.setId(this.getRecycler().getId());
		return companyRecyclerService.getNotEnterComList(recyclersBean);
	}

	/**
	 * 添加公司
	 * 
	 * @return
	 */
	@Api(name = "recycler.insertcomrec", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public boolean insertComRecByComIds(AppCompany appCompanys) {
		return companyRecyclerService.insertComRecByComIds(appCompanys, this.getRecycler().getId());
	}
	/**
	 * 删除与自己关联的公司
	 *
	 * @return
	 */
	@Api(name = "recycler.deleteCompanyRecycle", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object deleteCompanyRecycle(AppCompany appCompanys) {
		Recyclers recycler = RecyclersUtils.getRecycler();
		return companyRecyclerService.deleteCompanyRecycle(appCompanys, recycler.getId());
	}

	/**
	 * 获得当前回收人员数据
	 */
	@Api(name = "app.recycler.getrecord", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public AppOrderResult getRecord(TimeBean timeBean) {
		timeBean.setRecyclerId(this.getRecycler().getId().toString());
		return orderService.getRecord(timeBean);
	}

	/**
	 * 获得当前回收人员评价列表及汇总
	 */
	@Api(name = "app.recycler.getscore", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public AppScoreResult getScore(ScoreAppBean scoreAppBean) {
		scoreAppBean.setRecyclerId(this.getRecycler().getId().toString());
		AppScoreResult result = orderService.getScoreEvaRate(scoreAppBean);
		return result;
	}

	public static boolean testUrlWithTimeOut(String urlString, int timeOutMillSeconds) {
		boolean flag = false;
		URL url;
		try {
			if (urlString.contains("https")) {
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());
				// 从上述SSLContext对象中得到SSLSocketFactory对象
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				url = new URL(urlString);
				// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
				HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
				httpsConn.setSSLSocketFactory(ssf);
				httpsConn.setConnectTimeout(timeOutMillSeconds);
				httpsConn.connect();
				flag = true;
			} else {
				url = new URL(urlString);
				URLConnection co = url.openConnection();
				co.setConnectTimeout(timeOutMillSeconds);
				co.connect();
				flag = true;
			}
		} catch (Exception e1) {
			// System.out.println("连接打不开!");
			url = null;
			throw new ApiException("连接打不开");
		}
		return flag;
	}
	/**
	 * 获得当前回收人员经理的所有下属回人员
	 */
	@Api(name = "app.recycler.getRecycleSon", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getRecycleSon(RecyclersBean recyclersBean){
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		return recyclersService.getRecycleSon(recycler.getId(),recyclersBean);
	}
	/**
	 * 获得当前回收人员的经理信息
	 */
	@Api(name = "app.recycler.getRecycleDetails", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getRecycleDetails(RecyclersBean recyclersBean){
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		Wrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>().eq("recycler_id", recycler.getId()).eq("status_", "1");
				if("Y".equals(recyclersBean.getIsBigRecycle())){
					wrapper.eq("type_","4");
				}else {
					wrapper.eq("type_","1");
				}
		List<CompanyRecycler> companyRecyclerList = companyRecyclerService.selectList(wrapper);
		if(companyRecyclerList.isEmpty()){
			return "暂无信息";
		}
		List<Map<String,Object>> recycleList = recyclersService.getRecycleDetails(companyRecyclerList.get(0).getParentsId(),recyclersBean.getIsBigRecycle(),companyRecyclerList.get(0).getCompanyId());
		Recyclers recyclers = recyclersService.selectById(companyRecyclerList.get(0).getParentsId());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("recyclers",recyclers);
		resultMap.put("areaList",recycleList);
		return resultMap;
	}
	/**
	 * 上传身份证图片
	 */
	@Api(name = "util.aliUploadImage", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object aliUploadImage(PageBean param){
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		List<FileBase64Param> files=new ArrayList<>();
		FileBase64Param file = new FileBase64Param();
		file.setFileName(param.getFileName());
		file.setFileContentBase64(param.getFileContentBase64());
		files.add(file);
		//将身份证图片调用阿里接口解析数据
		Map<String, Object> resultMap = fileUploadServiceImpl.aliUploadImage(param.getFileContentBase64(), param.getFileName());
		if(200!=(int)resultMap.get("stat")){
			//身份信息解析失败
			resultMap.put("stat", "405");
			resultMap.put("body", "身份证不清晰，无法识别");
			return resultMap;
		}
		//将图片传入本地图片服务器
		List<FileBean> fileBeans = fileUploadService.uploadImage(files);
		if(fileBeans.isEmpty()) {
			//身份信息解析失败
			resultMap.put("stat", "405");
			resultMap.put("body", "图片保存失败");
			return resultMap;
		}
		resultMap.put("fileBeans",fileBeans);
		if ("face".equals(param.getFileName())){
			//身份信息解析成功并且是正面
			Object getBody = resultMap.get("getBody");
			Map<String,Object> map =  (Map<String,Object>) JSONObject.parse(getBody.toString());
			String name = map.get("name")+"";
			String num = map.get("num")+"";
			if(StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(num)){
				//调用身份认证初始化服务
				AlipayUserCertifyOpenInitializeResponse initialize = null;
				try {
					initialize = aliPayService.initializeAlipayUser(name, num);
				} catch (AlipayApiException e) {
					e.printStackTrace();
				}
				if (null == initialize){
					throw new ApiException("解析失败");
				}
				//身份认证开始认证服务接口
				AlipayUserCertifyOpenCertifyResponse getInitializeUrl = aliPayService.certifyAlipayUser(initialize.getCertifyId());
				recycler.setBizNo(initialize.getCertifyId());
//				System.out.println("--------------------------------getInitializeUrl:"+ initialize.getCertifyId()+ "-------------");
				recyclersService.updateById(recycler) ;
				resultMap.put("aliUrl",getInitializeUrl.getBody());
				resultMap.put("certifyId", initialize.getCertifyId());
//				System.out.println(initialize.getCertifyId());
			}
		}
		return resultMap;
	}
	/**	认证结果查询
	  * @author sgmark@aliyun.com
	  * @date 2019/8/30 0030
	  * @param
	  * @return
	  */
	@Api(name = "ali.certify.query", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> certifyOpenQuery(){
		Map<String, Object> resultMap = new HashMap<>();
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
//		System.out.println("------------------------------bizNo:"+recycler.getBizNo());
		AlipayUserCertifyOpenQueryResponse  alipayUserCertifyOpenQueryResponse = aliPayService.certifyOpenQuery(recycler.getBizNo());
		JSONObject jsonObject = JSONObject.parseObject(alipayUserCertifyOpenQueryResponse.getBody());
		JSONObject alipay_user_certify_open_query_response = null;
		if (null != jsonObject){
			alipay_user_certify_open_query_response = JSONObject.parseObject(jsonObject.get("alipay_user_certify_open_query_response").toString());

		}
		if("10000".equals(alipayUserCertifyOpenQueryResponse.getCode()) && "T".equals(alipay_user_certify_open_query_response.get("passed"))){
			resultMap.put("passed", "T");
			recycler.setIsReal("1");
			recyclersService.updateById(recycler) ;
		}else {
			resultMap.put("passed", "N");
		}
		resultMap.put("subMsg", alipayUserCertifyOpenQueryResponse.getSubMsg());
		resultMap.put("returnMap", alipay_user_certify_open_query_response);
		return resultMap;
	}

	/**
	 * 芝麻认证开始
	 */
	@Api(name = "app.recycler.certify", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object certify(){
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		ZhimaCustomerCertificationQueryResponse certify = aliPayService.certify(recycler.getBizNo());

		return certify.getBody();
	}
	/**
	 * 返回芝麻认证
	 */
	@Api(name = "app.recycler.getCertifyUrl", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getCertifyUrl(){
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		if (StringUtils.isBlank(recycler.getName())||StringUtils.isBlank(recycler.getIdCard())){
			return "信息不全";
		}
		//调用芝麻认知初始化接口
		ZhimaCustomerCertificationInitializeResponse initialize = aliPayService.initialize(recycler.getName(), recycler.getIdCard());
		//芝麻认证初返回URL
		ZhimaCustomerCertificationCertifyResponse getInitializeUrl = aliPayService.getInitializeUrl(initialize.getBizNo());
		recycler.setBizNo(initialize.getBizNo());
		recyclersService.updateById(recycler) ;
		return getInitializeUrl.getBody();
	}
	/**
	 * 返回芝麻认证
	 */
	@Api(name = "app.recycler.updatRecycleReal", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object updatRecycleReal(RecyclersBean recyclersBean){
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		recyclers.setIsReal(recyclersBean.getIsReal());
		recyclersService.updateById(recyclers) ;
		return "SUCCESS";
	}

	/**
	 * 返回回收人员的密码
	 */
	@Api(name = "app.recycler.getPassword", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	@DS("slave")
	public Object getPassword(){
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		Map<String,Object> resultMap = new HashMap<>();
		if (StringUtils.isBlank(recyclers.getPassword())) {
			resultMap.put("isPassword","NO");

		}else {
			resultMap.put("isPassword","YES");
		}
		return resultMap;
	}

	/**
	 * 新增、修改回收人员的密码
	 */
	@Api(name = "app.recycler.updatePassword", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object updatePassword(RecyclersBean recyclersBean){
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		if (StringUtils.isBlank(recyclersBean.getPassword())){
			return "密码不能为空";
		}
		recyclers.setPassword(recyclersBean.getPassword());
		recyclersService.updateById(recyclers);
		return "操作成功";
	}
	/**
	 * 更新回收人员的头像
	 */
	@Api(name = "app.recycler.updateHeadPicUrl", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object updateHeadPicUrl(RecyclersBean recyclersBean){
		if(StringUtils.isBlank(recyclersBean.getHeadPicUrl())){
			return "请传入头像连接";
		}
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		recyclers.setHeadPicUrl(recyclersBean.getHeadPicUrl());
		recyclersService.updateById(recyclers);
		return "操作成功";
	}
	/**
	 * 增加回收人员的支付宝号码
	 */
	@Api(name = "app.recycler.updateRecycleAccount", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object updateRecycleAccount(RecyclersBean recyclersBean){
		if(StringUtils.isBlank(recyclersBean.getAliAccountNumber())){
			return "请传支付宝账号";
		}
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		recyclers.setAliAccountNumber(recyclersBean.getAliAccountNumber());
		recyclersService.updateById(recyclers);
		return "操作成功";
	}
	/**
	 * 增加回收人员的支付宝号码
	 */
	@Api(name = "app.recycler.getCheckPassword", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	@DS("slave")
	public Object getCheckPassword(RecyclersBean recyclersBean) throws ApiException{
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		if(!recyclersBean.getPassword().equals(recyclers.getPassword())){
			throw new ApiException("密码不正确");
		}
		return "密码正确";
	}

	/**
	 * 返回芝麻认证
	 */
	@Api(name = "ali.certify.url", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object certifyOpenQueryUrl(){
		Recyclers recycler = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		if (StringUtils.isBlank(recycler.getName())||StringUtils.isBlank(recycler.getIdCard())){
			return "信息不全";
		}
		//调用身份认证初始化服务
		AlipayUserCertifyOpenInitializeResponse initialize = null;
		try {
			initialize = aliPayService.initializeAlipayUser(recycler.getName(), recycler.getIdCard());
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		if (null == initialize){
			throw new ApiException("解析失败");
		}
		//身份认证开始认证服务接口
		AlipayUserCertifyOpenCertifyResponse getInitializeUrl = aliPayService.certifyAlipayUser(initialize.getCertifyId());
		recycler.setBizNo(initialize.getCertifyId());
//				System.out.println("--------------------------------getInitializeUrl:"+ initialize.getCertifyId()+ "-------------");
		recyclersService.updateById(recycler) ;
//				System.out.println(initialize.getCertifyId());
		return getInitializeUrl.getBody();
	}

	/**
	 * 增加回收人员的支付宝号码
	 */
	@Api(name = "app.recycler.updateMobile", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object updateMobile(RecyclersLoginBean recyclersLoginBean) throws ApiException{
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		if(messageService.validMessage(recyclersLoginBean.getMobile(),recyclersLoginBean.getCaptcha())){
			if(recyclersLoginBean.getMobile().equals(recyclers.getTel())){
				throw new ApiException("手机号不能相同");
			};
			recyclers.setTel(recyclersLoginBean.getMobile());
			recyclersService.insertOrUpdate(recyclers);
			return "更改成功";
		}else{
			throw new ApiException("验证码错误");
		}
	}

	/**
	 * 获取加密后的号码
	 */
	@Api(name = "app.recycler.getAxnMobile", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getAxnMobile(RecyclersBean recyclersBean) throws ApiException{
		Recyclers recyclers = recyclersService.selectById(RecyclersUtils.getRecycler().getId());
		return bindAxnService.getAxnMobile(recyclersBean.getMobile(),recyclers.getTel());
	}



}
