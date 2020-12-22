import com.alibaba.fastjson.JSON;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author sgmark
 * @create 2019-11-11 10:51
 **/
public class MqttTest {
    public static void main(String[] args) throws Exception {
        String url = "https://183.194.243.82/clientgateway/";
        String appid = "6fa10b76-2444-44f5-aed5-de48d46c85e5";
        String appkey = "4d4c10af-46c1-46d8-8fa2-e00b06bfb084";
//        String appsignature = "b6892d8d-5823-4001-976e-96881f730740";
//        String apikey = "a6477c8196119cf6940b3e0e9542dc89";
        String apiID = "55030e2c-49d3-4e9b-a6b2-814ee17e9dbb";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String key = appkey.replaceAll("-", "");
        String stringtosign = appid + apiID + timestamp+100;
        System.out.println(stringtosign);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
//        6OB2hgp0irC5Rngf2kReRT5YvvW+303ptWYrR9XrBI3SNJeW5QFm76IqjqFCxTpe1bfM9zeCEV9TLsZnsNoBTaRTqxtFmZyAWgdSq7U2r/B8RLtjGECH9P7Vb3LomLHo
//        3vSk9Jx85VONjqyz5b04Bv8NsXtn85qqAMoQdtdo0ZseKklK+nCo41H+AcLEIEJCn+p+BD2MycPrEgqIFmFbE1sEzVrPZnxQkwfvZ8y1yfynSZpUUjAqWBC6AtINstIr
        //stringtosign是1.1生成的签名字符串
        byte[] bytes = cipher.doFinal(stringtosign.getBytes("utf-8"));
        String signature = new BASE64Encoder().encode(bytes).replaceAll("\r|\n|\t", "").replaceAll(" ", "");
        System.out.println("signature" + signature);
        System.out.println(FastHttpClient.post().url(url)
                .addHeader("appid", appid)
                .addHeader("apiname", apiID)
                .addHeader("signature", signature)
                .addParams("data", "https://s.sh.gov.cn/7f78f6ac4a18b8dfa7cc6b6c1852081599211252260")
                .addParams("from", key).build().execute().getResponse().body().string());
    }
}
