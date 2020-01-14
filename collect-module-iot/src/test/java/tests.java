import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.ali.MemberAddressBean;
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
        String token= JwtUtils.generateToken("8401", ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
        String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
        System.out.println("token是 : "+securityToken);

        String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
        String key = CipherTools.initKey(tokenCyptoKey);
        String decodeToken = CipherTools.decrypt(securityToken, key);
        Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
        String subjectStr = claims.getSubject();
        System.out.println("反向編譯 token是："+subjectStr);

        String api="http://open.mayishoubei.com/ali/api";
//                String api="http://localhost:9090/business/api";
//


        MemberAddressBean memberAddressBean = new MemberAddressBean();
        memberAddressBean.setCityId(737);

        IotPostParamBean iotPostParamBean = new IotPostParamBean();
        iotPostParamBean.setCabinetNo("SH0066@1");
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","iot.order.create");
        param.put("version","2.0");
        param.put("format","json");
        param.put("app_key","app_id_1");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token", "F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFAEZE4KJLBDRMPO62CEVHWOWPJBVKCYQTJ2A5SOBAS5SUOE4JKSJE6264ZVHDB2AGXAO6SVEJGQEIFMWWEP3SGFEKCK5J453KKPDGKWLCYGNI6JKPOOY5FYK534NXEHHDKFS4TORGUA26QBXOKKZBIL755RH3FVYGBTG36VVSVCNGBBK242LFZERYZKVJRTTKH4PN2GPPXRR6WVEYGRXRNDZAPRY332YM2RODLTGQ");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data","{\"isAddPoint\":true,\"sumPrice\":17.2,\"equipmentCode\":\"ceshi_code\",\"flowCodeOwn\":\"7845616561623456\",\"parentLists\":[{\"itemList\":[{\"name\":\"BOOK_MAGAZINE\",\"price\":3.4,\"quantity\":2.0,\"unit\":\"kg\"},{\"name\":\"CARD_BOARD_BOXES\",\"price\":5.2,\"quantity\":2.0,\"unit\":\"kg\"}],\"parentName\":\"PAPER\"}],\"sumPoint\":210,\"memberId\":\"20194176425738279099\",\"token\":\"F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CEQWJEMAIRCFOEPCSCWP6N5VXZUE5JRSLSAGIQADK5GV347M7FDSGP5FKG34NFYA2ULTVDEQJOCRU3CNTT6GOE3ATM4MXDWFNR5OJAVBXD2263AY7GYCIR2A3IESXHNQST3OFOHEYPYR3NTNYRR3GTPJCYX7TONQ6HYROE75NLAKN66ZED4IBJVO3TYE3IAMZWW4XBZDC4JSEA65AX7BHZQBR6LLA\"}");

        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"49a4b80ab9067f25370d4dcc343aafda");
        param.put("sign",sign);

        String s = "{\"app_key\":\"app_id_1\",\"data\":{\"cityId\":745},\"name\":\"memberAddress.memberAddressList\",\"format\":\"json\",\"sign\":\"FC9A826D26B306554FA6EEA9F5633520\",\"version\":\"1.0\",\"nonce\":1236131394899.91299,\"token\":\"3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24U65CVPQU32QS6WCOW4OQQ3AURAVX5JCW3DOJFI7QONQRCOYHSMZITFGRL7NE5YTTFKOD4CHYYW5XCU546HJVXIOTWEUH553LW7Q5I5HMBELSQBTBTYOQZEB2JBJEI7CERPJ5GXZGKOKLSCQFMEABHE2O75VTH57K7GHNNLDWI3HIJKS743QNBZCVVCNMPXWADKD4T3M3QCMWPAM7SRGIBEE27W26GDOMZF2JEZLQ\",\"timestamp\":\""+Calendar.getInstance().getTimeInMillis()+"\"}";

        System.out.println("请求的参数是 ："+JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是 ："+resultJson);
    }
}
