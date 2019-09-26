package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 每日答答答词库
 *
 * @author sgmark
 * @create 2019-08-09 11:36
 **/
@TableName("daily_lexicon")
@Data
public class DailyLexicon extends DataEntity<Long> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    @TableField(value = "name_")
    private String name;

    private LexType typeId;//类型id

    private Integer depth;//难易程度


    public enum LexType implements IEnum {
        BEFORE(0, "占位符"),// 占位符
        DRY(1, "干垃圾/其他垃圾"),   	 //干垃圾
        KITCHEN(2, "厨余垃圾/易腐垃圾/湿垃圾"),		//湿垃圾
        RECYCLABLE(3, "可回收物"),	//可回收物
        HARMFUL(4, "有害垃圾"),		//有害垃圾
        OTHER(5, "装修垃圾"); // 装修垃圾
        private int value;
        private String name;

        LexType(final int value, final String name) {
            this.value = value;
            this.name = name;
        }

        public Serializable getValue() {
            return this.value;
        }
        public String  getName() {
            return this.name;
        }

        public Map getInfo(){
            Map<String, Object> map = new HashMap<>();
            map.put("name", getName());
            map.put("value", name());
            return map;
        }
    }
}
