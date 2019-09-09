package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.AdminMapper;
import com.tzj.collect.core.mapper.NoticeMapper;
import com.tzj.collect.core.param.admin.AdminNoticeBean;
import com.tzj.collect.core.service.NoticeService;
import com.tzj.collect.entity.Notice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

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
@Service
@Transactional(readOnly=true)
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 审核消息
     * @param id
     */
    @Transactional(readOnly=false)
    public void auditNotice(Long id){
        if (null == id){
            throw new RuntimeException("id不能为空");
        }
        Notice notice = noticeMapper.selectById(id);
        if (null == notice){
            throw new RuntimeException("消息不存在");
        }
        notice.setAudit(true);
        noticeMapper.updateById(notice);
    }

    /**
     * 根据条件查询
     * @return
     */
    public List<Notice> listNotice(AdminNoticeBean adminBean){
        EntityWrapper wrapper = new EntityWrapper<Notice>();
        if(adminBean.getAudit()!= null ){
            wrapper.eq("audit",adminBean.getAudit());
        }
        if(StringUtils.isNotEmpty(adminBean.getContent())){
            wrapper.like("content",adminBean.getContent());
        }
        //开始时间大于查询开始时间
        if(adminBean.getStartBeginDate() != null){
            wrapper.gt("start_date",adminBean.getStartBeginDate());
        }
        if(adminBean.getStartEndDate() != null){
            wrapper.lt("start_date",adminBean.getStartEndDate());
        }
        wrapper.eq("del_flag","0");
        return noticeMapper.selectList(wrapper);
    }

    /**
     * 获取所有的有效的消息
     * @return
     */
    public List<Notice> getVaildNotices(){
        EntityWrapper<Notice> wrapper = new EntityWrapper<Notice>();
        wrapper.eq("del_flag","0");
        wrapper.eq("audit",true);
        wrapper.lt("start_date", Calendar.getInstance().getTime());
        return noticeMapper.selectList(wrapper);

    }
}
