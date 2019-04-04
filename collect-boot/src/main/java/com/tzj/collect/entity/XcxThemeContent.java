package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @author Administrator
 * 小程序主题内容表
 *
 */
@TableName("sb_xcx_theme_content")
public class XcxThemeContent extends DataEntity<Long> {
    private Long id;
    /**
     * 主题Id
     */
    private Integer  themeId;
    /**
     * 主题内容
     */
    private String content;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
