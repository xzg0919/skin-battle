package com.skin.api.admin;

import com.skin.common.util.AssertUtil;
import com.skin.core.service.BlindBoxService;
import com.skin.entity.BlindBox;
import com.skin.entity.BlindBoxSkin;
import com.skin.params.BlindBoxBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.exception.ApiException;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/15 10:19
 * @Description:
 */
@ApiService
public class BlindBoxApi {


    @Autowired
    private BlindBoxService blindBoxService;



    @Api(name = "blindBox.changeStatus", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object delete(BlindBoxBean blindBoxBean) {
        blindBoxService.changeStatus(blindBoxBean.getId());
        return "success";
    }

    @Api(name = "blindBox.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object blindBoxGetPage(BlindBoxBean blindBoxBean) {
        return blindBoxService.getPage(blindBoxBean.getPageBean().getPageNum(),blindBoxBean.getPageBean().getPageSize(),
                blindBoxBean.getBoxName(),blindBoxBean.getBoxType()) ;

    }

    @Api(name = "blindBox.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object blindBoxGetOne(BlindBoxBean blindBoxBean) {
        return   blindBoxService.getById(blindBoxBean.getId());
    }


    @SneakyThrows
    @Api(name = "blindBox.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object blindBoxIOU(BlindBoxBean blindBoxBean) {
        if(blindBoxBean.getDiscount()!=null &&( blindBoxBean.getDiscount()<1 || blindBoxBean.getDiscount()>10)  ){
            throw new ApiException("折扣必须在1-10之间");
        }
        if (blindBoxBean.getId() == null) {
            BlindBox blindBox = new BlindBox();
            BeanUtils.copyProperties(blindBoxBean,blindBox);
            blindBox.setEnable(1);
            blindBox.setDiscountPrice(blindBox.getPrice().multiply(blindBox.getDiscount()!=null?
                    new BigDecimal(String.valueOf( blindBox.getDiscount()/10.00)): BigDecimal.ONE));
            blindBoxService.save(blindBox);
        } else {
            BlindBox blindBox = blindBoxService.getById(blindBoxBean.getId());
            AssertUtil.isNull(blindBox,"数据不存在");
            BeanUtils.copyProperties(blindBoxBean,blindBox);
            blindBox.setDiscountPrice(blindBox.getPrice().multiply(blindBox.getDiscount()!=null?
                    new BigDecimal(String.valueOf( blindBox.getDiscount()/10.00)): BigDecimal.ONE));
            blindBoxService.updateById(blindBox);
        }
        return "success";
    }


    @Api(name = "blindBoxSkin.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object blindBoxSkinGetPage(BlindBoxBean blindBoxBean) {
        return blindBoxService.getSkinPage(blindBoxBean.getPageBean().getPageNum(),blindBoxBean.getPageBean().getPageSize(),
                blindBoxBean.getSkinName()) ;

    }
    @Api(name = "blindBoxSkin.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object blindBoxSkinGetOne(BlindBoxBean blindBoxBean) {
        return   blindBoxService.getSkinById(blindBoxBean.getId());
    }

    @Api(name = "blindBoxSkin.delete", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object blindBoxSkinDelete(BlindBoxBean blindBoxBean) {
        blindBoxService.delBoxSkin(blindBoxBean.getId());
        return "success";
    }

    @Api(name = "blindBoxSkin.edit", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object blindBoxSkinEdit(BlindBoxBean blindBoxBean) {
        BlindBoxSkin blindBoxSkin = blindBoxService.getSkinById(blindBoxBean.getId());
        AssertUtil.isNull(blindBoxSkin,"数据不存在");
        AssertUtil.isTrue(blindBoxBean.getProbability()==null,"概率不能为空");
        blindBoxSkin.setProbability(blindBoxBean.getProbability());
        blindBoxService.updateSkin(blindBoxSkin);
        return "success";
    }


    @Api(name = "blindBoxSkin.insert", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object blindBoxSkinInsert(BlindBoxBean blindBoxBean) {
        blindBoxService.insertSkin(blindBoxBean.getBoxId(),blindBoxBean.getId(),blindBoxBean.getProbability());
        return "success";
    }
}
