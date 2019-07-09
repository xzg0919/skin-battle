package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.api.lexicon.param.FlcxBean;
import com.tzj.collect.api.lexicon.param.FlcxLexiconBean;
import com.tzj.collect.api.lexicon.result.FlcxResult;
import com.tzj.collect.entity.*;
import com.tzj.collect.mapper.FlcxLexiconMapper;
import com.tzj.collect.service.FlcxEggshellService;
import com.tzj.collect.service.FlcxLexiconService;
import com.tzj.collect.service.FlcxLexiconTypeService;
import com.tzj.collect.service.FlcxTypeService;
import com.tzj.collect.service.FlcxRecordsService;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
  * @author sgmark@aliyun.com
  * @date 2019/6/19 0019
  * @param 
  * @return 
  */
@Service
@Transactional(readOnly = true)
public class FlcxLexiconServiceImpl extends ServiceImpl<FlcxLexiconMapper, FlcxLexicon> implements FlcxLexiconService {
    @Resource
    private FlcxLexiconMapper flcxLexiconMapper;
    @Resource
    private FlcxEggshellService flcxEggshellService;
    @Resource
    private FlcxRecordsService flcxRecordsService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private FlcxLexiconTypeService flcxLexiconTypeService;



    @Transactional(readOnly = false)
    @Cacheable(value = "lexCheckMap" , key = "#flcxBean.name", sync = true)
    public Map lexCheck(FlcxBean flcxBean) throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
        //根据名称从redis取出结果集
        Boolean object = null;
        if (!StringUtils.isBlank(flcxBean.getName())){
            object = redisUtil.hasKey(flcxBean.getName());
        }
        Map<String, Object> resultMap = null;
        if (true == object|| flcxBean.getTypeId()> 0){
            FlcxRecords flcxRecords = new FlcxRecords();
            if (StringUtils.isEmpty(flcxBean.getName()) && flcxBean.getTypeId() == 0 || StringUtils.isEmpty(flcxBean.getAliUserId())){
                throw new ApiException("参数错误");
            }
            if (null == flcxBean.getName() || StringUtils.isEmpty(flcxBean.getName()) || flcxBean.getTypeId() != 0){
                flcxBean.setName("");
                flcxRecords.setLexicons(flcxBean.getTypeId().toString());
            }else {
                flcxBean.setTypeId(0L);
                flcxRecords.setLexicons(flcxBean.getName());
            }

            FlcxResult flcxResult = flcxLexiconMapper.lexCheck(flcxBean.getName(), flcxBean.getTypeId());
            //记录查询
            flcxRecords.setAliUserId(flcxBean.getAliUserId());
            if (null != flcxResult){
                flcxResult.setRemarksList(Arrays.asList(flcxResult.getRemarks().split(";")));
                map.put("results", flcxResult);
                map.put("msg", "success");
                flcxRecords.setLexiconAfter(flcxResult.getLexicon());
                flcxRecords.setLexiconsId(flcxResult.getLexiconId());
            }else {
                //搜索是否存在彩蛋
                FlcxEggshell flcxEggshell = flcxEggshellService.selectOne(new EntityWrapper<FlcxEggshell>().eq("del_flag", 0).eq("lexicon_", flcxBean.getName()));
                if (null != flcxEggshell){
                    map.put("msg", "eggshell");
                    map.put("describe", flcxEggshell.getDescribe());
                }else {
                    map.put("msg", "empty");
                }
            }
            map.put("flcxRecords", flcxRecords);
            //把最后的结果放入redis
            return map;
        }else {
            map.put("msg", "empty");
            return map;
        }
    }

    @Cacheable(value = "lexCheckList" , key = "#flcxString", sync = true)
    public List<FlcxLexicon> keySearch(String flcxString) throws ApiException {
        return flcxLexiconMapper.selectList(new EntityWrapper<FlcxLexicon>().eq("del_flag", 0));
    }


    public Map keySearchInRedis(FlcxBean flcxBean) throws ApiException {
        HashMap<String, Object> map = new HashMap<>();
        List<FlcxLexicon> flcxLexiconList = this.selectList(new EntityWrapper<FlcxLexicon>().eq("del_flag", 0));
        List<FlcxEggshell> flcxEggshellList = flcxEggshellService.selectList(new EntityWrapper<FlcxEggshell>().eq("del_flag", 0));
        flcxLexiconList.stream().forEach(flcxLexicon -> {
            redisUtil.set(flcxLexicon.getName(), 1);
        });
        flcxEggshellList.stream().forEach(flcxEggshell -> {
            redisUtil.set(flcxEggshell.getLexicon(), 1);
        });
        System.out.println();
        return map;
    }

    @Override
    @Cacheable(value = "lexCheckMap" , key = "#flcxBean.typeId", sync = true)
    public Map lexCheckByType(FlcxBean flcxBean) {
        HashMap<String, Object> map = new HashMap<>();
        //根据名称从redis取出结果集
        Boolean object = false;
        if (!StringUtils.isBlank(flcxBean.getName())){
            object = redisUtil.hasKey(flcxBean.getName());
        }
        Map<String, Object> resultMap = null;
        if (true == object|| flcxBean.getTypeId()> 0){
            FlcxRecords flcxRecords = new FlcxRecords();
            if (StringUtils.isEmpty(flcxBean.getName()) && flcxBean.getTypeId() == 0 || StringUtils.isEmpty(flcxBean.getAliUserId())){
                throw new ApiException("参数错误");
            }
            if (null == flcxBean.getName() || StringUtils.isEmpty(flcxBean.getName()) || flcxBean.getTypeId() != 0){
                flcxBean.setName("");
                flcxRecords.setLexicons(flcxBean.getTypeId().toString());
            }else {
                flcxBean.setTypeId(0L);
                flcxRecords.setLexicons(flcxBean.getName());
            }

            FlcxResult flcxResult = flcxLexiconMapper.lexCheck(flcxBean.getName(), flcxBean.getTypeId());
            //记录查询
            flcxRecords.setAliUserId(flcxBean.getAliUserId());
            if (null != flcxResult){
                flcxResult.setRemarksList(Arrays.asList(flcxResult.getRemarks().split(";")));
                map.put("results", flcxResult);
                map.put("msg", "success");
                flcxRecords.setLexiconAfter(flcxResult.getLexicon());
                flcxRecords.setLexiconsId(flcxResult.getLexiconId());
            }else {
                //搜索是否存在彩蛋
                FlcxEggshell flcxEggshell = flcxEggshellService.selectOne(new EntityWrapper<FlcxEggshell>().eq("del_flag", 0).eq("lexicon_", flcxBean.getName()));
                if (null != flcxEggshell){
                    map.put("msg", "eggshell");
                    map.put("describe", flcxEggshell.getDescribe());
                }else {
                    map.put("msg", "empty");
                }
            }
            map.put("flcxRecords", flcxRecords);
            //把最后的结果放入redis
            return map;
        }else {
            map.put("msg", "empty");
            return map;
        }
    }

    /**
     * 根据关键字搜索 支持模糊查询
     * @param flcxBean
     * @return
     * @throws ApiException
     */
    @DS("slave")
    public Page searchByName(FlcxBean flcxBean){
        Page<FlcxLexicon> page = null;
        if(null == flcxBean.getPageBean()){
            page = new Page<FlcxLexicon>(1,10);
        }else {
            page = new Page<FlcxLexicon>(flcxBean.getPageBean() == null ? 1 : flcxBean.getPageBean().getPageNumber(), flcxBean.getPageBean().getPageSize() == null ? 10 : flcxBean.getPageBean().getPageSize());
        }
        HashMap<String, Object> map = new HashMap<>();
        EntityWrapper<FlcxLexicon> wrapper = new EntityWrapper<FlcxLexicon>();
        wrapper.eq("del_flag",0);
        wrapper.like("name_",flcxBean.getName());
        return this.selectPage(page,wrapper);
    }

    /**
     * 新增词库关键词
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @Transactional(readOnly = false)
    public Map addFlcxLexiconBean (FlcxLexiconBean flcxLexiconBean){
        HashMap<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(flcxLexiconBean.getName())){
            throw  new ApiException("名称不能为空");
        }
        //判断是否已经存在
        if(isExistFlcxLexicon(flcxLexiconBean)){
            throw  new ApiException("关键词已存在");
        }

        if(flcxLexiconBean.getTypeIds().isEmpty()){
            throw  new ApiException("必须关联类型");
        }

        FlcxLexicon flcxLexicon = new FlcxLexicon();
        flcxLexicon.setName(flcxLexiconBean.getName());
        flcxLexicon.setRecover(flcxLexiconBean.getRecover());
        flcxLexicon.setCreateDate(Calendar.getInstance().getTime());
        flcxLexicon.setUpdateDate(Calendar.getInstance().getTime());
        this.insert(flcxLexicon);

        //插入类型
        List<FlcxLexiconType> typeList = new ArrayList<>();
        FlcxLexiconType flcxLexiconType = null;
        for(Long typeid : flcxLexiconBean.getTypeIds()){
            flcxLexiconType = new FlcxLexiconType();
            flcxLexiconType.setTypeId(typeid);
            flcxLexiconType.setLexiconId(flcxLexicon.getId());
            flcxLexiconType.setCreateDate(Calendar.getInstance().getTime());
            flcxLexiconType.setUpdateDate(Calendar.getInstance().getTime());
            typeList.add(flcxLexiconType);
        }
        flcxLexiconTypeService.insertBatch(typeList);

        map.put("msg","success");
        return map;

    }

    /**
     * 关键词是否已存在
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @DS("slave")
    public boolean isExistFlcxLexicon (FlcxLexiconBean flcxLexiconBean){
        EntityWrapper<FlcxLexicon> wrapper = new EntityWrapper<FlcxLexicon>();
        wrapper.eq("name_",flcxLexiconBean.getName());
        wrapper.eq("del_flag",0);
        if(flcxLexiconBean.getId()!=null && flcxLexiconBean.getId()>0){
            wrapper.ne("id",flcxLexiconBean.getId());
        }
        return this.selectCount(wrapper)>0 ? true:false;

    }

    /**
     * 删除关键字
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @Transactional(readOnly = false)
    public Map deleteFlcxLexicon (FlcxLexiconBean flcxLexiconBean){
        HashMap<String, Object> map = new HashMap<>();
        if(null == flcxLexiconBean.getId()){
            throw new ApiException("id不能为空!");
        }
        if(flcxLexiconBean.getId()<=0){
            throw new ApiException("id参数错误");
        }
        FlcxLexicon flcxLexicon = this.selectById(flcxLexiconBean.getId());

        if(null == flcxLexicon){
            throw new ApiException("关键词不存在!");
        }

        //删除操作
        flcxLexicon.setDelFlag("1");
        flcxLexicon.setUpdateDate(Calendar.getInstance().getTime());
        this.updateById(flcxLexicon);

        //找到对应的类型 并批量删除
        EntityWrapper<FlcxLexiconType> wrapper = new EntityWrapper<FlcxLexiconType>();
        wrapper.eq("del_flag",0);
        wrapper.eq("lexicon_id",flcxLexiconBean.getId());
        List<FlcxLexiconType> typeList = flcxLexiconTypeService.selectList(wrapper);
        List<FlcxLexiconType> typeList4Update = new ArrayList<>();
        for(FlcxLexiconType type : typeList){
            type.setDelFlag("1");
            type.setUpdateDate(Calendar.getInstance().getTime());
            typeList4Update.add(type);
        }
        flcxLexiconTypeService.updateBatchById(typeList4Update);

        map.put("msg","success");
        return map;
    }


    /**
     * 修改关键字 如果传入了typeIds 说明需要修改类型 否则默认不修改
     * @param flcxLexiconBean
     * @return
     * @throws ApiException
     */
    @Transactional(readOnly = false)
    public Map updateFlcxLexicon (FlcxLexiconBean flcxLexiconBean){
        HashMap<String, Object> map = new HashMap<>();
        if(null == flcxLexiconBean.getId()){
            throw new ApiException("id不能为空!");
        }
        if(flcxLexiconBean.getId()<=0){
            throw new ApiException("id参数错误");
        }
        FlcxLexicon flcxLexicon = this.selectById(flcxLexiconBean.getId());

        if(null == flcxLexicon){
            throw new ApiException("关键词不存在!");
        }

        if(isExistFlcxLexicon(flcxLexiconBean)){
            throw new ApiException("关键词已存在!");
        }

        //修改关键字本身
        flcxLexicon.setName(flcxLexiconBean.getName());
        flcxLexicon.setRecover(flcxLexiconBean.getRecover());
        flcxLexicon.setUpdateDate(Calendar.getInstance().getTime());
        this.updateById(flcxLexicon);

        //如果typeids 不是空的说明修改了类型
        if(!flcxLexiconBean.getTypeIds().isEmpty()) {
            //修改类型关联
            //找到对应的类型
            EntityWrapper<FlcxLexiconType> wrapper = new EntityWrapper<FlcxLexiconType>();
            wrapper.eq("del_flag", 0);
            wrapper.eq("lexicon_id", flcxLexiconBean.getId());
            List<FlcxLexiconType> typeList = flcxLexiconTypeService.selectList(wrapper);

            //待删除的ids 以及待新增的ids
            List<FlcxLexiconType> typeList4DeleteOrg = typeList;
            List<FlcxLexiconType> typeList4add = new ArrayList<>();

            boolean isAdd = true;
            FlcxLexiconType addType = null;

            for(Long id : flcxLexiconBean.getTypeIds()){
                for (FlcxLexiconType type : typeList) {
                    //保留需要留下的类型
                    if(type.getId() == id) {
                        typeList4DeleteOrg.remove(type);
                        isAdd = false;
                        break;
                    }
                }
                if(isAdd) {
                    addType = new FlcxLexiconType();
                    addType.setTypeId(id);
                    addType.setLexiconId(flcxLexicon.getId());
                    addType.setCreateDate(Calendar.getInstance().getTime());
                    addType.setUpdateDate(Calendar.getInstance().getTime());
                    typeList4add.add(addType);
                }
            }
            //批量新增需要加入的类型
            flcxLexiconTypeService.insertBatch(typeList4add);
            //批量删除不需要的类型
            List<FlcxLexiconType> typeList4Delete = new ArrayList<>();
            for(FlcxLexiconType type : typeList4DeleteOrg){
                type.setDelFlag("1");
                type.setUpdateDate(Calendar.getInstance().getTime());
                typeList4Delete.add(type);
            }
            flcxLexiconTypeService.updateBatchById(typeList4Delete);
        }

        map.put("msg","success");
        return  map;

    }
}
