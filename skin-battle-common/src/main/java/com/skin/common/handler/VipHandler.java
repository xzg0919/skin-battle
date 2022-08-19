package com.skin.common.handler;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/18 14:37
 * @Description:
 */
public class VipHandler {


   public  static Map<Integer, BigDecimal> vipLevelMap = new TreeMap<>();
    public  static Map<Integer, BigDecimal> vipAwardMap = new TreeMap<>();
    static {
        vipLevelMap.put(1, new BigDecimal("50"));
        vipLevelMap.put(2, new BigDecimal("200"));
        vipLevelMap.put(3, new BigDecimal("500"));
        vipLevelMap.put(4, new BigDecimal("1000"));
        vipLevelMap.put(5, new BigDecimal("2000"));
        vipLevelMap.put(6, new BigDecimal("3500"));
        vipLevelMap.put(7, new BigDecimal("5000"));
        vipLevelMap.put(8, new BigDecimal("7500"));
        vipLevelMap.put(9, new BigDecimal("10000"));
        vipLevelMap.put(10, new BigDecimal("20000"));
    }


    static {
        vipAwardMap.put(1, new BigDecimal("5"));
        vipAwardMap.put(2, new BigDecimal("10"));
        vipAwardMap.put(3, new BigDecimal("35"));
        vipAwardMap.put(4, new BigDecimal("58"));
        vipAwardMap.put(5, new BigDecimal("106"));
        vipAwardMap.put(6, new BigDecimal("248"));
        vipAwardMap.put(7, new BigDecimal("387"));
        vipAwardMap.put(8, new BigDecimal("466"));
        vipAwardMap.put(9, new BigDecimal("888"));
        vipAwardMap.put(10, new BigDecimal("1877"));
    }


    public static Integer getVip(BigDecimal point) {
        Integer vip = 0;
        for (Map.Entry<Integer, BigDecimal> entry : vipLevelMap.entrySet()) {
            if (point.divide(entry.getValue()).compareTo(new BigDecimal("1")) >=0) {
                vip = entry.getKey();
            } else {
                break;
            }
        }
        return vip;
    }
}
