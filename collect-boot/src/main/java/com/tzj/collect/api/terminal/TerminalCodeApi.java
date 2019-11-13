package com.tzj.collect.api.terminal;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.util.EnterpriseTerminalUtils;
import com.tzj.collect.core.param.enterprise.EnterpriseCodeBean;
import com.tzj.collect.core.service.EnterpriseCodeService;
import com.tzj.collect.core.service.MessageService;
import com.tzj.collect.entity.EnterpriseCode;
import com.tzj.collect.entity.EnterpriseTerminal;
import static com.tzj.common.constant.TokenConst.TERMINAL_API_COMMON_AUTHORITY;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 券码相关接口
 */
@ApiService
public class TerminalCodeApi {

    @Autowired
    private EnterpriseCodeService enterpriseCodeService;
    @Autowired
    private MessageService messageService;

    /**
     * 新增以旧换新的相关券的信息 wangcan
     *
     * @param
     * @return
     */
    @Api(name = "terminal.saveEnterpriseCode", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = TERMINAL_API_COMMON_AUTHORITY)
    public Object saveEnterpriseCode(EnterpriseCodeBean enterpriseCodeBean) throws Exception {
        boolean bool = messageService.validMessage(enterpriseCodeBean.getCustomerTel(), enterpriseCodeBean.getCaptcha());
        if (!bool) {
            throw new ApiException("验证码错误");
        }
        EnterpriseTerminal enterpriseTerminal = EnterpriseTerminalUtils.getEnterpriseTerminal();
        return enterpriseCodeService.saveEnterpriseCode(enterpriseTerminal.getId(), enterpriseCodeBean);
    }

    /**
     * 以旧换新的券的列表 wangcan
     *
     * @param
     * @return
     */
    @Api(name = "terminal.enterpriseTerminalList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = TERMINAL_API_COMMON_AUTHORITY)
    @DS("slave")
    public Object enterpriseTerminalList(EnterpriseCodeBean enterpriseCodeBean) throws Exception {
        EnterpriseTerminal enterpriseTerminal = EnterpriseTerminalUtils.getEnterpriseTerminal();
        EntityWrapper wrapper = new EntityWrapper<EnterpriseCode>();
        wrapper.eq("terminal_id", enterpriseTerminal.getId());
        wrapper.eq("del_flag", 0);
        if (!StringUtils.isBlank(enterpriseCodeBean.getStartTime())) {
            wrapper.ge("create_date", enterpriseCodeBean.getStartTime() + " 00:00:01");
        }
        if (!StringUtils.isBlank(enterpriseCodeBean.getEndTime())) {

            wrapper.le("create_date", enterpriseCodeBean.getEndTime() + " 23:59:59");
        }
        if (!StringUtils.isBlank(enterpriseCodeBean.getIsUse())) {
            if ("0".equals(enterpriseCodeBean.getIsUse())) {
                wrapper.in("is_use", "0,1");
            } else {
                wrapper.eq("is_use", enterpriseCodeBean.getIsUse());
            }
        }
        int count = enterpriseCodeService.selectCount(wrapper);
        wrapper.last(" LIMIT " + ((enterpriseCodeBean.getPageBean().getPageNumber() - 1) * enterpriseCodeBean.getPageBean().getPageSize()) + "," + enterpriseCodeBean.getPageBean().getPageSize());
        wrapper.orderBy("create_date", true);
        List list = enterpriseCodeService.selectList(wrapper);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNumber", enterpriseCodeBean.getPageBean().getPageNumber());
        map.put("count", count);
        map.put("resultList", list);
        return map;
    }

    /**
     * 旧换新的相关券的信息 wangcan
     *
     * @param
     * @return
     */
    @Api(name = "terminal.getEnterpriseCodeDetal", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = TERMINAL_API_COMMON_AUTHORITY)
    public Object getEnterpriseCodeDetal(EnterpriseCodeBean enterpriseCodeBean) throws Exception {
        return enterpriseCodeService.enterpriseCodeDetil(enterpriseCodeBean.getId());
    }

}
