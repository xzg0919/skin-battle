package api.terminal;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.enterprise.param.EnterpriseCodeBean;
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

public class TerminalTest {

    public static void main(String[] args) throws Exception{
        String token = JwtUtils.generateToken("25",TERMINAL_API_EXPRIRE, ENTERPRISE_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, TERMINAL_API_TOKEN_CYPTO_KEY);
        System.out.println("生成的token是："+securityToken);

        String tokenCyptoKey = TERMINAL_API_TOKEN_CYPTO_KEY;
        String key = CipherTools.initKey(tokenCyptoKey);
        String decodeToken = CipherTools.decrypt(securityToken, key);
        Claims claims = JwtUtils.getClaimByToken(decodeToken, TERMINAL_API_TOKEN_SECRET_KEY);
        String subjectStr = claims.getSubject();
        System.out.println("反向編譯 token是："+subjectStr);

        String api="http://localhost:9000/terminal/api";

        EnterpriseCodeBean enterpriseCodeBean = new EnterpriseCodeBean();
        enterpriseCodeBean.setIsUse("0");
        enterpriseCodeBean.setPageBean(new PageBean());


        HashMap<String,Object> param=new HashMap<>();
        param.put("name","terminal.enterpriseTerminalList");
        param.put("version","1.0");
        param.put("format","json");
        param.put("app_key","app_id_6");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token",securityToken);
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",enterpriseCodeBean);

        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_88bbccdd");
        param.put("sign",sign);


        System.out.println("请求的参数是 ："+JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是 ："+resultJson);

    }
}
