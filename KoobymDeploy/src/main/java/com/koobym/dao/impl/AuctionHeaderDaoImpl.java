package com.koobym.dao.impl;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.tree.RestrictableStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.koobym.dao.AuctionDetailDao;
import com.koobym.dao.AuctionHeaderDao;
import com.koobym.dao.MeetUpDao;
import com.koobym.dao.UserNotificationDao;
import com.koobym.model.RentalHeader;
import com.koobym.model.SwapHeader;
import com.koobym.model.User;
import com.koobym.model.UserNotification;
import com.koobym.pusher.PusherServer;
import com.koobym.model.AuctionDetail;
import com.koobym.model.AuctionHeader;
import com.koobym.model.MeetUp;
import com.koobym.model.RentalDetail;

@Repository
public class AuctionHeaderDaoImpl extends BaseDaoImpl<AuctionHeader, Long> implements AuctionHeaderDao {

	@Autowired
	private MeetUpDao meetUpDao;

	@Autowired
	private UserNotificationDao userNotificationDao;
	
	@Autowired
	private PusherServer pusherServer;
	
	public AuctionHeaderDaoImpl() {
		super(AuctionHeader.class);
	}

	public AuctionHeader getAuctionHeader(long auctionDetailId, long userId) {
		AuctionHeader auctionHeader = new AuctionHeader();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AuctionHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.createAlias("auctionDetail", "auctionDetail");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.add(Restrictions.eq("auctionDetail.auctionDetailId", auctionDetailId));
		auctionHeader = (AuctionHeader) criteria.uniqueResult();

		return auctionHeader;
	}
	
	public List<AuctionHeader> getWinById(long userId){
		List<AuctionHeader> flag = new ArrayList<AuctionHeader>();
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AuctionHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.add(Restrictions.eq("status", "win"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<AuctionHeader>) criteria.list();
		
		return flag;
	}

	public List<AuctionHeader> getToReceiveById(int userId) {

		List<AuctionHeader> flag = new ArrayList<AuctionHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AuctionHeader.class);
		criteria = criteria.createAlias("auctionDetail", "auctionDetail");
		criteria = criteria.createAlias("auctionDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "win"));
		criteria = criteria.addOrder(Order.desc("dateDelivered")); 
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<AuctionHeader>) criteria.list();
		return flag;

	}
	
	public List<AuctionHeader> getToDeliverById(int userId) {

		List<AuctionHeader> flag = new ArrayList<AuctionHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AuctionHeader.class);
		criteria = criteria.createAlias("auctionDetail", "auctionDetail");
		criteria = criteria.createAlias("auctionDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("auctionDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "win"));
		criteria = criteria.add(Restrictions.and(Restrictions.ne("auctionExtraMessage", "Delivered"), Restrictions.eq("auctionExtraMessage","")));
		criteria = criteria.addOrder(Order.desc("dateDelivered")); 
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<AuctionHeader>) criteria.list();
		return flag;

	}
	
	 public AuctionHeader setApprovedExam(long auctionHeaderId, String status, String date) {
	 AuctionHeader auctionHeader = new AuctionHeader();
	 AuctionDetail auctionDetail = new AuctionDetail();
	
	 Session session = getSessionFactory().getCurrentSession();
	
	 String squery = "";
	
	 if (status.equals("win")) {
		 squery = "update auction_header set status = :status where auctionHeaderId = :auctionHeaderId";
	 } else if (status.equals("lose")) {
		 squery = "update auction_header set status = :status where auctionHeaderId = :auctionHeaderId";
	 }else if(status.equals("own")) {
		 squery = "update auction_header set status = :status where auctionHeaderId = :auctionHeaderId";
	}
	
	 SQLQuery query = session.createSQLQuery(squery);
	 query.setString("status", status);
	 query.setLong("auctionHeaderId", auctionHeaderId);
	 query.executeUpdate();
	
	 auctionHeader = get(auctionHeaderId);
	 
	 auctionDetail = auctionHeader.getAuctionDetail();
	 
	 Session sessionStat = getSessionFactory().getCurrentSession();
	 
	 if(status.equals("win")){
		 auctionDetail.setStatus("Not Available");
		 auctionDetail.getBookOwner().setBookStat("Not Available");
		 auctionHeader.setAuctionDetail(auctionDetail);
	 }
	 
	 sessionStat.update(auctionHeader);
	 
	
	 return auctionHeader;
	 }
	
	public AuctionHeader changeOwner(long auctionHeaderId){
		AuctionHeader ah = new AuctionHeader();
		User user = new User();
		
		user = ah.getUser();
		
		ah = get(auctionHeaderId);
		
		ah.getAuctionDetail().getBookOwner().setUser(user);
		ah.getAuctionDetail().getBookOwner().setBookStat("Not Available");
		
		Session session = getSessionFactory().getCurrentSession();
		
		session.update(ah);
		
		return ah;
			
	}
	
	public AuctionHeader deliveredBook(long auctionHeaderId){
		AuctionHeader ah = new AuctionHeader();
		
		ah = get(auctionHeaderId);
		
		ah.setAuctionExtraMessage("Delivered");
		
		Session session = getSessionFactory().getCurrentSession();
		session.update(ah);
		
		UserNotification un = new UserNotification();
		un.setActionId(auctionHeaderId);
		un.setActionName("auction");
		un.setActionStatus("delivered");
		un.setBookActionPerformedOn(ah.getAuctionDetail().getBookOwner());
		un.setUser(ah.getUser());
		un.setUserPerformer(ah.getAuctionDetail().getBookOwner().getUser());
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);
		
		return ah;
	}
	
	public AuctionHeader receivedBook(long auctionHeaderId, long userRatingId){
		AuctionHeader ah = new AuctionHeader();
		User user = new User();
		
		ah= get(auctionHeaderId);
		user = ah.getUser();
		ah.setStatus("Complete");
		ah.getAuctionDetail().getBookOwner().setStatus("none");
		ah.getAuctionDetail().getBookOwner().setUser(user);
		
		Session session = getSessionFactory().getCurrentSession();
		session.update(ah);
		
		UserNotification un = new UserNotification();
		un.setActionId(auctionHeaderId);
		un.setActionName("auction");
		un.setActionStatus("Complete");
		un.setBookActionPerformedOn(ah.getAuctionDetail().getBookOwner());
		un.setUser(ah.getAuctionDetail().getBookOwner().getUser());
		un.setUserPerformer(user);
		un.setExtraMessage(String.valueOf(userRatingId));
		
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);
		
		return ah;
	}
	 
	public List<AuctionHeader> history(long userId){
		List<AuctionHeader> ah = new ArrayList<AuctionHeader>();
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AuctionHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.add(Restrictions.eq("status", "Complete"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		ah = (List<AuctionHeader>) criteria.list();
		
		return ah;
	}
}
