package com.ehealth.mc.service.impl;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.Doctor;
import com.ehealth.mc.bo.QDoctor;
import com.ehealth.mc.dao.DoctorDAO;
import com.ehealth.mc.service.DoctorService;
import com.ehealth.mc.service.NotificationService;
import com.ehealth.mc.service.util.EntityConvertUtil;
import com.ehealth.mc.service.util.QueryExpressionUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("doctorService")
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	DoctorDAO doctorDAO;

	@Autowired
	private NotificationService notificationService;

	@Override
	public List<Doctor> findAll() {
		return Lists.newArrayList(doctorDAO.findAll());
	}

	@Override
	public Doctor findById(Long id) {
		return doctorDAO.findOne(id);
	}

	@Override
	@Transactional
	public Doctor upsertBasicInfo(Entity e) {
		Long id = EntityConvertUtil.getID(e);
		Doctor original = null;
		if (id != null) {
			original = findById(id);
		}
		Doctor objToSave = EntityConvertUtil.getDoctor(e, original);
		if (objToSave != null) {
			Doctor result = doctorDAO.save(objToSave);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	@Transactional
	public Doctor updateAvatar(String avatar, Long id) {
		int updatedCount = doctorDAO.setAvatar(avatar, id);
		if (updatedCount > 0) {
			return findById(id);
		}
		return null;
	}

	@Override
	@Transactional
	public Doctor updatePassword(String newPassword, Long id, String oldPassword) {
		int updatedCount = doctorDAO.updatePassword(newPassword, id, oldPassword);
		if (updatedCount > 0) {
			String nTitle = "账户密码修改成功";
			String nDescription = "您的账户密码修改成功！";
			notificationService.notifyDoctor(findById(id), nTitle, nDescription);
			return findById(id);
		}
		return null;
	}

	@Override
	public List<Doctor> findByIsDeleted(String isDeleted) {
		return doctorDAO.findByIsDeleted(isDeleted);
	}

	@Override
	public Doctor findOneByLoginAndPassword(String login, String password) {
		return doctorDAO.findOneByLoginAndPassword(login, password);
	}

	@Override
	public Doctor findOneByLogin(String login) {
		return doctorDAO.findOneByLogin(login);
	}

	@Override
	public List<Doctor> findByFilterString(String filterString) {
		Predicate querys = QueryExpressionUtil.getDoctorWhereClausesByFilterString(filterString);
		if (querys != null) {
			return Lists.newArrayList(doctorDAO.findAll(querys));
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean updateIsDeleted(String value, Long[] objIDs) throws RuntimeException {
		for (Long id : objIDs) {
			if (id == null) {
				throw new RuntimeException("Some id is empty!");
			}
			Doctor obj = doctorDAO.findOne(id);
			if (obj == null) {
				throw new RuntimeException("Can not find the object with ID:" + id);
			}
			obj.setIsDeleted(value);

			try {
				doctorDAO.save(obj);

				String nTitle = "账户状态通知";
				String nDescription = null;
				if ("Y".equals(value)) {
					nDescription = "您的账户已经被管理员禁用，更多详情请联系管理员。";
				} else if ("N".equals(value)) {
					nDescription = "您的账户已经被管理员启用，更多详情请联系管理员。";
				}

				if (nDescription != null) {
					notificationService.notifyDoctor(obj, nTitle, nDescription);
				}
			} catch (Exception e) {
				throw new RuntimeException("Can't update the object with ID:" + id);
			}
		}
		return true;
	}

	@Override
	public Doctor findOneByMail(String mail) {
		return doctorDAO.findOneByMail(mail);
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void mailAccountInfo(Doctor user) {
		if (user != null) {
			String nTitle = "找回账户密码信息";
			String nDescription = "您的帐户名为：" + user.getLogin() + ", 密码为：" + user.getPassword();
			notificationService.notifyDoctor(user, nTitle, nDescription);
		}
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean mailAccountInfoByMail(String mail) {
		QDoctor doctor = QDoctor.doctor;
		BooleanExpression exp1 = doctor.mail.eq(mail);
		List<Doctor> resultlist = Lists.newArrayList(doctorDAO.findAll(exp1));
		if (resultlist != null && resultlist.size() > 0) {
			for (Doctor one : resultlist) {
				mailAccountInfo(one);
			}
			return true;
		}
		return false;
	}

}
