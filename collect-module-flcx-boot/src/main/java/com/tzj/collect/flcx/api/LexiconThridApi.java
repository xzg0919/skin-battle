package com.tzj.collect.flcx.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.KeyWordDTO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.amap.AmapConst;
import com.tzj.collect.common.amap.AmapRegeoJson;
import com.tzj.collect.core.param.admin.AdminBean;
import com.tzj.collect.core.param.flcx.FlcxBean;
import com.tzj.collect.core.param.flcx.FlcxThirdBean;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.collect.core.result.flcx.AlipayResponseResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.common.file.upload.FileUpload;
import com.tzj.module.common.utils.FileUtil;
import com.tzj.module.easyopen.ApiContext;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.file.FileBean;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.tzj.collect.common.constant.TokenConst.*;
import static com.tzj.collect.common.constant.TokenConst.ADMIN_API_EXPRIRE;

/**
 * 关键字搜索
 *
 * @author sgmark
 * @create 2019-06-17 16:12
 *
 */
@ApiService
public class LexiconThridApi {

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
    @Reference(version = "${flcx.service.version}")
    private FlcxMerchantService flcxMerchantService;
    @Reference(version = "${flcx.service.version}")
    private FlcxMerchantLogService flcxMerchantLogService;
    @Reference(version = "${flcx.service.version}")
    private FlcxMerchantThresHoldService flcxMerchantThresHoldService;
    @Autowired
    private FileUpload fileUpload;
    @Resource
    private RedisUtil redisUtil;

    @Api(name = "flcx.token.get", version = "1.0")
    @SignIgnore
    @AuthIgnore //这个api忽略token验证
    public TokenBean getToken(FlcxThirdBean flcxThirdBean) throws Exception {

        //判断用户名和密钥
       FlcxMerchant flcxMerchant = flcxMerchantService.selectOne(new EntityWrapper<FlcxMerchant>().eq("del_flag", 0).eq("app_id", flcxThirdBean.getAppId()).eq("app_secret", flcxThirdBean.getAppSecret()));
        if (flcxMerchant != null) {
            String token = JwtUtils.generateToken(flcxMerchant.getAppId(), FLCX_API_EXPRIRE, FLCX_API_TOKEN_SECRET_KEY);
            String securityToken = JwtUtils.generateEncryptToken(token, FLCX_API_TOKEN_CYPTO_KEY);
            TokenBean tokenBean = new TokenBean();
            tokenBean.setExpire(FLCX_API_EXPRIRE);
            tokenBean.setToken(securityToken);
            return tokenBean;
        } else {
            throw new Exception("信息错误！！");
        }
    }
    /**
     * 垃圾分类查询
     *
     * @author sgmark@aliyun.com
     * @date 2019/6/19 0019
     * @param
     * @return
     */
    @Api(name = "flcx.searchTrashType", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = FLCX_API_COMMON_AUTHORITY)
    public Map lexCheck(FlcxThirdBean flcxBean) throws Exception {
        Subject subject = ApiContext.getSubject();
        String appId = subject.getName();
        FlcxMerchantThresHold flcxMerchantThresHold = flcxMerchantThresHoldService.selectOne(new EntityWrapper<FlcxMerchantThresHold>().eq("app_id", appId));
        if(flcxMerchantThresHold != null && flcxMerchantThresHold.getThreshold()>0 && flcxMerchantThresHold.getTodayTimes() > flcxMerchantThresHold.getThreshold()){
            throw new ApiException("接口调用已达阈值，明天再来吧！！");
        }else if(flcxMerchantThresHold == null){
            flcxMerchantThresHold = new FlcxMerchantThresHold();
            flcxMerchantThresHold.setAppId(appId);
            flcxMerchantThresHold.setTodayTimes(0);
            flcxMerchantThresHold.setThreshold(0);
        }
        flcxMerchantThresHold.setTodayTimes(flcxMerchantThresHold.getTodayTimes()+1);
        if (flcxMerchantThresHold.getId()==null) {
            flcxMerchantThresHoldService.insert(flcxMerchantThresHold);
        }else {
            flcxMerchantThresHoldService.updateById(flcxMerchantThresHold);
        }
        FlcxMerchantLog flcxMerchantLog = new FlcxMerchantLog();
        flcxMerchantLog.setAppId(appId);
        flcxMerchantLog.setRequestParam(JSON.toJSONString(flcxBean));
        flcxMerchantLog.setStatus(1);
        final Map<String, Object>[] map;
        try {
            if (cityIsExit(flcxBean)) {
                //里面必包含城市信息（id或名称）
            } else {
                flcxBean.setCityName("上海");
                flcxBean.setCityId(1L);
            }
            String imageUrl = "";
            map = new Map[]{new HashMap<>()};
            if (StringUtils.isNotBlank(flcxBean.getSpeechText()) || StringUtils.isNotBlank(flcxBean.getImageUrl())) {
                if (StringUtils.isNotBlank(flcxBean.getImageUrl())) {
                    imageUrl = flcxBean.getImageUrl();
                }

                //语音先经过自己系统匹配
                if (StringUtils.isNotBlank(flcxBean.getSpeechText())) {
                    //本地有返回异步调用阿里接口
                    flcxBean.setName(flcxBean.getSpeechText());
                    Map localMap = flcxLexiconService.lexThirdCheck(flcxBean);
                    if (!"empty".equals(localMap.get("msg"))) {
                        map[0] = localMap;
                        flcxMerchantLog.setResponseParam(JSON.toJSONString(map[0]));
                        flcxMerchantLog.setStatus(2);
                        flcxMerchantLogService.insert(flcxMerchantLog);
                        return map[0];
                    }
                }
                //语音搜索或者图片搜索
                AlipayResponseResult alipayResponse = aliFlcxService.returnTypeByPicOrVoice(imageUrl, flcxBean.getSpeechText(), "isv");
                if (null != alipayResponse && null != alipayResponse.getKeyWords()) {
                    List<KeyWordDTO> returnListMap = alipayResponse.getKeyWords();
                    String finalImageUrl = imageUrl;
                    returnListMap.stream().sorted(Comparator.comparing(KeyWordDTO::getScore).reversed()).forEach(returnList -> {
                        Map returnMap = null;
                        if (StringUtils.isNotEmpty(returnList.getKeyWord())) {
                            flcxBean.setName(returnList.getKeyWord());
                            returnMap = flcxLexiconService.lexThirdCheck(flcxBean);
                            returnMap.put("imageUrl", finalImageUrl);
                            map[0] = returnMap;
                            return;
                        } else if (StringUtils.isNotEmpty(returnList.getCategory())) {
                            //                        flcxBean.setNotCount(true);
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
                                returnMap.put("type", returnList.getKeyWord());
                                map[0] = returnMap;
                                return;
                            }
                            returnMap = flcxLexiconService.lexThirdCheck(flcxBean);
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
                            returnMap.put("msg", "empty");
                            returnMap.put("imageUrl", flcxBean.getImageUrl());
                            returnMap.put("type", "这是什么AI也不知道呀");
                            map[0] = returnMap;
                            return;
                        }
                    });
                } else {

                    map[0].put("msg", "empty");
                    map[0].put("imageUrl", flcxBean.getImageUrl());
                    map[0].put("type", "这是什么AI也不知道呀");
                    flcxMerchantLog.setStatus(2);
                    flcxMerchantLog.setResponseParam(JSON.toJSONString(map[0]));
                    flcxMerchantLogService.insert(flcxMerchantLog);
                    return map[0];
                }
            } else {
                //根据名称查询
                map[0] = flcxLexiconService.lexThirdCheck(flcxBean);
                //移除缓存内容
                if (map[0].containsKey("imageUrl")) {
                    map[0].remove("imageUrl");
                }
                if (map[0].containsKey("traceId")) {
                    map[0].remove("traceId");
                }
            }
            if ("empty".equals(map[0].get("msg"))) {
                flcxMerchantLog.setStatus(2);
            }
            flcxMerchantLog.setResponseParam(JSON.toJSONString(map[0]));
            flcxMerchantLogService.insert(flcxMerchantLog);
            return map[0];
        } catch (Exception e) {
            flcxMerchantLog.setStatus(0);
            flcxMerchantLogService.insert(flcxMerchantLog);
            throw new ApiException("参数错误！！");
        }
    }
    /**
     * city名称是否存在系统当中
     *
     * @author sgmark@aliyun.com
     * @date 2019/8/5 0005
     * @param
     * @return
     */
    public Boolean cityIsExit(FlcxThirdBean flcxBean) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Map cityByLocation(FlcxThirdBean flcxBean) throws Exception {
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
}
