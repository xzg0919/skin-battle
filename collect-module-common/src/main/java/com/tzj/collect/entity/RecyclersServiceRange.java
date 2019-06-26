package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @回收经理服务范围表
 * @Company: 上海挺之军科技有限公司
 * @Department：研发部
 * @author:[王灿]
 */
@TableName("sb_recyclers_service_range")
public class RecyclersServiceRange extends DataEntity<Integer>{
    private Integer id;// 主键
    /**
     * 回收经理Id
     */
    private Integer recyclersId;
    /**
     * 所属的区域id目前到街道
     */
    private Integer areaId;
    /**
     * 所属区域的父亲节点
     */
    private String areaParentsId;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecyclersId() {
        return recyclersId;
    }

    public void setRecyclersId(Integer recyclersId) {
        this.recyclersId = recyclersId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaParentsId() {
        return areaParentsId;
    }

    public void setAreaParentsId(String areaParentsId) {
        this.areaParentsId = areaParentsId;
    }
}
