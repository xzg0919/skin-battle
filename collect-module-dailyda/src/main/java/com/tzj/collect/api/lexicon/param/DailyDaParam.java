package com.tzj.collect.api.lexicon.param;

import com.tzj.collect.entity.DailyLexicon;
import lombok.Data;

/**
 * @author sgmark
 * @create 2019-08-13 9:40
 **/
@Data
public class DailyDaParam {

    private String uuId;//uuid

    private Integer depth;//难易程度

    private String lexName;//题目名称

    private DailyLexicon.LexType lexType;//答题类型

    private String aliUserId;//阿里用户id
}
