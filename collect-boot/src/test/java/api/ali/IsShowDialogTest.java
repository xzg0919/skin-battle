package api.ali;

import com.alibaba.fastjson.JSON;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.HashMap;

/**
 * @Author
 **/
public class IsShowDialogTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:9000/ali/api";
//      Member member = new Member();
//      member.setAliUserId("208812345678");
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","member.updateShowDialog");
        param.put("version","1.0");
        param.put("format","json");
        //param.put("data",member);
        param.put("token","3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24VDPYKJDZTFWQI47BL5WTPAJFXJ4XDEM6YHXKVKPQEWQR7CZM233IGEACH6QJ4HX3PI25RZPBHD62DREPUXDTYDFJRMWDPNULGB4RIXEMS2DT7LNXPOH2BBD3LO6VQI4GGIKAR7ZNPTJR7BBB35BM5VV7BW4GDJNV2KZQRPTJJWXKL4UWVVNWQ63MKLL554P2Q5A6CDF6SJSN5YN6PB5HJ5WMI43PZ4O2ZIFJROTQ");
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
