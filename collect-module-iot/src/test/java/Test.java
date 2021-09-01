import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tzj.collect.core.param.iot.IotParamBean;
import com.tzj.collect.core.service.EquipmentMessageService;
import com.tzj.collect.core.service.MemberService;
import com.tzj.collect.core.service.OrderService;
import com.tzj.collect.entity.CompanyEquipment;
import com.tzj.collect.entity.Member;
import com.tzj.iot.IoTApplication;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.utils.security.CipherTools;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_CYPTO_KEY;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_TOKEN_SECRET_KEY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/5/6 14:47
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IoTApplication.class})
public class Test {

    @Autowired
    OrderService orderService;
    @Autowired
    MemberService memberService;
    @Autowired
    EquipmentMessageService equipmentMessageService;

    @org.junit.Test
    public  void creatIotOrderByMqtt() {

        String message = "{\"clientTopic\":\"c33506b6\",\"code\":\"10000\",\"equipmentCode\":\"c33506b6\",\"parentLists\":[{\"itemList\":[{\"name\":\"BEVERAGE_BOTTLES\",\"price\":\"\",\"quantity\":\"1\",\"unit\":\"个\"}],\"parentName\":\"PLASTIC\"}],\"sumPrice\":\"0\",\"token\":\"3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24VVHUSAVURD6DYOV45YR6FEPV4LMTKDYUTDHHFI7YJ23R2CDMCSECYPEOATSP4LHG7PY3WY5F5BHFX2MU2FP5UTXCOMQACX7Y4GOKIZRGFIH2UEM5ADGGQ44JKACWDS27K5BOAZXI4RMKW4KZO46XW3IUVMW7PCBWLVBYJE4AI3YZUTPDFYRLG64OUUWNLBEVUNE5FS5JFMJHE7CWUQASPTAQ3YW6VZV5FVTQJ5Y5PCYP7G2CTU7SMTJ2I2YH4SV4W\"}";
        IotParamBean iotParamBean = new IotParamBean();
        JSONObject object = JSON.parseObject(message);
        String subjectStr = "";
        //验证token是否有效，若无效，直接丢弃消息
        try {
            Map<String, Object> messageMap = new HashMap<>();
            String token = object.get("token") + "";
            if (StringUtils.isEmpty(token)) {
                //说明接收到的消息token不存在，直接丢弃掉
                return;
            } else {
                //解析token(获取用户阿里userId)
                String tokenCyptoKey = ALI_API_TOKEN_CYPTO_KEY;
                String key = CipherTools.initKey(tokenCyptoKey);
                String decodeToken = CipherTools.decrypt(token, key);
                Claims claims = JwtUtils.getClaimByToken(decodeToken, ALI_API_TOKEN_SECRET_KEY);
                subjectStr = claims.getSubject();
            }
            if (!StringUtils.isEmpty(subjectStr)) {
                System.out.println("---------------ali_uid:" + subjectStr + "--------------------------");
                iotParamBean = new IotParamBean();
                iotParamBean.setSumPrice(new BigDecimal(object.get("sumPrice").toString()));
                iotParamBean.setEquipmentCode(object.get("equipmentCode").toString());
                Member member = memberService.selectMemberByAliUserId(subjectStr);
                iotParamBean.setMemberId(member.getCardNo());
                iotParamBean.setParentLists(object.getJSONArray("parentLists").toJavaList(IotParamBean.ParentList.class));
                //消息保存成功之后再处理
                orderService.iotCreatOrder(iotParamBean);
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_SUCCESS.getKey());
                messageMap.put("message", CompanyEquipment.EquipmentAction.EquipmentActionCode.UPLOAD_SUCCESS.getValue());
            } else {
                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                messageMap.put("message", "解析用户信息错误:" + CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getValue());
            }


        } catch (Exception e) {
            //说明接收到的消息有问题，直接丢弃掉
            Map<String, Object> messageMap = new HashMap<>();

                messageMap.put("code", CompanyEquipment.EquipmentAction.EquipmentActionCode.ERROR.getKey());
                messageMap.put("message", "消息错误---" + e.getMessage());

        }
    }
}
