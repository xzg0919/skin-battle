package com.tzj.green.api.app;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.*;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.green.entity.CompanyRecycler;
import com.tzj.green.entity.Member;
import com.tzj.green.entity.Recyclers;
import com.tzj.green.param.RecyclersBean;
import com.tzj.green.service.*;
import com.tzj.green.serviceImpl.FileUploadServiceImpl;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;
import com.tzj.module.easyopen.file.FileUploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.green.common.content.TokenConst.APP_API_COMMON_AUTHORITY;

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
	private CompanyRecyclerService companyRecyclerService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private FileUploadServiceImpl fileUploadServiceImpl;
	@Autowired
	private AliPayService aliPayService;
	@Resource
	private MemberService memberService;

	public Recyclers getRecycler() {
		Subject subject=ApiContext.getSubject();
		// 接口里面获取 Recyclers 的例子
		Recyclers recyclers = (Recyclers) subject.getUser();
		return recyclers;
	}

	/**
	 * 返回当前回收人员
	 */
	@Api(name = "app.recycler.current", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	@DS("slave")
	public Object getCurrenAppRecycler(RecyclersBean recyclersBean) {
		Recyclers recyclers = recyclersService.selectById(this.getRecycler().getId());
		return recyclers;
	}


	/**
	 * 保存第一步接口 姓名 性别 以及地址 必传
	 */
	@Api(name = "app.recycler.save2", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object save1(RecyclersBean recyclersBean) {
		System.out.println("回收人员的姓名是+："+recyclersBean.getName()+"性别是："+recyclersBean.getSex()+"省份正号是"+recyclersBean.getIdCard()+"地址是："+recyclersBean.getAddress());
		Recyclers recycler = recyclersService.selectById(getRecycler().getId());
		if (!StringUtils.isBlank(recyclersBean.getName())&&
			!StringUtils.isBlank(recyclersBean.getSex())&& 
			!StringUtils.isBlank(recyclersBean.getIdCard())&&
			!StringUtils.isBlank(recyclersBean.getAddress()) &&
			!StringUtils.isBlank(recyclersBean.getHeadPicUrl())) {
			recycler.setName(recyclersBean.getName());
			recycler.setSex(recyclersBean.getSex());
			recycler.setIdCard(recyclersBean.getIdCard());
			recycler.setAddress(recyclersBean.getAddress());
			recycler.setHeadPicUrl(recyclersBean.getHeadPicUrl());
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
	@Api(name = "app.recycler.save1", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object save2(RecyclersBean recyclersBean) {
		Recyclers recyclers = recyclersService.selectById(getRecycler().getId());
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
	 *
	 * 保存第三步接口  提交资料
	 *
	 */
	@Api(name = "app.recycler.save3", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object save3(RecyclersBean recyclersBean) {
		Recyclers recyclers = recyclersService.selectOne(new EntityWrapper<Recyclers>().eq("del_flag", 0).eq("recycler_id", getRecycler().getId()));
		if (null == recyclers) {
			recyclers = new Recyclers();
		}
		recyclers.setProvinceName(recyclersBean.getProvinceName());
		recyclers.setCityName(recyclersBean.getCityName());
		recyclers.setAreaName(recyclersBean.getAreaName());
		recyclers.setStreetName(recyclersBean.getStreetName());
		recyclers.setCommunityName(recyclersBean.getCommunityName());
		recyclers.setHouseName(recyclersBean.getHouseName());
		recyclersService.updateById(recyclers);
		CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", getRecycler().getId()).eq("company_id", recyclersBean.getCompanyId()));
		if (null == companyRecycler){
			companyRecycler = new CompanyRecycler();
			companyRecycler.setRecyclerId(getRecycler().getId());
			companyRecycler.setCompanyId(recyclersBean.getCompanyId());
		}else {
			companyRecycler.setDelFlag("0");
		}
		companyRecyclerService.insertOrUpdate(companyRecycler);
		return "true";
	}


	/**
	 * 上传身份证图片
	 */
	@Api(name = "util.aliUploadImage", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object aliUploadImage(FileBase64Param fileBase64Param){
		Recyclers recycler = recyclersService.selectById(getRecycler().getId());
		List<FileBase64Param> files=new ArrayList<>();
		files.add(fileBase64Param);
		//将身份证图片调用阿里接口解析数据
		Map<String, Object> resultMap = fileUploadServiceImpl.aliUploadImage(fileBase64Param.getFileContentBase64(), fileBase64Param.getFileName());
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
		if ("face".equals(fileBase64Param.getFileName())){
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
		Recyclers recycler = recyclersService.selectById(getRecycler().getId());
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
		Recyclers recycler = recyclersService.selectById(getRecycler().getId());
		ZhimaCustomerCertificationQueryResponse certify = aliPayService.certify(recycler.getBizNo());
		return certify.getBody();
	}
	/**
	 * 返回芝麻认证
	 */
	@Api(name = "app.recycler.getCertifyUrl", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object getCertifyUrl(){
		Recyclers recycler = recyclersService.selectById(getRecycler().getId());
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
		Recyclers recyclers = recyclersService.selectById(getRecycler().getId());
		recyclers.setIsReal(recyclersBean.getIsReal());
		recyclersService.updateById(recyclers) ;
		return "SUCCESS";
	}


	/**
	 * 新增、修改回收人员的密码
	 */
	@Api(name = "app.recycler.updatePassword", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object updatePassword(RecyclersBean recyclersBean){
		Recyclers recyclers = recyclersService.selectById(getRecycler().getId());
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
		Recyclers recyclers = recyclersService.selectById(getRecycler().getId());
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
		if(StringUtils.isBlank(recyclersBean.getAliUserId())){
			return "请传支付宝账号";
		}
		Recyclers recyclers = recyclersService.selectById(getRecycler().getId());
		recyclers.setAliUserId(recyclersBean.getAliUserId());
		recyclersService.updateById(recyclers);
		return "操作成功";
	}

	/**
	 * 返回芝麻认证
	 */
	@Api(name = "ali.certify.url", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Object certifyOpenQueryUrl(){
		Recyclers recycler = recyclersService.selectById(getRecycler().getId());
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
	 * 查询会员卡是否已绑定
	 * @author: sgmark@aliyun.com
	 * @Date: 2020/1/10 0010
	 * @Param: 
	 * @return: 
	 */
	@Api(name = "app.is.binding", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> isBindingByCard(RecyclersBean recyclersBean){
		Map<String, Object> returnMap = new HashMap<>();
		Member member = memberService.selectOne(new EntityWrapper<Member>().eq("del_flag", 0).eq("real_no", recyclersBean.getRealNo()));
		if (member == null){
			returnMap.put("isBinding", "N");
		}else {
			returnMap.put("isBinding", "Y");
		}
		return returnMap;
	}
	/**
	 * 线下回收员绑定新用户
	 * @author: sgmark@aliyun.com
	 * @Date: 2020/1/10 0010
	 * @Param: 
	 * @return: 
	 */
	@Api(name = "app.binding.card", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> bindingCardByRec(RecyclersBean recyclersBean){
		recyclersBean.setRecId(getRecycler().getId());
		return recyclersService.bindingCardByRec(recyclersBean);
	}
	/**
	 * 投放种类信息
	 * @author: sgmark@aliyun.com
	 * @Date: 2020/1/13 0013
	 * @Param: 
	 * @return: 
	 */
	@Api(name = "app.point.info", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public List<Map<String, Object>> categoryPointInfo(){
		return recyclersService.categoryPointInfo(getRecycler().getId());
	}
	/**
	 * 增减积分
	 * @author: sgmark@aliyun.com
	 * @Date: 2020/1/10 0010
	 * @Param:
	 * @return:
	 */
	@Api(name = "app.change.point", version = "1.0")
	@RequiresPermissions(values = APP_API_COMMON_AUTHORITY)
	public Map<String, Object> appChangePoint(Map<String, Object> paramMap){
		paramMap.put("recId", getRecycler().getId());
		return recyclersService.appChangePoint(paramMap);
	}


}
