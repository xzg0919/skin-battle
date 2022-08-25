package com.skin.api.admin;

import com.skin.common.util.AssertUtil;
import com.skin.core.service.RollRoomService;
import com.skin.entity.RollRoom;
import com.skin.params.RollRoomBean;
import com.skin.entity.RollRoomSkinInfo;
import com.skin.task.RollRoomItem;
import com.skin.task.RollRoomTask;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.common.utils.StringUtils;
import com.tzj.module.easyopen.exception.ApiException;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;

import static com.skin.common.constant.TokenConst.ADMIN_API_COMMON_AUTHORITY;

/**
 * @Auther: xiang
 * @Date: 2022/8/13 12:36
 * @Description:
 */
@ApiService
public class RollRoomApi {

    @Autowired
    RollRoomService     rollRoomService;


    public static void main(String[] args) {
        Integer ss= 128;
        Integer ss1 =128;

        System.out.println(ss.equals(ss1));
    }

    @Api(name = "rollRoom.delete", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object delete(RollRoomBean rollRoomBean) {
        rollRoomService.removeById(rollRoomBean.getId());
        return "success";
    }

    @Api(name = "rollRoom.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollRoomGetPage(RollRoomBean rollRoomBean) {

        return rollRoomService.getRoomPage(rollRoomBean.getPageBean().getPageNum(),rollRoomBean.getPageBean().getPageSize(),
                rollRoomBean.getRoomType(),rollRoomBean.getName()) ;

    }

    @Api(name = "rollRoom.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollRoomGetOne(RollRoomBean rollRoomBean) {
        return   rollRoomService.getById(rollRoomBean.getId());
    }


    @SneakyThrows
    @Api(name = "rollRoom.insertOrUpdate", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollRoomIOU(RollRoomBean rollRoomBean) {
        //设置的时间必须大于当前时间+5分钟
        Date now =new Date();
        RollRoom rollRoom;
        if(StringUtils.isNotBlank(rollRoomBean.getLotteryTime()) &&
                DateUtils.parseDate(rollRoomBean.getLotteryTime(),"yyyy-MM-dd HH:mm:ss").getTime()-now.getTime()<5*60*1000){
            throw new ApiException("设置的时间必须大于当前时间+5分钟") ;
        }
        if (rollRoomBean.getId() == null) {
            rollRoom= new RollRoom();
            BeanUtils.copyProperties(rollRoomBean,rollRoom);
            rollRoom.setRoomStatus(1);
            rollRoom.setLotteryTime(DateUtils.parseDate(rollRoomBean.getLotteryTime(),"yyyy-MM-dd HH:mm:ss"));
            rollRoomService.save(rollRoom);
        } else {

            rollRoom = rollRoomService.getById(rollRoomBean.getId());
            AssertUtil.isNull(rollRoom,"该房间不存在");
            //已开奖的数据不允许编辑
            if(rollRoom.getRoomStatus()==2){
                throw new ApiException("已开奖的数据不允许编辑") ;
            }
            BeanUtils.copyProperties(rollRoomBean,rollRoom);
            rollRoom.setLotteryTime(DateUtils.parseDate(rollRoomBean.getLotteryTime(),"yyyy-MM-dd HH:mm:ss"));
            RollRoomTask.queue.removeIf(rollRoomItem -> rollRoomItem.getId().equals(rollRoomBean.getId()));

            rollRoomService.updateById(rollRoom);
        }
        RollRoomItem rollRoomItem =new RollRoomItem(rollRoom.getName(),rollRoom.getLotteryTime().getTime(),rollRoom.getId());
        RollRoomTask.queue.put(rollRoomItem);
        return "success";
    }



    @Api(name = "rollSkin.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollSkinGetPage(RollRoomBean rollRoomBean) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("page", rollRoomService.getRoomSkinPage(rollRoomBean.getPageBean().getPageNum(),rollRoomBean.getPageBean().getPageSize(),
                rollRoomBean.getName(),rollRoomBean.getId()));
        map.put("sumPrice", rollRoomService.sumRoomPrice(rollRoomBean.getId()));
        return map;
    }

    @Api(name = "rollSkin.delete", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollSkinDelete(RollRoomBean rollRoomBean) {
        rollRoomService.delRoomSkinInfo(rollRoomBean.getId());
        return "success";
    }

    @Api(name = "rollSkin.edit", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollSkinEdit(RollRoomBean rollRoomBean) {
        RollRoomSkinInfo roomSkinInfo = rollRoomService.getRoomSkinInfo(rollRoomBean.getId());
        AssertUtil.isNull(roomSkinInfo,"该皮肤不存在");
        //判断有没有参加
         AssertUtil.isTrue(StringUtils.isNotBlank(rollRoomBean.getSpecifiedUser())
        && !rollRoomService.isJoin(rollRoomBean.getId(),rollRoomBean.getSpecifiedUser()),"该用户没有加入该房间");
        roomSkinInfo.setSpecifiedUser(rollRoomBean.getSpecifiedUser().isEmpty()?null:rollRoomBean.getSpecifiedUser());
        rollRoomService.updateRoomSkinInfo(roomSkinInfo);
        return "success";
    }

    @Api(name = "rollSkin.getOne", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollSkinGetOne(RollRoomBean rollRoomBean) {
        return rollRoomService.getRoomSkinInfo(rollRoomBean.getId());
    }


    @Api(name = "rollSkinUser.getPage", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollSkinUserGetPage(RollRoomBean rollRoomBean) {
        return rollRoomService.getRoomUserPage(rollRoomBean.getPageBean().getPageNum(),rollRoomBean.getPageBean().getPageSize(),rollRoomBean.getId());
    }


    @Api(name = "rollSkin.add", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public Object rollSkinAdd(RollRoomBean rollRoomBean) {
        rollRoomService.addToRoom(rollRoomBean.getSkinId(),rollRoomBean.getId());
        return "success";
    }

}
