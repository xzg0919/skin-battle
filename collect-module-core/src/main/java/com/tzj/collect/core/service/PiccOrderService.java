package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.core.param.ali.PiccOrderBean;
import com.tzj.collect.entity.PiccCompany;
import com.tzj.collect.entity.PiccOrder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface PiccOrderService extends IService<PiccOrder> {

    String insertPiccOrder(String aliUserId, PiccOrderBean piccOrderBean)throws ApiException;

    String deletePiccOrderList(String piccOrderIds);
    @DS("slave")
    Object selectPiccErrorOrderList(long piccCompanyId, PiccOrderBean piccOrderBean);
    @DS("slave")
    Object selectPiccSuccessOrderList(long piccCompanyId, PiccOrderBean piccOrderBean);
    @DS("slave")
    Object selectPiccOrderList(long piccCompanyId, PiccOrderBean piccOrderBean);

    Map<String,Object> piccOrderImportExcel(MultipartFile file);

    Object addPiccOrderExcel(PiccCompany piccCompany, List<PiccOrder> piccOrderList);

    String updatePiccWater(String aliUserId, Integer piccWaterId);

    void outPiccOrderExcel(HttpServletResponse response, PiccOrderBean piccOrderBean) throws Exception;
}
