package com.ehealth.mc.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.service.AdminService;

@Service("adminService")
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

	public static final String ADMIN_DEFAULT_LOGIN = "mcAdmin";
	public static final String ADMIN_DEFUALT_PASSWORD = "m3Admin";

	@Override
	public Long findOneByLoginAndPassword(String login, String password) {
		if (ADMIN_DEFAULT_LOGIN.equals(login)
				&& ADMIN_DEFUALT_PASSWORD.equals(password)) {
			return new Long(8001);
		}
		return null;
	}

}
