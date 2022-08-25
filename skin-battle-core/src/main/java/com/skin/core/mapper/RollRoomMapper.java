package com.skin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.skin.entity.RollRoom;
import com.skin.params.PageBean;
import com.skin.vo.RollVo;
import org.apache.ibatis.annotations.Param;

public interface RollRoomMapper extends BaseMapper<RollRoom> {

    Page<RollVo>  getRollPage(Page<RollVo> page, @Param("status") Integer status,@Param("userId") Long userId);

}
