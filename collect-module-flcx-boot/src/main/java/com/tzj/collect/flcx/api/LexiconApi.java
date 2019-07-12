package com.tzj.collect.flcx.api;


import com.alipay.api.domain.KeyWordDTO;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationFeedbackSyncResponse;
import com.alipay.api.response.AlipayIserviceCognitiveClassificationWasteQueryResponse;
import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.entity.FlcxLexicon;
import com.tzj.collect.entity.FlcxRecords;
import com.tzj.collect.service.*;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;
import com.tzj.module.easyopen.file.FileBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 关键字搜索
 *
 * @author sgmark
 * @create 2019-06-17 16:12
 **/
@ApiService
public class LexiconApi {

    @Resource
    private FlcxLexiconService flcxLexiconService;
    @Resource
    private FlcxTypeService flcxTypeService;
    @Resource
    private FlcxRecordsService flcxRecordsService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    private AliFlcxService aliFlcxService;
    @Resource
    private FlcxFileUploadService fileUploadService;

    @Autowired
    private RedisUtil redisUtil;

    @Api(name = "lex.check.before", version = "1.0")
    @AuthIgnore
    public Map keySearchInRedis(){
        return flcxLexiconService.keySearchInRedis(null);
    }

    /** 垃圾分类查询
      * @author sgmark@aliyun.com
      * @date 2019/6/19 0019
      * @param
      * @return
      */
    @Api(name = "lex.check", version = "1.0")
    @AuthIgnore
    public Map lexCheck(FlcxBean flcxBean)throws ApiException {
        Boolean isAr = false;
        String imageUrl = "";
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        if(StringUtils.isNotBlank(flcxBean.getSpeechText()) || StringUtils.isNotBlank(flcxBean.getImageUrl()) || StringUtils.isNotBlank(flcxBean.getImageUrlAR())){
            if (StringUtils.isNotBlank(flcxBean.getImageUrl())){
                imageUrl = flcxBean.getImageUrl();
                isAr = false;
            }else if (StringUtils.isNotBlank(flcxBean.getImageUrlAR())){
                imageUrl = flcxBean.getImageUrlAR();
                isAr = true;
            }
            //语音先经过自己系统匹配
            if(StringUtils.isNotBlank(flcxBean.getSpeechText())){
                //本地有返回异步调用阿里接口
                flcxBean.setName(flcxBean.getSpeechText());
                Map localMap = flcxLexiconService.lexCheck(flcxBean);
                if (!"empty".equals(localMap.get("msg"))){
                    localMap.put("isAr", isAr);
                    localMap.put("traceId", "");
//                    localMap.put("imageUrl", finalImageUrl);
                    localMap.put("name", flcxBean.getName());
                    map[0] = localMap;
                    new Thread(()->aliFlcxService.returnTypeVoice(flcxBean.getSpeechText()));
                    return map[0];
                }
            }
            //语音搜索或者图片搜索
            AlipayIserviceCognitiveClassificationWasteQueryResponse alipayResponse = aliFlcxService.returnTypeByPicOrVoice(imageUrl, flcxBean.getSpeechText());
            if(null != alipayResponse && null != alipayResponse.getKeyWords()){
                List<KeyWordDTO> returnListMap = alipayResponse.getKeyWords();
                Boolean finalIsAr = isAr;
                String finalImageUrl = imageUrl;
                returnListMap.stream().sorted(Comparator.comparing(KeyWordDTO::getScore).reversed()).forEach(returnList -> {
                    Map returnMap = null;
                    if (StringUtils.isNotEmpty(returnList.getKeyWord()) && !"unknown".equals(returnList.getKeyWord())){
                        flcxBean.setName(returnList.getKeyWord());
                        returnMap = flcxLexiconService.lexCheck(flcxBean);
                        returnMap.put("isAr", finalIsAr.booleanValue());
                        returnMap.put("traceId", alipayResponse.getTraceId());
                        returnMap.put("imageUrl", finalImageUrl);
//                        if ("unknown".equals(returnList.getKeyWord())){
//                            returnMap.put("isKnow", "N");
//                            returnMap.put("name", "这是什么AI也不知道呀");
//                        }else {
//                            returnMap.put("name", returnList.getKeyWord());
//                        }
                        map[0] = returnMap;
                    }else if(StringUtils.isNotEmpty(returnList.getCategory())){
                        flcxBean.setNotCount(true);
                        switch (returnList.getCategory()){
                            case "harmful":flcxBean.setName("这个可能是有害垃圾哦"); break;
                            case "recoverable":flcxBean.setName("这个可能是可回收物哦"); break;
                            case "kitchen":flcxBean.setName("这个可能是湿垃圾哦"); break;
                            case "dry":flcxBean.setName("这个可能是干垃圾哦"); break;
                            default :
                        }
                        if ("EasterEgg ".equals(returnList.getCategory())){
                            returnMap.put("msg", "eggshell");
                            returnMap.put("describe", returnList.getKeyWord());
                            map[0] = returnMap;
                            return;
                        }
                        returnMap = new HashMap();
                        returnMap = flcxLexiconService.lexCheck(flcxBean);
                        returnMap.put("isAr", finalIsAr.booleanValue());
                        returnMap.put("traceId", alipayResponse.getTraceId());
                        returnMap.put("imageUrl", finalImageUrl);
                        map[0] = returnMap;
                        return;

                    }else {
                        //语音未识别
                        if (StringUtils.isNotBlank(flcxBean.getSpeechText())){
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
                        return ;
                    }
                });
            }else {
                //图片未识别
                map[0].put("isAr", isAr);
                map[0].put("isKnow", "N");
                map[0].put("traceId", alipayResponse.getTraceId());
                map[0].put("imageUrl", flcxBean.getImageUrl());
                map[0].put("name", "这是什么AI也不知道呀");
                return map[0];
            }
        }else if (StringUtils.isEmpty(flcxBean.getName())){
            //根据typeId查询
            return  flcxLexiconService.lexCheckByType(flcxBean);
        }else {
            //根据名称查询
            map[0] = flcxLexiconService.lexCheck(flcxBean);
            map[0].remove("imageUrl");
        }
        if (null != map[0] && null != map[0].get("flcxRecords")){
            FlcxRecords flcxRecords = (FlcxRecords) map[0].get("flcxRecords");
            flcxBean.setLexicon(flcxBean.getName());
            flcxBean.setCity(flcxBean.getCity());
            flcxBean.setLexiconAfter(flcxRecords.getLexiconAfter());
            flcxBean.setLexiconId(flcxRecords.getLexiconsId());
        }
        //发送MQ消息
        if(!flcxBean.isNotCount())
            rabbitTemplate.convertAndSend("search_keywords_queue",flcxBean);
        return map[0];
    }
    /**
     *  反馈回流
      * @author sgmark@aliyun.com
      * @date 2019/7/8 0008
      * @param
      * @return
      */
    @Api(name = "lex.feed.back", version = "1.0")
    @AuthIgnore
    public Map lexCheckFeedBack(FlcxBean flcxBean)throws ApiException {
        Map<String, Object> map = new HashMap<>();
        String name = flcxBean.getName();
        String aliuserId = flcxBean.getAliUserId();
        AlipayIserviceCognitiveClassificationFeedbackSyncResponse alipayIserviceCognitiveClassificationFeedbackSyncResponse = null;
        if (StringUtils.isBlank(flcxBean.getTraceId())){
            throw new ApiException("缺少回流id");
        }else {
            alipayIserviceCognitiveClassificationFeedbackSyncResponse = aliFlcxService.lexCheckFeedBack(flcxBean.getTraceId(), flcxBean.getImageUrl(), flcxBean.getName(), flcxBean.getActionType());
        }
        if (!StringUtils.isBlank(name)){
            flcxBean = new FlcxBean();
            flcxBean.setName(name);
            flcxBean.setAliUserId(aliuserId);
            map = flcxLexiconService.lexCheck(flcxBean);
            if ("empty".equals(map.get("msg"))){
                map.put("code", "1");
            }else {
                map.put("code", "0");
            }
        }
        if(alipayIserviceCognitiveClassificationFeedbackSyncResponse.isSuccess()){
            map.put("message", "success");
        }else {
            map.put("message", "error");
        }
        return map;
    }

    /** 大分类列表
      * @author sgmark@aliyun.com
      * @date 2019/6/20 0020
      * @param
      * @return
      */
    @Api(name = "type.list", version = "1.0")
    @AuthIgnore
    public Map typeList()throws ApiException {
        return flcxTypeService.typeList();
    }
    @Api(name = "type.top5", version = "1.0")
    @AuthIgnore
    public Map topFive()throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
//        map.put("typeList", flcxRecordsMapper.selectList(new EntityWrapper<FlcxRecords>().eq("del_flag", 0).isNotNull("lexicon_after").groupBy("lexicon_after").setSqlSelect("count(1) as count_, lexicon_after").orderBy("count_", false).last("limit 0, 5")));
//        List<FlcxRecords> flcxRecordsList = new ArrayList<>();
//        FlcxRecords flcxRecords = new FlcxRecords();
//        flcxRecords.setLexiconAfter("面膜");
//        flcxRecordsList.add(flcxRecords);
//        flcxRecords = new FlcxRecords();
//        flcxRecords.setLexiconAfter("瓜子壳 ");
//        flcxRecordsList.add(flcxRecords);
//        flcxRecords = new FlcxRecords();
//        flcxRecords.setLexiconAfter("医用棉签 ");
//        flcxRecordsList.add(flcxRecords);
//        flcxRecords = new FlcxRecords();
//        flcxRecords.setLexiconAfter("虾壳");
//        flcxRecordsList.add(flcxRecords);
//        flcxRecords = new FlcxRecords();
//        flcxRecords.setLexiconAfter("塑料袋");
//        flcxRecordsList.add(flcxRecords);
//        map.put("typeList", flcxRecordsList);
//        return map;
        return flcxRecordsService.topFive("topFive");
    }


    /** 关键字模糊查询
      * @author sgmark@aliyun.com
      * @date 2019/7/4 0004
      * @param
      * @return
      */
    @Api(name = "keySearch", version = "1.0")
    @AuthIgnore
    public Map keySearch(FlcxBean flcxBean)throws ApiException {
        List<FlcxLexicon> flcxLexiconList = flcxLexiconService.keySearch("allCatch");
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(flcxBean.getName())){
            return map;
        }
        flcxLexiconList = flcxLexiconList.stream().filter(flcxLexicon -> flcxLexicon.getName().contains(flcxBean.getName())).collect(Collectors.toList());
        //匹配redis
        List<Map<String, Object>> objectMap =  redisUtil.mget(flcxLexiconList.stream().map(FlcxLexicon::getName).collect(Collectors.toList()));
        //排序取前五的值
        map.put("result", objectMap.stream().sorted(Comparator.comparing(LexiconApi::comparingByValue).reversed()).limit(5).collect(Collectors.toList()));
        return map;
    }
    private static Integer comparingByValue(Map<String, Object> map){
        return (Integer) map.get("value");
    }


    /**
     * 上传文件0
     */
    @Api(name = "util.uploadImage", version = "1.0")
    @SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
    @AuthIgnore
    public FileBean uploadImage(FlcxBean flcxBean){
        return fileUploadService.handleUploadField("",flcxBean.getHeadImg());
    }



}
