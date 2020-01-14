package com.tzj.green.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * httputils
 */
public class HttpUtil {
	
	private static Logger log = Logger.getLogger(HttpUtil.class);
	
	
	public static final String charset="UTF-8";
	public static final String JSON_CONTENT_TYPE="application/json";
	public static final String FORM_CONTENT_TYPE="application/x-www-form-urlencoded";

	/**
	 * 发送HTTP GET请求
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String sendByGet(String url) throws Exception {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		String resp = null;
		try {
			// 获取默认的httpclient
			client = HttpClients.createDefault();
			// 创建get请求
			HttpGet httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			if(response==null){
				return null;
			}
			HttpEntity entity = response.getEntity();
			resp = EntityUtils.toString(entity);
			return resp;
		} finally {
			response.close();
			client.close();
		}
	}
	
	/**
	 * 发送不带参数的HTTP POST请求
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String sendByPost(String url) throws Exception {
		return sendByPost(url, null);
	}
	
	/**
	 * 发送带 表单参数的HTTP POST请求
	 * @param url
	 * @param params  表单参数
	 * @return
	 * @throws Exception
	 */
	public static String sendByPost(String url, List<NameValuePair> params) throws Exception{
		return sendPost(url,params,null,null);
	}
	
	/**
	 * 发送JSON参数的HTTP POST请求
	 * @param url
	 * @param jsonParams   JSONObject参数
	 * @return
	 * @throws Exception
	 */
	public static String sendJsonByPost(String url, JSONObject jsonParams)throws Exception{
		return sendPost(url,null,jsonParams,JSON_CONTENT_TYPE);
	}
	
	
	public static String sendJsonByPost(String url)throws Exception{
		return sendPost(url,null,null,JSON_CONTENT_TYPE);
	}

	/**
	 * 发送HTTP POST请求
	 * @param url  
	 * @param params   普通的表单的参数
	 * @param jsonParams   JSON参数
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	private static String sendPost(String url, List<NameValuePair> params, JSONObject jsonParams, String contentType)
			throws Exception {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		String resp = null;
		try {
			//获取默认的httpclient
			client = HttpClients.createDefault();
			// 创建Post请求
			HttpPost httpPost = new HttpPost(url);
			if(StringUtils.isNotBlank(contentType)){
				httpPost.addHeader("Content-Type", contentType);
			}
			// 创建请求参数的List
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();

			if (params != null) {
				// 添加参数
				for (int i = 0; i < params.size(); i++) {
					formParams.add(params.get(i));
				}
				UrlEncodedFormEntity uefEntity;
				uefEntity = new UrlEncodedFormEntity(formParams,charset);
				httpPost.setEntity(uefEntity);
			}
			
			if(jsonParams!=null){
				StringEntity entity = new StringEntity(jsonParams.toString(),"UTF-8");
	            entity.setContentEncoding("UTF-8");
	            entity.setContentType("application/json");
	            httpPost.setEntity(entity);
			}

			// 执行http请求，获取response
			response = client.execute(httpPost);
			if(response==null){
				return null;
			}
			HttpEntity entity = response.getEntity();
			log.info(resp);
			resp = EntityUtils.toString(entity);
			//System.out.println("---------"+resp);
			/*System.out.println(resp);
			String li="";
			com.alibaba.fastjson.JSONObject obj =com.alibaba.fastjson.JSONObject.parseObject(resp);
			String  array=obj.getString("products");
			JSONArray ar=new JSONArray(array);
			for (int i = 0; i < ar.length(); i++) {
				JSONObject o=ar.getJSONObject(i);
				li+=o.getString("prodCode")+",";
			}
			System.out.println(li);*/
			return resp;
		} finally {
			if(response!=null){
				response.close();
			}
			if(response!=null){
				client.close();
			}
		}
	}
	
	/**
	 * 发送HTTP POST请求
	 * @param url
	 * @param jsonParams   JSON参数
	 * @return
	 * @throws Exception
	 */
	public static String sendPostUrlDecode(String url,JSONObject jsonParams)
			throws Exception {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		String resp = null;
		try {
			//获取默认的httpclient
			client = HttpClients.createDefault();
			// 创建Post请求
			HttpPost httpPost = new HttpPost(url);
				httpPost.addHeader("Content-Type", JSON_CONTENT_TYPE); 

			
			if(jsonParams!=null){
				StringEntity entity = new StringEntity(jsonParams.toString());
	            entity.setContentEncoding("UTF-8");    
	            entity.setContentType("application/json");    
	            httpPost.setEntity(entity);  
			}
			
			// 执行http请求，获取response
			response = client.execute(httpPost);
			if(response==null){
				return null;
			}
			HttpEntity entity = response.getEntity();
			log.info(resp);
			resp = EntityUtils.toString(entity);
			System.out.println("---------"+resp);
			return resp;
		} finally {
			if(response!=null){
				response.close();
			}
			if(response!=null){
				client.close();
			}
		}
	}
	
	
	/**
	 * string字符串转换为json对象
	 * @param str
	 * @return
	 * @throws JSONException
	 */
	public  static JSONObject stringToJsonObj(String str) throws JSONException{
		JSONObject obj=new JSONObject(str);
		return obj;
	}
	
	/**
	 *  string字符串转换为json数组
	 * @param str
	 * @return
	 * @throws JSONException
	 */
	public  static JSONArray stringTOJsonArray(String str) throws JSONException {
		JSONArray arra=new JSONArray(str);
		return arra;
	}
	
	public static StringBuffer receiveData(HttpServletRequest request) throws Exception {
        String inputLine;
        // 接收到的数据
        StringBuffer data = new StringBuffer();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                data.append(URLDecoder.decode(inputLine, "UTF-8"));
            }
        } catch (IOException e) {
        } finally {
            if (null != in) {
                in.close();
            }
        }
        return data;
    }
    
  /** 
   * 定义编码格式 UTF-8 
   */  
  public static final String URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";  
    
  /** 
   * 定义编码格式 GBK 
   */  
  public static final String URL_PARAM_DECODECHARSET_GBK = "GBK";  
    
  private static final String URL_PARAM_CONNECT_FLAG = "&";  
    
  private static final String EMPTY = "";  



  private static int connectionTimeOut = 25000;  

  private static int socketTimeOut = 25000;  

  private static int maxConnectionPerHost = 20;  

  private static int maxTotalConnections = 20;  

  private static HttpClient client;  

	
	 /** 
     * POST方式提交数据 
     * @param url 
     *          待请求的URL 
     * @param params 
     *          要提交的数据 
     * @param enc 
     *          编码 
     * @return 
     *          响应结果 
     * @throws IOException 
     *          IO异常 
     */  
    public static String URLPost(String url, Map<String, String> params, String enc){  
  
        String response = EMPTY;          
        PostMethod postMethod = null;  
        try {  
            postMethod = new PostMethod(url);  
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);  
            //将表单的值放入postMethod中  
            Set<String> keySet = params.keySet();  
            for(String key : keySet){  
                String value = params.get(key);  
                postMethod.addParameter(key,value);  
            }             
            //执行postMethod  
            int statusCode = client.executeMethod(postMethod);  
            if(statusCode == HttpStatus.SC_OK) {  
                response = postMethod.getResponseBodyAsString();  
            }else{  
                log.error("响应状态码 = " + postMethod.getStatusCode());  
            }  
        }catch(org.apache.commons.httpclient.HttpException e){  
            log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);  
            e.printStackTrace();  
        }catch(IOException e){  
            log.error("发生网络异常", e);  
            e.printStackTrace();  
        }finally{  
            if(postMethod != null){  
                postMethod.releaseConnection();  
                postMethod = null;  
            }  
        }  
          
        return response;  
    }  
}
