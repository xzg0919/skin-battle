package com.tzj.point.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;

import java.util.Date;

public class PushUtils {



    /**
     * 阿里推送接口
     * @return
     */
    public static PushResponse getAcsResponse(String recycleTel){
        String aliYunAppId= "27757137";
        String aliYunAccessKeyId= "LTAIIUwHN6Nam32H";
        String aliYunSercret= "B0TwGIvC0MOXah9Ncpb1k8HXR09ksa";
        String targetValue= recycleTel;
        String titles= "异地登录";
        String code= "1001";
        String message= "您的账号在异地登录";

        getAcsResponse(aliYunAppId,aliYunAccessKeyId,aliYunSercret,targetValue,titles,code,message);
        return null;
    }
    public static PushResponse getAcsResponse(String aliYunAppId,String aliYunAccessKeyId,String aliYunSercret,String targetValue,String title,String code,String message){
        PushRequest pushRequest = new PushRequest();
        pushRequest.setAppKey(Long.parseLong(aliYunAppId));
        pushRequest.setTarget("DEVICE"); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
        pushRequest.setTargetValue(targetValue); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
//        pushRequest.setTarget("ALL"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALL: 推送给全部
//        pushRequest.setTargetValue("ALL"); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
        pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.
        // 推送配置
        pushRequest.setTitle(title); // 消息的标题
        pushRequest.setBody(message); // 消息的内容
        // 推送配置: iOS
//        pushRequest.setIOSBadge(5); // iOS应用图标右上角角标
//        pushRequest.setIOSMusic("default"); // iOS通知声音
//        pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
//        pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
//        pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
//        pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
//        pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
//        pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
//        pushRequest.setIOSExtParameters("{\"_ENV_\":\"DEV\",\"k2\":\"v2\"}"); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
        // 推送配置: Android
        pushRequest.setAndroidNotifyType("BOTH");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
        pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
        pushRequest.setAndroidOpenType("NONE"); //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
        pushRequest.setAndroidOpenUrl("http://www.aliyun.com"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
        pushRequest.setAndroidActivity("com.alibaba.push2.demo.XiaoMiPushActivity"); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
        pushRequest.setAndroidMusic("default"); // Android通知音乐
        pushRequest.setAndroidPopupActivity("com.ali.demo.PopupActivity");//设置该参数后启动辅助弹窗功能, 此处指定通知点击后跳转的Activity（辅助弹窗的前提条件：1. 集成第三方辅助通道；2. StoreOffline参数设为true）
        pushRequest.setAndroidPopupTitle(title);
        pushRequest.setAndroidPopupBody(message);
        pushRequest.setAndroidExtParameters("{\"title\":\""+title+"\",\"message\":\""+message+"\"}"); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
        // 推送控制
        Date pushDate = new Date(System.currentTimeMillis()) ; // 30秒之间的时间点, 也可以设置成你指定固定时间
        String pushTime = ParameterHelper.getISO8601Time(pushDate);
        pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
        pushRequest.setExpireTime(expireTime);
        pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到

        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", aliYunAccessKeyId, aliYunSercret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushResponse pushResponse = null;
        try{
            System.out.println("开始给用户推送信息了 ："+targetValue);
            pushResponse = client.getAcsResponse(pushRequest);
            System.out.println( pushResponse.getRequestId()+"......."+ pushResponse.getMessageId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return pushResponse;
    }



    public static void main(String[] args) {
        String aliYunAppId= "27757137";
        String aliYunAccessKeyId= "LTAIIUwHN6Nam32H";
        String aliYunSercret= "B0TwGIvC0MOXah9Ncpb1k8HXR09ksa";
        String targetValue= "f8ce356718644404a2d0b6b72cfa608c";
        String titles= "异地登录";
        String code= "1001";
        String message= "您的账号在异地登录";
        getAcsResponse(aliYunAppId,aliYunAccessKeyId,aliYunSercret,targetValue,titles,code,message);
    }
}
