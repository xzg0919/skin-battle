package com.tzj.fish.common.mqtt.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by alvin on 17-3-29.
 */
public class Tools {
    public static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("test.properties"));
        } catch (IOException e) {
        }
        return properties;
    }

    /**
     * 计算签名，参数分别是参数对以及密钥
     *
     * @param requestParams 参数对，即参与计算签名的参数
     * @param secretKey 密钥
     * @return 签名字符串
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String doHttpSignature(Map<String, String> requestParams,
        String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        List<String> paramList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            paramList.add(entry.getKey() + "=" + entry.getValue());
        }
        Collections.sort(paramList);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < paramList.size(); i++) {
            if (i > 0) {
                sb.append('&');
            }
            sb.append(paramList.get(i));
        }
        return macSignature(sb.toString(), secretKey);
    }

    /**
     * @param text 要签名的文本
     * @param secretKey 阿里云MQ secretKey
     * @return 加密后的字符串
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public static String macSignature(String text,
        String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        Charset charset = Charset.forName("UTF-8");
        String algorithm = "HmacSHA1";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey.getBytes(charset), algorithm));
        byte[] bytes = mac.doFinal(text.getBytes(charset));
        return new String(Base64.encodeBase64(bytes), charset);
    }

    /**
     * 创建HTTPS 客户端
     */
    private static CloseableHttpClient httpClient = null;

    public static CloseableHttpClient getHttpsClient() throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        if (httpClient != null) {
            return httpClient;
        }
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        };
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[] {xtm}, null);
        SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", scsf).build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000).setConnectTimeout(5000).setSocketTimeout(5000).build();
        PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(sfr);
        httpClient = HttpClientBuilder.create().setConnectionManager(pcm).setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

    /**
     * 发起Https Get请求，并得到返回的JSON响应
     *
     * @param url 接口Url
     * @param params 参数u对
     * @return
     * @throws IOException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static JSONObject httpsGet(String url,
                                      Map<String, String> params) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient client = Tools.getHttpsClient();
        JSONObject jsonResult = null;
        //发送get请求
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        String paramUrl = URLEncodedUtils.format(urlParameters, Charset.forName("UTF-8"));
        HttpGet request = new HttpGet(url + "?" + paramUrl);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                jsonResult = JSON.parseObject(strResult);
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return jsonResult;
    }

    /**
     * 工具方法，发送一个http post请求，并尝试将响应转换为JSON
     *
     * @param url 请求的方法名url
     * @param params 参数表
     * @return 如果请求成功则返回JSON, 否则抛异常或者返回空
     * @throws IOException
     */
    public static JSONObject httpsPost(String url,
                                       Map<String, String> params) throws IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        JSONObject jsonResult = null;
        //发送get请求
        CloseableHttpClient client = getHttpsClient();
        HttpPost request = new HttpPost(url);
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
        request.setEntity(postParams);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                jsonResult = JSON.parseObject(strResult);
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return jsonResult;
    }

//    public static void main(String[] args) {
//        JSONObject object = new JSONObject();
//        object.put("order", "true");//设置顺序发送的标记
//        System.out.println(object.toJSONString());
//    }

    private static final String applyTokenUrl = "/token/apply";
    private static final String revokeTokenUrl = "/token/revoke";

    public static void main(String[] args)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException,
            KeyStoreException, InvalidKeyException {
        List<String> resource = new ArrayList<String>();
        resource.add("sgmark/equipment_code(as_SH0066)");
        String token = applyToken("https://mqauth.aliyuncs.com", "LTAI4FfqJ3H3SjZ3FVGyWa4T", "Ln1DbAaG5ljy6i3I5FbLikjukmpM27",
                resource, "R,W", 2592000000L, "post-cn-v0h1dockd17");
        System.out.println(token);
    }

    /**
     * 申请 Token 接口，具体参数参考链接https://help.aliyun.com/document_detail/54276.html?spm=a2c4g.11186623.6.562.f12033f5ay6nu5
     *
     * @param apiUrl token 服务器地址，参考文档设置正确的地址
     * @param accessKey 账号 AccessKey，由控制台获取
     * @param secretKey 账号 SecretKey，由控制台获取
     * @param topics 申请的 topic 列表
     * @param action Token类型
     * @param expireTime Token 过期的时间戳
     * @param instanceId MQ4IoT 实例 Id
     * @return 如果申请成功则返回 token 内容
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static String applyToken(String apiUrl, String accessKey, String secretKey, List<String> topics,
                                    String action,
                                    long expireTime,
                                    String instanceId) throws InvalidKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        Map<String, String> paramMap = new HashMap<String, String>();
        Collections.sort(topics);
        StringBuilder builder = new StringBuilder();
        for (String topic : topics) {
            builder.append(topic).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        paramMap.put("resources", builder.toString());
        paramMap.put("actions", action);
        paramMap.put("serviceName", "mq");
        paramMap.put("expireTime", String.valueOf(System.currentTimeMillis() + expireTime));
        paramMap.put("instanceId", instanceId);
        String signature = Tools.doHttpSignature(paramMap, secretKey);
        paramMap.put("proxyType", "MQTT");
        paramMap.put("accessKey", accessKey);
        paramMap.put("signature", signature);
        JSONObject object = Tools.httpsPost(apiUrl + applyTokenUrl, paramMap);
        if (object != null) {
            return (String) object.get("tokenData");
        }
        return null;
    }

    /**
     * 提前注销 token，一般在 token 泄露出现安全问题时，提前禁用特定的客户端
     *
     * @param apiUrl token 服务器地址，参考文档设置正确的地址
     * @param accessKey 账号 AccessKey，由控制台获取
     * @param secretKey 账号 SecretKey，由控制台获取
     * @param token 禁用的 token 内容
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws KeyManagementException
     */
    public static void revokeToken(String apiUrl, String accessKey, String secretKey,
                                   String token) throws InvalidKeyException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("token", token);
        String signature = Tools.doHttpSignature(paramMap, secretKey);
        paramMap.put("signature", signature);
        paramMap.put("accessKey", accessKey);
        JSONObject object = Tools.httpsPost(apiUrl + revokeTokenUrl, paramMap);
    }

}
