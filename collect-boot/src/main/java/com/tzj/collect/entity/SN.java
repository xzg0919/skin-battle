package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 * 例子
 *
 * @Author 胡方明（12795880@qq.com）
 **/
@TableName("xx_sn")
public class SN extends DataEntity<Long>{
    private Long id;
    private Long lastValue;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastValue() {
        return lastValue;
    }

    public void setLastValue(Long lastValue) {
        this.lastValue = lastValue;
    }
}
