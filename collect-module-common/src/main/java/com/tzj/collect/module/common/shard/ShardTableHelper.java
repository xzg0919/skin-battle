package com.tzj.collect.module.common.shard;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * 分表规则
 */
public class ShardTableHelper {
    /**
     * 根据取模做分表
     * @param tableName
     * @param shardSource
     * @param modeling
     * @return
     */
    public static String getTableNameByModeling(String tableName,Long shardSource,int modeling){
        long num=shardSource%modeling + 1;
        String numTable = num >= 10 ? String.valueOf(num):"0"+ String.valueOf(num);
        StringBuffer sb=new StringBuffer();
        sb.append(tableName).append("_").append(numTable);
        return sb.toString();
    }

    /**
     * 根据年月日做分表
     * @param tableName
     * @param date
     * @return
     */
    public static String getTableNameByDay(String tableName, Date date){
        String dateString= DateFormatUtils.format(new Date(), "yyyyMMdd");
        StringBuffer sb=new StringBuffer();
        sb.append(tableName).append("_").append(dateString);
        return sb.toString();
    }


}
