package api.amap;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import junit.framework.TestCase;

public class AmapTest extends TestCase {

    private String amap_key="43d675a28f264a241bd878df2c420176";

    /**
     * 测试高德逆地址编码
     * @throws Exception
     */
    public void testRegeo() throws Exception {
        String url="https://restapi.amap.com/v3/geocode/regeo";
        Response response= FastHttpClient.get().url(url)
                .addParams("key",amap_key)
                .addParams("location","116.481488,39.990464")
                .build().execute();
        String result=response.body().string();
        System.out.println(result);
    }
}
