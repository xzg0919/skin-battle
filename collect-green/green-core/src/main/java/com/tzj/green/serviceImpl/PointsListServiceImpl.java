package com.tzj.green.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;

import com.tzj.green.entity.MemberPoints;
import com.tzj.green.entity.PointsList;
import com.tzj.green.entity.PointsListItem;
import com.tzj.green.mapper.PointsListMapper;
import com.tzj.green.param.PageBean;
import com.tzj.green.param.PointsListBean;
import com.tzj.green.service.MemberPointsService;
import com.tzj.green.service.PointsListItemService;
import com.tzj.green.service.PointsListService;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

import static com.tzj.green.common.content.TokenConst.*;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [积分流水表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PointsListServiceImpl extends ServiceImpl<PointsListMapper, PointsList> implements PointsListService
{
    @Resource
    private PointsListMapper pointsListMapper;
    @Resource
    private PointsListItemService pointsListItemService;
    @Resource
    private MemberPointsService memberPointsService;
    @Override
    public Object getPointsListByCompanyId(Long companyId, PointsListBean pointsListBean) {
        PageBean pageBean = pointsListBean.getPageBean();
        if (null == pageBean){
            pageBean = new PageBean();
        }
        Integer pageStart = (pageBean.getPageNum()-1)*pageBean.getPageSize();
        List<Map<String, Object>> pointsList = pointsListMapper.getPointsListByCompanyId(companyId, pointsListBean.getPointsType(), pointsListBean.getStartTime(), pointsListBean.getEndTime(), pointsListBean.getName(), pointsListBean.getTel(), pointsListBean.getCityId(), pointsListBean.getAreaId(), pointsListBean.getStreetId(), pointsListBean.getCommunityId(), pointsListBean.getCommunityHouseId(), pageStart, pageBean.getPageSize());
        Integer count = pointsListMapper.getPointsListCount(companyId, pointsListBean.getPointsType(), pointsListBean.getStartTime(), pointsListBean.getEndTime(), pointsListBean.getName(), pointsListBean.getTel(), pointsListBean.getCityId(), pointsListBean.getAreaId(), pointsListBean.getStreetId(), pointsListBean.getCommunityId(), pointsListBean.getCommunityHouseId());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pointsList",pointsList);
        resultMap.put("count",count);
        resultMap.put("pageNum",pageBean.getPageNum());
        return resultMap;
    }
    
    /**
     * 积分变动
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/14 0014
     * @Param: 
     * @return: 
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean changePoint(String userNo, Map<String, Object> paramMap) {
        if (CollectionUtils.isEmpty(paramMap) || !paramMap.containsKey("pointType")
            || !paramMap.containsKey("points") || !paramMap.containsKey("source")){
            return false;
        }
        MemberPoints memberPoints = memberPointsService.selectOne(new EntityWrapper<MemberPoints>().eq("del_flag", 0).eq("user_no", userNo).last(" limit 1"));
        PointsList pointsList = new PointsList();
        //检查积分是否足够
        if (null != memberPoints){
            //扣分
            if (paramMap.get("pointType").equals("1")) {
                if (memberPoints.getRemnantPoints().compareTo(Long.parseLong(paramMap.get("points") + "")) < 0) {
                    //查询收呗积分是否足够：是)扣完当前积分，收呗积分补足； 否) 返回false
                    if (StringUtils.isEmpty(paramMap.get("aliUserId"))){
                        //未绑定支付宝用户，积分不够，直接返回false
                        return false;
                    }
                    try {
                        Map<String, Object> pointMap = rpcCollectApi("point.getPoint", paramMap.get("aliUserId")+"", new HashMap<>());
                        if (null == paramMap || !pointMap.containsKey("remainPoint")){
                            return false;
                        }
                        Long remainPoint = (Long) pointMap.get("remainPoint");
                        Long points = remainPoint+memberPoints.getRemnantPoints();
                        //总分都不够，直接返回false
                        if (points.compareTo(Long.parseLong(paramMap.get("points") + "")) < 0){
                            return false;
                        }else {
                            //扣除所有积分商户并扣除收呗积分
                            memberPoints.setRemnantPoints(0L);
                            //再扣除收呗积分
                            Map<String, Object> rpcParamMap = new HashMap<>();
                            rpcParamMap.put("pointsReason", paramMap.get("pointsReason"));
                            //所需分值（负数）
                            rpcParamMap.put("points", memberPoints.getRemnantPoints()- Long.parseLong(paramMap.get("points") +""));
                            Map<String, Object> reducePointMap = rpcCollectApi("point.reduce", paramMap.get("aliUserId")+"", rpcParamMap);
                            if(CollectionUtils.isEmpty(reducePointMap) || !reducePointMap.containsKey("code") || "200".equals(reducePointMap.get("code"))){
                                //扣分失败
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }else {
                    memberPoints.setRemnantPoints(memberPoints.getRemnantPoints() - Long.parseLong(paramMap.get("points") + ""));
                }
            }else {
                memberPoints.setRemnantPoints(memberPoints.getRemnantPoints() + Long.parseLong(paramMap.get("points") + ""));
                memberPoints.setTatalPoints(memberPoints.getTatalPoints() + Long.parseLong(paramMap.get("points") + ""));
            }
        }else {
            memberPoints = new MemberPoints();
            if (paramMap.get("pointType").equals("1")) {
                return false;
            }
            memberPoints.setRemnantPoints(memberPoints.getRemnantPoints() + Long.parseLong(paramMap.get("points") + ""));
            memberPoints.setTatalPoints(memberPoints.getTatalPoints() + Long.parseLong(paramMap.get("points") + ""));
        }
        pointsList.setPoints(Long.parseLong(paramMap.get("points")+""));
        pointsList.setPointsType(paramMap.get("pointType")+"");
        pointsList.setSource(Integer.parseInt(paramMap.get("source")+""));
        pointsList.setRecyclerId(Long.parseLong(paramMap.get("recId")+""));
        pointsList.setCompanyId(Long.parseLong(paramMap.get("companyId")+""));
        pointsList.setAliUserId(paramMap.containsKey("aliUserId") ? paramMap.get("aliUserId")+"" : "");
        pointsList.setUserName(paramMap.containsKey("userName") ? paramMap.get("userName")+"" : "");
        pointsList.setUserNo(paramMap.containsKey("userNo") ? paramMap.get("userNo")+"" : "");
        if (!this.insert(pointsList)){
            return false;
        }
        if (paramMap.containsKey("pointList")){
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
        }
        //用户增加/减少积分
        memberPointsService.insertOrUpdate(memberPoints);
        return true;
    }
    /**
     * 远程调用收呗Api接口
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/14 0014
     * @Param: 
     * @return: 
     */
    public Map<String, Object> rpcCollectApi(String apiName, String userId, Map<String, Object> paramMap) throws Exception {
        String token= JwtUtils.generateToken(userId, ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
        String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
        System.out.println("token是 : "+securityToken);
        String api="http://localhost:9090/ali/api";
        HashMap<String,Object> param=new HashMap<>();
        param.put("name", apiName);
        param.put("version","1.0");
        param.put("format","json");
        param.put("app_key","app_id_1");
        param.put("timestamp",  Calendar.getInstance().getTimeInMillis());
        param.put("token", securityToken);
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data", paramMap);
        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
        param.put("sign",sign);
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        Map<String, Object> resultMap =  JSONObject.parseObject(resultJson);
        Map<String, Object> returnMap = null;
        if (resultMap.containsKey("data")){
            returnMap = (Map<String, Object>) JSONObject.parseObject(resultJson).get("data");
        }
        return returnMap;
    }

}