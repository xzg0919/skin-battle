package mq;

import api.util.ApiUtils;
import api.util.TestConst;
import com.tzj.collect.Application;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;


public class SendMQTest extends TestCase {



    @Test
    public void testSendMsg() throws Exception{
        String resultJson= ApiUtils.createCommonParam(TestConst.appid,TestConst.gateway,
                "keySearch.save",null,null,null);
        System.out.println(resultJson);
    }
}
