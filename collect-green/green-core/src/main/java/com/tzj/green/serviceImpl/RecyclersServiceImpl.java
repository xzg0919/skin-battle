package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;

import com.tzj.green.entity.Member;
import com.tzj.green.entity.Recyclers;
import com.tzj.green.mapper.RecyclersMapper;
import com.tzj.green.param.RecyclersBean;
import com.tzj.green.service.MemberService;
import com.tzj.green.service.MessageService;
import com.tzj.green.service.RecyclersService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收人员service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class RecyclersServiceImpl extends ServiceImpl<RecyclersMapper, Recyclers> implements RecyclersService
{
    @Resource
    private RecyclersService recyclersService;
    @Resource
    private MemberService memberService;
    @Resource
    private MessageService messageService;
    @Resource
    private RecyclersMapper recyclersMapper;
    /**
     * 根据手机号查询回收人员
     *
     * @param mobile
     * @return
     */
    @Override
    public Recyclers selectByMobile(String mobile) {
        return selectOne(new EntityWrapper<Recyclers>().eq("tel", mobile));
    }

    @Override
    public Map<String, Object> bindingCardByRec(RecyclersBean recyclersBean) {
        //验证手机验证码是否正确有效
        if(!messageService.validMessage(recyclersBean.getMobile(), recyclersBean.getCaptcha())){
            throw new ApiException("手机验证码错误");
        }
        Map<String, Object> returnMap = new HashMap<>();
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("del_flag", 0).eq("id_card", recyclersBean.getIdCard()));
        if (null == member){
            member = new Member();
            member.setName(recyclersBean.getName());
            member.setMobile(recyclersBean.getMobile());
            member.setAddress(recyclersBean.getAddress());
            member.setGender(recyclersBean.getSex());
        }
        member.setRealNo(recyclersBean.getRealNo());
        member.setDetailAddress(recyclersBean.getDetailAddress());
        //保存用户当前回收人员所在回收服务范围所在的小区地址
        Map<String, Object> recMap =  recyclersMapper.selectRecRange(recyclersBean.getId());
        if (null == recMap){
            throw new ApiException("录入失败, 检查是否通过审核");
        }else {
            try {
                member.setProvinceId(Long.parseLong(recMap.get("province_id")+""));
                member.setProvinceName(recMap.get("province_name")+"");
                member.setCityId(Long.parseLong(recMap.get("city_id")+""));
                member.setCityName(recMap.get("city_name")+"");
                member.setCommunityId(Long.parseLong(recMap.get("area_id")+""));
                member.setCommunityName(recMap.get("area_name")+"");
                member.setStreetId(Long.parseLong(recMap.get("street_id")+""));
                member.setStreetName(recMap.get("street_name")+"");
                member.setCommunityHouseId(Long.parseLong(recMap.get("house_id")+""));
                member.setCommunityHouseName(recMap.get("house_name")+"");
                member.setCompanyId(Long.parseLong(recMap.get("company_id")+""));
                memberService.insertOrUpdate(member);
                returnMap.put("msg", "Y");
            }catch (Exception e){
                throw new ApiException("录入失败:"+e.getMessage());
            }
        }
        return returnMap;
    }
    /**
     * 查找回收人员服务地址
     * @author: sgmark@aliyun.com
     * @Date: 2020/1/10 0010
     * @Param: 
     * @return: 
     */
    @Override
    public Map<String, Object> selectRecRange(Long recId) {
        return recyclersMapper.selectRecRange(recId);
    }
}