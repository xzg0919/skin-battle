package com.tzj.collect.api.lexicon.result;

import com.tzj.collect.entity.DailyLexicon;
import lombok.Data;

/**
 * @author sgmark
 * @create 2019-08-14 16:24
 **/
@Data
public class LexResult {
    private String name;

    private DailyLexicon.LexType lexType;

    private DailyLexicon.LexType userLexType;

    private String lexTypeName;

    public String getLexTypeName() {
        return lexType.getName();
    }
    public String getUserLexTypeName() {
        if (null != userLexType) {
            return userLexType.getName();
        }else {
            return "";
        }
    }
}
