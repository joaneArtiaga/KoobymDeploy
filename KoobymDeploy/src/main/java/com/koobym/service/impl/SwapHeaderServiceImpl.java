package com.koobym.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koobym.dao.LocationDao;
import com.koobym.dao.SwapHeaderDao;
import com.koobym.dao.UserNotificationDao;
import com.koobym.model.SwapHeader;
import com.koobym.model.UserNotification;
import com.koobym.service.SwapHeaderService;

@Service
@Transactional
public class SwapHeaderServiceImpl extends BaseServiceImpl<SwapHeader, Long> implements SwapHeaderService {

	private SwapHeaderDao swapHeaderDao;
	private LocationDao locationDao;
	private UserNotificationDao userNotificationDao;

	@Autowired
	public SwapHeaderServiceImpl(SwapHeaderDao swapHeaderDao, LocationDao locationDao,
			UserNotificationDao userNotificationDao) {
		super(swapHeaderDao);
		this.swapHeaderDao = swapHeaderDao;
		this.locationDao = locationDao;
		this.userNotificationDao = userNotificationDao;
	}

	@Override
	public List<SwapHeader> getToDeliverById(int userId) {
		return swapHeaderDao.getToDeliverById(userId);
	}

	@Override
	public List<SwapHeader> getToReceiveByIdRenter(int userId) {
		return swapHeaderDao.getToReceiveByIdRenter(userId);
	}

	@Override
	public SwapHeader setApprovedExam(long swapHeaderId, String status) {
		SwapHeader swapHeader = get(swapHeaderId);
		UserNotification userNotif = new UserNotification();
		userNotif.setUser(swapHeader.getSwapDetail().getBookOwner().getUser());
		userNotif.setActionId(swapHeader.getSwapHeaderId());
		userNotif.setActionName("swap");
		userNotif.setActionStatus(status);
		userNotif.setUserPerformer(swapHeader.getUser());
		userNotif.setBookActionPerformedOn(swapHeader.getSwapDetail().getBookOwner());
		userNotificationDao.save(userNotif);
		return swapHeaderDao.setApprovedExam(swapHeaderId, status);
	}

	@Override
	public SwapHeader addNewSwapHeader(SwapHeader swapHeader) {
		swapHeaderDao.save(swapHeader);
		UserNotification userNotif = new UserNotification();
		userNotif.setUser(swapHeader.getSwapDetail().getBookOwner().getUser());
		userNotif.setActionId(swapHeader.getSwapHeaderId());
		userNotif.setActionName("swap");
		userNotif.setActionStatus("request");
		userNotif.setUserPerformer(swapHeader.getUser());
		userNotif.setBookActionPerformedOn(swapHeader.getSwapDetail().getBookOwner());
		userNotificationDao.save(userNotif);
		return swapHeader;
	}

	@Override
	public SwapHeader swapRequested(SwapHeader swapHeader) {
		locationDao.save(swapHeader.getLocation());
		return swapHeaderDao.swapRequested(swapHeader);
	}

	@Override
	public List<SwapHeader> getApprovedSwaps(long userId) {
		return swapHeaderDao.getApprovedSwaps(userId);
	}

	@Override
	public List<SwapHeader> getRequestedSwaps(long userId) {
		return swapHeaderDao.getRequestedSwaps(userId);
	}

	@Override
	public List<SwapHeader> getToApproveSwaps(long userId) {
		return swapHeaderDao.getToApproveSwaps(userId);
	}

	@Override
	public List<SwapHeader> getCompleteById(int userId) {
		return swapHeaderDao.getCompleteById(userId);
	}

	@Override
	public List<SwapHeader> getRejectedByIdOwner(int userId) {
		return swapHeaderDao.getRejectedByIdOwner(userId);
	}

	@Override
	public List<SwapHeader> getRejectedByIdRenter(int userId) {
		return swapHeaderDao.getRejectedByIdRenter(userId);
	}

	@Override
	public List<SwapHeader> getCompleteAllById(int userId) {
		return swapHeaderDao.getCompleteAllById(userId);
	}

}
