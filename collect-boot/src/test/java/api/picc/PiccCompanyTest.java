package api.picc;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.ali.param.PiccOrderBean;
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

public class PiccCompanyTest {
    public static void main(String[] args) throws Exception{
        String token = JwtUtils.generateToken("1",PICC_API_EXPRIRE, PICC_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, PICC_API_TOKEN_CYPTO_KEY);
        System.out.println("生成的token是："+securityToken);

        String tokenCyptoKey = PICC_API_TOKEN_CYPTO_KEY;
        String key = CipherTools.initKey(tokenCyptoKey);
        String decodeToken = CipherTools.decrypt(securityToken, key);
        Claims claims = JwtUtils.getClaimByToken(decodeToken, PICC_API_TOKEN_SECRET_KEY);
        String subjectStr = claims.getSubject();
        System.out.println("反向編譯 token是："+subjectStr);

        String api="http://localhost:9000/picc/api";

//        PiccInsurancePolicyBean piccInsurancePolicyBean = new PiccInsurancePolicyBean();
//        piccInsurancePolicyBean.setTitle("测试保险标题2修改");
//        piccInsurancePolicyBean.setInitPrice(3000);
//        piccInsurancePolicyBean.setUnderwritingPrice(5000);
//        piccInsurancePolicyBean.setId("656");

//        List<PiccInsurancePolicyAgreementBean> piccInsurancePolicyAgreementBeanList = new ArrayList<>();
//        List<PiccInsurancePolicyContentBean> piccInsurancePolicyContentBeanList = new ArrayList<>();
//        PiccInsurancePolicyContentBean piccInsurancePolicyContentBean1 = new PiccInsurancePolicyContentBean();
//        piccInsurancePolicyContentBean1.setContent("我是保障内容3修改");
//        piccInsurancePolicyContentBean1.setInsurancePrice(500);
//        piccInsurancePolicyContentBean1.setContentId("655");
//        PiccInsurancePolicyContentBean piccInsurancePolicyContentBean2 = new PiccInsurancePolicyContentBean();
//        piccInsurancePolicyContentBean2.setContent("我是保障内容4");
//        piccInsurancePolicyContentBean2.setInsurancePrice(600);
//        piccInsurancePolicyContentBean2.setContentId("656");
//        PiccInsurancePolicyContentBean piccInsurancePolicyContentBean3 = new PiccInsurancePolicyContentBean();
//        piccInsurancePolicyContentBean3.setContent("我是保障内容5新增");
//        piccInsurancePolicyContentBean3.setInsurancePrice(600);
//        piccInsurancePolicyContentBeanList.add(piccInsurancePolicyContentBean1);
//        piccInsurancePolicyContentBeanList.add(piccInsurancePolicyContentBean2);
//        piccInsurancePolicyContentBeanList.add(piccInsurancePolicyContentBean3);
//
//        PiccInsurancePolicyAgreementBean piccInsurancePolicyAgreementBean1 = new PiccInsurancePolicyAgreementBean();
//        piccInsurancePolicyAgreementBean1.setAgreementName("保障协议名称3修改");
//        piccInsurancePolicyAgreementBean1.setAgreementUrl("http://www.baidu.com");
//        piccInsurancePolicyAgreementBean1.setAgreementId("655");
//        PiccInsurancePolicyAgreementBean piccInsurancePolicyAgreementBean2 = new PiccInsurancePolicyAgreementBean();
//        piccInsurancePolicyAgreementBean2.setAgreementName("保障协议名称4");
//        piccInsurancePolicyAgreementBean2.setAgreementUrl("http://www.baidu.com");
//        piccInsurancePolicyAgreementBean2.setAgreementId("656");
//        PiccInsurancePolicyAgreementBean piccInsurancePolicyAgreementBean3 = new PiccInsurancePolicyAgreementBean();
//        piccInsurancePolicyAgreementBean3.setAgreementName("保障协议名称5新增");
//        piccInsurancePolicyAgreementBean3.setAgreementUrl("http://www.baidu.com");
//        piccInsurancePolicyAgreementBeanList.add(piccInsurancePolicyAgreementBean1);
//        piccInsurancePolicyAgreementBeanList.add(piccInsurancePolicyAgreementBean2);
//        piccInsurancePolicyAgreementBeanList.add(piccInsurancePolicyAgreementBean3);
//
//        piccInsurancePolicyBean.setPiccInsurancePolicyContentBeanList(piccInsurancePolicyContentBeanList);
//        piccInsurancePolicyBean.setPiccInsurancePolicyAgreementBeanList(piccInsurancePolicyAgreementBeanList);

        PiccOrderBean piccOrderBean = new PiccOrderBean();
        piccOrderBean.setMemberName("王");
        piccOrderBean.setPageBean(new PageBean());
        piccOrderBean.setStartTime("2018-12-25");
        piccOrderBean.setEndTime("2018-12-28");
        piccOrderBean.setAuditingStartTime("2018-12-25");
        piccOrderBean.setAuditingEndTime("2018-12-28");

        HashMap<String,Object> param=new HashMap<>();
        param.put("name","picc.addPiccOrderExcel");
        param.put("version","1.0");
        param.put("app_key","app_id_7");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token",securityToken);
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",null);

        String jsonStr= JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_77bbccdd");
        param.put("sign",sign);


        System.out.println("请求的参数是 ："+JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println("返回的参数是 ："+resultJson);
    }

}
