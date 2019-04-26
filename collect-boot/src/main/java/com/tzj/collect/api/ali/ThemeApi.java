package com.tzj.collect.api.ali;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.ProductBean;
import com.tzj.collect.entity.XcxTheme;
import com.tzj.collect.entity.XcxThemeContent;
import com.tzj.collect.service.XcxThemeContentService;
import com.tzj.collect.service.XcxThemeService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 标题相关接口
 * @author wangcan
 *
 */
@ApiService
public class ThemeApi {
    @Autowired
    private XcxThemeService xcxThemeService;
    @Autowired
    private XcxThemeContentService xcxThemeContentService;

    /**
     * 返回小程序首页标题列表
     * @author 王灿
     * @param
     */
    @Api(name = "theme.getXcxThemeContent", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getXcxThemeContent() {
        List<XcxTheme> xcxThemes = xcxThemeService.selectList(new EntityWrapper<XcxTheme>().eq("type", 0).eq("del_flag", 0));
        xcxThemes.stream().forEach(xcxTheme -> {
            List<XcxThemeContent> xcxThemeContents = xcxThemeContentService.selectList(new EntityWrapper<XcxThemeContent>().eq("theme_id", xcxTheme.getId()).eq("del_flag", 0));
            xcxTheme.setContentList(xcxThemeContents);
        });
        return xcxThemes;
    }
    /**
     * 返回小程序首页蚂蚁深林文案
     * @author 王灿
     * @param
     */
    @Api(name = "theme.getXcxThemeMysl", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getXcxThemeMysl() {
        List<XcxTheme> xcxThemes = xcxThemeService.selectList(new EntityWrapper<XcxTheme>().eq("type", 1).eq("del_flag", 0));
        xcxThemes.stream().forEach(xcxTheme -> {
            List<XcxThemeContent> xcxThemeContents = xcxThemeContentService.selectList(new EntityWrapper<XcxThemeContent>().eq("theme_id", xcxTheme.getId()).eq("del_flag", 0));
            xcxTheme.setContentList(xcxThemeContents);
        });
        return xcxThemes;
    }

}
