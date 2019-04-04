package api.ali;

import okhttp3.*;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.param.BOrderBean;
import com.tzj.collect.entity.Recyclers;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
public class UtilApiTest {
    public static void main(String[] args) throws Exception {
        String api="http://localhost:8080/ali/api";

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
        param.put("name","business.order.updateOdrerStatus");
        param.put("version","1.0");  
        param.put("format","json");
        param.put("app_key","app_id_3");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPIWVF7FTVZNCPSETCYWSIQUPXLGOFKMADQBRNAD3WA5QFHLLJYVHVHWSUDMQVNDVCVRZE4RUOLVUQBC63GQI4DTF5WKH7UBKDWL4LFJSOLLQZCZPOSELKKAZNC6HPFVSBUHIJPEBR2CYXGOWNFEBSK7U6QDXV3ZGNUG5G776EEZFAQNUU2B5LGKS42BZE7BIVVAX7BHZQBR6LLA");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",bOrderBean);
        
        String jsonStr = JSON.toJSONString(param);
        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_99aabbcc");
        param.put("sign", sign);

        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
