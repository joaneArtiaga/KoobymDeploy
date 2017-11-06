package com.koobym.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.koobym.dao.RentalHeaderDao;
import com.koobym.dao.UserNotificationDao;
import com.koobym.model.RentalHeader;
import com.koobym.model.UserNotification;

@Component
public class TaskScheduler {

	@Autowired
	private RentalHeaderDao rentalHeaderDao;

	@Autowired
	private UserNotificationDao userNotificationDao;

	@Transactional
	@Scheduled(fixedRate = 300000)
	public void checkRentalEndDates() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		List<RentalHeader> rentalHeadersWithElapsedEndDates = rentalHeaderDao.getElapsedRentalDate();
		UserNotification un;
		for (RentalHeader rh : rentalHeadersWithElapsedEndDates) {
			un = new UserNotification();

			un.setActionId(rh.getRentalHeaderId());
			un.setActionName("rental");
			un.setActionStatus("Due");
			un.setBookActionPerformedOn(rh.getRentalDetail().getBookOwner());
			un.setUser(rh.getUserId());
			un.setUserPerformer(rh.getRentalDetail().getBookOwner().getUser());

			userNotificationDao.save(un);

			un = new UserNotification();
			un.setActionId(rh.getRentalHeaderId());
			un.setActionName("rental");
			un.setActionStatus("Due");
			un.setBookActionPerformedOn(rh.getRentalDetail().getBookOwner());
			un.setUser(rh.getRentalDetail().getBookOwner().getUser());
			un.setUserPerformer(rh.getUserId());

			userNotificationDao.save(un);

			rentalHeaderDao.setApprovedExam(rh.getRentalHeaderId(), "Due", format.format(new Date()));
		}			
	}
}
