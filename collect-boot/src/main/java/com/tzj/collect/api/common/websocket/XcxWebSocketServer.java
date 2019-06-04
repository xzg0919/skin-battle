package com.tzj.collect.api.common.websocket;


import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_CYPTO_KEY;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_SECRET_KEY;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.MemberBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;

import org.springframework.stereotype.Component;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

@ServerEndpoint(value = "/xcxwebsocket/{type}/{token}")
@Component
public class XcxWebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static Map<String,XcxWebSocketServer> xcxWebSocketSetMap = new HashMap<String,XcxWebSocketServer>();

    private static String memberId=null;

    private static String types=null;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token,@PathParam("type") String type) {
        this.session = session;
        memberId = this.getTokens(token);
        types = type;
        System.out.println("memberId: "+memberId+"types: "+types);
        xcxWebSocketSetMap.put(memberId, this);

        session.setMaxIdleTimeout((long)86400000);//设置连接时间
        addOnlineCount();           //在线数加1
        System.out.println("用户---"+memberId+" 连接成功");
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }
    //	//连接打开时执行
    //	@OnOpen
    //	public void onOpen(@PathParam("user") String user, Session session) {
    //		currentUser = user;
    //		System.out.println("Connected ... " + session.getId());
    //	}

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        xcxWebSocketSetMap.remove(memberId);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String companyId, String message) {
//    	 System.out.println("来自客户端的消息:" + message);
//        //群发消息
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 單發发自定义消息
     * */
    public static void sendInfo(String memberId,String type,String message) throws IOException {
        try {
            XcxWebSocketServer item = xcxWebSocketSetMap.get(memberId);
            //判断企业是否连接
            if(item!=null) {
                item.sendMessage(message);
                System.out.println("来自客户端的消息: 发给"+memberId+"用户 '" + message+"'");
            }else {
                System.out.println("来自客户端的消息: 发给"+memberId+"用户 '" + message+"' 但是：用户未建立连接");
            }
        } catch (IOException e) {
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        XcxWebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        XcxWebSocketServer.onlineCount--;
    }

    public String getTokens(String token) {
        String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
        String key = CipherTools.initKey(tokenCyptoKey);
        String decodeToken = CipherTools.decrypt(token, key);
        Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
        String subjectStr = claims.getSubject();
        System.out.println("反向編譯 token是："+subjectStr);
        return subjectStr;
    }

    public static void pushXcxDetail(String memberId,String type,String message){
        try{
            XcxWebSocketServer.sendInfo(memberId,type,message);

            String api="http://172.19.182.58:9090/ali/api";
            if("172.19.182.58".equals(InetAddress.getLocalHost().getHostAddress())){
                api = "http://172.19.182.59:9090/ali/api";
            }

            MemberBean memberBean = new MemberBean();
            memberBean.setMemberId(memberId);
            memberBean.setType(type);
            memberBean.setMessage(message);
            HashMap<String,Object> param=new HashMap<>();
            param.put("name","member.pushXcxDetail");
            param.put("version","1.0");
            param.put("format","json");
            param.put("app_key","app_id_1");
            param.put("timestamp", Calendar.getInstance().getTimeInMillis());
            //param.put("token",securityToken);
            //param.put("sign","111");
            param.put("nonce", UUID.randomUUID().toString());
            param.put("data",memberBean);
            String jsonStr=JSON.toJSONString(param);
            String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
            param.put("sign",sign);
            Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
            String resultJson=response.body().string();
        }catch (Exception e){
            System.out.println("给另一台推送消息失败");
        }
    }


}
