package mq;

import api.util.ApiUtils;
import api.util.TestConst;
import junit.framework.TestCase;
import org.junit.Test;


public class SendMQTest extends TestCase {



    @Test
    public void testSendMsg() throws Exception{
        String resultJson= ApiUtils.createCommonParam(TestConst.appid,TestConst.gateway,
                "jdkProxy",null,null,null);
        System.out.println(resultJson);
    }
}
