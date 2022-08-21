package com.skin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.skin.entity.PullBoxSkin;
import org.apache.ibatis.annotations.Param;

public interface PullBoxSkinMapper extends BaseMapper<PullBoxSkin> {


    void updateSkin(@Param("pullBoxId") Long pullBoxId);
}
