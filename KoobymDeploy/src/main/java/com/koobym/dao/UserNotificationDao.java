package com.koobym.dao;

import java.util.List;

import com.koobym.model.UserNotification;

public interface UserNotificationDao extends BaseDao<UserNotification, Long> {
	public List<UserNotification> getUserNotificationsForUser(long userId);
	public void notificationIsRead(long userNotificationId);
	public UserNotification sendEarlyNotif(long rentalHeaderId);
	public UserNotification confirmEarlyNotif(long rentalHeaderId);
	public UserNotification rejectEarlyNotif(long rentalHeaderId);
	public UserNotification updateRentalExtraMessage(long userNotificationId);
	
}