package com.tzj.green.serviceImpl;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;

import com.tzj.green.common.content.AlipayConst;
import com.tzj.green.entity.*;
import com.tzj.green.mapper.RecyclersMapper;
import com.tzj.green.param.RecyclersBean;
import com.tzj.green.service.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收人员service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class RecyclersServiceImpl extends ServiceImpl<RecyclersMapper, Recyclers> implements RecyclersService
{
    @Resource
    private MemberService memberService;
    @Resource
    private MessageService messageService;
    @Resource
    private RecyclersMapper recyclersMapper;
    @Resource
    private CompanyCategoryService companyCategoryService;
    @Resource
    private CompanyRecyclerService companyRecyclerService;
    @Resource
    private PointsListService pointsListService;
    @Resource
    private PointsListItemService pointsListItemService;
    @Resource
    private MemberPointsService memberPointsService;
    @Resource
    private AliPayService aliPayService;
    @Resource
    private CommunityHouseNameService communityHouseNameService;
    @Resource
    private CompanyCommunityService companyCommunityService;
    @Resource
    private AreaService areaService;
    @Resource
    private CommunityService communityService;
    @Resource
    private MemberCardService memberCardService;
    /**
     * 根据手机号查询回收人员
     *
     * @param mobile
     * @return
     */
    @Override
    public Recyclers selectByMobile(String mobile) {
        return selectOne(new EntityWrapper<Recyclers>().eq("tel", mobile));
    }
    @Override
    public Object getCompanyAddressByLocal(Long recyclerId,String lng,String lat){
        //保存用户当前回收人员所在回收服务范围所在的小区地址
        RecyclersBean recyclersBean = new RecyclersBean();
        CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclerId).eq("status_","1"));
        if (null == companyRecycler){
            throw new ApiException("该回收人员暂无申请公司");
        }
        CommunityHouseName communityHouseName = communityHouseNameService.selectOneByLocalCompanyId(companyRecycler.getCompanyId(), lng, lat);
        if (null == communityHouseName){
            return recyclersBean;
        }
        CompanyCommunity companyCommunity = companyCommunityService.selectById(communityHouseName.getCommunityId());
        if (null == companyCommunity){
            return recyclersBean;
        }
        recyclersBean.setProvinceId(companyCommunity.getProvinceId().toString());
        recyclersBean.setProvinceName(companyCommunity.getProvinceName());
        recyclersBean.setCityId(companyCommunity.getCityId().toString());
        recyclersBean.setCityName(companyCommunity.getCityName());
        recyclersBean.setAreaId(companyCommunity.getAreaId().toString());
        recyclersBean.setAreaName(companyCommunity.getAreaName());
        recyclersBean.setStreetId(companyCommunity.getStreetId().toString());
        recyclersBean.setStreetName(companyCommunity.getStreetName());
        recyclersBean.setCommunityId(companyCommunity.getId().toString());
        recyclersBean.setCommunityName(companyCommunity.getCommunityName());
        return recyclersBean;
    }
    @Override
    public Object getAreaDetail(String parentId){
        EntityWrapper<Area> wrapper = new EntityWrapper<>();
        if (!StringUtils.isBlank(parentId)){
            wrapper.eq("parent_id",parentId);
        }else {
            wrapper.eq("type","0");
        }
        return areaService.selectList(wrapper);
    }
    @Override
    public Object getCommunityByStreetId(String streetId,String communityName){
        EntityWrapper<Community> wrapper = new EntityWrapper<>();
            wrapper.eq("area_id",streetId);
            if (StringUtils.isNotBlank(communityName)){
                wrapper.like("community_name",communityName);
            }
        return communityService.selectList(wrapper);
    }
    @Override
    public Object checkCardNo(String realNo,Long recyclerId){
        MemberCard memberCard = memberCardService.selectOne(new EntityWrapper<MemberCard>().eq("member_card", realNo));
        if (null == memberCard){
            throw new ApiException("该卡号不存在");
        }
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("del_flag", 0).eq("real_no", realNo));
        if (null != member){
            throw new ApiException("该卡号已绑定");
        }
        CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclerId).eq("status_","1"));
        if (null == companyRecycler){
            throw new ApiException("该回收人员暂无申请公司");
        }
        return "success";
    }
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> bindingCardByRec(RecyclersBean recyclersBean) {
        //验证手机验证码是否正确有效
//        if(!messageService.validMessage(recyclersBean.getMobile(), recyclersBean.getCaptcha())){
//            throw new ApiException("手机验证码错误");
//        }
        Map<String, Object> returnMap = new HashMap<>();
        MemberCard memberCard = memberCardService.selectOne(new EntityWrapper<MemberCard>().eq("member_card", recyclersBean.getRealNo()));
        if (null == memberCard){
            throw new ApiException("该卡号不存在");
        }
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("del_flag", 0).eq("real_no", recyclersBean.getRealNo()));
        if (null != member){
            throw new ApiException("该卡号已绑定");
        }
        Member member1 = memberService.selectOne(new EntityWrapper<Member>().eq("del_flag", 0).eq("mobile", recyclersBean.getMobile()));
        if (null != member1){
            throw new ApiException("该手机号已绑定");
        }
        CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclersBean.getRecId()).eq("status_","1"));
        if (null == companyRecycler){
            throw new ApiException("该回收人员暂无申请公司");
        }
        member = new Member();
        member.setCompanyId(companyRecycler.getCompanyId());
        member.setName(recyclersBean.getName());
        member.setMobile(recyclersBean.getMobile());
        member.setAddress(recyclersBean.getAddress());
        member.setGender(recyclersBean.getSex());
        member.setRealNo(recyclersBean.getRealNo());
        member.setIdCardRev(recyclersBean.getIdCardRev());
        member.setIdCardObv(recyclersBean.getIdCardObv());
        member.setIdCard(recyclersBean.getIdCard());
        member.setAddress(recyclersBean.getAddress());
        try {
            if (StringUtils.isNotBlank(recyclersBean.getProvinceId())){
                member.setProvinceId(Long.parseLong(recyclersBean.getProvinceId()));
            }
            if (StringUtils.isNotBlank(recyclersBean.getCityId())){
                member.setCityId(Long.parseLong(recyclersBean.getCityId()));
            }
            if (StringUtils.isNotBlank(recyclersBean.getAreaId())){
                member.setAreaId(Long.parseLong(recyclersBean.getAreaId()));
            }
            if (StringUtils.isNotBlank(recyclersBean.getStreetId())){
                member.setStreetId(Long.parseLong(recyclersBean.getStreetId()));
            }
            member.setProvinceName(recyclersBean.getProvinceName());
            member.setCityName(recyclersBean.getCityName());
            member.setAreaName(recyclersBean.getAreaName());
            member.setStreetName(recyclersBean.getStreetName());
            if (StringUtils.isNotBlank(recyclersBean.getCommunityId())){
                member.setCommunityId(Long.parseLong(recyclersBean.getCommunityId()));
            }
            member.setCommunityName(recyclersBean.getCommunityName());
            member.setCommunityHouseName(recyclersBean.getHouseName());
            member.setCompanyId(companyRecycler.getCompanyId());
            memberService.insertOrUpdate(member);
            returnMap.put("msg", "Y");
        }catch (Exception e){
            e.printStackTrace();
            throw  new ApiException("开卡参数异常");
        }
        return returnMap;
    }
    /**
     * 查找回收人员服务地址
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/10 0010
     * @Param: 
     * @return: 
     */
    @Override
    public Map<String, Object> selectRecRange(Long recId) {
        return recyclersMapper.selectRecRange(recId);
    }
    /**
     * 投放种类信息
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/13 0013
     * @Param:
     * @return:
     */
    @Override
    public List<Map<String, Object>> categoryPointInfo(Long recId) {
        CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id",recId).eq("status_","1"));
        if (null == companyRecycler){
            throw new ApiException("暂未找到回收公司");
        }
        List<Map<String, Object>> returnListMap = new ArrayList<>();
        if (null != companyRecycler){
            returnListMap = (List<Map<String, Object>>)companyCategoryService.getAppCompanyCategoryById(companyRecycler.getCompanyId());
        }
        return returnListMap;
    }

    /**
     * 扫码加分扣分： 加分加总分及剩余分 扣分只扣剩余分  分值不够扣收呗积分（加分只加积分商户中用户积分）
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/14 0014
     * @Param: 
     * @return: 
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> appChangePoint(Map<String, Object> paramMap,Long recyclerId) {
        Map<String, Object> returnMap = new HashMap<>();
        PointsList pointsList = new PointsList();
        if (!paramMap.containsKey("pointList")){
            throw new ApiException("pointList 不能为空");
        }
        MemberPoints memberPoints = memberPointsService.selectOne(new EntityWrapper<MemberPoints>().eq("del_flag", 0).eq("user_no", paramMap.get("realNo")).last(" limit 1"));
        if(!paramMap.containsKey("pointType")){
            throw new ApiException("pointType 不能为空");
        }else {
            pointsList.setPointsType(paramMap.get("pointType")+"");
            pointsList.setSource(0);
            //检查积分是否足够
            if (null != memberPoints){
                //扣分
                if (pointsList.getPointsType().equals("1")) {
                    pointsList.setPointsType("1");
                    if (memberPoints.getRemnantPoints().compareTo(Long.parseLong(paramMap.get("points") + "")) < 0) {
                        throw new ApiException("积分不足");
                    }
                    memberPoints.setRemnantPoints(memberPoints.getRemnantPoints() - Long.parseLong(paramMap.get("points") + ""));
                }else {
                    pointsList.setPointsType("0");
                    memberPoints.setRemnantPoints(memberPoints.getRemnantPoints() + Long.parseLong(paramMap.get("points") + ""));
                    memberPoints.setTatalPoints(memberPoints.getTatalPoints() + Long.parseLong(paramMap.get("points") + ""));
                }
            }else {
                memberPoints = new MemberPoints();
                if (pointsList.getPointsType().equals("1")) {
                    throw new ApiException("积分不足");
                }
                CompanyRecycler companyRecycler = companyRecyclerService.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclerId).eq("status_", "1"));
                memberPoints.setCompanyId(companyRecycler.getCompanyId());
                memberPoints.setRemnantPoints(Long.parseLong(paramMap.get("points") + ""));
                memberPoints.setTatalPoints( Long.parseLong(paramMap.get("points") + ""));
                memberPoints.setUserNo(paramMap.get("realNo")+"");
                memberPoints.setUserName(paramMap.get("userName")+"");
                memberPoints.setAliUserId(paramMap.get("aliUserId")+"");
            }
        }
        //用户增加/减少积分
        memberPointsService.insertOrUpdate(memberPoints);
        if (!paramMap.containsKey("realNo")){
            throw new ApiException("实体卡号不能为空");
        }else {
            pointsList.setUserNo(paramMap.get("realNo")+"");
            pointsList.setUserName(paramMap.get("userName")+"");
        }
        Map<String, Object> comRecMap = companyRecyclerService.selectMap(new EntityWrapper<CompanyRecycler>().setSqlSelect("company_id, status_").eq("del_flag", 0).eq("recycler_id", paramMap.get("recId")).eq("status_", "1").last(" limit 1"));
        if (null == comRecMap){
            throw new ApiException("当前管理员尚未通过验证");
        }
        pointsList.setRecyclerId(Long.parseLong(paramMap.get("recId")+""));
        pointsList.setCompanyId(Long.parseLong(comRecMap.get("company_id")+""));
        //验证加减分是否异常
//        if (!checkPoint(paramMap)){
//            throw new ApiException("提交失败");
//        }
        pointsList.setPoints(Long.parseLong(paramMap.get("points")+""));
        pointsList.setAliUserId(paramMap.get("aliUserId")+"");
        if (pointsListService.insert(pointsList)){
            returnMap.put("msg", "保存成功");
            returnMap.put("code", "200");
        }
        List<Map<String, Object>> pointLists = (List<Map<String, Object>>) paramMap.get("pointList");
        pointLists.stream().forEach(pointList ->{
            PointsListItem pointsListItem = new PointsListItem();
            pointsListItem.setPointsListId(pointsList.getId());
            pointsListItem.setPoints(new BigDecimal(pointList.get("point")+""));
            pointsListItem.setAmount(new BigDecimal(pointList.get("amount")+""));
            if (null!=pointList.get("categoryId")){
                pointsListItem.setCategoryId(Long.parseLong(pointList.get("categoryId")+""));
            }
            if (null!=pointList.get("categoryName")){
                pointsListItem.setCategoryName(pointList.get("categoryName")+"");
            }
            if (null!=pointList.get("parentId")){
                pointsListItem.setParentId(Long.parseLong(pointList.get("parentId")+""));
            }
            if (null!=pointList.get("parentName")){
                pointsListItem.setParentName(pointList.get("parentName")+"");
            }
            if (null!=pointList.get("parentIds")){
                pointsListItem.setParentIds(pointList.get("parentIds")+"");
            }
            pointsListItemService.insert(pointsListItem);
        });
        return returnMap;
    }

    @Transactional
    @Override
    public String getAuthCode(String authCode, Long recyclersId) throws Exception {

        Recyclers recyclers = this.selectById(recyclersId);
        //根据用户授权的具体authCode查询是用户的userid和token
        AlipaySystemOauthTokenResponse response = aliPayService.selectUserToken(authCode, AlipayConst.appId);
        if (!response.isSuccess()) {
            throw new ApiException("授权失败，请重新授权");
        }
        String accessToken = response.getAccessToken();
        String userId = response.getUserId();
        recyclers.setAliUserId(userId);
        this.updateById(recyclers);
        return "操作成功";
    }

    @Override
    public Map<String, Object> pointsList(RecyclersBean recyclersBean) {
        Map<String, Object> returnMap = new HashMap<>();
        Integer pointsCount = recyclersMapper.pointListCount(recyclersBean.getId());
        List<Map<String, Object>> pointsList = recyclersMapper.pointsLists(recyclersBean.getId(), (recyclersBean.getPageBean().getPageNum()-1)*recyclersBean.getPageBean().getPageSize(), recyclersBean.getPageBean().getPageSize());
        returnMap.put("count", pointsCount);
        returnMap.put("list", pointsList);
        return returnMap;
    }

    @Override
    @Transactional
    public Object updatePassword(Long recyclersId, RecyclersBean recyclersBean) {
        Recyclers recyclers = this.selectById(recyclersId);
        recyclers.setPassword(recyclersBean.getPassword());
        this.updateById(recyclers);
        return "操作成功";
    }


    /**
     * 验证分数是否异常(自欺欺人)
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/13 0013
     * @Param: 
     * @return: 
     */
    private boolean checkPoint(Map<String, Object> paramMap) {
        List<Map<String, Object>> pointLists = (List<Map<String, Object>>) paramMap.get("pointList");
        BigDecimal points = BigDecimal.ZERO;
        pointLists.stream().forEach(pointList ->{
            BigDecimal point = BigDecimal.valueOf(Double.parseDouble(pointList.get("point")+""));
            BigDecimal weight = BigDecimal.valueOf(Double.parseDouble(pointList.get("amount")+""));
            points.add(point.multiply(weight));
        });
        if (paramMap.get("points").equals(points.setScale(0, BigDecimal.ROUND_DOWN))){
            return true;
        }
        return false;
    }
}