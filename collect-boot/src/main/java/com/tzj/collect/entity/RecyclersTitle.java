package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 回收人员回收类型关联表
 */
@TableName("sb_recyclers_title")
public class RecyclersTitle extends  DataEntity<Long>{

    private Long id ;

    private Integer recycleId;

    private String titleName;

    private Integer titleId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Integer getRecycleId() {
        return recycleId;
    }

    public void setRecycleId(Integer recycleId) {
        this.recycleId = recycleId;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }
}
