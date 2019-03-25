package com.tzj.collect.entity;


/**
 *
 * 小程序主题表
 *
 * @Author 王灿
 **/

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * 小程序主题表
 *
 */
@TableName("sb_xcx_theme")
public class XcxTheme  extends DataEntity<Long> {
    private Long id;
    /**
     * 内容
     */
    private String theme;
    /**
     * 主题类型
     */
    private ThemeType type;

    /**
     * 仅用于页面返回
     */
    @TableField(exist = false)
    private List<XcxThemeContent> contentList;

    public List<XcxThemeContent> getContentList() {
        return contentList;
    }

    public void setContentList(List<XcxThemeContent> contentList) {
        this.contentList = contentList;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public ThemeType getType() {
        return type;
    }

    public void setType(ThemeType type) {
        this.type = type;
    }

    public enum ThemeType implements IEnum {
        XCXHOME(0); // 小程序首页主题
        private int value;

        ThemeType(final int value) {
            this.value = value;
        }

        public Serializable getValue() {
            return this.value;
        }
    }
}
