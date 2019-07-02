import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Tests {

    public static void main(String[] args) {
        String url = "https://restapi.amap.com/v3/config/district";
        String param = "page=1&key=43d675a28f264a241bd878df2c420176&subdistrict=3";

        String s = sendGet(url,param);


        JSONObject object = JSON.parseObject(s);
        Object districts = object.get("districts");


        System.out.println(districts);
        List<Object> objList= (List<Object>)JSONArray.fromObject(districts);
        for ( Object objects:objList) {
            CityBean cityBean=(CityBean)JSONObject.parseObject(objects.toString(), CityBean.class);
            System.out.println(cityBean.getCitycode());
            System.out.println(cityBean.getAdcode());
            System.out.println(cityBean.getName());
            List<CityBean> districts1 = cityBean.getDistricts();
            for (CityBean cityBean1:districts1){
                System.out.println(cityBean1.getCitycode());
                System.out.println(cityBean1.getAdcode());
                System.out.println(cityBean1.getName());
                List<CityBean> districts2 = cityBean1.getDistricts();
                for (CityBean cityBean2:districts2){
                    System.out.println(cityBean2.getCitycode());
                    System.out.println(cityBean2.getAdcode());
                    System.out.println(cityBean2.getName());
                    List<CityBean> districts3 = cityBean2.getDistricts();
                    for (CityBean cityBean3:districts3){
                        System.out.println(cityBean3.getCitycode());
                        System.out.println(cityBean3.getAdcode());
                        System.out.println(cityBean3.getName());
                    }
                }
            }
        }
    }



    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
