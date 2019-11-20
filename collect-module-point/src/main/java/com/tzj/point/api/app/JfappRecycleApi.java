package com.tzj.point.api.app;


import com.tzj.collect.common.utils.ToolUtils;
import com.tzj.collect.entity.Member;
import com.tzj.collect.entity.Point;
import com.tzj.module.api.annotation.*;
import com.tzj.collect.core.param.jfapp.JfappRecyclerBean;
import com.tzj.collect.core.param.jfapp.PageBean;
import com.tzj.collect.common.util.JfappRecyclerUtils;
import com.tzj.collect.entity.JfappRecycler;
import com.tzj.collect.core.service.JfappPointListService;
import com.tzj.collect.core.service.JfappRecyclerService;
import com.tzj.collect.core.service.Member4JfAppService;
import com.tzj.collect.core.service.Point4JfAppService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.JFAPP_API_COMMON_AUTHORITY;

@ApiService
public class JfappRecycleApi {

    @Autowired
    private JfappRecyclerService jfappRecyclerService;
    @Autowired
    private Member4JfAppService member4JfAppService;
    @Autowired
    private JfappPointListService jfappPointListService;
    @Autowired
    private Point4JfAppService point4JfAppService;

    @Api(name = "jfapp.get.token", version = "1.0")
    @SignIgnore
    @AuthIgnore //这个api忽略token验证
    public Object getRecycleToken(JfappRecyclerBean jfappRecyclerBean){
        return jfappRecyclerService.getRecycleToken(jfappRecyclerBean);
    }

    @Api(name = "jfapp.recycler.getUserByCardNo", version = "1.0")
    @RequiresPermissions(values = JFAPP_API_COMMON_AUTHORITY)
    public Object getUserByCardNo(JfappRecyclerBean jfappRecyclerBean){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String aliUserId = ToolUtils.getAliUserIdByOrderNo(jfappRecyclerBean.getCardNo());
        Member member = member4JfAppService.selectMemberByAliUserId(aliUserId);
        Point point = point4JfAppService.getPoint(aliUserId);
        if(member==null) {
            resultMap.put("code", "500");
            resultMap.put("member", "该卡号不存在");
            return resultMap;
        }
        resultMap.put("code", "200");
        resultMap.put("member",member);
        resultMap.put("point",point);
        return resultMap;
    }
    @Api(name = "jfapp.recycler.addOrDeleteUserPoint", version = "1.0")
    @RequiresPermissions(values = JFAPP_API_COMMON_AUTHORITY)
    public Object addOrDeleteUserPoint(JfappRecyclerBean jfappRecyclerBean){
        JfappRecycler jfappRecycler = JfappRecyclerUtils.getRecycler();
        return jfappPointListService.addOrDeleteUserPoint(jfappRecyclerBean,jfappRecycler.getId());
    }

    @Api(name = "jfapp.recycler.getJfappPointList", version = "1.0")
    @RequiresPermissions(values = JFAPP_API_COMMON_AUTHORITY)
    public Object getJfappPointList(PageBean pageBean){
        JfappRecycler jfappRecycler = JfappRecyclerUtils.getRecycler();
        return jfappPointListService.getJfappPointList(pageBean,jfappRecycler.getId());
    }

    @Api(name = "jfapp.recycler.getJfPointListByAdmin", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = JFAPP_API_COMMON_AUTHORITY)
    public Object getJfPointListByAdmin(JfappRecyclerBean jfappRecyclerBean){
        return jfappPointListService.getJfPointListByAdmin(jfappRecyclerBean);
    }

}
