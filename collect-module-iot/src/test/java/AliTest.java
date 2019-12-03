import com.alibaba.fastjson.JSON;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author sgmark
 * @create 2019-12-02 14:35
 **/
public class AliTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:9006/ali/api";
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","equipment.error.code");
        param.put("version","1.0");
        param.put("format","json");
        param.put("app_key","app_id_1");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token", "3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24VVHUSAVURD6DYMTLXOHVOYHZ3WMT3F7XWVRQE3ZYJ23R2CDMCSECAZVENOS7FGM3JYS2AIERTHV2QSMU2FP5UTXCOMWJW3YLJLVSVDRQUF5GWJN7ECOADJ43YBRRXOZRHNSJ44LG6L3STLYMDNSCQFXHDUGMG5TR35BCJ3YLLPODE2OD6X4F6WRGVFFDFCT2BOYIBB2CSBDTVXHW4MVBPQBKQ5G4Q5DNOYJQT7WEDUKQR3CQUTGJC2ONKRONILCYW");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());

//        param.put("data","{\"fToken\":\"XXXX\"}");

        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
        param.put("sign",sign);

        String s = "{\"app_key\":\"app_id_1\",\"data\":{\"cityId\":745},\"name\":\"memberAddress.memberAddressList\",\"format\":\"json\",\"sign\":\"FC9A826D26B306554FA6EEA9F5633520\",\"version\":\"1.0\",\"nonce\":1236131394899.91299,\"token\":\"3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24U65CVPQU32QS6WCOW4OQQ3AURAVX5JCW3DOJFI7QONQRCOYHSMZITFGRL7NE5YTTFKOD4CHYYW5XCU546HJVXIOTWEUH553LW7Q5I5HMBELSQBTBTYOQZEB2JBJEI7CERPJ5GXZGKOKLSCQFMEABHE2O75VTH57K7GHNNLDWI3HIJKS743QNBZCVVCNMPXWADKD4T3M3QCMWPAM7SRGIBEE27W26GDOMZF2JEZLQ\",\"timestamp\":\""+Calendar.getInstance().getTimeInMillis()+"\"}";

        System.out.println("请求的参数是 ："+JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是 ："+resultJson);
    }
}
