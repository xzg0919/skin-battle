package com.skin.api.admin;

import com.skin.common.util.AssertUtil;
import com.skin.core.service.BlindBoxService;
import com.skin.core.service.BoxBattleService;
import com.skin.entity.BlindBox;
import com.skin.entity.BlindBoxSkin;
import com.skin.entity.BoxBattle;
import com.skin.entity.BoxBattleSkin;
import com.skin.params.BlindBoxBean;
import com.skin.params.BoxBattleBean;
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
public class BoxBattleApi {


    @Autowired
    private BoxBattleService boxBattleService;



    @Api(name = "boxBattle.changeStatus", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object delete(BoxBattleBean boxBattleBean) {
        boxBattleService.changeStatus(boxBattleBean.getId());
        return "success";
    }

    @Api(name = "boxBattle.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object boxBattleGetPage(BoxBattleBean boxBattleBean) {
        return boxBattleService.getPage(boxBattleBean.getPageBean().getPageNum(),boxBattleBean.getPageBean().getPageSize(),
                boxBattleBean.getBoxName()) ;

    }

    @Api(name = "boxBattle.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object boxBattleGetOne(BoxBattleBean boxBattleBean) {
        return   boxBattleService.getById(boxBattleBean.getId());
    }


    @SneakyThrows
    @Api(name = "boxBattle.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object boxBattleIOU(BoxBattleBean boxBattleBean) {

        if (boxBattleBean.getId() == null) {
            BoxBattle boxBattle = new BoxBattle();
            BeanUtils.copyProperties(boxBattleBean,boxBattle);
            boxBattle.setStatus(0);
            boxBattleService.save(boxBattle);
        } else {
            BoxBattle boxBattle  = boxBattleService.getById(boxBattleBean.getId());
            AssertUtil.isNull(boxBattle,"数据不存在");
            BeanUtils.copyProperties(boxBattleBean,boxBattle);
            boxBattleService.updateById(boxBattle);
        }
        return "success";
    }


    @Api(name = "boxBattleSkin.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object boxBattleSkinGetPage(BoxBattleBean boxBattleBean) {
        return boxBattleService.getSkinPage(boxBattleBean.getPageBean().getPageNum(),boxBattleBean.getPageBean().getPageSize(),
                boxBattleBean.getSkinName()) ;

    }
    @Api(name = "boxBattleSkin.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object boxBattleSkinGetOne(BoxBattleBean boxBattleBean) {
        return   boxBattleService.getSkinById(boxBattleBean.getId());
    }

    @Api(name = "boxBattleSkin.delete", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object boxBattleSkinDelete(BoxBattleBean boxBattleBean) {
        boxBattleService.delBoxSkin(boxBattleBean.getId());
        return "success";
    }

    @Api(name = "boxBattleSkin.edit", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object boxBattleSkinEdit(BoxBattleBean boxBattleBean) {
        BoxBattleSkin boxBattleSkin = boxBattleService.getSkinById(boxBattleBean.getId());
        AssertUtil.isNull(boxBattleSkin,"数据不存在");
        AssertUtil.isTrue(boxBattleBean.getProbability()==null,"概率不能为空");
        boxBattleSkin.setProbability(boxBattleBean.getProbability());
        boxBattleService.updateSkin(boxBattleSkin);
        return "success";
    }


    @Api(name = "boxBattleSkin.insert", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object boxBattleSkinInsert(BoxBattleBean boxBattleBean) {
        boxBattleService.insertSkin(boxBattleBean.getBoxBattleId(),boxBattleBean.getId(),boxBattleBean.getProbability());
        return "success";
    }
}
