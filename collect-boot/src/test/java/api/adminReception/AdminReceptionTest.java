package api.adminReception;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.admin.AdminBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static com.tzj.collect.common.constant.TokenConst.*;

public class AdminReceptionTest {

    public static void main(String[] args) throws Exception {
        String token= JwtUtils.generateToken("1", ADMIN_RECEPTION_API_EXPRIRE,ADMIN_API_TOKEN_SECRET_KEY);
        String securityToken=JwtUtils.generateEncryptToken(token,ADMIN_RECEPTION_API_TOKEN_CYPTO_KEY);
        System.out.println("token是 : "+securityToken);

        String tokenCyptoKey = ADMIN_RECEPTION_API_TOKEN_CYPTO_KEY;
        String key = CipherTools.initKey(tokenCyptoKey);
        String decodeToken = CipherTools.decrypt(securityToken, key);
        Claims claims = JwtUtils.getClaimByToken(decodeToken, ADMIN_RECEPTION_API_TOKEN_SECRET_KEY);
        String subjectStr = claims.getSubject();
        System.out.println("反向編譯 token是："+subjectStr);


        String api="http://localhost:9090/admin/reception/api";
        OrderBean orderBean = new OrderBean();
//        orderBean.setStartTime("2019-09-01");
//        orderBean.setEndTime("2019-10-01");
//        orderBean.setCompanyId(41);
//        orderBean.setStatus("1");
//        orderBean.setTel("15691728708");
//        orderBean.setOrderNo("20190924123218492923");
//        orderBean.setLinkName("马欣竹");
//        orderBean.setIsComplaint("Y");
//        orderBean.setCompanyName("再生资源");
        orderBean.setOrderNo("20180727143553755258");
        orderBean.setType("1");
        orderBean.setReason("老板催我来接单");


        HashMap<String,Object> param=new HashMap<>();
        param.put("name","admin.order.saveOrderReceptionByOrderNo");
        param.put("version","1.0");
        param.put("format","json");
        param.put("app_key","app_id_4");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token",securityToken);
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",orderBean);

        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_9988767");
        param.put("sign",sign);
        System.out.println("请求的参数是："+JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是："+resultJson);
    }


}
