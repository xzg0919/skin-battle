package com.tzj.collect.flcx.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.KeyWordDTO;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationFeedbackSyncResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationWasteQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.amap.AmapRegeoJson;
import com.tzj.collect.core.param.flcx.FlcxBean;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.amap.AmapConst;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.FlcxCity;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.file.FileBean;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 关键字搜索
 *
 * @author sgmark
 * @create 2019-06-17 16:12
 *
 */
@ApiService
public class LexiconApi {

    @Reference(version = "${flcx.service.version}")
    private FlcxLexiconService flcxLexiconService;
    @Reference(version = "${flcx.service.version}")
    private FlcxTypeService flcxTypeService;
    @Reference(version = "${flcx.service.version}")
    private FlcxRecordsService flcxRecordsService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Reference(version = "${flcx.service.version}")
    private AliFlcxService aliFlcxService;
    @Reference(version = "${flcx.service.version}")
    private FlcxFileUploadService fileUploadService;
    @Reference(version = "${flcx.service.version}")
    private FlcxCityService flcxCityService;

    @Resource
    private RedisUtil redisUtil;

    @Api(name = "lex.check.before", version = "1.0")
    @AuthIgnore
    public Map keySearchInRedis() {
        return flcxLexiconService.keySearchInRedis(null);
    }

    /**
     * 垃圾分类查询
     *
     * @author sgmark@aliyun.com
     * @date 2019/6/19 0019
     * @param
     * @return
     */
    @Api(name = "lex.check", version = "1.0")
    @AuthIgnore
    public Map lexCheck(FlcxBean flcxBean) throws Exception {
        if (cityIsExit(flcxBean)) {
            //里面必包含城市信息（id或名称）
        } else {
            flcxBean.setCityName("上海");
            flcxBean.setCityId(1L);
        }
        if (StringUtils.isEmpty(flcxBean.getSource())) {
            flcxBean.setSource("isv");
        }
//        //没传城市，只有经纬度
//        if (StringUtils.isNotEmpty(cityName) || flcxBean.getCityId() != 0){
//            //有传值撒都不干
//            flcxBean.setCityName("杭州市".equals(flcxBean.getCityName()) ? "杭州市" : "上海市");
//        }else if(StringUtils.isNotEmpty(flcxBean.getLatitude()) && StringUtils.isNotEmpty(flcxBean.getLongitude())){
//            flcxBean.setCityName("杭州市".equals(this.cityByLocation(flcxBean).get("city").toString()) ? "杭州市" : "上海市");
//        }else if (StringUtils.isEmpty(flcxBean.getCity())&& flcxBean.getCityId() == 0){
//            flcxBean.setCityName("上海市");
//            flcxBean.setCityId(1L);
//        }
        Boolean isAr = false;
        String imageUrl = "";
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        if (StringUtils.isNotBlank(flcxBean.getSpeechText()) || StringUtils.isNotBlank(flcxBean.getImageUrl()) || StringUtils.isNotBlank(flcxBean.getImageUrlAR())) {
            if (StringUtils.isNotBlank(flcxBean.getImageUrl())) {
                imageUrl = flcxBean.getImageUrl();
                isAr = false;
            } else if (StringUtils.isNotBlank(flcxBean.getImageUrlAR())) {
                imageUrl = flcxBean.getImageUrlAR();
                isAr = true;
            }
            //语音先经过自己系统匹配
            if (StringUtils.isNotBlank(flcxBean.getSpeechText())) {
                //本地有返回异步调用阿里接口
                flcxBean.setName(flcxBean.getSpeechText());
                Map localMap = flcxLexiconService.lexCheck(flcxBean);
                if (!"empty".equals(localMap.get("msg"))) {
                    localMap.put("isAr", isAr);
                    localMap.put("traceId", "");
//                    localMap.put("imageUrl", finalImageUrl);
                    localMap.put("name", flcxBean.getName());
                    map[0] = localMap;
                    new Thread(() -> aliFlcxService.returnTypeVoice(flcxBean.getSpeechText()));
                    return map[0];
                }
            }
            //语音搜索或者图片搜索
            AlipayIserviceCognitiveClassificationWasteQueryResponse alipayResponse = aliFlcxService.returnTypeByPicOrVoice(imageUrl, flcxBean.getSpeechText(), flcxBean.getSource());
            if (null != alipayResponse && null != alipayResponse.getKeyWords()) {
                List<KeyWordDTO> returnListMap = alipayResponse.getKeyWords();
                Boolean finalIsAr = isAr;
                String finalImageUrl = imageUrl;
                returnListMap.stream().sorted(Comparator.comparing(KeyWordDTO::getScore).reversed()).forEach(returnList -> {
                    Map returnMap = null;
                    if (StringUtils.isNotEmpty(returnList.getKeyWord())) {
                        flcxBean.setName(returnList.getKeyWord());
                        returnMap = flcxLexiconService.lexCheck(flcxBean);
                        returnMap.put("isAr", finalIsAr.booleanValue());
                        returnMap.put("traceId", alipayResponse.getTraceId());
                        returnMap.put("imageUrl", finalImageUrl);
                        map[0] = returnMap;
                        return;
                    } else if (StringUtils.isNotEmpty(returnList.getCategory())) {
                        flcxBean.setNotCount(true);
                        switch (returnList.getCategory()) {
                            case "harmful":
                                flcxBean.setName("这个可能是有害垃圾哦");
                                break;
                            case "recoverable":
                                flcxBean.setName("这个可能是可回收物哦");
                                break;
                            case "kitchen":
                                flcxBean.setName("这个可能是湿垃圾哦");
                                break;
                            case "dry":
                                flcxBean.setName("这个可能是干垃圾哦");
                                break;
                            default:
                        }
                        if ("EasterEgg ".equals(returnList.getCategory())) {
                            returnMap.put("msg", "eggshell");
                            returnMap.put("describe", returnList.getKeyWord());
                            map[0] = returnMap;
                            return;
                        }
                        returnMap = flcxLexiconService.lexCheck(flcxBean);
                        returnMap.put("isAr", finalIsAr.booleanValue());
                        returnMap.put("traceId", alipayResponse.getTraceId());
                        returnMap.put("imageUrl", finalImageUrl);
                        map[0] = returnMap;
                        return;

                    } else {
                        //语音未识别
                        if (StringUtils.isNotBlank(flcxBean.getSpeechText())) {
                            map[0].put("msg", "empty");
                            return;
                        }
                        //图片未识别
                        returnMap = new HashMap();
                        returnMap.put("isKnow", "N");
                        returnMap.put("isAr", finalIsAr.booleanValue());
                        returnMap.put("traceId", alipayResponse.getTraceId());
                        returnMap.put("imageUrl", flcxBean.getImageUrl());
                        returnMap.put("name", "这是什么AI也不知道呀");
                        map[0] = returnMap;
                        return;
                    }
                });
            } else {
                //图片未识别
                map[0].put("isAr", isAr);
                map[0].put("isKnow", "N");
                map[0].put("traceId", alipayResponse.getTraceId());
                map[0].put("imageUrl", flcxBean.getImageUrl());
                map[0].put("name", "这是什么AI也不知道呀");
                return map[0];
            }
        } else {
            //根据名称查询
            map[0] = flcxLexiconService.lexCheck(flcxBean);
            //移除缓存内容
            if (map[0].containsKey("imageUrl")) {
                map[0].remove("imageUrl");
            }
            if (map[0].containsKey("traceId")) {
                map[0].remove("traceId");
            }
        }
        if (null != map[0] && null != map[0].get("flcxRecords")) {
            FlcxRecords flcxRecords = (FlcxRecords) map[0].get("flcxRecords");
            flcxBean.setLexicon(flcxBean.getName());
            flcxBean.setCity(flcxBean.getCity());
            flcxBean.setLexiconAfter(flcxRecords.getLexiconAfter());
            flcxBean.setLexiconId(flcxRecords.getLexiconsId());
        }
        //发送MQ消息
        if (!flcxBean.getName().contains("这个可能") && StringUtils.isNotEmpty(flcxBean.getName())) {
            //赋值城市
            flcxBean.setCity(flcxBean.getCityName());
            rabbitTemplate.convertAndSend("search_keywords_queue", flcxBean);
        }
        return map[0];
    }

    @SignIgnore
    @AuthIgnore
    @Api(name = "lex.check.test", version = "1.0", ignoreTimestamp = true, ignoreNonce = true)
    public Map lexCheckTest(FlcxBean flcxBean) throws Exception {
        return this.lexCheck(flcxBean);
    }

    /**
     * 反馈回流
     *
     * @author sgmark@aliyun.com
     * @date 2019/7/8 0008
     * @param
     * @return
     */
    @Api(name = "lex.feed.back", version = "1.0")
    @AuthIgnore
    public Map lexCheckFeedBack(FlcxBean flcxBean) throws ApiException {
        Map<String, Object> map = new HashMap<>();
        String name = flcxBean.getName();
        String aliuserId = flcxBean.getAliUserId();
        AlipayIserviceCognitiveClassificationFeedbackSyncResponse alipayIserviceCognitiveClassificationFeedbackSyncResponse = null;
        if (StringUtils.isBlank(flcxBean.getTraceId())) {
            throw new ApiException("缺少回流id");
        } else {
            alipayIserviceCognitiveClassificationFeedbackSyncResponse = aliFlcxService.lexCheckFeedBack(flcxBean.getTraceId(), flcxBean.getImageUrl(), flcxBean.getName(), flcxBean.getActionType());
        }
        if (!StringUtils.isBlank(name)) {
            flcxBean = new FlcxBean();
            flcxBean.setName(name);
            flcxBean.setAliUserId(aliuserId);
            map = flcxLexiconService.lexCheck(flcxBean);
            if ("empty".equals(map.get("msg"))) {
                map.put("code", "1");
            } else {
                map.put("code", "0");
            }
        }
        if (alipayIserviceCognitiveClassificationFeedbackSyncResponse.isSuccess()) {
            map.put("message", "success");
        } else {
            map.put("message", "error");
        }
        return map;
    }

    /**
     * 大分类列表
     *
     * @author sgmark@aliyun.com
     * @date 2019/6/20 0020
     * @param
     * @return
     */
    @Api(name = "type.list", version = "1.0")
    @AuthIgnore
    public Map typeList(FlcxBean flcxBean) throws Exception {
        //首次根据定位获取当前城市大分类名称（切换城市再改）
        if (cityIsExit(flcxBean)) {
            //里面必包含城市信息（id或名称）
        } else {
            flcxBean.setCityName("上海");
            flcxBean.setCityId(1L);
        }
        return flcxTypeService.typeList(flcxBean);
    }

    @Api(name = "type.top5", version = "1.0")
    @AuthIgnore
    public Map topFive() throws ApiException {
        return flcxRecordsService.topFive("topFive");
    }

    @Api(name = "type.top5.test", version = "1.0", ignoreTimestamp = true, ignoreNonce = true)
    @AuthIgnore
    @SignIgnore
    public Map topFiveTest() throws ApiException {
        return this.topFive();
    }

    /**
     * 关键字模糊查询
     *
     * @author sgmark@aliyun.com
     * @date 2019/7/4 0004
     * @param
     * @return
     */
    @Api(name = "keySearch", version = "1.0")
    @AuthIgnore
    public Map keySearch(FlcxBean flcxBean) throws ApiException {
        List<FlcxLexicon> flcxLexiconList = flcxLexiconService.keySearch("allCatch");
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(flcxBean.getName())) {
            return map;
        }
        flcxLexiconList = flcxLexiconList.stream().filter(flcxLexicon -> flcxLexicon.getName().contains(flcxBean.getName())).collect(Collectors.toList());
        //匹配redis
        List<Map<String, Object>> objectMap = redisUtil.mget(flcxLexiconList.stream().map(FlcxLexicon::getName).collect(Collectors.toList()));
        //排序取前五的值
        map.put("result", objectMap.stream().sorted(Comparator.comparing(LexiconApi::comparingByValue).reversed()).limit(5).collect(Collectors.toList()));
        return map;
    }

    private static Integer comparingByValue(Map<String, Object> map) {
        return (Integer) map.get("value");
    }

    /**
     * 上传文件0
     */
    @Api(name = "util.uploadImage", version = "1.0")
    @SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
    @AuthIgnore
    public FileBean uploadImage(FlcxBean flcxBean) {
        return fileUploadService.handleUploadField("", flcxBean.getHeadImg());
    }

    /**
     * 根据定位获取城市
     *
     * @author sgmark@aliyun.com
     * @date 2019/7/16 0016
     * @return
     */
    @Api(name = "city.location", version = "1.0")
    @AuthIgnore
    public Map cityByLocation(FlcxBean flcxBean) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String url = "https://restapi.amap.com/v3/geocode/regeo";
        Response response = null;
        response = FastHttpClient.get().url(url)
                .addParams("key", AmapConst.AMAP_KEY)
                .addParams("location", flcxBean.getLongitude() + "," + flcxBean.getLatitude())
                .build().execute();
        String resultJson = response.body().string();
        if (StringUtils.isNotEmpty(resultJson)) {
            resultJson = resultJson.replaceAll("\n", "");
            try {
                AmapRegeoJson amapRegeoJson = JSON.parseObject(resultJson, AmapRegeoJson.class);
                if (null != amapRegeoJson && amapRegeoJson.getRegeocode().getAddressComponent().getDistrict().contains("市")){
                    resultMap.put("city", amapRegeoJson.getRegeocode().getAddressComponent().getDistrict().replace("市", ""));
                }else if (null != amapRegeoJson && amapRegeoJson.getRegeocode().getAddressComponent().getCity().size() > 0) {
                    resultMap.put("city", amapRegeoJson.getRegeocode().getAddressComponent().getCity().get(0).toString().replace("市", ""));
                } else if (StringUtils.isNotEmpty(amapRegeoJson.getRegeocode().getAddressComponent().getProvince())) {
                    //定位没找到城市(配合支付宝页面展示，去掉市)
                    resultMap.put("city", amapRegeoJson.getRegeocode().getAddressComponent().getProvince().replace("市", ""));
                } else {
                    resultMap.put("city", "");
                }
            } catch (Exception e) {
                System.out.println("=============异常抛出来咯：" + resultJson + "============");
            }
        } else {
            resultMap.put("city", "");
        }
        return resultMap;
    }

    /**
     * 获取所有的开通城市
     *
     * @author wangmeixia
     * @date 2019/8/29
     * @return
     */
    @Api(name = "city.getAllOpenCity", version = "1.0")
    @AuthIgnore
    public List getAllOpenCity() throws Exception {
        return flcxCityService.getAllOpenCity();
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String url = "https://restapi.amap.com/v3/geocode/regeo";
        Response response = null;
        response = FastHttpClient.get().url(url)
                .addParams("key", AmapConst.AMAP_KEY)
                .addParams("location", "105.379" + "," + "30.8671")
                .build().execute();
        String resultJson = response.body().string();
        if (StringUtils.isNotEmpty(resultJson)) {
            resultJson = resultJson.replaceAll("\n", "");
            try {
                AmapRegeoJson amapRegeoJson = JSON.parseObject(resultJson, AmapRegeoJson.class);
                if (null != amapRegeoJson && amapRegeoJson.getRegeocode().getAddressComponent().getDistrict().contains("市")){
                    resultMap.put("city", amapRegeoJson.getRegeocode().getAddressComponent().getDistrict().replace("市", ""));
                }else if (null != amapRegeoJson && amapRegeoJson.getRegeocode().getAddressComponent().getCity().size() > 0) {
                    resultMap.put("city", amapRegeoJson.getRegeocode().getAddressComponent().getCity().get(0).toString());
                } else if (StringUtils.isNotEmpty(amapRegeoJson.getRegeocode().getAddressComponent().getProvince())) {
                    //定位没找到城市
                    resultMap.put("city", amapRegeoJson.getRegeocode().getAddressComponent().getProvince());
                } else {
                    resultMap.put("city", "");
                }
            } catch (Exception e) {
                System.out.println("=============异常抛出来咯：" + resultJson + "============");
            }
        } else {
            resultMap.put("city", "");
        }
        System.out.println(resultMap.get("city"));
    }

    /**
     * city名称是否存在系统当中
     *
     * @author sgmark@aliyun.com
     * @date 2019/8/5 0005
     * @param
     * @return
     */
    public Boolean cityIsExit(FlcxBean flcxBean) throws Exception {
        if (StringUtils.isNotEmpty(flcxBean.getLatitude()) && StringUtils.isNotEmpty(flcxBean.getLongitude())) {
            flcxBean.setCityName(this.cityByLocation(flcxBean).get("city").toString());
        }
        if (StringUtils.isNotEmpty(flcxBean.getCityName()) || flcxBean.getCityId() != 0) {
            if (flcxBean.getCityId() != 0) {
                //当前id或名称存在系统当中则返回true
                if (null != flcxCityService.selectById(flcxBean.getCityId())) {
                    return true;
                }
            } else if (StringUtils.isNotEmpty(flcxBean.getCityName())) {
                if (null != flcxCityService.selectOne(new EntityWrapper<FlcxCity>().eq("del_flag", 0).eq("city_name", flcxBean.getCityName()))) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

}
