package api;

import com.alibaba.fastjson.JSON;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author sgmark
 * @create 2019-04-09 14:19
 **/
public class IotMq {

    public static final String TOPIC_NAME_IOT_ORDER="IOT-TOPIC-TEST";

    public static final String TOPIC_ACCESS_ID="LTAIwQ0kfJaOky8n";

    public static final String TOPIC_ACCESS_KEY="tTDv8g3evSiFQSwjzMhjn5dSVvwIvI";

    public static final String TOPIC_URL="http://1804870195031869.mns.cn-shanghai.aliyuncs.com/";


    public static class  ParentList {
        private String parentName;

        private List<ItemList> itemList;

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public List<ItemList> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemList> itemList) {
            this.itemList = itemList;
        }
    }

    public static class ItemList{
        private String name;

        private float quantity;//量

        private String unit;//单位

        private float price;//单价

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getQuantity() {
            return quantity;
        }

        public void setQuantity(float quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }
    }
    public static void main(String[] args) throws IOException {
        HashMap<String,Object> param=new HashMap<>();
        ItemList itemList1 = new ItemList();
        itemList1.setName("BOOK_MAGAZINE");
        itemList1.setPrice(3.40f);
        itemList1.setQuantity(2);
        itemList1.setUnit("kg");
        ItemList itemList2 = new ItemList();
        itemList2.setName("CARD_BOARD_BOXES");
        itemList2.setPrice(5.20f);
        itemList2.setQuantity(2);
        itemList2.setUnit("kg");
        List<ItemList> itemLists = new ArrayList<>();
        itemLists.add(itemList1);
        itemLists.add(itemList2);

        ParentList parentList = new ParentList();
        parentList.setParentName("PAPER");
        parentList.setItemList(itemLists);
        List<ParentList> parentLists = new ArrayList<>();
        parentLists.add(parentList);

        param.put("memberId", "20194176425738279099");
        param.put("equipmentCode", "ceshi_code");
        param.put("sumPrice",17.20);
        param.put("sumPoint",210);
        param.put("isAddPoint",true);
        param.put("parentLists", parentLists);
        param.put("flowCodeOwn", "7845616561623456");//第三方流水号
        param.put("token", "F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CEQWJEMAIRCFOEPCSCWP6N5VXZUE5JRSLSAGIQADK5GV347M7FDSGP5FKG34NFYA2ULTVDEQJOCRU3CNTT6GOE3ATM4MXDWFNR5OJAVBXD2263AY7GYCIR2A3IESXHNQST3OFOHEYPYR3NTNYRR3GTPJCYX7TONQ6HYROE75NLAKN66ZED4IBJVO3TYE3IAMZWW4XBZDC4JSEA65AX7BHZQBR6LLA");
        System.out.println(JSON.toJSONString(param));
        String jsonStr=JSON.toJSONString(param);
        String sign= buildSign(JSON.parseObject(jsonStr),"sign_key_99aabbcc");
        param.put("sign",sign);
        sendDeliveryOrder(JSON.toJSONString(param), TOPIC_NAME_IOT_ORDER);
    }

    //向socket发送消息
    public static void sendDeliveryOrder(String param,String topicName) {
        CloudAccount account = new CloudAccount(TOPIC_ACCESS_ID, TOPIC_ACCESS_KEY,
                TOPIC_URL);
        MNSClient client = account.getMNSClient(); // 在程序中，CloudAccount以及MNSClient单例实现即可，多线程安全
        CloudTopic topic = client.getTopicRef(topicName);
        try {
            TopicMessage msg = new RawTopicMessage() ; //可以使用TopicMessage结构，选择不进行Base64加密
            //String jsonStr="{\"aliUId\":null,\"member_id\":\"330227\",\"user_code\":\"010031357626\",\"level\":\"2\",\"name\":\"hhh\",\"valid_level_time\":\"2019-12-31 23:59:59\"}";
            msg.setMessageBody(param);
            //msg.setMessageTag("filterTag"); //设置该条发布消息的filterTag
            msg = topic.publishMessage(msg);
            System.out.println("rocketMq发送消息成功 ："+msg.getMessageId()+" 内容是："+param);
//            System.out.println(msg.getMessageBodyMD5());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("rocketMq发送消息失败");
        }
        client.close();
    }
    public static String buildSign(Map<String, ?> paramsMap, String secret) throws IOException {
        Set<String> keySet = paramsMap.keySet();
        List<String> paramNames = new ArrayList(keySet);
        Collections.sort(paramNames);
        List<String> list = new ArrayList();
        Iterator var5 = paramNames.iterator();

        String paramName;
        while(var5.hasNext()) {
            paramName = (String)var5.next();
            String value = paramsMap.get(paramName).toString();
            if (StringUtils.isNotEmpty(paramName) && StringUtils.isNotEmpty(value)) {
                list.add(paramName + "=" + (value != null ? value : ""));
            }
        }

        String source = StringUtils.join(list, "&") + secret;
        paramName = md5(source);
        return paramName;
    }
    public static String md5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = message.getBytes();
            byte[] buff = md.digest(input);
            return byte2hex(buff);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();

        for(int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(bytes[i] & 255);
            if (hex.length() == 1) {
                sign.append("0");
            }

            sign.append(hex.toUpperCase());
        }

        return sign.toString();
    }

}