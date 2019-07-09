package api.ali;

import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.param.BOrderBean;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public class UtilApiTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:9003/app/api";

//        OkHttpClient client = new OkHttpClient();
//
//        File file=new File("E:\\timg.jpg");
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
//
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", "timg.jpg", fileBody)
//                .addFormDataPart("name", "util.upload")
//                .addFormDataPart("version", "1.0").addFormDataPart("format","json")
//                .addFormDataPart("token","3F3TEMH74565Q5QORHNPE76UZM6VT4JPWVV4OPUNTGAXLLRLC6B5GYU3LW34YHVNOEFL2LXPVT24UAJWCBI7NJ42KSYJ2KXG2OVQSA6ZMU4VMMCLQUKIRXAWTX2BD3K6MDOZDBJ4Q62CYGOB7DVAUP4CYQAHL3JSQRIG7P2UO77IZBN7W3E4RZK42VEEUWCHGAZLS7LGRB4EVIIYSQVYYSGAETEUZC4JUVVV2UDRKIOBGXURUGYCOGKTBVFLZYU2QFPF2G4I7DVNKBWCOFWBQDLZLJYEDSPIL6T46KLPZ4O2ZIFJROTQ")
//                .build();
//
//
//        Request request = new Request.Builder()
//                .url(api)
//                .post(requestBody)
//                .build();
//
//        Response response;
//        try {
//            response = client.newCall(request).execute();
//            String jsonString = response.body().string();
//
//            System.out.println(jsonString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        BOrderBean bOrderBean = new BOrderBean();
        bOrderBean.setCommunityId(1);
        bOrderBean.setCompanyId(1);
        bOrderBean.setCategoryId(2);
        bOrderBean.setOrderNo("2018");
        bOrderBean.setLinkMan("测试");
        //测试修改订单状态
        bOrderBean.setStatus("TOSEND");
        bOrderBean.setCancelReason("测试驳回状态");
        bOrderBean.setRecyclerId(2);
        
        Recyclers recyclers = new Recyclers();
        recyclers.setName("");
         bOrderBean.setRecyclers(recyclers);

        PageBean pageBean = new PageBean();
        pageBean.setPageNumber(1);
        pageBean.setPageSize(10); 
        bOrderBean.setPagebean(pageBean);

        bOrderBean.setId(12);
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","lex.check.before");
        param.put("version","1.0");  
        param.put("format","json");
        param.put("app_key","app_id_1");
        param.put("timestamp", System.currentTimeMillis());
//        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPIWVF7FTVZNCPSETCYWSIQUPXLGOFKMADQBRNAD3WA5QFHLLJYVHVHWSUDMQVNDVCVRZE4RUOLVUQBC63GQI4DTF5WKH7UBKDWL4LFJSOLLQZCZPOSELKKAZNC6HPFVSBUHIJPEBR2CYXGOWNFEBSK7U6QDXV3ZGNUG5G776EEZFAQNUU2B5LGKS42BZE7BIVVAX7BHZQBR6LLA");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID());
        param.put("data","{\"traceId\":\"4d448d6c65d145c690443c5fdd950953\",\"aliUserId\":\"15225253338\",\"name\":\"朱园圆\"}");
        
        String jsonStr = JSON.toJSONString(param);
        System.out.println(jsonStr);
        String sign = buildSign(JSON.parseObject(jsonStr), "sign_key_11223344");
//        System.out.println(sign);
        param.put("sign", sign);
//        System.out.println("app_key=app_id_1&data={\"aliUserId\":\"15225253338\",\"name\":\"瓜子壳\"}&format=json&name=lex.check&nonce=99b0a3ed-99c1-53ca-b113-b7811aa2a2c81562179558291&timestamp=1562179558291&version=1.0sign_key_11223344");
        System.out.println(ApiUtil.md5("app_key=app_id_1&data={\"aliUserId\":\"15225-253338\",\"name\":\"\"}&format=json&name=lex.check&nonce=99b0a3ed-99c1-53ca-b113-b7811aa2a2c81562341804411&timestamp=1562341804411&version=1.0sign_key_11223344"));
//        System.out.println(JSON.toJSONString(param));
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }

    public static String buildSign(Map<String, ?> paramsMap, String secret) throws IOException {
        Set<String> keySet = paramsMap.keySet();
        List<String> paramNames = new ArrayList(keySet);
        Collections.sort(paramNames);
        List<String> list = new ArrayList();
        Iterator var5 = paramNames.iterator();

        String paramName;
        while(var5.hasNext()) {
            paramName = (String)var5.next();
            String value = paramsMap.get(paramName).toString();
            if (StringUtils.isNotEmpty(paramName) && StringUtils.isNotEmpty(value)) {
                list.add(paramName + "=" + (value != null ? value : ""));
            }
        }

        String source = StringUtils.join(list, "&") + secret;
        System.out.println(source);
        paramName = ApiUtil.md5(source);
        System.out.println("02da1943e498347f99e201e48ea68209".equalsIgnoreCase("02DA1943E498347F99E201E48EA68209"));
        return paramName;
    }
}
