package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.admin.AdminNoticeBean;
import com.tzj.collect.entity.Notice;

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
public interface NoticeService extends IService<Notice> {

    void delNotice(Long id);

    void auditNotice(Long id);

    @DS("slave")
    List<Notice> getVaildNotices();

    @DS("slave")
    List<Notice> listNotice(AdminNoticeBean adminNoticeBean);

}
