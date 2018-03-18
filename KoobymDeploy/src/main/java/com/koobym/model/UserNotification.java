package com.koobym.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_notification")
public class UserNotification {

	@Id
	@Column(name = "user_notificationId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userNotificationId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	private User user;

	@Column(name = "actionId")
	private long actionId;

	@Column(name = "actionName")
	private String actionName;

	@Column(name = "actionStatus")
	private String actionStatus;

	@Column(name = "extraMessage")
	private String extraMessage;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_actionPerformerId")
	private User userPerformer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_actionPerformedId")
	private BookOwner bookActionPerformedOn;
	

	@Column(name = "isRead")
	private boolean isRead;

	@Column(name = "processedBool")
	private boolean processedBool;
	
	public Boolean getProcessedBool(){
		return processedBool;
	}
	
	public void setProcessedBool(boolean processedBool){
		this.processedBool = processedBool;
	}
	
	public String getExtraMessage(){
		return this.extraMessage;
	}
	
	public void setExtraMessage(String extraMessage){
		this.extraMessage = extraMessage;
	}
	
	public long getUserNotificationId() {
		return userNotificationId;
	}

	public void setUserNotificationId(long userNotificationId) {
		this.userNotificationId = userNotificationId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getActionId() {
		return actionId;
	}

	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public User getUserPerformer() {
		return userPerformer;
	}

	public void setUserPerformer(User userPerformer) {
		this.userPerformer = userPerformer;
	}

	public BookOwner getBookActionPerformedOn() {
		return bookActionPerformedOn;
	}

	public void setBookActionPerformedOn(BookOwner bookActionPerformedOn) {
		this.bookActionPerformedOn = bookActionPerformedOn;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
}
