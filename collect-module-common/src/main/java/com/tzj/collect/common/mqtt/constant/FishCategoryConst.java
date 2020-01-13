package com.tzj.collect.common.mqtt.constant;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * 咸鱼对接类目信息
 *
 * @author sgmark
 * @create 2020-01-03 13:36
 **/
public class FishCategoryConst {
    /**
     * 咸鱼，一级类型(以code对应)
     * @author sgmark@aliyun.com
     * @date 2019/3/30 0030
     * @param
     * @return
     */
    public enum ParentType implements IEnum{
        WASTE_PAPER("001"),   	 //纸类
        WASTE_PLASTIC("002"),		//塑料类
        WASTE_GLASS("003"),	//玻璃类
        WASTE_TEXTILE("004"),		//纺织类
        SMALL_APPLIANCES("005"),	//小家电
        WASTE_METAL("006"),	//金属类
        OTHER("007"); //其他类型

        private String value;

        ParentType(final String value) {
            this.value = value;
        }

        @Override
        public Serializable getValue() {
            return this.value;
        }
    }
    /**
     * 咸鱼，二级类型(以code对应)
     * @author sgmark@aliyun.com
     * @date 2019/3/30 0030
     * @param
     * @return
     */
    public enum SecondType implements IEnum {
        //纸类(PAPER)
        BOOK("001", "0011"),   	 //书本
        CARTON("001", "0015"),   	 //纸板箱
        MAGAZINE("001", "0012"),   	 //杂志
        NEWSPAPER("001", "0016"),   	 //报纸
        TETRA_PACKS("001", "0017"),		//包装盒
        UNCONTAMINATED_PAPER("001", "0018"),	//杂纸

        //塑料类(PLASTIC)
        BEVERAGE_BOTTLES("002", "0021"),		//饮料瓶
        PLASTIC_TOY("002", "0022"),	//玩具
        JERRY_CANS("002", "0023"), //油壶油桶
        CLEANING_PRODUCT_BOTTLES("002", "0023"), //洗化品瓶（冲洗干净）
        PLASTIC_PIPE("002", "0024"),	//塑料管件
        PLASTIC_FOAM("002", "0024"),	//泡沫塑料
        UNCONTAMINATED_PLASTIC("002", "0025"),	//杂塑料

        //玻璃类(GLASS)
        GLASS_BOTTLE("003", "0031"),//玻璃瓶
        WINDOW_GLASS("003", "0031"),//门窗玻璃
        CULLET("003", "0033"),//碎玻璃
        THE_OTHER_GLASSWARE("003", "0034"),//其他玻璃制品

        //纺织类(FABRIC)
        SPRING_SUMMER_CLOTHING("004", "0041"),//春夏服装
        AUTUMN_WINTER_CLOTHING("004", "0042"),//秋冬服装
        CHILDREN_CLOTHING("004", "0044"),//儿童服装
        CAPS("004", "0047"),//帽子
        SHOES("004", "0043"),//鞋子
        BAGS("004", "0045"),//包袋
        FABRIC_LINING("004", "0046"),//织物或内胆
        THE_OTHERS_TEXTILE("004", "0048"),//其他

        //金属类(METAL)
        CANS("006", "0061"),//易拉罐
        ALUMINUM_WINDOW_FRAME("006", "0061"),//铝合金门窗框架
        METAL_SECURITY_DOOR("006", "0061"),//金属防盗门
        BIKE_WHEELCHAIR("006", "0061"),//自行车、轮椅
        POWER_LINE_POWER_BOARD("006", "0065"),	//电源线
        THE_OTHERS_META("006","0066"),//其他废旧金属

        //小家电 SMALL_APPLIANCES
        PLASTIC_SMALL_APPLIANCES("005","0051"),//塑料小家电
        METAL_SMALL_APPLIANCES("005","0052"),//金属小家电

        //其他类型(OTHER)
        OTHERS("007", "0071");	//其他类型(暂存六废)
        private String key;

        private String value;

        SecondType(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        public static String getValueByKey(String key) {
            SecondType[] enums = SecondType.values();
            for (int i = 0; i < enums.length; i++) {
                if (enums[i].getKey().equals(key)) {
                    return enums[i].getValue();
                }
            }
            return "";
        }
    }
}
