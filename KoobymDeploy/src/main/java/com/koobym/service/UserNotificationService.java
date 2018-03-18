package com.koobym.service;

import java.util.List;

import com.koobym.model.UserNotification;

public interface UserNotificationService extends BaseService<UserNotification, Long> {
	public List<UserNotification> getUserNotificationsForUser(long userId);
	public void notificationIsRead(long userNotificationId);
	public UserNotification sendEarlyNotif(long rentalHeaderId);
	public UserNotification confirmEarlyNotif(long rentalHeaderId);
	public UserNotification rejectEarlyNotif(long rentalHeaderId);
	public UserNotification updateRentalExtraMessage(long userNotificationId);
	public UserNotification updateSwapExtraMessage(long userNotificationId);
	public int getCountNotRead(long userId);
	public void notificationIsProcessed(long userNotificationId);
}
