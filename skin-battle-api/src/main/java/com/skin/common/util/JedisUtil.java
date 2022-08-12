package com.skin.common.util;

import org.springframework.scheduling.annotation.Async;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;

/**
 * @author sgmark
 * @create 2019-09-11 10:00
 **/
public class JedisUtil {

    /**
     * 从redis中读取存入数据(存入对象需序列化)
     *
     * @param
     * @author sgmark@aliyun.com
     * @date 2018/10/18 0018
     * @return
     */
    public static class SaveOrGetFromRedis {
        @Async
        public void saveInRedis(String uuId, Object value, Integer expire, JedisPool jedisPool, Integer index) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.select(index);
                jedis.set(uuId.getBytes(), this.serialize(value));
                jedis.expire(uuId, expire);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //pool.returnResource(jedis);
                jedis.close();//Jedis3.0版本后建议用close替换
            }
        }

        public Object getFromRedis(String uuId, JedisPool jedisPool, Integer index) {
            Jedis jedis = null;
            Object code = null;
            try {
                jedis = jedisPool.getResource();
                jedis.select(index);
                if (null ==jedis.get(uuId.getBytes()))
                {
                    return null;
                }
                code = this.unSerialize(jedis.get(uuId.getBytes()));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                jedis.close();
            }
            return code;
        }

        /**
         * 存入对象序列化
         *
         * @param
         * @return
         * @author sgmark@aliyun.com
         * @date 2018/10/30 0030
         */
        public byte[] serialize(Object obj) {
            ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            try {
                // 序列化
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                byte[] byteArray = baos.toByteArray();
                return byteArray;
            } catch (IOException e) {
            }
            return null;
        }

        /**
         * 反序列化
         *
         * @param
         * @return
         * @author sgmark@aliyun.com
         * @date 2018/10/30 0030
         */
        public Object unSerialize(byte[] bytes) {
            ByteArrayInputStream bais = null;
            try {
                // 反序列化为对象
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
            }
            return null;
        }
    }
}
