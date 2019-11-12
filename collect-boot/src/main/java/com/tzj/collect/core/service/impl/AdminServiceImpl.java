package com.tzj.collect.core.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.AdminMapper;
import com.tzj.collect.core.service.AdminService;
import com.tzj.collect.entity.Admin;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.*;


@Service
@Transactional(readOnly=true)
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
	
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

	@Override
	public Object getToken(String userName, String password) {

		Admin admin = this.selectOne(new EntityWrapper<Admin>().eq("username", userName).eq("password", password).eq("del_flag", 0));
		if (null == admin ){
			throw new ApiException("用户名或密码错误");
		}
		Map<String,Object> resultMap = new HashMap<>();
		String token= JwtUtils.generateToken(admin.getId().toString(), ALI_API_EXPRIRE,ADMIN_API_TOKEN_SECRET_KEY);
		String securityToken=JwtUtils.generateEncryptToken(token,ADMIN_API_TOKEN_CYPTO_KEY);
		System.out.println("token是 : "+securityToken);
		resultMap.put("token",securityToken);
		resultMap.put("Admin",admin);
		return resultMap;
	}
}
