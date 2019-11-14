package com.tzj.collect.common.constant;

import com.baomidou.mybatisplus.enums.IEnum;
import java.io.Serializable;

public enum XingeMessageCode implements IEnum
{
    order(0),
    cancelOrder(4);

    private int value;

    XingeMessageCode(final int value) {
        this.value = value;
    }

    public Serializable getValue() {
        return this.value;
    }
}