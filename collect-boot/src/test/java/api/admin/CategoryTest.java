package api.admin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.admin.param.AdminBean;
import com.tzj.collect.api.admin.param.CategoryAttrBean;
import com.tzj.collect.api.admin.param.CategoryBean;
import com.tzj.collect.api.admin.param.CompanyCategoryBean;
import com.tzj.collect.entity.Category;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

public class CategoryTest {
	public static void main(String[] args) throws Exception {
        String api="http://localhost:8080/admin/api";
        AdminBean bean= new AdminBean();
        bean.setUsername("admin");
        bean.setPassword("admin");
        CompanyCategoryBean companyCategoryBean = new CompanyCategoryBean();
       // companyCategoryBean.setCompanyId("1");
       // companyCategoryBean.setCategoryId("18");
        
        Category category = new Category();
        category.setName("测试");
        companyCategoryBean.setCategory(category);
        
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setId((long)16);
       // categoryBean.setIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521109229886&di=873cf8e184286446af3e9c5653641590&imgtype=0&src=http://pic.58pic.com/58pic/13/95/89/56M58PICZBR_1024.jpg");
       // categoryBean.setName("豪华大彩电");
       // categoryBean.setPrice(new BigDecimal("887"));
       // categoryBean.setMarketPrice(new BigDecimal("999"));
        
        CategoryAttrBean categoryAttrBean = new CategoryAttrBean();
       // categoryAttrBean.setCategoryAttrOptionId("15");
        
       // categoryAttrBean.setCategoryId(16);
        categoryAttrBean.setId("8");
       // categoryAttrBean.setName("豪华规格改");
       // categoryAttrBean.setCategoryAttrOptionIds("15,16");
       // categoryAttrBean.setCategoryAttrOptionNames("豪华单开门改,豪华双开门改");
       // categoryAttrBean.setCategoryAttrOptionPrices("55,66");
        
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","category.getCategoryList");
        param.put("version","1.0");  
        param.put("format","json");
        param.put("app_key","app_id_4");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPIWVF7FTVZNCPSETCYWSIQUPXLGOFKMADQBRNAD3WA5QFHLLJYVHVHWSUDMQVNDVCVRZE4RUOLVUQBC63GQI4DTF5WKH7UBKDWL4LFJSOLLQZCZPOSELKKAZNC6HPFVSBUHIJPEBR2CYXGOWNFEBSK7U6QDXV3ZGNUG5G776EEZFAQNUU2B5LGKS42BZE7BIVVAX7BHZQBR6LLA");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",null);

        String jsonStr=JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_998877");
        param.put("sign",sign);

        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
