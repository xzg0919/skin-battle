package com.skin.api.admin;

import com.skin.common.util.AssertUtil;
import com.skin.core.service.PullBoxService;
import com.skin.entity.PullBox;
import com.skin.entity.PullBoxSkin;
import com.skin.params.PullBoxBean;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiang
 * @Date: 2022/8/14 13:11
 * @Description:
 */
@ApiService
public class PullBoxApi {

    @Autowired
    PullBoxService pullBoxService;

    @Api(name = "pullBox.changeStatus", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object delete(PullBoxBean pullBoxBean) {
        pullBoxService.changeStatus(pullBoxBean.getId());
        return "success";
    }

    @Api(name = "pullBox.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pullBoxGetPage(PullBoxBean pullBoxBean) {
        return pullBoxService.getPage(pullBoxBean.getPageBean().getPageNum(),pullBoxBean.getPageBean().getPageSize(),
                pullBoxBean.getSkinName()) ;

    }

    @Api(name = "pullBox.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pullBoxGetOne(PullBoxBean pullBoxBean) {
        return   pullBoxService.getById(pullBoxBean.getId());
    }


    @SneakyThrows
    @Api(name = "pullBox.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pullBoxIOU(PullBoxBean pullBoxBean) {
        if (pullBoxBean.getId() == null) {
            PullBox pullBox = new PullBox();
            BeanUtils.copyProperties(pullBoxBean,pullBox);
            pullBox.setEnable(1);
            pullBoxService.save(pullBox);
        } else {

            PullBox pullBox = pullBoxService.getById(pullBoxBean.getId());
            AssertUtil.isNull(pullBox,"数据不存在");
            BeanUtils.copyProperties(pullBoxBean,pullBox);
            pullBoxService.updateById(pullBox);
        }
        return "success";
    }


    @Api(name = "pullBoxSkin.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pullBoxSkinGetPage(PullBoxBean pullBoxBean) {
        return pullBoxService.getSkinPage(pullBoxBean.getPageBean().getPageNum(),pullBoxBean.getPageBean().getPageSize(),
                pullBoxBean.getSkinName()) ;

    }
    @Api(name = "pullBoxSkin.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pullBoxSkinGetOne(PullBoxBean pullBoxBean) {
        return   pullBoxService.getSkinById(pullBoxBean.getId());
    }

    @Api(name = "pullBoxSkin.delete", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pullBoxSkinDelete(PullBoxBean pullBoxBean) {
           pullBoxService.delSkin(pullBoxBean.getId());
        return "success";
    }

    @Api(name = "pullBoxSkin.edit", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pullBoxSkinEdit(PullBoxBean pullBoxBean) {
        PullBoxSkin pullBoxSkin = pullBoxService.getSkinById(pullBoxBean.getId());
        AssertUtil.isNull(pullBoxSkin,"数据不存在");
        AssertUtil.isTrue(pullBoxBean.getProbability()==null,"概率不能为空");
        pullBoxSkin.setProbability(pullBoxBean.getProbability());
        pullBoxService.updateSkin(pullBoxSkin);
        return "success";
    }


    @Api(name = "pullBoxSkin.insert", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object pullBoxSkinInsert(PullBoxBean pullBoxBean) {
        pullBoxService.insertSkin(pullBoxBean.getSkinId(),pullBoxBean.getId(),pullBoxBean.getProbability());
        return "success";
    }
}
