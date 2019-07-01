package com.tzj.collect.module.common.shard;

/**
 * 分表规则
 */
public class ShardTableHelper {
    public static String getTableNameByModeling(String tableName,Long shardSource,int modeling){
        long num=shardSource%modeling;
        StringBuffer sb=new StringBuffer();
        sb.append(tableName).append("_");
        return sb.toString();
    }
}
