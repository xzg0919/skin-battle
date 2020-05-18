package api.adminReception;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.admin.AdminBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.param.app.ArrivalTimeLogBean;
import com.tzj.collect.core.param.business.BOrderBean;
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
//        orderBean.setIsNormal("0");
//        orderBean.setCompanyId(41);
//        orderBean.setStatus("0");
//        orderBean.setTel("18375336389");
//        orderBean.setOrderNo("912462913569884234");
//        orderBean.setLinkName("王灿");
//        orderBean.setStartTime("2020-03-01");
//        orderBean.setEndTime("2020-03-31");
//        orderBean.setParentId("2");
        orderBean.setOrderId(70368);
        orderBean.setCompanyId(41);

        BOrderBean bOrderBean = new BOrderBean();
        bOrderBean.setId(70389);
        bOrderBean.setCancelReason("我是驳回原因");



        HashMap<String,Object> param=new HashMap<>();
        param.put("name","admin.updateOrderStatusByAdminReception");
        param.put("version","1.0");
        param.put("format","json");
        param.put("app_key","app_id_4");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token",securityToken);
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",bOrderBean);

        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_9988767");
        param.put("sign",sign);
        System.out.println("请求的参数是："+JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是："+resultJson);
    }


}
