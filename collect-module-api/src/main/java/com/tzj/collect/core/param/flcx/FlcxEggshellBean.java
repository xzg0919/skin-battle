package com.tzj.collect.core.param.flcx;

import lombok.Data;

/**
 * Created on 2019/7/5
 * Title: TODO
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 铸乾信息
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@Data
public class FlcxEggshellBean {
    private Long id;

    /**
     * 分类名称
     */
    private String  lexicon;

    //彩蛋描述
    private String describe;

    /**
     * 分页信息
     */
    private PageBean pageBean;

}
