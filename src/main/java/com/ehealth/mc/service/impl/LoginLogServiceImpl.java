package com.ehealth.mc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.LoginLog;
import com.ehealth.mc.dao.LoginLogDAO;
import com.ehealth.mc.service.LoginLogService;

@Service("loginLogService")
@Transactional
public class LoginLogServiceImpl implements LoginLogService {

	@Autowired
	LoginLogDAO loginLogDAO;

	@Override
	public LoginLog create(LoginLog loginLog) {
		return loginLogDAO.save(loginLog);
	}
}
