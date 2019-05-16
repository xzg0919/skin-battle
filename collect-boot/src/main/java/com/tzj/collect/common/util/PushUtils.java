package com.tzj.collect.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import com.tzj.collect.api.common.constant.Const;

import java.util.Date;

public class PushUtils {

    /**
     * 阿里推送接口
     * @param targetValue 推送到制定账号
     * @param title  推送标题
     * @param body   推送内容`
     * @param code 返给app的状态码
     * @param message 返给app的信息
     * @return
     */
    public static PushResponse getAcsResponse(String targetValue,String title,String body,String code,String message){
        getAcsResponse(Const.APP_ID_1,Const.ALIYUN_ACCESS_KEY_ID_1,Const.ALIYUN_SECRET_1,targetValue,title,body,code,message);
        getAcsResponse(Const.APP_ID_2,Const.ALIYUN_ACCESS_KEY_ID_2,Const.ALIYUN_SECRET_2,targetValue,title,body,code,message);
        getAcsResponse(Const.APP_ID_3,Const.ALIYUN_ACCESS_KEY_ID_3,Const.ALIYUN_SECRET_3,targetValue,title,body,code,message);
        return null;
    }
    public static PushResponse getAcsResponse(String aliYunAppId,String aliYunAccessKeyId,String aliYunSercret,String targetValue,String title,String body,String code,String message){
        PushRequest pushRequest = new PushRequest();
        pushRequest.setAppKey(Long.parseLong(aliYunAppId));
        pushRequest.setTarget("ACCOUNT"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALIAS: 按别名推送; ALL: 全推
        pushRequest.setTargetValue(targetValue); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setDeviceType("ANDROID"); // 设备类型deviceType, iOS设备: "iOS"; Android设备: "ANDROID"; 全部: "ALL", 这是默认值.

        // 推送配置
        pushRequest.setPushType("NOTICE"); // MESSAGE:表示消息(默认), NOTICE:表示通知
        pushRequest.setTitle(title); // 消息的标题
        pushRequest.setBody("{\"code\":\""+code+"\",\"message\":\""+message+"\"}"); // 消息的内容
        // 推送配置: iOS
//        pushRequest.setIOSBadge(5); // iOS应用图标右上角角标
//        pushRequest.setIOSSilentNotification(false);//开启静默通知
//        pushRequest.setIOSMusic("default"); // iOS通知声音
//        pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
//        pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
//        pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
//        pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
//        pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
//        pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
//        pushRequest.setIOSExtParameters("{\"_ENV_\":\"DEV\",\"k2\":\"v2\"}"); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
        // 推送配置: Android
        pushRequest.setAndroidOpenType("NONE"); //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
        pushRequest.setAndroidNotifyType("SOUND");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
        //pushRequest.setAndroidActivity("com.alibaba.push2.demo.XiaoMiPushActivity"); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
        //pushRequest.setAndroidOpenUrl("http://www.aliyun.com"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
        pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
        pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
        pushRequest.setAndroidMusic("default"); // Android通知音乐
//        pushRequest.setAndroidXiaoMiActivity("com.ali.demo.MiActivity");//设置该参数后启动小米托管弹窗功能, 此处指定通知点击后跳转的Activity（托管弹窗的前提条件：1. 集成小米辅助通道；2. StoreOffline参数设为true）
//        pushRequest.setAndroidXiaoMiNotifyTitle("Mi title");
//        pushRequest.setAndroidXiaoMiNotifyBody("MiActivity Body");
        pushRequest.setAndroidExtParameters("{\"code\":\""+code+"\",\"message\":\""+message+"\"}"); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
//        // 推送控制
//        Date pushDate = new Date(System.currentTimeMillis()); // 30秒之间的时间点, 也可以设置成你指定固定时间
//        String pushTime = ParameterHelper.getISO8601Time(pushDate);
//        pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
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
        PushUtils.getAcsResponse("15691728708","垃圾分类回收","垃圾分类回收内容","1002","垃圾是龙建");
    }
}
