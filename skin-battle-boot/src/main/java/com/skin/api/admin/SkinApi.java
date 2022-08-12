package com.skin.api.admin;

import com.skin.core.service.MallSkinService;
import com.skin.core.service.SkinService;
import com.skin.entity.MallSkin;
import com.skin.entity.Skin;
import com.skin.params.SkinBean;
import com.skin.params.UserBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 13:52
 * @Description:
 */
@ApiService
public class SkinApi {


    @Autowired
    SkinService skinService;

    @Autowired
    MallSkinService mallSkinService;

    @Api(name = "skin.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object insertOrUpdate(Skin skin) {
        if (skin.getId() == null) {
            skinService.save(skin);
        } else {
            skinService.updateById(skin);
        }
        return "success";
    }


    @Api(name = "skin.delete", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object delete(Skin skin) {
        skinService.removeById(skin.getId());
        return "success";
    }

    @Api(name = "skin.pageList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pageList(SkinBean skinBean) {
        return skinService.getSkinPage(skinBean.getPageBean().getPageNum(), skinBean.getPageBean().getPageSize(), skinBean.getSkinName());
    }

    @Api(name = "skin.skinInfo", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object skinInfo(Skin skin) {
        return skinService.getById(skin.getId());
    }


    @Api(name = "mallSkin.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object mallSkinInsertOrUpdate(MallSkin skin) {
        if (skin.getId() == null) {
            skin.setConsumeStock(0);
            skin.setTotalStock(skin.getStock());
            mallSkinService.save(skin);
        } else {
            skin.setTotalStock(skin.getStock());
            mallSkinService.updateById(skin);
        }
        return "success";
    }


    @Api(name = "mallSkin.delete", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object delete(MallSkin skin) {
        mallSkinService.removeById(skin.getId());
        return "success";
    }

    @Api(name = "mallSkin.pageList", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object mallSkinpageList(SkinBean skinBean) {
        return mallSkinService.getSkinPage(skinBean.getPageBean().getPageNum(), skinBean.getPageBean().getPageSize(), skinBean.getSkinName());
    }

    @Api(name = "mallSkin.skinInfo", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object mallSkinInfo(MallSkin skin) {
        return mallSkinService.getById(skin.getId());
    }


}
