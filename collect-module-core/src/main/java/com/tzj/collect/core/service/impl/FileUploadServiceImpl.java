package com.tzj.collect.core.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.module.common.file.upload.FileUpload;
import com.tzj.module.common.utils.FileUtil;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;
import com.tzj.module.easyopen.file.FileUploadService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileUpload fileUpload;

    @Override
    public Map<String, Object> upload(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();

        if(request instanceof StandardMultipartHttpServletRequest){
            StandardMultipartHttpServletRequest multipartHttpServletRequest= (StandardMultipartHttpServletRequest) request;
            Map<String, String[]> parameterMaps=multipartHttpServletRequest.getParameterMap();

            for (Map.Entry<String, String[]> entry : parameterMaps.entrySet()) {
                String key = entry.getKey();
                String[] value = entry.getValue();
                if (value.length <= 1) {
                    map.put(key,value[0]);
                } else {
                    map.put(key, org.apache.commons.lang3.StringUtils.join(value, ","));
                }
            }

            MultipartFile multipartFile = null;
            Iterator<String> itr =  multipartHttpServletRequest.getFileNames();
            List<FileBean> fileBeanList=new LinkedList<>();
            while(itr.hasNext()){
                String str = itr.next();
                multipartFile = multipartHttpServletRequest.getFile(str);
                String fileName = multipartFile.getOriginalFilename();   //原文件名
                MultipartFile mpf = multipartHttpServletRequest.getFile(str);

                FileBean fileBean=handleUploadField(fileName,mpf);
                fileBeanList.add(fileBean);
            }
            map.put("data", JSON.toJSONString(fileBeanList));
        }

        return map;
    }

    
    /**
     * 上传图片 base64加密过的
     * @return
     */
    @Override
    public List<FileBean> uploadImage(List<FileBase64Param> list) {


//    	Base64.Decoder decoder = Base64.getDecoder();
    	String tempPath = System.getProperty("java.io.tmpdir");
    	

    	 FileBean fileBean=new FileBean();
    	 List<FileBean> fileBeanList=new LinkedList<>();
    	 //开始上传文件
    	for(FileBase64Param file : list){
    		String extensionName = FileUtil.getExtension(file.getFileName());
    		
    		 if (StringUtils.isBlank(extensionName)) {
    	            extensionName = "jpg";
    	        }
    		
    		String uuid = UUID.randomUUID().toString();
       	 	//先把文件放入临时的地方
           File tempFile = new File(
                   tempPath + "/original_" + uuid + "." +extensionName);
           
//	        byte[] bytes;
//			try {
//				bytes = new String(decoder.decode(file.getFileContentBase64().substring(file.getFileContentBase64().lastIndexOf(",")+1,file.getFileContentBase64().length())), "UTF-8").getBytes();
//				  BufferedOutputStream buffStream;
//				  buffStream = new BufferedOutputStream(new FileOutputStream(tempFile));
//					buffStream.write(bytes);
//		           buffStream.close();
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}catch (IOException e) {
//					e.printStackTrace();
//			}
			
			
			  BufferedImage image = null;
				byte[] imageByte = null;
				
				try {
					imageByte = DatatypeConverter.parseBase64Binary(file.getFileContentBase64().
							substring(file.getFileContentBase64().lastIndexOf(",")+1,file.getFileContentBase64().length()));
					ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
					image = ImageIO.read(new ByteArrayInputStream(imageByte));
					bis.close();

					ImageIO.write(image,extensionName,tempFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
         
       	



           String savePath = "collect/" + new SimpleDateFormat("yyyyMMdd").format(new Date());

           String saveLargeFile=savePath+"/bigpicture_" + uuid + "." +extensionName;

           fileUpload.upload(saveLargeFile,tempFile,null);

           fileBean.setThumbnail(fileUpload.getImageDomain()+"/"+saveLargeFile);
           fileBean.setBigPicture(fileUpload.getImageDomain()+"/"+saveLargeFile);
           fileBean.setOriginal(fileUpload.getImageDomain()+"/"+saveLargeFile);

            //上传到OSS后，删除临时文件
            try{
                tempFile.delete();
            }catch (Exception e){
                e.printStackTrace();
            }
           
           fileBeanList.add(fileBean);
    	}
    	 
    	return fileBeanList;
    }

    @Override
    public List<FileBean> uploadImage() {
        List<MultipartFile> multipartFiles= ApiContext.getUploadContext().getAllFile();
        if(multipartFiles==null || multipartFiles.size()<=0){
            return new LinkedList<>();
        }

        String tempPath = System.getProperty("java.io.tmpdir");
        List<FileBean> fileBeanList=new LinkedList<>();

        for(MultipartFile multipartFile:multipartFiles){
            String fileName = multipartFile.getOriginalFilename();
            String extensionName = FileUtil.getExtension(fileName);

            if (StringUtils.isBlank(extensionName)) {
                extensionName = "jpg";
            }

            String uuid = UUID.randomUUID().toString();
            //先把文件放入临时的地方
            File tempFile = new File(
                    tempPath + "/original_" + uuid + "." +extensionName);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }

            BufferedImage image = null;
            byte[] imageByte = null;

            if (!multipartFile.isEmpty()) {
                try {
                    byte[] bytes = multipartFile.getBytes();
                    BufferedOutputStream buffStream =
                            new BufferedOutputStream(new FileOutputStream(tempFile));
                    buffStream.write(bytes);
                    buffStream.close();
                } catch (Exception e) {
                    throw new ApiException("api文件上传发生异常："+e.getMessage());
                }
            }

            //保存的路径
            String savePath = "erp/supplier/" + new SimpleDateFormat("yyyyMMdd").format(new Date());

            String saveFile=savePath+"/" + uuid + "." +extensionName;

            fileUpload.upload(saveFile,tempFile,multipartFile.getContentType());

            FileBean fileBean=new FileBean();
            fileBean.setThumbnail(fileUpload.getImageDomain()+"/"+saveFile);
            fileBean.setBigPicture(fileUpload.getImageDomain()+"/"+saveFile);
            fileBean.setOriginal(fileUpload.getImageDomain()+"/"+saveFile);

            fileBeanList.add(fileBean);

            //上传到OSS后，删除临时文件
            try{
                tempFile.delete();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return fileBeanList;
    }

    /**
     * 处理文件信息
     */
    public FileBean handleUploadField(String fileName,MultipartFile mpf) {

        FileBean fileBean=new FileBean();
        String extensionName = FileUtil.getExtension(fileName);

        if (StringUtils.isBlank(extensionName)) {
            extensionName = "jpg";
        }

        String tempPath = System.getProperty("java.io.tmpdir");

        //tempPath="/Volumes/Macintosh HD/temp/";
        //System.out.println("tempPath:"+tempPath);

        String uuid = UUID.randomUUID().toString();

        //先把文件放入临时的地方
        File tempFile = new File(
                tempPath + "/original_" + uuid + "." +extensionName);

        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }

        if (!mpf.isEmpty()) {
            try {
                byte[] bytes = mpf.getBytes();
                BufferedOutputStream buffStream =
                        new BufferedOutputStream(new FileOutputStream(tempFile));
                buffStream.write(bytes);
                buffStream.close();
            } catch (Exception e) {
                throw new ApiException("api文件上传发生异常："+e.getMessage());
            }
        }




        String savePath = "collect/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String saveOriginalFile=savePath+"/original_" + uuid + "." +extensionName;

        fileUpload.upload(saveOriginalFile,tempFile,mpf.getContentType());

        fileBean.setThumbnail(fileUpload.getImageDomain()+"/"+saveOriginalFile);
        fileBean.setBigPicture(fileUpload.getImageDomain()+"/"+saveOriginalFile);
        fileBean.setOriginal(fileUpload.getImageDomain()+"/"+saveOriginalFile);

        //上传到OSS后，删除临时文件
        try{
            tempFile.delete();
        }catch (Exception e){
            e.printStackTrace();
        }

        return fileBean;
    }

    /**
     * 上传身份证图片
     */
    public Map<String,Object> aliUploadImage(String fileContentBase64,String side){
        Map<String,Object> resultMap = new HashMap<String,Object>();

        String host = AlipayConst.ALI_host;
        String path = AlipayConst.ALI_path;
        String appcode = AlipayConst.ALI_appcode;
        //String imgFile = "图片路径";
        Boolean is_old_format = false;//如果文档的输入中含有inputs字段，设置为True， 否则设置为False
        //请根据线上文档修改configure字段
        JSONObject configObj = new JSONObject();
        configObj.put("side", side);
        String config_str = configObj.toString();
        //            configObj.put("min_size", 5);
        //            String config_str = "";

        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();

        // 对图像进行base64编码
        String imgBase64 = fileContentBase64;
//        try {
//            File file = new File(imgFile);
//            byte[] content = new byte[(int) file.length()];
//            FileInputStream finputstream = new FileInputStream(file);
//            finputstream.read(content);
//            finputstream.close();
//            imgBase64 = new String(encodeBase64(content));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        try {
            if(is_old_format) {
                JSONObject obj = new JSONObject();
                obj.put("image", getParam(50, imgBase64));
                if(config_str.length() > 0) {
                    obj.put("configure", getParam(50, config_str));
                }
                JSONArray inputArray = new JSONArray();
                inputArray.add(obj);
                requestObj.put("inputs", inputArray);
            }else{
                requestObj.put("image", imgBase64);
                if(config_str.length() > 0) {
                    requestObj.put("configure", config_str);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodys = requestObj.toString();
        String  results = "";
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            resultMap.put("stat",stat);
            if(stat != 200){
                System.out.println("Http code: " + stat);
                System.out.println("http header error msg: "+ response.getFirstHeader("X-Ca-Error-Message"));
                System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                return resultMap;
            }

            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);

            if(is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
                System.out.println(out.toJSONString());
                results = out.toJSONString();
            }else{
                System.out.println(res_obj.toJSONString());
                results = res_obj.toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("getBody",results);
        return resultMap;
    }
    /*
     * 获取参数的json对象
     */
    public JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
    public HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }
    private String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }
    private HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }
    private void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }
                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }


    public static void main(String[] args) {
		System.out.println(FileUtil.getExtension(""));
    	  
	}


}
