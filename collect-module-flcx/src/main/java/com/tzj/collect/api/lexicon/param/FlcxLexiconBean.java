package com.tzj.collect.api.lexicon.param;

import lombok.Data;

import java.util.List;

/**
 * Created on 2019/7/8
 * Title: 分类查询bean
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 铸乾信息
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@Data
public class FlcxLexiconBean {
    private Long id;
    private String  name;

    private String recover = "0";//平台回收与否（1:回收，0:不回收）,初始值为0

    //所属类型 可以几种类型
    private List<Long> typeIds;



}
