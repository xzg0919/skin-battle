package com.tzj.collect.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.mapper.AdminMapper;
import com.tzj.collect.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly=true)
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService{
	
	@Autowired
	private AdminMapper adminMapper;

	/**
	 * 
	 * @Title: selectByUserNameAndPwd 
	 * @Description:账号密码登录
	 * @author: 向忠国
	 * @param @param UserName
	 * @param @param Pwd
	 * @param @return   Admin  
	 * @throw
	 */
	@Override
	public Admin selectByUserNameAndPwd(String userName, String pwd) {
		return selectOne(new EntityWrapper<Admin>().eq("username", userName).eq("password", pwd));
	}

	/**
	 * 
	 * @Title: selectByid 
	 * @Description:通过ID获取admin
	 * @author: 向忠国
	 * @param @param id
	 * @param @return   Admin 
	 * @throw
	 */
	@Override
	public Admin selectByid(Long id) {
		return adminMapper.selectById(id);
	}
}
