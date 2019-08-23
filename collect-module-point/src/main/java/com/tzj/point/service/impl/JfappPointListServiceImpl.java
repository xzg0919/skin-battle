package com.tzj.point.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Point;
import com.tzj.collect.entity.PointList;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.point.api.app.param.JfappRecyclerBean;
import com.tzj.point.api.app.param.PageBean;
import com.tzj.point.entity.JfappPointList;
import com.tzj.point.mapper.JfappPointListMapper;
import com.tzj.point.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class JfappPointListServiceImpl extends ServiceImpl<JfappPointListMapper, JfappPointList> implements JfappPointListService {

    @Autowired
    private PointService pointService;
    @Autowired
    private PointListService pointListService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private JfappPointListMapper jfappPointListMapper;

    @Override
    @Transactional
    public Object addOrDeleteUserPoint(JfappRecyclerBean jfappRecyclerBean, long jfappRecyclerId) {
        Member member = memberService.selectMemberByAliUserId(jfappRecyclerBean.getAliUserId());
        //查询用户积分
        Point point = pointService.getPoint(jfappRecyclerBean.getAliUserId());
             PointList pointList =new  PointList();
        JfappPointList jfappPointList = new JfappPointList();
        if ("0".equals(jfappRecyclerBean.getType())){
            if (null == point){
                point = new Point();
                point.setAliUserId(jfappRecyclerBean.getAliUserId());
                point.setPoint(Double.parseDouble(jfappRecyclerBean.getPoint()));
                point.setRemainPoint(Double.parseDouble(jfappRecyclerBean.getPoint()));
                pointService.insert(point);
            }else {
                point.setPoint(point.getPoint()+Double.parseDouble(jfappRecyclerBean.getPoint()));
                point.setRemainPoint(point.getRemainPoint()+Double.parseDouble(jfappRecyclerBean.getPoint()));
                pointService.updateById(point);
            }
            pointList.setPoint(jfappRecyclerBean.getPoint());
            pointList.setDescrb("扫码加分");
            jfappPointList.setDescrb("扫码加分");
        }else if ("1".equals(jfappRecyclerBean.getType())){
            if (null == point||Double.parseDouble(jfappRecyclerBean.getPoint())>point.getRemainPoint()){
                throw new ApiException("用户积分不足");
            }else {
                point.setRemainPoint(point.getRemainPoint()-Double.parseDouble(jfappRecyclerBean.getPoint()));
                pointService.updateById(point);
            }
            pointList.setPoint("-"+jfappRecyclerBean.getPoint());
            pointList.setDescrb("扫码减分");
            jfappPointList.setDescrb("扫码减分");
        }
        pointList.setAliUserId(jfappRecyclerBean.getAliUserId());
        pointList.setType(jfappRecyclerBean.getType());
        pointList.setDocumentNo(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(899999)+100000));
        pointListService.insert(pointList);
        jfappPointList.setAliUserId(jfappRecyclerBean.getAliUserId());
        jfappPointList.setPoint(jfappRecyclerBean.getPoint());
        jfappPointList.setType(jfappRecyclerBean.getType());
        jfappPointList.setRecyclerId(jfappRecyclerId+"");
        jfappPointList.setUserName(jfappRecyclerBean.getUserName());
        jfappPointList.setMobile(jfappRecyclerBean.getMobile());
        this.insert(jfappPointList);
        DecimalFormat df   = new DecimalFormat("######0.00");
        aliPayService.updatePoint(member.getAliCardNo(), member.getOpenCardDate(), Double.parseDouble(df.format(point.getRemainPoint())) + "", null);
        return "success";
    }

    @Override
    public Object getJfappPointList(PageBean pageBean,long jfappRecyclerId) {
        Integer startSize = (pageBean.getPageNum()-1)*pageBean.getPageSize();
        return jfappPointListMapper.getJfappPointList(startSize,pageBean.getPageSize(),jfappRecyclerId);
    }

    @Override
    public Object getJfPointListByAdmin(JfappRecyclerBean jfappRecyclerBean) {
        Integer startSize = (jfappRecyclerBean.getPageBean().getPageNum()-1)*jfappRecyclerBean.getPageBean().getPageSize();
        List<Map<String, Object>> jfPointListByAdminList = jfappPointListMapper.getJfPointListByAdmin(jfappRecyclerBean.getUserName(), jfappRecyclerBean.getRecyclerName(), jfappRecyclerBean.getMobile(), jfappRecyclerBean.getStartDate(), jfappRecyclerBean.getEndDate(), startSize, jfappRecyclerBean.getPageBean().getPageSize());
        Integer count = jfappPointListMapper.getJfPointListCountByAdmin(jfappRecyclerBean.getUserName(), jfappRecyclerBean.getRecyclerName(), jfappRecyclerBean.getMobile(), jfappRecyclerBean.getStartDate(), jfappRecyclerBean.getEndDate());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("jfPointListByAdminList",jfPointListByAdminList);
        resultMap.put("count",count);
        resultMap.put("pageNum",jfappRecyclerBean.getPageBean().getPageNum());
        return resultMap;
    }
}
