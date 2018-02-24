package com.koobym.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.koobym.dao.MeetUpDao;
import com.koobym.dao.RentalHeaderDao;
import com.koobym.dao.UserNotificationDao;
import com.koobym.model.AuctionHeader;
import com.koobym.model.BookOwner;
import com.koobym.model.MeetUp;
import com.koobym.model.RentalDetail;
import com.koobym.model.RentalHeader;
import com.koobym.model.User;
import com.koobym.model.UserNotification;
import com.koobym.pusher.PusherServer;

@Repository
public class RentalHeaderDaoImpl extends BaseDaoImpl<RentalHeader, Long> implements RentalHeaderDao {

	@Autowired
	private PusherServer pusherServer;

	@Autowired
	private MeetUpDao meetUpDao;

	@Autowired
	private UserNotificationDao userNotificationDao;

	public RentalHeaderDaoImpl() {
		super(RentalHeader.class);
	}

	public RentalHeader setReturnMeetUp(long rentalHeaderId, long meetUpId) {
		RentalHeader ah = new RentalHeader();
		MeetUp mu = new MeetUp();

		ah = get(rentalHeaderId);
		mu = meetUpDao.get(meetUpId);

		ah.setReturnMeetUp(mu);

		Session session = getSessionFactory().getCurrentSession();
		session.update(ah);

		UserNotification un = new UserNotification();
		un.setActionId(rentalHeaderId);
		un.setActionName("rental");
		un.setActionStatus("return");
		un.setBookActionPerformedOn(ah.getRentalDetail().getBookOwner());
		un.setUserPerformer(ah.getUserId());
		un.setUser(ah.getRentalDetail().getBookOwner().getUser());

		userNotificationDao.save(un);
		pusherServer.sendNotification(un);

		return ah;
	}

	public List<RentalHeader> getListRentalById(int userId) {
		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;
	}

	public List<RentalHeader> getListRentalByBookOwnerId(long bookOwnerId) {
		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.add(Restrictions.eq("bookOwner.book_ownerId", bookOwnerId));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;
	}

	public long numberOfCompletedRentsByBookOwnerId(long bookOwnerId) {
		String query = "select count(rentalHeaderId) from rental_header join rental_detail on rental_header.rentalDetailId where rental_detail.bookOwnerId = :bookOwnerId and rental_header.status='COMPLETE'";

		SQLQuery sqlQuery = getSessionFactory().getCurrentSession().createSQLQuery(query);
		sqlQuery.setLong("bookOwnerId", bookOwnerId);
		Object obj = sqlQuery.uniqueResult();
		BigInteger bigIntVal = (BigInteger) obj;

		return bigIntVal.longValue();
	}

	public List<RentalHeader> getToDeliverById(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("rentalDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Approved"));
		criteria = criteria.addOrder(Order.desc("dateDeliver"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getToReceiveByIdRenter(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("status", "Approved"));
		criteria = criteria.addOrder(Order.desc("dateDeliver"));
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getToReceive(long userId) {
		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("rentalDetail.bookOwner.user", "userOwner");
		criteria = criteria.add(Restrictions.or(Restrictions.eq("status", "Approved"),Restrictions.and(Restrictions.eq("status", "Received"), Restrictions.eq("userOwner.userId", userId))));
		criteria = criteria.add(Restrictions.or(Restrictions.eq("userOwner.userId", new Long(userId)),
				Restrictions.eq("user.userId", new Long(userId))));
		criteria = criteria.addOrder(Order.desc("dateDeliver"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();

		return flag;
	}

	public List<RentalHeader> getToReturnByIdRenter(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.and(Restrictions.eq("status", "Received"), Restrictions.eq("rentalExtraMessage","Delivered")));
		criteria = criteria.addOrder(Order.desc("rentalEndDate"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getToReturnByIdOwner(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("rentalDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Received"));
		criteria = criteria.addOrder(Order.desc("rentalEndDate"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getMyRequestsById(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		/* criteria = criteria.createAlias("user", "user"); */
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Confirmation"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getToReceiveByIdOwner(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("rentalDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("status", "Received"));
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.addOrder(Order.desc("dateDeliver"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();

		return flag;

	}

	public List<RentalHeader> getRequestReceivedById(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("rentalDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Confirmation"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;
	}

	public RentalHeader setApprovedExam(long rentalHeaderId, String status, String dateApproved) {
		RentalHeader rentalHeader = new RentalHeader();
		RentalDetail rentalDetail = new RentalDetail();
		BookOwner bookOwner = new BookOwner();

		Session session = getSessionFactory().getCurrentSession();

		String squery = "";

		if (status.equals("Approved")) {
			squery = "update rental_header set status = :status, dateApproved = :dateApproved where rentalHeaderId = :rentalHeaderId";
			SQLQuery query = session.createSQLQuery(squery);
			query.setString("status", status);
			query.setString("dateApproved", dateApproved);
			query.setLong("rentalHeaderId", rentalHeaderId);
			query.executeUpdate();
		} else if (status.equals("Confirm")) {
			squery = "update rental_header set status = :status, dateConfirmed = :dateApproved where rentalHeaderId = :rentalHeaderId";
			SQLQuery query = session.createSQLQuery(squery);
			query.setString("status", status);
			query.setString("dateApproved", dateApproved);
			query.setLong("rentalHeaderId", rentalHeaderId);
			query.executeUpdate();
		}

		rentalHeader = get(rentalHeaderId);

		rentalDetail = rentalHeader.getRentalDetail();
		bookOwner = rentalDetail.getBookOwner();

		if (status.equals("Approved")) {
			rentalDetail.setRentalStatus("Not Available");
			bookOwner.setBookStat("Not Available");
			rentalDetail.setBookOwner(bookOwner);
			rentalHeader.setRentalDetail(rentalDetail);
		} else if (status.equals("Complete")) {
			rentalDetail.setRentalStatus("Available");
			bookOwner.setBookStat("Not Available");
			rentalDetail.setBookOwner(bookOwner);
			rentalHeader.setRentalDetail(rentalDetail);
			rentalHeader.setStatus("Complete");
		}

		Session sessionStat = getSessionFactory().getCurrentSession();

		sessionStat.update(rentalHeader);

		return rentalHeader;
	}

	public RentalHeader setMeetUp(long rentalHeaderId, long meetUpId) {
		RentalHeader rentalHeader = new RentalHeader();

		Session session = getSessionFactory().getCurrentSession();
		String squery = "update rental_header set meet_upId = :meetUpId  where rentalHeaderId = :rentalHeaderId";

		SQLQuery query = session.createSQLQuery(squery);
		query.setLong("rentalHeaderId", rentalHeaderId);
		query.setLong("meet_upId", meetUpId);
		query.executeUpdate();

		rentalHeader = get(rentalHeaderId);

		return rentalHeader;
	}

	public RentalHeader checkExist(long userId, long rentalDetailId) {
		RentalHeader rentalHeader = new RentalHeader();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.add(Restrictions.eq("rentalDetail.rental_detailId", rentalDetailId));
		rentalHeader = (RentalHeader) criteria.uniqueResult();

		return rentalHeader;
	}

	public RentalHeader setRentalHeader(RentalHeader rentalHeader) {
		RentalHeader rentalHeaderModel = new RentalHeader();

		Session session = getSessionFactory().getCurrentSession();
		String squery = "insert into rental_header (userId, locationId, rentalTimeStamp, totalPrice, status, rentalDetailId)"
				+ "values(rentalHeader.userId, rentalHeader.locationId, rentalHeader.rentalTimeStamp, rentalHeader.price, rentalHeader.status, rentalHeader.rentalDetailId)";

		SQLQuery query = session.createSQLQuery(squery);
		query.executeUpdate();

		rentalHeader = get(rentalHeader.getRentalHeaderId());

		return rentalHeader;
	}

	public List<RentalHeader> getCompleteByIdRenter(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Complete"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getRejectedByIdRenter(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Rejected"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getCompleteByIdOwner(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("rentalDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Complete"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getRejectedByIdOwner(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("rentalDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Rejected"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getCompleteByRentalDetail(long rentalDetailId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.add(Restrictions.eq("rentalDetail.rental_detailId", new Long(rentalDetailId)));
		criteria = criteria.add(Restrictions.eq("status", "Complete"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getRentalHeader(long bookOwnerId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.add(Restrictions.eq("bookOwner.book_ownerId", new Long(bookOwnerId)));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;

	}

	public List<RentalHeader> getElapsedRentalDate() {
		List<RentalHeader> flag = new ArrayList<RentalHeader>();
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.add(Restrictions.ge("rentalEndDate", new Date()));
		criteria = criteria.add(Restrictions.not(Restrictions.eq("status", "Due")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;
	}

	public List<RentalHeader> getToDeliverToday() {
		List<RentalHeader> flag = new ArrayList<RentalHeader>();
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.add(Restrictions.ge("dateDeliver", new Date()));
		criteria = criteria.add(Restrictions.eq("status", "Confirm"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;
	}

	public List<RentalHeader> getOngoingByOwner(int userId) {

		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("rentalDetail", "rentalDetail");
		criteria = criteria.createAlias("rentalDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("rentalDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.not(Restrictions.eq("status", "Rejected")));
		criteria = criteria.add(Restrictions.not(Restrictions.eq("status", "Complete")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;
	}

	public List<RentalHeader> getOngoingRequestsByUser(int userId) {
		List<RentalHeader> flag = new ArrayList<RentalHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RentalHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.not(Restrictions.eq("status", "Complete")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<RentalHeader>) criteria.list();
		return flag;
	}

	public void rejectAllOtherRequests(RentalHeader rentalHeader) {

		String sQuery = "update rental_header set Status = 'Rejected' where "
				+ "rental_header.rentalDetailId = :rentalDetailId and rental_header.rentalHeaderId != :rentalHeaderId";

		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(sQuery);
		query.setLong("rentalDetailId", rentalHeader.getRentalDetail().getRental_detailId());
		query.setLong("rentalHeaderId", rentalHeader.getRentalHeaderId());
		query.executeUpdate();
	}

	public RentalHeader setReturnToReceive(long rentalHeaderId){
		RentalHeader rh = new RentalHeader();
		
		rh = get(rentalHeaderId);

		rh.setStatus("Received");
		rh.setRentalExtraMessage("Returned");
		
		UserNotification un = new UserNotification();
		
		un.setActionId(rentalHeaderId);
		un.setActionName("rental");
		un.setActionStatus("returned");
		un.setBookActionPerformedOn(rh.getRentalDetail().getBookOwner());
		un.setExtraMessage("Returned");
		un.setUser(rh.getRentalDetail().getBookOwner().getUser());
		un.setUserPerformer(rh.getUserId());
		
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);

		Session session = getSessionFactory().getCurrentSession();
		session.update(rh);
		return rh;
	}
	
	public RentalHeader setCompleteRental(long rentalHeaderId, long userRatingId){
		RentalHeader rh = new RentalHeader();
		
		rh = get(rentalHeaderId);
		rh.setStatus("Complete");
		rh.getRentalDetail().setRentalStatus("Available");
		rh.getRentalDetail().getBookOwner().setBookStat("Available");
		
		Session session = getSessionFactory().getCurrentSession();
		session.update(rh);
		UserNotification un = new UserNotification();
		
		un.setActionId(rentalHeaderId);
		un.setExtraMessage(String.valueOf(userRatingId));
		un.setActionName("rental");
		un.setActionStatus("Complete");
		un.setBookActionPerformedOn(rh.getRentalDetail().getBookOwner());
		un.setUser(rh.getUserId());
		un.setUserPerformer(rh.getRentalDetail().getBookOwner().getUser());
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);
		
		
		return rh;
	}
	
	public RentalHeader delivered(long rentalHeaderId){
		RentalHeader rh = new RentalHeader();
		
		rh = get(rentalHeaderId);
		
		rh.setRentalExtraMessage("Delivered");
		rh.getRentalDetail().setRentalStatus("Not Available");
		rh.getRentalDetail().getBookOwner().setBookStat("Not Available");
		
		Session session = getSessionFactory().getCurrentSession();
		session.update(rh);
		
		UserNotification un = new UserNotification();
		un.setActionId(rentalHeaderId);
		un.setActionName("rental");
		un.setActionStatus("delivered");
		un.setBookActionPerformedOn(rh.getRentalDetail().getBookOwner());
		un.setUser(rh.getUserId());
		un.setUserPerformer(rh.getRentalDetail().getBookOwner().getUser());
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);
		
		return rh;
	}
	
	public RentalHeader received(long rentalHeaderId){
		RentalHeader rh = new RentalHeader();
		
		rh = get(rentalHeaderId);
		
		rh.setStatus("Received");
		rh.getRentalDetail().setRentalStatus("Not Available");
		rh.getRentalDetail().getBookOwner().setBookStat("Not Available");
		
		Session session = getSessionFactory().getCurrentSession();
		session.update(rh);
		
		UserNotification un = new UserNotification();
		un.setActionId(rentalHeaderId);
		un.setActionName("rental");
		un.setActionStatus("received");
		un.setBookActionPerformedOn(rh.getRentalDetail().getBookOwner());
		un.setUser(rh.getRentalDetail().getBookOwner().getUser());
		un.setUserPerformer(rh.getUserId());
		
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);
		
		return rh;
	}


	public RentalHeader complete(long rentalHeaderId){
		RentalHeader rh = new RentalHeader();
		
		rh = get(rentalHeaderId);
		
		rh.setStatus("Complete");
		rh.getRentalDetail().setRentalStatus("Not Available");
		rh.getRentalDetail().getBookOwner().setBookStat("Not Available");
		rh.getRentalDetail().getBookOwner().setStatus("none");
		
		UserNotification un = new UserNotification();
		un.setActionId(rentalHeaderId);
		un.setActionName("rental");
		un.setActionStatus("Complete");
		un.setBookActionPerformedOn(rh.getRentalDetail().getBookOwner());
		un.setUserPerformer(rh.getRentalDetail().getBookOwner().getUser());
		un.setUser(rh.getUserId());
		
		rh.getRentalDetail().getBookOwner().setUser(rh.getUserId());
				
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);
		
		Session session = getSessionFactory().getCurrentSession();
		session.update(rh);
		
		return rh;
	}
}
