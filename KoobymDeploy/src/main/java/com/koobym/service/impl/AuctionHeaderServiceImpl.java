package com.koobym.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koobym.dao.LocationDao;
import com.koobym.dao.AuctionHeaderDao;
import com.koobym.dao.UserNotificationDao;
import com.koobym.model.AuctionHeader;
import com.koobym.model.SwapHeader;
import com.koobym.model.UserNotification;
import com.koobym.pusher.PusherServer;
import com.koobym.service.AuctionHeaderService;

@Service
@Transactional
public class AuctionHeaderServiceImpl extends BaseServiceImpl<AuctionHeader, Long> implements AuctionHeaderService {

	private AuctionHeaderDao auctionHeaderDao;
	private LocationDao locationDao;
	private UserNotificationDao userNotificationDao;

	@Autowired
	private PusherServer pusherServer;

	@Autowired
	public AuctionHeaderServiceImpl(AuctionHeaderDao auctionHeaderDao, LocationDao locationDao,
			UserNotificationDao userNotificationDao) {
		super(auctionHeaderDao);
		this.auctionHeaderDao = auctionHeaderDao;
		this.locationDao = locationDao;
		this.userNotificationDao = userNotificationDao;
	}

	public boolean canAuction(long userId) {
		return auctionHeaderDao.canAuction(userId);
	}

	@Override
	public AuctionHeader addNewAuctionHeader(AuctionHeader auctionHeader) {
		auctionHeaderDao.save(auctionHeader);
		return auctionHeader;
	}

	@Override
	public List<AuctionHeader> getAuctionHeader(long auctionDetailId, long userId) {
		return auctionHeaderDao.getAuctionHeader(auctionDetailId, userId);
	}

	@Override
	public AuctionHeader setApprovedExam(long auctionHeaderId, String status, String dateApproved) {
		return auctionHeaderDao.setApprovedExam(auctionHeaderId, status, dateApproved);
	}

	@Override
	public List<AuctionHeader> getToDeliverById(int userId) {
		return auctionHeaderDao.getToDeliverById(userId);
	}

	@Override
	public List<AuctionHeader> getToReceiveById(int userId) {
		return auctionHeaderDao.getToReceiveById(userId);
	}

	@Override
	public List<AuctionHeader> getWinById(long userId) {
		return auctionHeaderDao.getWinById(userId);
	}

	@Override
	public AuctionHeader changeOwner(long auctionHeaderId) {
		return auctionHeaderDao.changeOwner(auctionHeaderId);
	}

	@Override
	public AuctionHeader deliveredBook(long auctionHeaderId) {
		return auctionHeaderDao.deliveredBook(auctionHeaderId);
	}

	@Override
	public AuctionHeader receivedBook(long auctionHeaderId, long userRatingId) {
		return auctionHeaderDao.receivedBook(auctionHeaderId, userRatingId);
	}

	@Override
	public List<AuctionHeader> history(long userId) {
		return auctionHeaderDao.history(userId);
	}

	@Override
	public List<AuctionHeader> getAllWin() {
		return auctionHeaderDao.getAllWin();
	}

}
