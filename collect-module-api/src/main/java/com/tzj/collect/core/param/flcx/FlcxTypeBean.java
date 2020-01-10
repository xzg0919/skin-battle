package com.tzj.collect.core.param.flcx;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.io.Serializable;

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
public class FlcxTypeBean implements Serializable {

    //层级
    private Integer level;

    //所属分类
    private Long parentId ;
}
