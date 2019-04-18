package api.ali;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.*;
import com.tzj.collect.api.app.param.OrderPayParam;
import com.tzj.collect.entity.OrderPic;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
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
                String token= JwtUtils.generateToken("172487", ALI_API_EXPRIRE,ALI_API_TOKEN_SECRET_KEY);
                String securityToken=JwtUtils.generateEncryptToken(token,ALI_API_TOKEN_CYPTO_KEY);
                System.out.println("token是 : "+securityToken);

                String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
                String key = CipherTools.initKey(tokenCyptoKey);
                String decodeToken = CipherTools.decrypt(securityToken, key);
                Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
                String subjectStr = claims.getSubject();
                System.out.println("反向編譯 token是："+subjectStr);

                //String api="http://open.mayishoubei.com/ali/api";
                String api="http://localhost:9090/ali/api";
//
                OrderBean orderbean = new OrderBean();
                orderbean.setCategoryId(73);
                orderbean.setCityId("737");
                orderbean.setArrivalTime("2019-01-01");
                orderbean.setArrivalPeriod("pm");
                orderbean.setPrice(new BigDecimal("-1"));
                orderbean.setAddress("上海市浦东新区洲海路100号");
                orderbean.setFullAddress("10栋101");
                orderbean.setTel("18375555555");
                orderbean.setLinkMan("测试大象");
                orderbean.setRemarks("我是回收物描述");
                orderbean.setIsMysl("0");

                OrderPic orderPic = new OrderPic();
                orderPic.setSmallPic("http://images.sqmall.top/collect/20180727/bigpicture_140f4f1a-a415-41be-a0f9-ee1e53c49977.jpg");
                orderPic.setOrigPic("http://images.sqmall.top/collect/20180727/bigpicture_140f4f1a-a415-41be-a0f9-ee1e53c49977.jpg");
                orderPic.setPicUrl("http://images.sqmall.top/collect/20180727/bigpicture_140f4f1a-a415-41be-a0f9-ee1e53c49977.jpg");
                orderbean.setOrderPic(orderPic);

                OrderItemBean orderItemBean = new OrderItemBean();
                orderItemBean.setCategoryAttrOppIds("326,330,334,338");
                orderbean.setOrderItemBean(orderItemBean);


                OrderBean orderbean1 = new OrderBean();
               orderbean1.setId(9095);

                OrderPayParam orderPayParam = new OrderPayParam();
                orderPayParam.setOrderId(8841);
                orderPayParam.setPrice(new BigDecimal("1"));

            CategoryBean categoryBean = new CategoryBean();
            categoryBean.setId(103);

                HashMap<String,Object> param=new HashMap<>();
                param.put("name","order.updateForest");
                param.put("version","1.0");
                param.put("format","json");
                param.put("app_key","app_id_1");
                param.put("timestamp", Calendar.getInstance().getTimeInMillis());
                param.put("token",securityToken);
                //param.put("sign","111");
                param.put("nonce", UUID.randomUUID().toString());
                param.put("data",orderbean1);

                String jsonStr=JSON.toJSONString(param);
                String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_11223344");
                param.put("sign",sign);

                String s = "{\"app_key\":\"app_id_1\",\"data\":{\"cityId\":745},\"name\":\"memberAddress.memberAddressList\",\"format\":\"json\",\"sign\":\"FC9A826D26B306554FA6EEA9F5633520\",\"version\":\"1.0\",\"nonce\":1236131394899.91299,\"token\":\"3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24U65CVPQU32QS6WCOW4OQQ3AURAVX5JCW3DOJFI7QONQRCOYHSMZITFGRL7NE5YTTFKOD4CHYYW5XCU546HJVXIOTWEUH553LW7Q5I5HMBELSQBTBTYOQZEB2JBJEI7CERPJ5GXZGKOKLSCQFMEABHE2O75VTH57K7GHNNLDWI3HIJKS743QNBZCVVCNMPXWADKD4T3M3QCMWPAM7SRGIBEE27W26GDOMZF2JEZLQ\",\"timestamp\":\""+Calendar.getInstance().getTimeInMillis()+"\"}";

                System.out.println("请求的参数是 ："+JSON.toJSONString(param));
                Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
                String resultJson=response.body().string();
                System.out.println("返回的参数是 ："+resultJson);
        }
}
