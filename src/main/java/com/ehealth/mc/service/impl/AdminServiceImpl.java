package com.ehealth.mc.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.service.AdminService;

@Service("adminService")
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

	@Value("${mc.admin.login}")
	public String ADMIN_DEFAULT_LOGIN;

	@Value("${mc.admin.password}")
	public String ADMIN_DEFUALT_PASSWORD;

	@Value("${mc.admin.id}")
	public Long ADMIN_DEFUALT_ID;

	@Override
	public Long findOneByLoginAndPassword(String login, String password) {
		if (ADMIN_DEFAULT_LOGIN.equals(login)
				&& ADMIN_DEFUALT_PASSWORD.equals(password)) {
			return ADMIN_DEFUALT_ID;
		}
		return null;
	}

}
