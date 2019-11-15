package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.function.BooleanSupplier;

/**
 * Created on 2019/9/5
 * Title: TODO
 * Description: TODO
 * Copyright: Copyright (c) 2019
 * Company: 上海铸乾信息科技有限公司
 * Department:研发部
 *
 * @author:Michael_Wang
 * @Version 1.0
 **/
@TableName("sb_notice")
@Data
public class Notice extends DataEntity<Long> {
    private Long id;

    //内容
    private String content;

    //状态
    private Boolean audit = Boolean.FALSE;

    //消息类型
    private Type type = Type.CAROUSE;

    //开始日期
    private Date startDate;

    //标题
    private String title;

    public enum Type implements IEnum {
        CAROUSE(0);//暂时只有一种为首页轮播通知
        private int value;

        Type(final int value) {
            this.value = value;
        }

        public Serializable getValue() {
            return this.value;
        }
    }
}
