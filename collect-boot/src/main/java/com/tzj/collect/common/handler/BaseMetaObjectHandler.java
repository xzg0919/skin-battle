package com.tzj.collect.common.handler;


import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.tzj.collect.common.constant.DataBaseConstant;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

public class BaseMetaObjectHandler extends MetaObjectHandler {

	/**
	 * 新增
	 */
	public void insertFill(MetaObject metaObject) {
		// 创建用户
		/*
		Object createBy = getFieldValByName(DataBaseConstant.CREATE_BY, metaObject);
		if (createBy == null) {
			setFieldValByName(DataBaseConstant.CREATE_BY, UserUtils.getUser(), metaObject);
		}
		*/

		// 创建时间
		Object createDate = getFieldValByName(DataBaseConstant.CREATE_DATE, metaObject);
		if (createDate == null) {
			setFieldValByName(DataBaseConstant.CREATE_DATE, new Date(), metaObject);
		}

		// 删除标记
		Object delFlag = getFieldValByName(DataBaseConstant.DEL_FLAG, metaObject);
		if (delFlag == null) {
			setFieldValByName(DataBaseConstant.DEL_FLAG, DataBaseConstant.DEL_FLAG_NORMAL, metaObject);
		}

		Object updateDate = getFieldValByName(DataBaseConstant.UPDATE_DATE, metaObject);
		if (updateDate == null) {
			setFieldValByName(DataBaseConstant.UPDATE_DATE, new Date(), metaObject);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 更新用户
		/*
		Object updateBy = getFieldValByName(DataBaseConstant.UPDATE_BY, metaObject);
		if (updateBy == null) {
			setFieldValByName(DataBaseConstant.UPDATE_BY, UserUtils.getUser(), metaObject);
		}
		*/

		// 更新用户
		Object updateDate = getFieldValByName(DataBaseConstant.UPDATE_DATE, metaObject);
		if (updateDate == null) {
			setFieldValByName(DataBaseConstant.UPDATE_DATE, new Date(), metaObject);
		}
	}
}