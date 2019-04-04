import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.admin.param.RecyclersBean;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.module.easyopen.util.ApiUtil;

import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

public class RecyclersApiTest {
	public static void main(String[] args) throws Exception {
        String api="http://localhost:8080/admin/api";
        PageBean pageBean=new PageBean();
        pageBean.setPageNumber(1);
        pageBean.setPageSize(20);
        RecyclersBean bean=new RecyclersBean();
        bean.setRecycleCompany("挺之军");
        bean.setRecyclerId(1L);
        bean.setRecyclerName("测试");
        bean.setPage(pageBean);
        HashMap<String,Object> param=new HashMap<>();
        param.put("name","recycler.getPage");
        param.put("version","1.0");  
        param.put("format","json");
        param.put("app_key","app_id_4");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token","F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CERH5SHVK5VFUCPONTLHC6KMKD3C3NXPNJLJJOWWPISYS43FZFB2LZRZR727NDIP3VSIVPZ6MZAJ2K54KFBVXWWQRSAM4HFFTEPOYYE5POROKVB3AKKSJERWHBW72FCCMCBI4VPRR2YL6AJA6QMS77RCEZTE7WQSJSO57HUZMQPMHHHYPBBY42J3CO4DTSIGTIUPECLSQ7OQOF45AX7BHZQBR6LLA");
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",bean);

        String jsonStr=JSON.toJSONString(param);
        String sign= ApiUtil.buildSign(JSON.parseObject(jsonStr),"sign_key_998877");
        param.put("sign",sign);

        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        String resultJson=response.body().string();
        System.out.println(resultJson);
    }
}
