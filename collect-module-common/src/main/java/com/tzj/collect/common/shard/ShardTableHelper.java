package com.tzj.collect.common.shard;

import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
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
    public static String getTableNameByModeling(String tableName,String shardSource,int modeling){
        if(  StringUtils.isBlank(shardSource)){
            throw new ApiException("用户id不能为空！");
        }

        //如果不是2088开头的表名直接返回40
        if(!shardSource.startsWith("2088")){
            return tableName+"40";
        }

        Long  aliUserId = Long.parseLong(shardSource);
        long num=aliUserId%modeling;
        StringBuffer sb=new StringBuffer();
        if (num <= 9){
            sb.append(tableName).append(0).append(num);
        }else {
            sb.append(tableName).append(num);
        }
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
