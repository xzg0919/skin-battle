package api.business;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.entity.CompanyCategory;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

/**
* @ClassName: BusinessCategoryApiTest
* @author:
*/
public class BusinessCategoryApiTest {
	
	public static void main(String[] args) throws Exception{
		String api="http://localhost:9000/business/api";
		
		
		
		CompanyCategory newCompanyCategory = new CompanyCategory();
		
		newCompanyCategory.setCategoryId("31");
		newCompanyCategory.setPrice(1.4f);
		
//		CategoryBean categoryBean = new CategoryBean();
//		
//		categoryBean.setId("4");
		
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","businessCategory.updatePrice");
        param.put("version","1.0");  
        param.put("format","json");
        param.put("app_key","app_id_3");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CEQLIYHODEKHUB2OIRF4OQQDPUVILB4KKE2LXNZTE3HI2RXJHNJYLNV2NN4XJADFQAQ3MUWG2DJOBMFH5QIXSMXGCD62GYVOEVK26DGX3JW4DRMHCTCTFSJCYU5SKMSPJOCM6GA4YKNC5XNZ35I4S3AXTUAOMMY6RHXMGQ3PDRTL7JW55ONOKQUHCGHEQHWIBITHMBGXETITWTEFAX7BHZQBR6LLA");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",newCompanyCategory);
        
        String jsonStr = JSON.toJSONString(param);
        System.out.println(jsonStr);
        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), "sign_key_99aabbcc");
        param.put("sign", sign);

        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
