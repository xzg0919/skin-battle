package com.tzj.collect.core.result.flcx;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author sgmark
 * @create 2019-06-19 10:39
 **/
@Data
public class FlcxResult implements Serializable {

    private Long id;

    private String recover;//是否回收

    private String lexicon;//查询结果关键字

    private Long lexiconId;//关键字id lexiconId

    private String parentName;//大分类名称

    private String engName;//英文名称

    private List<String> imgUrl;//子分类图片地址

    private List<Map<String, String>> imgUrlAfter;//子分类图片地址(改)

    private List<Map<String, String>> imgUrlAfterSpecial;//子分类图片地址(特殊)

    private String describe;//上描述

    private List<Map<String, Object>> nameLists;//小分类名称地址

    private String remarks;//后描述

    private List<String> remarksList;//描述拆分

    private String isHarmful;//是否有害；1：有害

}