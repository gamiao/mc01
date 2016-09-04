package com.ehealth.mc.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "mailing_record")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class MailingRecord implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = -9083511053874173932L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "mailing_record_gen")
	@SequenceGenerator(name = "mailing_record_gen", sequenceName = "mailing_record_gen", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "n_type")
	private String notificationType;

	@Column(name = "n_id")
	private Long notificationID;

	@Column(name = "mail_from")
	private String mailFrom;

	@Column(name = "mail_To")
	private String mailTo;

	@Column(name = "mail_result")
	private String mailResult;

	@Column(name = "mail_result_detail")
	private String mailResultDetail;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "is_deleted")
	private String isDeleted;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Long getNotificationID() {
		return notificationID;
	}

	public void setNotificationID(Long notificationID) {
		this.notificationID = notificationID;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailResult() {
		return mailResult;
	}

	public void setMailResult(String mailResult) {
		this.mailResult = mailResult;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getMailResultDetail() {
		return mailResultDetail;
	}

	public void setMailResultDetail(String mailResultDetail) {
		this.mailResultDetail = mailResultDetail;
	}

}
