package com.tzj.collect.api.admin;

import com.tzj.collect.core.param.admin.AdminBean;
import com.tzj.collect.core.service.AdminService;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.core.param.token.TokenBean;
import com.tzj.module.api.annotation.*;
import com.tzj.module.api.entity.Subject;
import com.tzj.module.api.utils.JwtUtils;
import com.tzj.module.easyopen.ApiContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static com.tzj.common.constant.TokenConst.*;

@ApiService
public class AdminTokenApi {
	@Autowired
	private AdminService adminService;
	
	/**
	 * @Title: getToken 
	 * @Description:admin获取token
	 * @author: 向忠国
	 * @param @param adminBean
	 * @param @return    设定文件  
	 * @return TokenBean    返回类型  
	 * @throw
	 * 忽略token验证，需要sign签名验证
	 */
    @Api(name = "admin.token.get", version = "1.0")
    @SignIgnore
    @AuthIgnore //这个api忽略token验证
    public TokenBean getToken(AdminBean adminBean) {

       //todo
       //判断用户名和密码
    	Admin admin = adminService.selectByUserNameAndPwd(adminBean.getUsername(),adminBean.getPassword());
        if(admin!=null){
        String token = JwtUtils.generateToken(admin.getId().toString(), ADMIN_API_EXPRIRE, ADMIN_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ADMIN_API_TOKEN_CYPTO_KEY);
        TokenBean tokenBean = new TokenBean();
        tokenBean.setExpire(ADMIN_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        return tokenBean;
        }else{
        	try {
				throw new Exception("用户名或密码错误");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return null;
    }
    
    
    /**
     * 
     * @Title: flushToken 
     * @Description:刷新token 
     * 需要token验证，忽略sign签名验证
     * 需要 ADMIN_API_COMMON_AUTHORITY 权限
     * @author: 向忠国
     * @param @return    设定文件  
     * @return TokenBean    返回类型  
     * @throw
     */
    @Api(name = "admin.token.flush", version = "1.0")
    @RequiresPermissions(values = ADMIN_API_COMMON_AUTHORITY)
    public TokenBean flushToken() {
        HttpServletRequest request = ApiContext.getRequest();
        Subject subject = (Subject) request.getAttribute("subject");

        //接口里面获取  Recyclers 的例子
        Admin admin = (Admin) subject.getUser();

        String token = JwtUtils.generateToken(subject.getId(), ADMIN_API_EXPRIRE, ADMIN_API_TOKEN_SECRET_KEY);
        String securityToken = JwtUtils.generateEncryptToken(token, ADMIN_API_TOKEN_CYPTO_KEY);

        TokenBean tokenBean = new TokenBean();
        tokenBean.setExpire(ADMIN_API_EXPRIRE);
        tokenBean.setToken(securityToken);
        return tokenBean;

    }
}
