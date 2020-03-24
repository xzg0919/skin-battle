package com.tzj.collect.api.ali;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tzj.collect.common.amap.AmapConst;
import com.tzj.collect.common.amap.AmapRegeoJson;
import com.tzj.collect.common.amap.AmapResult;
import com.tzj.collect.common.amap.AmapRoundJson;
import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;
import com.tzj.collect.core.param.ali.AmapAroundParam;
import com.tzj.collect.core.service.CommunityService;
import com.tzj.collect.core.service.MapService;
import com.tzj.collect.entity.Community;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.RequiresPermissions;

import com.tzj.module.easyopen.exception.ApiException;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import io.jsonwebtoken.lang.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 高德地图相关API
 */
@ApiService
public class AmapApi {

    private static Logger logger = LoggerFactory.getLogger(AmapApi.class);

    @Resource
    private CommunityService communityService;
    @Autowired
    private MapService mapService;

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
    public AmapResult regeo(String location) throws Exception {
        Assert.hasLength(location, "请输入当前经纬度");
        return mapService.getAmap(location);

    }

    @Api(name = "amap.around", version = "1.0", ignoreSign = true)
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
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




}
