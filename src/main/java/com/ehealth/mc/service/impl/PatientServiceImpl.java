package com.ehealth.mc.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehealth.mc.bo.Patient;
import com.ehealth.mc.bo.QPatient;
import com.ehealth.mc.dao.PatientDAO;
import com.ehealth.mc.service.NotificationService;
import com.ehealth.mc.service.PatientService;
import com.ehealth.mc.service.util.EntityConvertUtil;
import com.ehealth.mc.service.util.QueryExpressionUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("patientService")
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PatientDAO patientDAO;

	@Autowired
	private NotificationService notificationService;

	@Override
	public Patient findById(Long id) {
		return patientDAO.findOne(id);
	}

	@Override
	@Transactional
	public Patient upsertBasicInfo(Entity e) {
		Long id = EntityConvertUtil.getID(e);
		Patient original = null;
		if (id != null) {
			original = findById(id);
		}
		Patient objToSave = EntityConvertUtil.getPatient(e, original);
		if (objToSave != null) {
			patientDAO.save(objToSave);
			return objToSave;
		}
		return null;
	}

	@Override
	@Transactional
	public Patient updateAvatar(String avatar, Long id) {
		int updatedCount = patientDAO.setAvatar(avatar, id);
		if (updatedCount > 0) {
			return findById(id);
		}
		return null;
	}

	@Override
	@Transactional
	public Patient updatePassword(String newPassword, Long id, String oldPassword) {
		int updatedCount = patientDAO.updatePassword(newPassword, id, oldPassword);
		if (updatedCount > 0) {
			String nTitle = "账户密码修改成功";
			String nDescription = "您的账户密码修改成功！";
			notificationService.notifyPatient(findById(id), nTitle, nDescription);
			return findById(id);
		}
		return null;
	}

	@Override
	public Patient findOneByLoginAndPassword(String login, String password) {
		return patientDAO.findOneByLoginAndPassword(login, password);
	}

	@Override
	public Patient findOneByLogin(String login) {
		return patientDAO.findOneByLogin(login);
	}

	@Override
	public List<Patient> findByFilterString(String filterString) {

		Predicate querys = QueryExpressionUtil.getPatientWhereClausesByFilterString(filterString);
		if (querys != null) {
			return Lists.newArrayList(patientDAO.findAll(querys));
		}
		return null;
	}

	@Override
	public List<Patient> findAll() {
		return Lists.newArrayList(patientDAO.findAll());
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean updateIsDeleted(String value, Long[] objIDs) throws RuntimeException {
		for (Long id : objIDs) {
			if (id == null) {
				throw new RuntimeException("Some id is empty!");
			}
			Patient obj = patientDAO.findOne(id);
			if (obj == null) {
				throw new RuntimeException("Can not find the object with ID:" + id);
			}
			obj.setIsDeleted(value);

			try {
				patientDAO.save(obj);
			} catch (Exception e) {
				throw new RuntimeException("Can't update the object with ID:" + id);
			}

			String nTitle = "账户状态通知";
			String nDescription = null;
			if ("Y".equals(value)) {
				nDescription = "您的账户已经被管理员禁用，更多详情请联系管理员。";
			} else if ("N".equals(value)) {
				nDescription = "您的账户已经被管理员启用，更多详情请联系管理员。";
			}

			if (nDescription != null) {
				notificationService.notifyPatient(obj, nTitle, nDescription);
			}
		}
		return true;
	}

	@Override
	public List<Patient> findByIsDeleted(String isDeleted) {
		return patientDAO.findByIsDeleted(isDeleted);
	}

	@Override
	public Patient findOneByMail(String mail) {
		return patientDAO.findOneByMail(mail);
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void mailAccountInfo(Patient user) {
		if (user != null) {
			String nTitle = "找回账户密码信息";
			String nDescription = "您的帐户名为：" + user.getLogin() + ", 密码为：" + user.getPassword();
			notificationService.notifyPatient(user, nTitle, nDescription);
		}
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean mailAccountInfoByMail(String mail) {
		QPatient patient = QPatient.patient;
		BooleanExpression exp1 = patient.mail.eq(mail);
		List<Patient> resultlist = Lists.newArrayList(patientDAO.findAll(exp1));
		if (resultlist != null && resultlist.size() > 0) {
			for (Patient one : resultlist) {
				mailAccountInfo(one);
			}
			return true;
		}
		return false;
	}

}
