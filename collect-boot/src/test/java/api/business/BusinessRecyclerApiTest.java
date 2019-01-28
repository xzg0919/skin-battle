package api.business;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.collect.api.business.param.BusinessRecyclerBean;
import com.tzj.collect.api.business.param.CompanyAccountBean;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

public class BusinessRecyclerApiTest {
public static void main(String[] args) throws Exception {
	String api="http://localhost:8080/business/api";
	
	BusinessRecyclerBean BRecyclerBean = new BusinessRecyclerBean();
	
	PageBean page = new PageBean();
	page.setPageNumber(1);
	page.setPageSize(10);
	BRecyclerBean.setPage(page);
//	BRecyclerBean.setRecyclerName("1385641158");
//	BRecyclerBean.setRecyclerId((long)2);
	BRecyclerBean.setCompanyId((long)1);
//	BRecyclerBean.setDelflag("1");
//	BRecyclerBean.setApplyStatus("2");
    HashMap<String,Object> param=new HashMap<>();
    param.put("name","business.search.getRecyclerList");
    param.put("version","1.0");
    param.put("format","json");
    param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPJE2EAOG5T5HXX5OUXAQQN6M6J2KWWDFV5SIYZGJ7YJO6Z5QCC3M5MZCL6HII774ZDCLAGJJMFX6ZUJ4Y4P4FA5PVUWKZDOHRJJJDHBDWST5D2RHHGV5RJ6IOGFNMLXJ2DZABERURFCN4N6UBSSEYD7LX4XQ674DXHIG7EY7ULDS4A26ZYVUM3JQKGQNQLB3NNAX7BHZQBR6LLA");
    param.put("app_key", "app_id_3");
    param.put("timestamp", Calendar.getInstance().getTimeInMillis());
    param.put("nonce", UUID.randomUUID().toString());
    param.put("data",BRecyclerBean);

    String jsonStr = JSON.toJSONString(param);
    String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_99aabbcc");
    param.put("sign", sign);

    Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
    String resultJson=response.body().string();
    System.out.println(resultJson);
}
}
