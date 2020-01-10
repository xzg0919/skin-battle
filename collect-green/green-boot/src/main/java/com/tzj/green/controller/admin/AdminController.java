package com.tzj.green.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("admin")
public class AdminController {



	@RequestMapping("/upload/file")
	public String uploadFile(String name) {
		System.out.println("name  " + name);
		return "admin";
	}


}
