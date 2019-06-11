package api.amap;

import api.util.ApiUtils;
import api.util.TestConst;
import com.tzj.collect.api.ali.param.AmapAroundParam;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import junit.framework.TestCase;
import org.junit.Test;

public class AmapTest extends TestCase {

    private String amap_key="43d675a28f264a241bd878df2c420176";

    /**
     * 测试高德逆地址编码
     * @throws Exception
     */
    @Test
    public void testRegeo() throws Exception {
        String url="https://restapi.amap.com/v3/geocode/regeo";
        Response response= FastHttpClient.get().url(url)
                .addParams("key",amap_key)
                .addParams("location","121.557677,31.284158")
                .build().execute();
        String result=response.body().string();
        System.out.println(result);
    }

    @Test
    public void testAround() throws Exception {
        String url="https://restapi.amap.com/v3/place/around";
        Response response= FastHttpClient.get().url(url)
                .addParams("key",amap_key)
                .addParams("location","121.551919,31.281297")
                //.addParams("keywords","紫藤苑")
                .addParams("offset","20")
                .addParams("page","2")
                //.addParams("extensions","all")
                .build().execute();
        String result=response.body().string();
        System.out.println(result);
    }

    @Test
    public void testAmapApiRegeo() throws Exception{
        String resultJson= ApiUtils.createCommonParam(TestConst.appid,TestConst.gateway,
                "amap.regeo","121.557677,31.284158",TestConst.token,null);
        System.out.println(resultJson);
    }

    @Test
    public void testAmapApiRound() throws Exception{

        AmapAroundParam aroundParam=new AmapAroundParam();
        aroundParam.setOffset("50");
        aroundParam.setPage("1");
        aroundParam.setLocation("121.557677,31.284158");

        String resultJson= ApiUtils.createCommonParam(TestConst.appid,TestConst.gateway,
                "amap.around",aroundParam,TestConst.token,null);
        System.out.println(resultJson);
    }
}
