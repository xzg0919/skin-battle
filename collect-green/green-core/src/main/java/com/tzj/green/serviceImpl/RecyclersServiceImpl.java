package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;

import com.tzj.green.entity.*;
import com.tzj.green.mapper.RecyclersMapper;
import com.tzj.green.param.RecyclersBean;
import com.tzj.green.service.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.easyopen.exception.ApiException;
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
    private RecyclersService recyclersService;
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
    @Transactional(readOnly = false)
    public Map<String, Object> bindingCardByRec(RecyclersBean recyclersBean) {
        //验证手机验证码是否正确有效
        if(!messageService.validMessage(recyclersBean.getMobile(), recyclersBean.getCaptcha())){
            throw new ApiException("手机验证码错误");
        }
        Map<String, Object> returnMap = new HashMap<>();
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("del_flag", 0).eq("real_no", recyclersBean.getRealNo()));
        if (null == member){
            member = new Member();
        }
        member.setName(recyclersBean.getName());
        member.setMobile(recyclersBean.getMobile());
        member.setAddress(recyclersBean.getAddress());
        member.setGender(recyclersBean.getSex());
        member.setRealNo(recyclersBean.getRealNo());
        member.setIdCardRev(recyclersBean.getIdCardRev());
        member.setIdCardObv(recyclersBean.getIdCardObv());
        member.setIdCard(recyclersBean.getIdCard());
        member.setDetailAddress(recyclersBean.getDetailAddress());
        member.setAddress(recyclersBean.getAddress());
        member.setDetailAddress(recyclersBean.getDetailAddress());
        //保存用户当前回收人员所在回收服务范围所在的小区地址
        Map<String, Object> recMap =  recyclersMapper.selectRecRange(recyclersBean.getRecId());
        if (null == recMap){
            throw new ApiException("录入失败, 检查是否通过审核");
        }else {
            try {
                member.setProvinceId(Long.parseLong(recMap.get("province_id")+""));
                member.setProvinceName(recMap.get("province_name")+"");
                member.setCityId(Long.parseLong(recMap.get("city_id")+""));
                member.setCityName(recMap.get("city_name")+"");
                member.setCommunityId(Long.parseLong(recMap.get("area_id")+""));
                member.setCommunityName(recMap.get("area_name")+"");
                member.setStreetId(Long.parseLong(recMap.get("street_id")+""));
                member.setStreetName(recMap.get("street_name")+"");
                member.setCommunityHouseId(Long.parseLong(recMap.get("house_id")+""));
                member.setCommunityHouseName(recMap.get("house_name")+"");
                member.setCompanyId(Long.parseLong(recMap.get("company_id")+""));
                memberService.insertOrUpdate(member);
                returnMap.put("msg", "Y");
            }catch (Exception e){
                throw new ApiException("录入失败:"+e.getMessage());
            }
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
        CompanyRecycler companyRecycler = companyRecyclerService.selectById(recId);
        List<Map<String, Object>> returnListMap = new ArrayList<>();
        if (null != companyRecycler){
            returnListMap = (List<Map<String, Object>>)companyCategoryService.getCompanyCategoryById(companyRecycler.getCompanyId());
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
    public Map<String, Object> appChangePoint(Map<String, Object> paramMap) {
        Map<String, Object> returnMap = new HashMap<>();
        PointsList pointsList = new PointsList();
        if (!paramMap.containsKey("pointList")){
            throw new ApiException("pointList 不能为空");
        }
        MemberPoints memberPoints = memberPointsService.selectOne(new EntityWrapper<MemberPoints>().eq("del_flag", 0).eq("user_no", pointsList.getUserNo()).last(" limit 1"));
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
                memberPoints.setRemnantPoints(memberPoints.getRemnantPoints() + Long.parseLong(paramMap.get("points") + ""));
                memberPoints.setTatalPoints(memberPoints.getTatalPoints() + Long.parseLong(paramMap.get("points") + ""));
            }
        }
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
        if (!checkPoint(paramMap)){
            throw new ApiException("提交失败");
        }
        pointsList.setPoints(Long.parseLong(comRecMap.get("points")+""));
        pointsList.setAliUserId(paramMap.get("aliUserId")+"");
        if (pointsListService.insert(pointsList)){
            returnMap.put("msg", "保存成功");
            returnMap.put("code", "200");
        }
        List<Map<String, Object>> pointLists = (List<Map<String, Object>>) paramMap.get("pointList");
        pointLists.stream().forEach(pointList ->{
            PointsListItem pointsListItem = new PointsListItem();
            pointsListItem.setPointsListId(pointsList.getId());
            pointsListItem.setPoints(Long.parseLong(pointList.get("point")+""));
            pointsListItem.setAmount(Long.parseLong(pointList.get("amount")+""));
            pointsListItem.setCategoryId(Long.parseLong(pointList.get("categoryId")+""));
            pointsListItem.setCategoryName(pointList.get("categoryName")+"");
            pointsListItem.setParentId(Long.parseLong(pointList.get("parentId")+""));
            pointsListItem.setParentName(pointList.get("parentName")+"");
            pointsListItem.setParentIds(pointList.get("parentIds")+"");
            pointsListItemService.insert(pointsListItem);
        });
        //用户增加/减少积分
        memberPointsService.insertOrUpdate(memberPoints);
        return returnMap;
    }
    /**
     * 验证分数是否异常
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/13 0013
     * @Param: 
     * @return: 
     */
    private boolean checkPoint(Map<String, Object> paramMap) {
        List<Map<String, Object>> pointLists = (List<Map<String, Object>>) paramMap.get("pointList");
        BigDecimal points = BigDecimal.ZERO;
        pointLists.stream().forEach(pointList ->{
            BigDecimal point = (BigDecimal) pointList.get("point");
            BigDecimal weight = (BigDecimal) pointList.get("amount");
            points.add(point.multiply(weight));
        });
        if (paramMap.get("points").equals(points.setScale(0, BigDecimal.ROUND_DOWN))){
            return true;
        }
        return false;
    }
}