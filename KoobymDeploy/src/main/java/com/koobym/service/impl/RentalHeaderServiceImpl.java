package com.koobym.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koobym.dao.RentalHeaderDao;
import com.koobym.dao.UserNotificationDao;
import com.koobym.model.AuctionHeader;
import com.koobym.model.RentalHeader;
import com.koobym.model.UserNotification;
import com.koobym.pusher.PusherServer;
import com.koobym.service.RentalHeaderService;

@Service
@Transactional
public class RentalHeaderServiceImpl extends BaseServiceImpl<RentalHeader, Long> implements RentalHeaderService {

	private RentalHeaderDao rentalHeaderDao;
	private UserNotificationDao userNotificationDao;

	@Autowired
	private PusherServer pusherServer;

	@Autowired
	public RentalHeaderServiceImpl(RentalHeaderDao rentalHeaderDao, UserNotificationDao userNotificationDao) {
		super(rentalHeaderDao);
		this.rentalHeaderDao = rentalHeaderDao;
		this.userNotificationDao = userNotificationDao;
	}

	public boolean canRent(long userId) {
		return rentalHeaderDao.canRent(userId);
	}

	@Override
	public List<RentalHeader> getToDeliverById(int userId) {
		return rentalHeaderDao.getToDeliverById(userId);
	}

	@Override
	public List<RentalHeader> getListRentalById(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getListRentalById(userId);
	}

	@Override
	public List<RentalHeader> getToReceiveByIdRenter(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getToReceiveByIdRenter(userId);
	}

	@Override
	public List<RentalHeader> getRequestReceivedById(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getRequestReceivedById(userId);
	}

	@Override
	public List<RentalHeader> getMyRequestsById(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getMyRequestsById(userId);
	}

	@Override
	public List<RentalHeader> getToReturnByIdRenter(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getToReturnByIdRenter(userId);
	}

	@Override
	public List<RentalHeader> getToReturnByIdOwner(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getToReturnByIdOwner(userId);
	}

	@Override
	public List<RentalHeader> getCompleteByIdRenter(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getCompleteByIdRenter(userId);
	}

	@Override
	public List<RentalHeader> getToReceiveByIdOwner(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getToReceiveByIdOwner(userId);
	}

	@Override
	public List<RentalHeader> getCompleteByIdOwner(int userId) {
		// TODO Auto-generated method stub
		return rentalHeaderDao.getCompleteByIdOwner(userId);
	}

	@Override
	public RentalHeader setApprovedExam(long rentalHeaderId, String status, String dateApproved) {
		RentalHeader rentalHeader = get(rentalHeaderId);

		if ("Request".equals(status)) {
			UserNotification userNotif = new UserNotification();
			userNotif.setUser(rentalHeader.getRentalDetail().getBookOwner().getUser());
			userNotif.setActionId(rentalHeader.getRentalHeaderId());
			userNotif.setActionName("rental");
			userNotif.setActionStatus(status);
			userNotif.setProcessedBool(false);
			userNotif.setUserPerformer(rentalHeader.getUserId());
			userNotif.setBookActionPerformedOn(rentalHeader.getRentalDetail().getBookOwner());
			userNotificationDao.save(userNotif);

			pusherServer.sendNotification(userNotif);
		}

		if ("Approved".equals(status)) {
			UserNotification userNotif = new UserNotification();
			userNotif.setUserPerformer(rentalHeader.getRentalDetail().getBookOwner().getUser());
			userNotif.setActionId(rentalHeader.getRentalHeaderId());
			userNotif.setActionName("rental");
			userNotif.setProcessedBool(false);
			userNotif.setActionStatus(status);
			userNotif.setUser(rentalHeader.getUserId());
			userNotif.setBookActionPerformedOn(rentalHeader.getRentalDetail().getBookOwner());
			userNotificationDao.save(userNotif);

			rentalHeaderDao.rejectAllOtherRequests(rentalHeader);
			pusherServer.sendNotification(userNotif);
		}

		if ("Rejected".equals(status)) {
			UserNotification userNotif = new UserNotification();
			userNotif.setUserPerformer(rentalHeader.getRentalDetail().getBookOwner().getUser());
			userNotif.setActionId(rentalHeader.getRentalHeaderId());
			userNotif.setActionName("rental");
			userNotif.setActionStatus(status);
			userNotif.setProcessedBool(false);
			userNotif.setUser(rentalHeader.getUserId());
			userNotif.setBookActionPerformedOn(rentalHeader.getRentalDetail().getBookOwner());
			userNotificationDao.save(userNotif);

			pusherServer.sendNotification(userNotif);
		}

		if ("Confirm".equals(status)) {
			UserNotification userNotif = new UserNotification();
			userNotif.setUser(rentalHeader.getRentalDetail().getBookOwner().getUser());
			userNotif.setActionId(rentalHeader.getRentalHeaderId());
			userNotif.setActionName("rental");
			userNotif.setProcessedBool(false);
			userNotif.setActionStatus(status);
			userNotif.setUserPerformer(rentalHeader.getUserId());
			userNotif.setBookActionPerformedOn(rentalHeader.getRentalDetail().getBookOwner());
			userNotificationDao.save(userNotif);

			pusherServer.sendNotification(userNotif);
		}
		return rentalHeaderDao.setApprovedExam(rentalHeaderId, status, dateApproved);
	}

	@Override
	public RentalHeader setRentalHeader(RentalHeader rentalHeader) {
		return rentalHeaderDao.setRentalHeader(rentalHeader);
	}

	@Override
	public RentalHeader checkExist(long userId, long rentalDetailId) {
		return rentalHeaderDao.checkExist(userId, rentalDetailId);
	}

	@Override
	public List<RentalHeader> getRejectedByIdRenter(int userId) {
		return rentalHeaderDao.getRejectedByIdRenter(userId);
	}

	@Override
	public List<RentalHeader> getRejectedByIdOwner(int userId) {
		return rentalHeaderDao.getRejectedByIdOwner(userId);
	}

	@Override
	public List<RentalHeader> getCompleteByRentalDetail(long rentalDetailId) {
		return rentalHeaderDao.getCompleteByRentalDetail(rentalDetailId);
	}

	@Override
	public RentalHeader addNewRentalHeader(RentalHeader rentalHeader) {
		rentalHeaderDao.save(rentalHeader);

		UserNotification userNotif = new UserNotification();
		userNotif.setUser(rentalHeader.getRentalDetail().getBookOwner().getUser());
		userNotif.setActionId(rentalHeader.getRentalHeaderId());
		userNotif.setActionName("rental");
		userNotif.setActionStatus("Request");
		userNotif.setProcessedBool(false);
		userNotif.setUserPerformer(rentalHeader.getUserId());
		userNotif.setBookActionPerformedOn(rentalHeader.getRentalDetail().getBookOwner());
		userNotificationDao.save(userNotif);

		pusherServer.sendNotification(userNotif);
		return rentalHeader;
	}

	@Override
	public long numberOfCompletedRentsByBookOwnerId(long bookOwnerId) {
		return rentalHeaderDao.numberOfCompletedRentsByBookOwnerId(bookOwnerId);
	}

	@Override
	public List<RentalHeader> getRentalHeader(long bookOwnerId) {
		return rentalHeaderDao.getRentalHeader(bookOwnerId);
	}

	@Override
	public RentalHeader setMeetUp(long rentalHeaderId, long meetUpId) {

		RentalHeader rentalHeader = get(rentalHeaderId);

		UserNotification userNotif = new UserNotification();
		userNotif.setUserPerformer(rentalHeader.getRentalDetail().getBookOwner().getUser());
		userNotif.setActionId(rentalHeader.getRentalHeaderId());
		userNotif.setActionName("rental");
		userNotif.setActionStatus("Chose meet up details.");
		userNotif.setProcessedBool(false);
		userNotif.setUser(rentalHeader.getUserId());
		userNotif.setBookActionPerformedOn(rentalHeader.getRentalDetail().getBookOwner());
		userNotificationDao.save(userNotif);

		pusherServer.sendNotification(userNotif);

		return rentalHeaderDao.setMeetUp(rentalHeaderId, meetUpId);
	}

	@Override
	public RentalHeader setReturnMeetUp(long rentalHeaderId, long meetUpId, String currDate) {
		return rentalHeaderDao.setReturnMeetUp(rentalHeaderId, meetUpId, currDate);
	}

	@Override
	public List<RentalHeader> getToReceive(long userId) {
		return rentalHeaderDao.getToReceive(userId);
	}

	@Override
	public RentalHeader setReturnToReceive(long rentalHeaderId, long bookRatingId, long bookReviewId, String currDate) {
		return rentalHeaderDao.setReturnToReceive(rentalHeaderId, bookRatingId, bookReviewId, currDate);
	}

	@Override
	public RentalHeader setCompleteRental(long rentalHeaderId, long userRatingId, String currDate) {
		return rentalHeaderDao.setCompleteRental(rentalHeaderId, userRatingId, currDate);
	}

	@Override
	public RentalHeader delivered(long rentalHeaderId, String currDate) {
		return rentalHeaderDao.delivered(rentalHeaderId, currDate);
	}

	@Override
	public RentalHeader received(long rentalHeaderId, String currDate) {
		return rentalHeaderDao.received(rentalHeaderId, currDate);
	}

	@Override
	public RentalHeader complete(long rentalHeaderId, long userRatingId, String currDate) {
		return rentalHeaderDao.complete(rentalHeaderId, userRatingId, currDate);
	}

	@Override
	public List<RentalHeader> allHistory(long userId) {
		return rentalHeaderDao.allHistory(userId);
	}

	@Override
	public RentalHeader acceptRequest(long rentalHeaderId, String currDate) {
		return rentalHeaderDao.acceptRequest(rentalHeaderId, currDate);
	}

	@Override
	public RentalHeader rejectRequest(long rentalHeaderId, String currDate) {
		return rentalHeaderDao.rejectRequest(rentalHeaderId, currDate);
	}

	@Override
	public RentalHeader setConfirm(long rentalHeaderId, long meetUpDeliveryId, long meetUpReturnId, String currDate) {
		return rentalHeaderDao.setConfirm(rentalHeaderId, meetUpDeliveryId, meetUpReturnId, currDate);
	}

	@Override
	public RentalHeader getLatestRenter(long rentalDetailId) {
		return rentalHeaderDao.getLatestRenter(rentalDetailId);
	}

	@Override
	public List<RentalHeader> allRequested(long userId) {
		return rentalHeaderDao.allRequested(userId);
	}
}
