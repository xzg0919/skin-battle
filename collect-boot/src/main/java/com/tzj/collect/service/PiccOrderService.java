package com.tzj.collect.service;

import com.baomidou.mybatisplus.service.IService;
import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.param.PiccOrderBean;
import com.tzj.collect.entity.PiccCompany;
import com.tzj.collect.entity.PiccOrder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface PiccOrderService extends IService<PiccOrder> {

    String insertPiccOrder(long memberId,PiccOrderBean piccOrderBean)throws ApiException;

    String deletePiccOrderList(String piccOrderIds);

    Object selectPiccErrorOrderList(long piccCompanyId,PiccOrderBean piccOrderBean);

    Object selectPiccSuccessOrderList(long piccCompanyId,PiccOrderBean piccOrderBean);

    Object selectPiccOrderList(long piccCompanyId,PiccOrderBean piccOrderBean);

    Map<String,Object> piccOrderImportExcel(MultipartFile file);

    Object addPiccOrderExcel(PiccCompany piccCompany, List<PiccOrder> piccOrderList);

    String updatePiccWater(Integer memberId,Integer piccWaterId);

    void outPiccOrderExcel(HttpServletResponse response, PiccOrderBean piccOrderBean) throws Exception;
}
