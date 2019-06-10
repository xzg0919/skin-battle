package api.ali;

import com.alibaba.fastjson.JSON;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public class TokenFlushApiTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:9090/ali/api";


        HashMap<String,Object> param=new HashMap<>();
        param.put("name","token.flush");
        param.put("version","1.0");
        param.put("format","json");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token","3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24UAJWCBI7NJ42KSYJ2KXG2OVQSA6ZMU4VMMCLQUKIRXAWTX2BD3K6MDOZDBJ4Q62CYGOB7DVAUP4CYQAHL3JSQRIG7P2UO77IZBN7W3E4RZK42VEEUWCHGAZLS7LGRB4EVIIYSQVYYSGAETEUZC4JUVVV2UDRKIOBGXURUGYCOGKTBVFLZYU2QFPF2G4I7DVNKBWCOFWBQDLZLJYEDSPIL6T46KLPZ4O2ZIFJROTQ");

        	


        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
