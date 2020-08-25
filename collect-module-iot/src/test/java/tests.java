import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.ali.MemberAddressBean;
import com.tzj.collect.core.param.ali.MemberBean;
import com.tzj.collect.core.param.iot.EquipmentParamBean;
import com.tzj.collect.core.param.iot.IotPostParamBean;
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
import static com.tzj.collect.common.constant.TokenConst.EQUIPMENT_APP_API_TOKEN_SECRET_KEY;

/**
 * @author sgmark
 * @create 2019-11-07 17:34
 **/
public class tests {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String token= JwtUtils.generateToken("12", EQUIPMENT_APP_API_EXPRIRE,EQUIPMENT_APP_API_TOKEN_SECRET_KEY);
        String securityToken=JwtUtils.generateEncryptToken(token,EQUIPMENT_APP_API_TOKEN_CYPTO_KEY);
        System.out.println("token是 : "+securityToken);

        String tokenCyptoKey = EQUIPMENT_APP_API_TOKEN_CYPTO_KEY;
        String key = CipherTools.initKey(tokenCyptoKey);
        String decodeToken = CipherTools.decrypt(securityToken, key);
        Claims claims = JwtUtils.getClaimByToken(decodeToken, EQUIPMENT_APP_API_TOKEN_SECRET_KEY);
        String subjectStr = claims.getSubject();
        System.out.println("反向編譯 token是："+subjectStr);

        String api="http://localhost:7070/equipment/iot/app/api";
//                String api="http://localhost:9090/business/api";
//

        EquipmentParamBean equipmentParamBean = new EquipmentParamBean();
        equipmentParamBean.setHardwareCode("SH0024");
        equipmentParamBean.setCaptcha("226267");
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","equipment.openTheDoor");
        param.put("version","1.0");
        param.put("format","json");
        param.put("app_key","app_id_8");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token", securityToken);
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",equipmentParamBean);

        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_55667788");
        param.put("sign",sign);


        System.out.println("请求的参数是 ："+JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是 ："+resultJson);
    }
}
