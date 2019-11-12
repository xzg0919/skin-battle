package com.tzj.collect.common.utils;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.core.param.json.AmapRegeoJson;
import com.tzj.collect.core.result.ali.AmapResult;
import com.tzj.module.easyopen.exception.ApiException;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmapUtil {


    private static Logger logger = LoggerFactory.getLogger(AmapUtil.class);

    public static  AmapResult getAmap(String location) throws Exception{
        if(StringUtils.isBlank(location)){
            throw new ApiException("经纬度不能为空");
        }

        AmapResult result = new AmapResult();

        String url = "https://restapi.amap.com/v3/geocode/regeo";
        Response response = FastHttpClient.get().url(url)
                .addParams("key", AmapConst.AMAP_KEY)
                .addParams("location", location)
                //.addParams("extensions","all")
                //.addParams("poitype","120000|120100|120200|120201|120202|120203|120300|120301|120302|120303|120304")
                //.addParams("homeorcorp","1")
                .build().execute();

        String resultJson = response.body().string();
        if (logger.isDebugEnabled()) {
            logger.debug(resultJson);
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(resultJson)) {

            resultJson = resultJson.replaceAll("\n", "");

            AmapRegeoJson amapRegeoJson = JSON.parseObject(resultJson, AmapRegeoJson.class);
            if (amapRegeoJson != null && "1".equals(amapRegeoJson.getStatus())) {
                AmapRegeoJson.RegeocodeBean regeocodeBean = amapRegeoJson.getRegeocode();
                if (regeocodeBean != null) {
                    //有地址信息
                    String address = regeocodeBean.getFormatted_address();
                    AmapRegeoJson.RegeocodeBean.AddressComponentBean addressComponentBean = regeocodeBean.getAddressComponent();
                    if (addressComponentBean != null) {
                        String city = "";
                        if (addressComponentBean.getCity() != null && addressComponentBean.getCity().size() > 0) {
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
                            if (province.endsWith("市")) {
                                result.setCity(province);
                            } else {
                                result.setCity(district);
                            }
                        } else {
                            result.setCity(city);
                        }

                        /*String name = "";
                        if (building != null) {
                            if (building.getName() != null && building.getName().size() > 0) {
                                name = building.getName().get(0).toString();
                                result.setName(name);
                            }
                        }*/

                        if (neighborhood != null) {
                            if (neighborhood.getName() != null && neighborhood.getName().size() > 0) {
                                String neighborhoodName = neighborhood.getName().get(0).toString();
                                result.setNeighborhood(neighborhoodName);

                                if (org.apache.commons.lang3.StringUtils.isBlank(result.getName())) {
                                    result.setName(neighborhoodName);
                                }
                            }
                        }


                        //还为空，那么截取
                        String formattedName = address.substring(address.lastIndexOf(township) + township.length(), address.length());
                        result.setFormattedAd(formattedName);
                        if (org.apache.commons.lang3.StringUtils.isBlank(result.getName())) {
                            result.setName(formattedName);
                        }


                    }
                }
            } else {
                throw new ApiException("调用高德地图服务出错啦！原因：" + amapRegeoJson.getInfo());
            }
        } else {
            throw new ApiException("调用高德地图服务出错啦！原因：高德API结果返回为空！");
        }

        return result;
    }
}
