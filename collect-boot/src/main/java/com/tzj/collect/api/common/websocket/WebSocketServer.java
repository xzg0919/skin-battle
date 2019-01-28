package com.tzj.collect.api.common.websocket;

import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_TOKEN_CYPTO_KEY;
import static com.tzj.collect.common.constant.TokenConst.BUSINESS_API_TOKEN_SECRET_KEY;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;

import io.jsonwebtoken.Claims;

@ServerEndpoint(value = "/websocket/{token}")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static Map<String,WebSocketServer> webSocketSetMap = new HashMap<String,WebSocketServer>();
 
    private static String companyId=null;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token) {
        this.session = session;
        companyId = this.getTokens(token);
        webSocketSetMap.put(companyId, this);
        
        session.setMaxIdleTimeout((long)86400000);//设置连接时间
        addOnlineCount();           //在线数加1
        System.out.println("企业---"+companyId+" 连接成功");
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
    	webSocketSetMap.remove(companyId);  //从set中删除
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
    public static void sendInfo(String companyId,String message) throws IOException {
   	 try {
   		 WebSocketServer item = webSocketSetMap.get(companyId);
   		 //判断企业是否连接
   		 if(item!=null) {
   			 item.sendMessage(message);
   			 System.out.println("来自客户端的消息: 发给"+companyId+"企业 '" + message+"'");
   		 }else {
   			System.out.println("来自客户端的消息: 发给"+companyId+"企业 '" + message+"' 但是：企业未登录");
   		 }
		} catch (IOException e) {
		}	
    }
 
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
 
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
    
    public String getTokens(String token) {
    	
    	String tokenCyptoKey = BUSINESS_API_TOKEN_CYPTO_KEY;
        String key = CipherTools.initKey(tokenCyptoKey);
        String decodeToken = CipherTools.decrypt(token, key);
        Claims claims = JwtUtils.getClaimByToken(decodeToken, BUSINESS_API_TOKEN_SECRET_KEY);
        String subjectStr = claims.getSubject();
        System.out.println("反向編譯 token是："+subjectStr);
        return subjectStr;
    }


}
