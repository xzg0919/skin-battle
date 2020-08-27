package api.ali;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.core.param.ali.*;
import com.tzj.collect.core.param.app.OrderPayParam;
import com.tzj.collect.core.param.business.CategoryBean;
import com.tzj.collect.core.param.iot.IotPostParamBean;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.Claims;

import java.math.BigDecimal;
import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

public class OrderTest {

        /**
         * @param args
         * @throws Exception
         */
        public static void main(String[] args) throws Exception {
                String token= JwtUtils.generateToken("2088212854989662", ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
//                String token= JwtUtils.generateToken(userId, ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
                String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
                System.out.println("token是 : "+securityToken);
                //String api="http://shoubeics.mayishoubei.com/ali/api";
                String api="http://localhost:9090/ali/api";

                FileBase64Param file = new FileBase64Param();
                file.setFileName("tests");
                file.setFileContentBase64("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");

                HashMap<String,Object> param=new HashMap<>();
                param.put("name", "util.uploadImage");
                param.put("version","1.0");
                param.put("format","json");
                param.put("app_key","app_id_1");
                param.put("timestamp",  Calendar.getInstance().getTimeInMillis());
                param.put("token", securityToken);
                param.put("nonce", UUID.randomUUID().toString());
                param.put("data", file);

                String jsonStr= JSON.toJSONString(param);
                System.out.println(jsonStr);
                String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
                param.put("sign",sign);
                Long i = new Date().getTime();
                System.out.println("请求的参数是 ："+JSON.toJSONString(param));
                Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
                String resultJson=response.body().string();
                Long ii = new Date().getTime();
                System.out.println(ii-i);
                System.out.println("返回的参数是 ："+resultJson);
        }
}
