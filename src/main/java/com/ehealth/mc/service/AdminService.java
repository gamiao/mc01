package com.ehealth.mc.service;

public interface AdminService {

	Long findOneByLoginAndPassword(String login, String password);

}
