package com.tzj.collect.api.ali;

import com.alibaba.fastjson.JSON;
import com.tzj.collect.common.amp.AmapRegeoJson;
import com.tzj.collect.common.amp.AmapRoundJson;
import com.tzj.collect.common.constant.AmapConst;
import com.tzj.collect.core.param.ali.AmapAroundParam;
import com.tzj.collect.core.result.ali.AmapResult;
import com.tzj.collect.core.service.CommunityService;
import com.tzj.collect.entity.Community;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.easyopen.doc.annotation.ApiDoc;
import com.tzj.module.easyopen.doc.annotation.ApiDocField;
import com.tzj.module.easyopen.doc.annotation.ApiDocMethod;
import com.tzj.module.easyopen.exception.ApiException;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.tzj.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 高德地图相关API
 */
@ApiService
@ApiDoc(value = "高德模块", appModule = "ali")
public class AmapApi {

    private static Logger logger = LoggerFactory.getLogger(AmapApi.class);

    @Resource
    private CommunityService communityService;

    /**
     * 更新小区的高德名称
     */
    @Api(name = "amap.update", version = "1.0", ignoreSign = true)
    public void completeCommunity() throws Exception {
        List<Community> communityList = communityService.list20CommunityByAmapNameNull();
        for (Community community : communityList) {
            String longitude = community.getLongitude().toString();
            String latitude = community.getLatitude().toString();
            AmapResult amapResult = regeo(longitude + "," + latitude);
            communityService.updateCommunityAmapName(amapResult.getName(), amapResult.getNeighborhood(), amapResult.getFormattedAd(), community.getId());
        }
    }

    /**
     * 根据经纬度获取地址详细信息
     *
     * @param location
     * @return
     */
    @Api(name = "amap.regeo", version = "1.0", ignoreSign = true)
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    @ApiDocMethod(description = "当前位置详细信息")
    public AmapResult regeo(String location) throws Exception {

        Assert.hasLength(location, "请输入当前经纬度");

       return this.getAmap(location);

    }

    @Api(name = "amap.around", version = "1.0", ignoreSign = true)
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    @ApiDocMethod(description = "周边搜索", remark = "关键字可以为空", results = {@ApiDocField(description = "周边地址信息", name = "amapResults", elementClass = AmapResult.class)})
    public List<AmapResult> around(AmapAroundParam aroundParam) throws Exception {

        Assert.notNull(aroundParam);
        Assert.hasLength(aroundParam.getLocation(), "请输入当前经纬度");
        Assert.hasLength(aroundParam.getOffset(), "请输入每页条数");
        Assert.hasLength(aroundParam.getPage(), "请输入页数");

        ArrayList<AmapResult> amapResults = new ArrayList<>();

        String url = "https://restapi.amap.com/v3/place/around";
        Response response = FastHttpClient.get().url(url)
                .addParams("key", AmapConst.AMAP_KEY)
                .addParams("location", aroundParam.getLocation())
                .addParams("keywords", StringUtils.isBlank(aroundParam.getSearchKey()) ? "" : aroundParam.getSearchKey())
                .addParams("offset", aroundParam.getOffset())
                .addParams("page", aroundParam.getPage())
                .build().execute();

        String resultJson = response.body().string();
        if (logger.isDebugEnabled()) {
            logger.debug(resultJson);
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(resultJson)) {
            resultJson = resultJson.replaceAll("\n", "");
            AmapRoundJson amapRoundJson = JSON.parseObject(resultJson, AmapRoundJson.class);
            if (amapRoundJson != null && "1".equals(amapRoundJson.getStatus())) {
                if (amapRoundJson.getPois() != null && amapRoundJson.getPois().size() > 0) {

                    for (AmapRoundJson.PoisBean poisBean : amapRoundJson.getPois()) {
                        AmapResult amapResult = new AmapResult();

                        String name = poisBean.getName();
                        String address = poisBean.getAddress();
                        String poisLocation = poisBean.getLocation();

                        amapResult.setName(name);
                        amapResult.setAddress(address);
                        amapResult.setLocation(poisLocation);

                        amapResults.add(amapResult);
                    }
                }
            } else {
                throw new ApiException("调用高德地图服务出错啦！原因：" + amapRoundJson.getInfo());
            }

        } else {
            throw new ApiException("调用高德地图服务出错啦！原因：高德API结果返回为空！");
        }


        return amapResults;
    }

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
