package com.tzj.collect.api.admin;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.admin.TransStationBean;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.api.utils.SignUtils;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;

import java.util.*;

import static com.tzj.collect.common.constant.TokenConst.*;

/**
 * 中转站管理
 * @author sgmark
 * @create 2018-12-25 9:28
 **/
@ApiService
public class AdminStationApi {


    /**
     * 中转站列表
     * @author wangcan
     * @param
     * @return
     */
    @Api(name = "admin.getTransStationList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getTransStationList(TransStationBean transStationBean) throws Exception{
        return this.httpPost(transStationBean,"admin.getTransStationList");
    }

    /**
     * 中转公司列表
     * @author wangcan
     * @param
     * @return
     */
    @Api(name = "admin.getTransCompanyList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getTransCompanyList() throws Exception{
        return this.httpPost(null,"admin.getTransCompanyList");
    }
    /**
     * 根据中转站Id查询详情
     * @author wangcan
     * @param
     * @return
     */
    @Api(name = "admin.getTransStationDetails", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object getTransStationDetails(TransStationBean transStationBean) throws Exception{
        return this.httpPost(transStationBean,"admin.getTransStationDetails");
    }

    /**
     * 保存/更改中转站信息
     * @author wangcan
     * @param
     * @return
     */
    @Api(name = "admin.updateTransStion", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object updateTransStion(TransStationBean transStationBean) throws Exception{
        return this.httpPost(transStationBean,"admin.updateTransStion");
    }
    /**
     * 启用/禁用中转站信息
     * @author wangcan
     * @param
     * @return
     */
    @Api(name = "admin.updateIsEnableTransStion", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object updateIsEnableTransStion(TransStationBean transStationBean) throws Exception{
        return this.httpPost(transStationBean,"admin.updateIsEnableTransStion");
    }


    public Object httpPost(TransStationBean transStationBean,String name) throws Exception{
        String token = JwtUtils.generateToken("1", ADMIN_API_EXPRIRE, ADMIN_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ADMIN_API_TOKEN_CYPTO_KEY);
        String api="http://localhost:9001/admin/api";

        HashMap<String,Object> param=new HashMap<>();
        param.put("name",name);
        param.put("version","1.0");
        param.put("format","json");
        param.put("timestamp", Calendar.getInstance().getTimeInMillis());
        param.put("token",securityToken);
        //param.put("sign","111");
        param.put("nonce", UUID.randomUUID().toString());
        param.put("data",transStationBean);
        String signKey = SignUtils.produceSignKey(token, ADMIN_API_TOKEN_SIGN_KEY);
        String jsonStr = JSON.toJSONString(param);
        String sign = ApiUtil.buildSign(JSON.parseObject(jsonStr), signKey);
        param.put("sign", sign);
        Response response= FastHttpClient.post().url(api).body(JSON.toJSONString(param)).build().execute();
        Object data = JSON.parseObject(response.body().string()).get("data");
        return data;
    }

}
