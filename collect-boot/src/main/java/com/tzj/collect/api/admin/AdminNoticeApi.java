package com.tzj.collect.api.admin;

import com.alibaba.druid.util.StringUtils;
import com.taobao.api.ApiException;
import com.tzj.collect.core.param.admin.AdminNoticeBean;
import com.tzj.collect.entity.Notice;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * Created on 2019/9/6
 * Title: TODO
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 上海铸乾信息科技有限公司
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@ApiService
public class AdminNoticeApi {

    @Autowired
    private com.tzj.collect.core.service.NoticeService noticeService;
//
    /**
     * 保存消息
     * @param adminBean
     * @return
     */
    @Api(name = "admin.notice.saveNotice", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map saveNotice(AdminNoticeBean adminBean) throws Exception{
        Map<String,Object> map = new HashMap();
        if(StringUtils.isEmpty(adminBean.getContent())){
            throw new ApiException("内容不能为空");
        }

        if(StringUtils.isEmpty(adminBean.getTitle())){
            throw new ApiException("标题不能为空");
        }
        if(null == adminBean.getStartDate()){
            throw new ApiException("开始时间不能为空");
        }
        Notice entity = new Notice();

        entity.setContent(adminBean.getContent());
        entity.setTitle(adminBean.getTitle());
        entity.setStartDate(adminBean.getStartDate());
        noticeService.insert(entity);
        map.put("result","success");
        return  map;
    }


    /**
     * 消息列表
     * @param adminBean
     * @return
     */
    @Api(name = "admin.notice.listNotice", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public List<Notice> listNotice(AdminNoticeBean adminBean){
        return noticeService.listNotice(adminBean);


    }


    /**
     * 审核消息
     * @param adminBean
     * @return
     */
    @Api(name = "admin.notice.auditNotice", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> auditNotice(AdminNoticeBean adminBean){
        Map map = new HashMap();
        try{
            noticeService.auditNotice(adminBean.getId());
        }catch (RuntimeException exception){
            map.put("errMessage",exception.getMessage());
        }
        map.put("result","success");
        return map;
    }


    /**
     * 删除消息
     * @param adminBean
     * @return
     */
    @Api(name = "admin.notice.delNotice", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Map<String,Object> delNotice(AdminNoticeBean adminBean){
        Map map = new HashMap();
        try{
            noticeService.delNotice(adminBean.getId());
        }catch (RuntimeException exception){
            map.put("errMessage",exception.getMessage());
        }
        map.put("result","success");
        return map;
    }

}
