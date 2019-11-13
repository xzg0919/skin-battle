package com.tzj.collect.api.enterprise;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.util.EnterpriseUtils;
import com.tzj.collect.core.param.enterprise.EnterpriseTerminalBean;
import com.tzj.collect.core.service.EnterpriseTerminalService;
import com.tzj.collect.entity.EnterpriseAccount;
import com.tzj.collect.entity.EnterpriseTerminal;
import static com.tzj.common.constant.TokenConst.ENTERPRISE_API_COMMON_AUTHORITY;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@ApiService
public class EnterpriseTerminalApi {

    @Autowired
    private EnterpriseTerminalService enterpriseTerminalService;

    /**
     * 更改/新增以旧换新的销售终端 王灿
     *
     * @param enterpriseTerminalBean
     * @return
     */
    @Api(name = "enterprise.updateEnterpriseTerminal", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    public Object updateEnterpriseTerminal(EnterpriseTerminalBean enterpriseTerminalBean) throws Exception {
        EnterpriseAccount enterpriseAccount = EnterpriseUtils.getEnterpriseAccount();
        return enterpriseTerminalService.updateEnterpriseTerminal(enterpriseTerminalBean, enterpriseAccount.getEnterpriseId());
    }

    /**
     * 删除以旧换新的销售终端 王灿
     *
     * @param enterpriseTerminalBean
     * @return
     */
    @Api(name = "enterprise.deleteEnterpriseTerminal", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    public Object deleteEnterpriseTerminal(EnterpriseTerminalBean enterpriseTerminalBean) throws Exception {
        EnterpriseAccount enterpriseAccount = EnterpriseUtils.getEnterpriseAccount();
        EnterpriseTerminal enterpriseTerminal = enterpriseTerminalService.selectById(enterpriseTerminalBean.getId());
        enterpriseTerminal.setDelFlag("1");
        boolean b = enterpriseTerminalService.updateById(enterpriseTerminal);
        if (!b) {
            return "操作失败";
        }
        return "操作成功";
    }

    /**
     * 以旧换新的销售终端列表 王灿
     *
     * @param enterpriseTerminalBean
     * @return+Zx` `
     */
    @Api(name = "enterprise.enterpriseTerminalList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    @DS("slave")
    public Object enterpriseTerminalList(EnterpriseTerminalBean enterpriseTerminalBean) throws Exception {
        EnterpriseAccount enterpriseAccount = EnterpriseUtils.getEnterpriseAccount();
        EntityWrapper<EnterpriseTerminal> enterpriseTerminalEntityWrapper = new EntityWrapper<>();
        enterpriseTerminalEntityWrapper.eq("enterprise_id", enterpriseAccount.getEnterpriseId()).eq("del_flag", 0);
        if (!StringUtils.isBlank(enterpriseTerminalBean.getName())) {
            enterpriseTerminalEntityWrapper.like("name_", enterpriseTerminalBean.getName());
        }
        enterpriseTerminalEntityWrapper.orderBy("create_date", false);
        //查询条数
        int count = enterpriseTerminalService.selectCount(enterpriseTerminalEntityWrapper);
        enterpriseTerminalEntityWrapper.last(" LIMIT " + ((enterpriseTerminalBean.getPageBean().getPageNumber() - 1) * enterpriseTerminalBean.getPageBean().getPageSize()) + "," + enterpriseTerminalBean.getPageBean().getPageSize());
        List<EnterpriseTerminal> terminalsList = enterpriseTerminalService.selectList(enterpriseTerminalEntityWrapper);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("terminalsList", terminalsList);
        map.put("count", count);
        map.put("pageNum", enterpriseTerminalBean.getPageBean().getPageNumber());
        return map;
    }

    /**
     * 根据以旧换新终端Id查询销售终端详细信息 王灿
     *
     * @param enterpriseTerminalBean
     * @return+Zx` `
     */
    @Api(name = "enterprise.enterpriseTerminalById", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ENTERPRISE_API_COMMON_AUTHORITY)
    @DS("slave")
    public Object enterpriseTerminalById(EnterpriseTerminalBean enterpriseTerminalBean) throws Exception {

        return enterpriseTerminalService.selectById(enterpriseTerminalBean.getId());
    }

}
