package com.skin.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import java.time.LocalDateTime;
import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;


@Component
public class BaseMetaObjectHandler  implements MetaObjectHandler {


    /**
     * 新增
     */

    @Override
    public void insertFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject, DataBaseConstant.CREATE_DATE, () -> new Date(),Date.class);


        this.strictInsertFill(metaObject, DataBaseConstant.DEL_FLAG, () -> DataBaseConstant.DEL_FLAG_NORMAL, String.class);

        this.strictInsertFill(metaObject, DataBaseConstant.UPDATE_DATE, () ->  new Date(),Date.class);


    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject, DataBaseConstant.UPDATE_DATE, () -> LocalDateTime.now(), LocalDateTime.class);
    }
}
