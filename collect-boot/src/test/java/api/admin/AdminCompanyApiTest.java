
/**
* @Title: AdminApiTest.java
* @date 2018年3月20日 下午12:52:44
* @version V1.0
* @author:[王池]
*/

package api.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.admin.param.AdminBean;
import com.tzj.collect.api.admin.param.CompanyBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

/**
* @ClassName: AdminApiTest
* @date 2018年3月20日 下午12:52:44
* @author:[王池]
*/

public class AdminCompanyApiTest {
	 public static void main(String[] args) throws Exception {
	        String api="http://localhost:8080/admin/api";	 
	        CompanyBean companybean = new CompanyBean();
//	        PageBean page = new PageBean();
//	        page.setPageNumber(1);
//	        page.setPageSize(5);
//	        companybean.setPageBean(page);
	        List<Integer> Category_id = new ArrayList<Integer>();
//	        List<Integer> listcommunityId = new ArrayList<Integer>();
//	        listcommunityId.add(17);
	        Category_id.add(1);
	        Category_id.add(3);
	        companybean.setCategory_id(Category_id);
	        companybean.setAreaId(52);
//	        companybean.setCommunityId(listcommunityId);
//	        companybean.setCountyId(4);
//	        companybean.setStreetId(53);
//	        companybean.setDelectCommunityId(1);
//	        companybean.setInsertCommunityId(2);
//	        companybean.setCompanyName("挺之军666");
//	        companybean.setAddress("浦东新区");
//	        companybean.setTelphone("13324347551");
	        companybean.setId((long)12);

	        HashMap<String,Object> param=new HashMap<>();
	        param.put("name","admin.company.editorCommunity");
	        param.put("version","1.0");  
	        param.put("format","json");
	        param.put("app_key","app_id_4");
	        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
	        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPIN3MOJMM2X26FUTCYWSIQUPXLGISYS43FZFB2LZXKU47QBX4MRCZN3JZLEBJCS7S4IBA2CH7QTDQMEZ4ANTEESM6UKRWRO77J2H7BKQLQFUOJRQAOI4AZRQIH46HAHJRN2RIRJH2PKY5INS6HGZXQIMI2JR36WLMNJL6LEEATJRAMH75T2XNQ3IV6SDRLCI4NAX7BHZQBR6LLA");
	        //param.put("sign","111");
	        param.put("nonce", UUID.randomUUID().toString());
	        param.put("data",companybean);

	        String jsonStr=JSON.toJSONString(param);
	        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_998877");
	        param.put("sign",sign);

	        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
	        String resultJson=response.body().string();
	        System.out.println(resultJson);
	    }
}
