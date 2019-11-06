package com.tzj.collect.common.auto;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import ch.ethz.ssh2.Connection;


/**
 * <p>Created on 2019年11月5日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: []</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
*/
public class Auto
{
    private static String JAR_PATH = null; 
    private static String JAR_NAME = null;
    //public static String IPS[] = new String[]{"1","2","3","4"};   
    public static String IPS[] = new String[]{"172.19.182.91","172.19.182.90","172.19.182.88","172.19.182.89","172.19.182.59","172.19.182.58"};
    public static Integer PORT = 22;
    public static String USER = "root";
    public static String PSW = "Anping520";
    public static String TARGET_PATH = "/collect/collect-boot.jar";
    
    private static Connection conn;
    /**
     * <p>Created on 2019年11月5日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    public static void main(String[] args)
    {
        // 1. 获取jia包
        System.out.println("1. 准备文件。。。");
        // 1.1 获取jar目录
        getJarPath();
        // 1.2 获取jar名称
        getJarName();
        System.out.println("1.2 jar包：" + JAR_PATH + JAR_NAME);

        // 2. 上传
        upload();
       
        for (int i = 0; i < IPS.length; i++)
        {
            try
            {
                // 3. 重启
                restart(IPS[i]);
                Thread.sleep(30000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        // 4. 完成
        System.out.println("4. 齐活");
        
    }
    
    /**
     * <p>Created on 2019年11月6日</p>
     * <p>Description:[获取jar名称]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    private static void getJarName()
    {
        String jarName = null;
        if(StringUtils.isBlank(JAR_PATH))
        {
            System.out.println("1.2 获取jar名称----不正确的jar目录");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jarName = "collect-boot-".concat(sdf.format(new Date())).concat(".jar");
        JAR_NAME =  jarName;
    }
    /**
     * 
     * <p>Created on 2019年6月5日</p>
     * <p>Description:[获取jar目录]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    private static void getJarPath()
    {
        String rootPath = null;
        try
        {
            rootPath = Auto.class.getResource("").toString();
            rootPath = rootPath.replaceAll("file:/","");
            rootPath = rootPath.substring(0,rootPath.indexOf("/classes"));
        }
        catch (Exception e)
        {
            System.out.println("1.1 获取jar目录失败");
            e.printStackTrace();
        }
        JAR_PATH = rootPath + "/";
    }
    
    private static void upload()
    {
        CountDownLatch latch = new CountDownLatch(IPS.length);
        for (int i = 0; i < IPS.length; i++)
        {
            int j = i;
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    uploadRun(IPS[j]);
                    latch.countDown();
                }
            }).start();

        }
        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        
    }
    
    
    private static void uploadRun(String ip)
    {
        ChannelSftp sftp = null;
        Session session = null;
        try
        {
            JSch jsch = new JSch();
            session = jsch.getSession(USER, ip,PORT);
            session.setPassword(PSW);
            //设置第一次登陆的时候提示，可选值:(ask | yes | no)  
            session.setConfig("StrictHostKeyChecking", "no");
            //30秒连接超时  
            session.connect(3000000);
        }
        catch (JSchException e)
        {
            e.printStackTrace();
            System.out.println("2." + ip + " 上传jar失败：登录服务器失败");
            return;
        }
        
        try
        {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("2." + ip + " 上传jar失败：连接服务器发生错误");
            return;
        }
        
        try
        {
            sftp.put(JAR_PATH + JAR_NAME,TARGET_PATH);
            System.out.println("2." + ip + " 上传jar：成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("2." + ip + " 上传jar失败：上传文件发生错误");
            return;
        }
        finally
        {
            if(null != sftp)
            {
                sftp.exit();
            }
            if(null != session)
            {
                session.disconnect();
            }
        }
    }

    
    /**
     * <p>Created on 2019年11月6日</p>
     * <p>Description:[重启]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    private static void restart(String ip)
    {
        String result = exec("/collect/collect.sh restart",ip);
        System.out.println("重启"+ ip + " : " + result);
    }

    /**
     * <p>Created on 2019年11月6日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void,
     */
    private static String exec(String cmd,String ip)
    {

        InputStream in = null;
        ch.ethz.ssh2.Session session = null;
        String result = "";
        try
        {
            if (login(ip))
            {
                session = conn.openSession(); // 打开一个会话  
                session.execCommand(cmd);
                in = session.getStdout();
                result = processStdout(in, "UTF-8");
            }
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        finally
        {
            if(null != session)
            {
                session.close();
            }
        }
        return result;
    
        
    }
    
    /**
     * <p>Created on 2019年11月6日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    private static String processStdout(InputStream in, String string)
    {
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        try
        {
            while (in.read(buf) != -1)
            {
                sb.append(new String(buf, "UTF-8"));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 
     * <p>Created on 2019年6月5日</p>
     * <p>Description:[创建链接]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return boolean
     */
    private static boolean login(String ip) throws IOException
    {
        conn = new Connection(ip);
        conn.connect(); // 连接  
        return conn.authenticateWithPassword(USER, PSW); // 认证  
    }
    
    
    
}
