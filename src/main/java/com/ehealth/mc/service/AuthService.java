package com.ehealth.mc.service;

import java.util.List;

import com.ehealth.mc.bo.Doctor;

public interface AuthService {
	
	List<Doctor> findByName(String name);

}
