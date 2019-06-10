package api.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

public class SignUtil {
    private static Logger logger = LoggerFactory.getLogger(SignUtil.class);

    private static final String MD5 = "MD5";
    private static final String ZERO = "0";





    /**
     * 构建签名
     *
     * @param paramsMap
     *            参数
     * @param secret
     *            密钥
     * @return
     * @throws IOException
     */
    public static String buildSign(Map<String, ?> paramsMap, String secret) throws IOException {
        Set<String> keySet = paramsMap.keySet();
        List<String> paramNames = new ArrayList<String>(keySet);
        Collections.sort(paramNames);
        List<String> list = new ArrayList<>();
        for (String paramName : paramNames) {
            String key = paramName;
            String value = paramsMap.get(paramName).toString();
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                list.add(key + "=" + (value != null ? value : ""));
            }
        }
        String source=StringUtils.join(list, "&")+secret;
        logger.info("客户端签名前："+source);
        String md5=md5(source);
        logger.info("客户端签名后："+md5);
        return md5;
    }

    /**
     * 生成md5,全部大写
     *
     * @param message
     * @return
     */
    public static String md5(String message) {
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance(MD5);

            // 2 将消息变成byte数组
            byte[] input = message.getBytes();

            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);

            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            return byte2hex(buff);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 二进制转十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append(ZERO);
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
