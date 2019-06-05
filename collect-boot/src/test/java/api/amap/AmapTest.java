package api.amap;

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
                .addParams("location","121.551919,31.281297")
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
                .build().execute();
        String result=response.body().string();
        System.out.println(result);
    }
}
