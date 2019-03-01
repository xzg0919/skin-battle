package api.admin;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.admin.param.AdminCommunityBean;
import com.tzj.collect.api.admin.param.RecyclersBean;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * 
 * @author sgmark@aliyun.com
 *
 */
public class RecyclerApiTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:8080/admin/api";
        
//        RecyclersBean bean = new RecyclersBean();
//        bean.setRecyclerId((long)1);
        AdminCommunityBean bean = new AdminCommunityBean();
        bean.setCommunityId("1");
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","admin.area.getcomlist");
        param.put("version","1.0");
        param.put("format","json");
        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPOCTPQSX26SFXWTWXWGK4CCO5LLISYS43FZFB2LZAG7R7ECXSGGY4XFSTHNI3PG33WZMRXXQWSWDCWK7G3G7YENACSA22W75RUGHLYX7H7X66F5MOUOPDTAFS4NIG4MZVW7WIGZSLCF3RPFJWE5JPFTJDMZRK4566VCPLYYLXEEVZF2AN2666OZHNUANOB6OTVAX7BHZQBR6LLA");
        param.put("app_key", "app_id_4");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data", bean);
        String jsonStr = JSON.toJSONString(param);
        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_998877");
        param.put("sign", sign);
        System.out.println(jsonStr);
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
