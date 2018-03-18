package com.koobym.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koobym.dao.UserNotificationDao;
import com.koobym.model.UserNotification;
import com.koobym.service.UserNotificationService;

@Service
@Transactional
public class UserNotificationServiceImpl extends BaseServiceImpl<UserNotification, Long>
		implements UserNotificationService {

	private UserNotificationDao userNotificationDao;

	@Autowired
	public UserNotificationServiceImpl(UserNotificationDao userNotificationDao) {
		super(userNotificationDao);
		this.userNotificationDao = userNotificationDao;
	}

	@Override
	public List<UserNotification> getUserNotificationsForUser(long userId) {
		return userNotificationDao.getUserNotificationsForUser(userId);
	}

	@Override
	public void notificationIsRead(long userNotificationId) {
		userNotificationDao.notificationIsRead(userNotificationId);
	}

	@Override
	public UserNotification sendEarlyNotif(long rentalHeaderId){
		return userNotificationDao.sendEarlyNotif(rentalHeaderId);
	}
	
	@Override
	public UserNotification confirmEarlyNotif(long rentalHeaderId){
		return userNotificationDao.confirmEarlyNotif(rentalHeaderId);
	}

	@Override
	public UserNotification rejectEarlyNotif(long rentalHeaderId){
		return userNotificationDao.rejectEarlyNotif(rentalHeaderId);
	}
	
	@Override
	public UserNotification updateRentalExtraMessage(long userNotificationId){
		return userNotificationDao.updateRentalExtraMessage(userNotificationId);
	}
	
	@Override
	public UserNotification updateSwapExtraMessage(long userNotificationId){
		return userNotificationDao.updateSwapExtraMessage(userNotificationId);
	}
	
	@Override
	public int getCountNotRead(long userId){
		return userNotificationDao.getCountNotRead(userId);
	}
	
	@Override
	public void notificationIsProcessed(long userNotificationId){
		userNotificationDao.notificationIsProcessed(userNotificationId);
	}
}
