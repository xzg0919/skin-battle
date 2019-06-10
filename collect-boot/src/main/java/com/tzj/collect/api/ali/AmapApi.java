package com.tzj.collect.api.ali;

import com.alibaba.fastjson.JSON;
import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.json.AmapRegeoJson;
import com.tzj.collect.api.ali.result.AmapResult;
import com.tzj.collect.common.constant.AmapConst;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.easyopen.doc.annotation.ApiDoc;
import com.tzj.module.easyopen.doc.annotation.ApiDocField;
import com.tzj.module.easyopen.doc.annotation.ApiDocMethod;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 高德地图相关API
 */
@ApiService
@ApiDoc(value = "高德模块",appModule = "ali")
public class AmapApi {

    private static Logger logger = LoggerFactory.getLogger(AmapApi.class);

    /**
     * 根据经纬度获取地址详细信息
     * @param location
     * @return
     */
    @Api(name = "amap.regeo", version = "1.0",ignoreSign = true)
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    @ApiDocMethod(description="当前位置详细信息")
            public AmapResult regeo(String location) throws Exception{
        AmapResult result=new AmapResult();

        String url="https://restapi.amap.com/v3/geocode/regeo";
        Response response= FastHttpClient.get().url(url)
                .addParams("key", AmapConst.AMAP_KEY)
                .addParams("location",location)
                .build().execute();

        String resultJson=response.body().string();
        if(logger.isDebugEnabled()){
            logger.debug(resultJson);
        }

        if(org.apache.commons.lang3.StringUtils.isNotBlank(resultJson)) {

            resultJson=resultJson.replaceAll("\n","");

            AmapRegeoJson amapRegeoJson = JSON.parseObject(resultJson, AmapRegeoJson.class);
            if ("1".equals(amapRegeoJson.getStatus())) {
                AmapRegeoJson.RegeocodeBean regeocodeBean = amapRegeoJson.getRegeocode();
                if (regeocodeBean != null) {
                    //有地址信息
                    String address = regeocodeBean.getFormatted_address();
                    AmapRegeoJson.RegeocodeBean.AddressComponentBean addressComponentBean = regeocodeBean.getAddressComponent();
                    if (addressComponentBean != null) {
                        String city="";
                        if(addressComponentBean.getCity()!=null && addressComponentBean.getCity().size()>0){
                            city = addressComponentBean.getCity().get(0).toString();
                        }

                        String province = addressComponentBean.getProvince();
                        String adcode = addressComponentBean.getAdcode();
                        String district = addressComponentBean.getDistrict();
                        String towncode = addressComponentBean.getTowncode();
                        String township = addressComponentBean.getTownship();
                        String citycode = addressComponentBean.getCitycode();

                        AmapRegeoJson.RegeocodeBean.AddressComponentBean.StreetNumberBean streetNumber = addressComponentBean.getStreetNumber();
                        AmapRegeoJson.RegeocodeBean.AddressComponentBean.BuildingBean building = addressComponentBean.getBuilding();
                        AmapRegeoJson.RegeocodeBean.AddressComponentBean.NeighborhoodBean neighborhood = addressComponentBean.getNeighborhood();

                        result.setCitycode(citycode);
                        result.setLocation(location);
                        result.setProvince(province);
                        result.setAdcode(adcode);
                        result.setDistrict(district);
                        result.setTowncode(towncode);
                        result.setTownship(township);
                        result.setAddress(address);
                        if (StringUtils.isEmpty(city)) {
                            result.setCity(province);
                        } else {
                            result.setCity(city);
                        }

                        String name = "";
                        if (building != null) {
                            if (building.getName() != null && building.getName().size() > 0) {
                                name = building.getName().get(0).toString();
                            }
                        }

                        if (org.apache.commons.lang3.StringUtils.isBlank(name) && neighborhood != null) {
                            if (neighborhood.getName() != null && neighborhood.getName().size() > 0) {
                                name = neighborhood.getName().get(0).toString();
                            }
                        }

                        if (org.apache.commons.lang3.StringUtils.isBlank(name)) {
                            //还为空，那么截取
                            name = address.substring(address.lastIndexOf(township)+township.length(), address.length());
                        }

                        result.setName(name);

                    }
                }
            } else {
                throw new ApiException("调用高德地图服务出错啦！原因：" + amapRegeoJson.getInfo());
            }
        }else{
            throw new ApiException("调用高德地图服务出错啦！原因：高德API结果返回为空！");
        }

        return result;
    }

    @Api(name = "amap.around", version = "1.0",ignoreSign = true)
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    @ApiDocMethod(description="周边搜索",remark = "关键字可以为空", results={@ApiDocField(description="周边地址信息",name="amapResults", elementClass=AmapResult.class)})
    public List<AmapResult> around(String location,String searchKey,String offset,String page){
        ArrayList<AmapResult> amapResults=new ArrayList<>();

        return amapResults;
    }
}
