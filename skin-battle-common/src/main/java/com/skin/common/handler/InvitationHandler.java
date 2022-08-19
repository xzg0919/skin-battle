package com.skin.common.handler;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/18 15:32
 * @Description:
 */
public class InvitationHandler {


    public static Map<Integer, BigDecimal> invitationLevelMap = new TreeMap<>();
    public static Map<Integer, Integer> invitationRewardMap = new TreeMap<>();

    static {
        invitationLevelMap.put(1, new BigDecimal("0"));
        invitationLevelMap.put(2, new BigDecimal("1000"));
        invitationLevelMap.put(3, new BigDecimal("5000"));
        invitationLevelMap.put(4, new BigDecimal("20000"));
        invitationLevelMap.put(5, new BigDecimal("50000"));
    }


    static {
        invitationRewardMap.put(1, 3);
        invitationRewardMap.put(2, 4);
        invitationRewardMap.put(3, 5);
        invitationRewardMap.put(4, 6);
        invitationRewardMap.put(5, 7);
    }


    public int getReward(BigDecimal point) {
        int reward = 0;
        for (Map.Entry<Integer, BigDecimal> entry : invitationLevelMap.entrySet()) {
            if (point.divide(entry.getValue()).compareTo(new BigDecimal("1")) >= 0) {
                reward = entry.getKey();
            } else {
                break;
            }
        }
        return invitationRewardMap.get(reward);
    }


}
