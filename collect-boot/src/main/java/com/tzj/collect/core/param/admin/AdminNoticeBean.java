package com.tzj.collect.core.param.admin;

import com.tzj.collect.entity.Notice;
import lombok.Data;

import java.util.Date;

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
@Data
public class AdminNoticeBean {
    private Long id;

    //内容
    private String content;

    //标题
    private String title;

    //状态
    private Boolean audit = Boolean.FALSE;

    //消息类型
    private Notice.Type type = Notice.Type.CAROUSE;

    //开始日期起始时间
    private Date startBeginDate;

    //开始日期起始时间
    private Date startEndDate;

    //生效时间
    private Date startDate;

}
