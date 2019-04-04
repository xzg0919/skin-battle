package com.tzj.collect.api.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public final class PostTool
{
    /**
     * 
     * <p>Description:[构造器方法描述]</p>
     * @coustructor 方法.
     */
    private PostTool()
    {

    }

    /**
    *  Created on 2017年1月18日
    * <p>Description:[调用]</p>
    * @author:[杨欢][yanghuan1937]@aliyun.com
    * @return String
    */
    public static String post(final String strURL,final String params)
    {
        URL url = null;
        HttpURLConnection connection = null;
        OutputStreamWriter out = null;
        InputStream is = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer("");
        try
        {
            url = new URL(strURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5*1000);
            connection.setReadTimeout(30*1000); 
            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(params);
            out.flush();
            
            //读取响应
            is = connection.getInputStream();
            if (is != null)
            {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String lines;
                
                while ((lines = reader.readLine()) != null)
                {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                System.out.println(sb);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {   
            if(null != connection)
            {
                connection.disconnect();
            }
            
            try
            {
                if(null != reader)
                {
                    reader.close();
                }
                if(null != is)
                {
                    is.close();
                }
                if(null != out)
                {
                    out.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
        }
        return sb.toString();
    }
    /**
     *  Created on 2017年1月18日
     * <p>Description:[调用]</p>
     * @author:[杨欢][yanghuan1937]@aliyun.com
     * @return String
     */
     public static String postB(final String strURL,final String params)
     {
         URL url = null;
         HttpURLConnection connection = null;
         DataOutputStream out = null;
         InputStream is = null;
         BufferedReader reader = null;
         StringBuffer sb = new StringBuffer("");
         try
         {
             url = new URL(strURL);
             connection = (HttpURLConnection) url.openConnection();
             connection.setDoOutput(true);
             connection.setDoInput(true);
             connection.setUseCaches(false);
             connection.setInstanceFollowRedirects(true);
             connection.setRequestMethod("POST");
             connection.setRequestProperty("Accept", "application/json");
             connection.setConnectTimeout(5*1000);
             connection.setReadTimeout(30*1000); 
             connection.connect();
             out = new DataOutputStream(connection.getOutputStream());
             out.writeBytes(params);
             out.flush();
             
             //读取响应
             is = connection.getInputStream();
             if (is != null)
             {
                 reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 String lines;
                 
                 while ((lines = reader.readLine()) != null)
                 {
                     lines = new String(lines.getBytes(), "utf-8");
                     sb.append(lines);
                 }
                 System.out.println(sb);
             }
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
         finally
         {   
             if(null != connection)
             {
                 connection.disconnect();
             }
             
             try
             {
                 if(null != reader)
                 {
                     reader.close();
                 }
                 if(null != is)
                 {
                     is.close();
                 }
                 if(null != out)
                 {
                     out.close();
                 }
             }
             catch (IOException e)
             {
                 e.printStackTrace();
             }
             
         }
         return sb.toString();
     }
}
