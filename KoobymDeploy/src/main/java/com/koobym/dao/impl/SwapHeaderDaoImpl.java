package com.koobym.dao.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.koobym.dao.SwapHeaderDao;
import com.koobym.dao.UserNotificationDao;
import com.koobym.model.BookOwner;
import com.koobym.model.RentalHeader;
import com.koobym.model.SwapDetail;
import com.koobym.model.SwapHeader;
import com.koobym.model.SwapHeaderDetail;
import com.koobym.model.User;
import com.koobym.model.UserNotification;
import com.koobym.pusher.PusherServer;

@Repository
public class SwapHeaderDaoImpl extends BaseDaoImpl<SwapHeader, Long> implements SwapHeaderDao {

	@Autowired
	private UserNotificationDao userNotificationDao;

	@Autowired
	private PusherServer pusherServer;

	public SwapHeaderDaoImpl() {
		super(SwapHeader.class);
	}

	public List<SwapHeader> getToDeliverById(int userId) {

		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail", "swapDetail");
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("swapDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Confirm"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;

	}

	public List<SwapHeader> getListSwapHeaderByBookOwnerId(long bookOwnerId) {
		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail", "swapDetail");
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwner");
		criteria = criteria.add(Restrictions.eq("bookOwner.book_ownerId", bookOwnerId));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;
	}

	public List<SwapHeader> getCompleteById(int userId) {

		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("requestedSwapDetail", "requestedSwapDetail");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner", "bookOwnerRequested");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner.user", "userRequested");
		criteria = criteria.add(Restrictions.or(Restrictions.eq("userRequested.userId", new Long(userId)),
				Restrictions.eq("user.userId", new Long(userId))));
		criteria = criteria.add(Restrictions.eq("status", "Complete"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;
	}

	@Override
	public void save(SwapHeader t) {
		super.save(t);
		for (SwapHeaderDetail shd : t.getSwapHeaderDetails()) {
			shd.setSwapHeaderId(t.getSwapHeaderId());
		}
		super.update(t);
	}

	public List<SwapHeader> getToReceiveByIdRenter(int userId) {

		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("requestedSwapDetail", "requestedSwapDetail");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.or(Restrictions.eq("status", "Confirm"), Restrictions.eq("status", "Delivered")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;

	}

	public List<SwapHeader> getRejectedByIdRenter(int userId) {

		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("requestedSwapDetail", "requestedSwapDetail");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.eq("status", "Rejected"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;

	}

	public List<SwapHeader> getRejectedByIdOwner(int userId) {

		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail", "swapDetail");
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwnerSwap");
		criteria = criteria.createAlias("swapDetail.bookOwner.user", "userSwap");
		criteria = criteria.createAlias("requestedSwapDetail", "requestedSwapDetail");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner", "bookOwnerRequest");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner.user", "userRequest");
		criteria = criteria.add(Restrictions.or(Restrictions.eq("userSwap.userId", new Long(userId)),
				Restrictions.eq("userRequest.userId", new Long(userId)),
				Restrictions.eq("user.userId", new Long(userId))));
		criteria = criteria.add(Restrictions.eq("status", "Rejected"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;

	}

	public List<SwapHeader> getCompleteAllById(int userId) {

		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail", "swapDetail");
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwnerSwap");
		criteria = criteria.createAlias("swapDetail.bookOwner.user", "userSwap");
		criteria = criteria.createAlias("requestedSwapDetail", "requestedSwapDetail");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner", "bookOwnerRequest");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner.user", "userRequest");
		criteria = criteria.add(Restrictions.or(Restrictions.eq("userSwap.userId", new Long(userId)),
				Restrictions.eq("userRequest.userId", new Long(userId)),
				Restrictions.eq("user.userId", new Long(userId))));
		criteria = criteria.add(Restrictions.eq("status", "Completed"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;

	}

	public SwapHeader swapOwner(long userId) {
		SwapHeader flag = new SwapHeader();
		SwapDetail mySwapDetail = new SwapDetail();
		SwapDetail toBeSwapped = new SwapDetail();
		User ms = new User();
		User tbs = new User();

		flag = get(userId);
		mySwapDetail = flag.getRequestedSwapDetail();
		toBeSwapped = flag.getSwapDetail();
		ms = mySwapDetail.getBookOwner().getUser();
		tbs = toBeSwapped.getBookOwner().getUser();

		mySwapDetail.getBookOwner().setUser(tbs);
		toBeSwapped.getBookOwner().setUser(ms);
		flag.setSwapDetail(toBeSwapped);
		flag.setRequestedSwapDetail(mySwapDetail);

		Session session = getSessionFactory().getCurrentSession();

		session.update(flag);
		return flag;
	}

	public SwapHeader swapRequested(SwapHeader swapHeader) {

		String squery = "insert into swap_header (userId, swap_detailId, locationId, userDayTimeId, dateSwap, status) "
				+ "values(?,?,?,?,?,?)";

		Session session = getSessionFactory().getCurrentSession();

		SQLQuery query = session.createSQLQuery(squery);
		query.setLong(0, swapHeader.getUser().getUserId());
		query.setLong(1, swapHeader.getSwapDetail().getSwapDetailId());
		query.setLong(2, swapHeader.getLocation().getLocationId());
		query.setLong(3, swapHeader.getUserDayTime().getUserDayTimeId());
		query.setString(4, swapHeader.getDateTimeStamp());
		query.setString(5, swapHeader.getStatus());

		query.executeUpdate();

		BigInteger lastId = (BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult();
		System.out.println("last Id" + lastId.longValue());

		swapHeader = retrieveSwapHeader(lastId.longValue());

		return swapHeader;
	}

	public SwapHeader retrieveSwapHeader(long swapheaderId) {
		SwapHeader flag = new SwapHeader();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.add(Restrictions.eq("swapHeaderId", new Long(swapheaderId)));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (SwapHeader) criteria.uniqueResult();
		return flag;
	}

	public SwapHeader setApprovedExam(String status, long swapHeaderId, String date) {
		SwapHeader swapHeader = new SwapHeader();
		SwapDetail swapDetail = new SwapDetail();
		SwapDetail swapDetailReq = new SwapDetail();
		BookOwner bookOwner = new BookOwner();

		Session session = getSessionFactory().getCurrentSession();
		String squery = "";

		if (status.equals("Approved")) {
			squery = "update swap_header set status = :status , dateApproved = :date where swapHeaderId = :swapHeaderId";
		} else if (status.equals("Confirm")) {
			squery = "update swap_header set status = :status , dateConfirmed = :date where swapHeaderId = :swapHeaderId";
		} else if (status.equals("Rejected")) {
			squery = "update swap_header set status = :status , dateRejected = :date where swapHeaderId = :swapHeaderId";
		} else if (status.equals("ToReceive")) {
			squery = "update swap_header set status = :status , dateDelivered = :date where swapHeaderId = :swapHeaderId";
		} else if (status.equals("Complete")) {
			squery = "update swap_header set status = :status , dateReceived = :date where swapHeaderId = :swapHeaderId";
		}

		SQLQuery query = session.createSQLQuery(squery);
		query.setString("status", status);
		query.setString("date", date);
		query.setLong("swapHeaderId", swapHeaderId);
		query.executeUpdate();

		swapHeader = get(swapHeaderId);

		swapDetail = swapHeader.getSwapDetail();
		swapDetailReq = swapHeader.getRequestedSwapDetail();

		Session sessionStat = getSessionFactory().getCurrentSession();

		if (status.equals("Approved")) {
			swapDetail.setSwapStatus("Not Available");
			swapDetail.getBookOwner().setBookStat("Not Available");
			swapDetailReq.setSwapStatus("Not Available");
			swapDetailReq.getBookOwner().setBookStat("Not Available");
		}

		swapHeader.setSwapDetail(swapDetail);
		swapHeader.setRequestedSwapDetail(swapDetailReq);

		sessionStat.update(swapHeader);

		return swapHeader;
	}

	@Override
	public List<SwapHeader> getRequestedSwaps(long userId) {
		List<SwapHeader> flag = null;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;
	}

	public List<SwapHeader> getRequests(long userId) {
		List<SwapHeader> flag = null;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("bookOwner.user", "userbook");
		criteria = criteria.add(Restrictions.eq("userbook.userId", userId));
		criteria = criteria.add(Restrictions.eq("status", "Request"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;
	}

	public List<SwapHeader> getToApproveSwaps(long userId) {
		List<SwapHeader> flag = null;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("requestedSwapDetail", "requestedSwapDetail");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.add(Restrictions.eq("status", "APPROVED_BY_REQUESTOR"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;
	}

	public List<SwapHeader> getApprovedSwaps(long userId) {
		List<SwapHeader> flag = null;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail", "swapDetail");
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("swapDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.add(Restrictions.eq("status", "APPROVED_BY_REQUESTOR"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;
	}

	public List<SwapHeader> getOngoingSwaps(long userId) {
		List<SwapHeader> flag = null;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail", "swapDetail");
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwner");
		criteria = criteria.createAlias("swapDetail.bookOwner.user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.add(Restrictions.not(Restrictions.eq("status", "Complete")));
		criteria = criteria.add(Restrictions.not(Restrictions.eq("status", "Rejected")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;
	}

	public List<SwapHeader> getOngoingSwapRequestsByUser(long userId) {
		List<SwapHeader> flag = null;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.eq("user.userId", userId));
		criteria = criteria.add(Restrictions.not(Restrictions.eq("status", "Complete")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();
		return flag;
	}

	public void rejectAllOtherRequests(SwapHeader swapHeader) {

		String sQuery = "update swap_header set Status = 'Rejected' where "
				+ "swap_header.swap_detailId = :swapDetailId and swap_header.swapHeaderId != :swapHeaderId";

		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(sQuery);
		query.setLong("swapDetailId", swapHeader.getSwapDetail().getSwapDetailId());
		query.setLong("swapHeaderId", swapHeader.getSwapHeaderId());
		query.executeUpdate();
	}

	public SwapHeader setDelivered(long swapHeaderId) {
		SwapHeader sh = new SwapHeader();

		sh = get(swapHeaderId);

		sh.setStatus("Delivered");
		sh.setSwapExtraMessage("delivered");
		sh.getSwapDetail().setSwapStatus("Not Available");
		sh.getRequestedSwapDetail().setSwapStatus("Not Available");
		sh.getSwapDetail().getBookOwner().setBookStat("Not Available");
		sh.getRequestedSwapDetail().getBookOwner().setBookStat("Not Available");

		Session session = getSessionFactory().getCurrentSession();
		session.update(sh);

		UserNotification un = new UserNotification();
		un.setActionId(swapHeaderId);
		un.setActionName("swap");
		un.setActionStatus("delivered");
		un.setBookActionPerformedOn(sh.getRequestedSwapDetail().getBookOwner());
		un.setExtraMessage(String.valueOf(sh.getSwapDetail().getSwapDetailId()));
		un.setUser(sh.getRequestedSwapDetail().getBookOwner().getUser());
		un.setUserPerformer(sh.getSwapDetail().getBookOwner().getUser());
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);

		return sh;
	}

	public SwapHeader setComplete(long swapHeaderId, long userRatingId) {
		Session session = getSessionFactory().getCurrentSession();
		SwapHeader sh = new SwapHeader();
		sh = get(swapHeaderId);
		SwapDetail mySwapDetail = new SwapDetail();
		SwapDetail toBeSwapped = new SwapDetail();
		User requestor = new User();
		User requestee = new User();

		System.out.println("SwapUSer=" + sh.getSwapDetail().getBookOwner().getUser().getUserId());
		System.out.println("ReqSwapUSer=" + sh.getRequestedSwapDetail().getBookOwner().getUser().getUserId());

		System.out.println("SwapUSer=" + sh.getSwapDetail().getBookOwner().getUser().getUserId());
		System.out.println("ReqSwapUSer=" + sh.getRequestedSwapDetail().getBookOwner().getUser().getUserId());
		
		List<SwapDetail> requestorSwapDetail = new ArrayList<>();
		List<SwapDetail> requesteeSwapDetail = new ArrayList<>();

		for (SwapHeaderDetail shd : sh.getSwapHeaderDetails()) {
			if ("Requestor".equals(shd.getSwapType())) {
				requestorSwapDetail.add(shd.getSwapDetail());
			} else if ("Requestee".equals(shd.getSwapType())) {
				requesteeSwapDetail.add(shd.getSwapDetail());
			}
		}

		mySwapDetail = sh.getRequestedSwapDetail();
		toBeSwapped = sh.getSwapDetail();
		requestor = mySwapDetail.getBookOwner().getUser();
		requestee = toBeSwapped.getBookOwner().getUser();

		for (SwapDetail sd : requestorSwapDetail) {
			sd.getBookOwner().setUser(requestee);
			sd.setSwapStatus("Not Available");
			sd.getBookOwner().setBookStat("Not Available");
			session.update(sd);
		}

		for (SwapDetail sd : requesteeSwapDetail) {
			sd.getBookOwner().setUser(requestor);
			sd.setSwapStatus("Not Available");
			sd.getBookOwner().setBookStat("Not Available");
			session.update(sd);
		}

		sh.setSwapDetail(toBeSwapped);
		sh.setRequestedSwapDetail(mySwapDetail);
		sh.setStatus("Complete");
		session.update(sh);

		return sh;
	}

	public List<SwapHeader> history(long userId) {
		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("swapDetail", "swapDetail");
		criteria = criteria.createAlias("swapDetail.bookOwner", "bookOwnerSwap");
		criteria = criteria.createAlias("swapDetail.bookOwner.user", "userSwap");
		criteria = criteria.createAlias("requestedSwapDetail", "requestedSwapDetail");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner", "bookOwnerRequest");
		criteria = criteria.createAlias("requestedSwapDetail.bookOwner.user", "userRequest");
		criteria = criteria.add(Restrictions.or(Restrictions.eq("userSwap.userId", userId),
				Restrictions.eq("userRequest.userId", userId)));
		criteria = criteria
				.add(Restrictions.or(Restrictions.eq("status", "Complete"), Restrictions.eq("status", "Rejected")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();

		return flag;
	}

	public SwapHeader approveRequest(long swapHeaderId) {
		SwapHeader sh = new SwapHeader();
		
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		String currDate =df.format(cal.getTime());
		
		sh = get(swapHeaderId);

		Set<SwapHeaderDetail> shd = sh.getSwapHeaderDetails();
		Set<SwapHeaderDetail> shdetail = new HashSet<SwapHeaderDetail>();

		sh.setDateApproved(currDate);
		sh.setStatus("Approved");
		sh.setSwapHeaderDetails(shdetail);
		Session session = getSessionFactory().getCurrentSession();
		session.update(sh);

		UserNotification un = new UserNotification();
		un.setActionId(swapHeaderId);
		un.setActionName("swap");
		un.setActionStatus("Approved");
		un.setBookActionPerformedOn(sh.getSwapDetail().getBookOwner());
		un.setUser(sh.getUser());
		System.out.println("User = " + sh.getUser().getUserFname());
		un.setUserPerformer(sh.getSwapDetail().getBookOwner().getUser());
		userNotificationDao.save(un);
		pusherServer.sendNotification(un);

		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.createAlias("requestedSwapDetail", "requestedSwapDetail");
		criteria = criteria.add(Restrictions.eq("status", "Request"));
		criteria = criteria.add(
				Restrictions.eq("requestedSwapDetail.swapDetailId", sh.getRequestedSwapDetail().getSwapDetailId()));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();

		SwapHeader swapHeaderTemp = new SwapHeader();
		for (int init = 0; init < flag.size(); init++) {
			swapHeaderTemp = flag.get(init);

			if (!(swapHeaderTemp.equals(swapHeaderId))) {
				swapHeaderTemp.setStatus("Rejected");
				swapHeaderTemp.setDateRejected(currDate);
				swapHeaderTemp.setSwapExtraMessage("Accepted other request.");
				session.update(swapHeaderTemp);

				UserNotification userN = new UserNotification();
				userN.setActionId(swapHeaderTemp.getSwapHeaderId());
				userN.setActionName("swap");
				userN.setActionStatus("Rejected");
				userN.setBookActionPerformedOn(swapHeaderTemp.getSwapDetail().getBookOwner());
				userN.setUser(swapHeaderTemp.getUser());
				userN.setUserPerformer(swapHeaderTemp.getSwapDetail().getBookOwner().getUser());
				userN.setExtraMessage("Accepted other request.");
				userNotificationDao.save(userN);
				pusherServer.sendNotification(userN);

			}
		}
		return sh;
	}

	public SwapHeader rejectedRequest(long swapHeaderId) {
		SwapHeader sh = new SwapHeader();
		Set<SwapHeaderDetail> sd = new HashSet<SwapHeaderDetail>();
		BookOwner bo = new BookOwner();
		Session session = getSessionFactory().getCurrentSession();

		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		String currDate =df.format(cal.getTime());

		
		sh = get(swapHeaderId);
		sd = sh.getSwapHeaderDetails();

		sh.setDateRejected(currDate);
		sh.setStatus("Rejected");

		session.update(sh);

		return sh;
	}

	public List<SwapHeader> swapNotifyScheuler() {
		List<SwapHeader> flag = new ArrayList<SwapHeader>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SwapHeader.class);
		criteria = criteria.add(Restrictions.eq("status", "Approved"));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<SwapHeader>) criteria.list();

		return flag;
	}

	public SwapHeader checkExist(long userId, long swapDetailId) {
		SwapHeader flag = null;

		SwapHeaderDetail swp;
		String query = "select shd.swapHeaderId from swap_header_detail shd inner join swap_header sh on shd.swapHeaderId = "
				+ " sh.swapHeaderId where shd.swapDetailId = :swapDetailId and sh.userId = :userId and sh.status = 'Request'";

		SQLQuery sqlQuery = getSessionFactory().getCurrentSession().createSQLQuery(query);
		sqlQuery.setLong("swapDetailId", swapDetailId);
		sqlQuery.setLong("userId", userId);
		Object obj = sqlQuery.uniqueResult();
		try {
			Long swapHeaderId = new Long((Integer) obj);

			if (obj != null) {
				flag = get(swapHeaderId);
			}
		} catch (Exception e) {

		}

		return flag;
	}

	public SwapHeader updateConfirm(long swapHeaderId) {
		SwapHeader sh = new SwapHeader();

		sh = get(swapHeaderId);

		Session session = getSessionFactory().getCurrentSession();
		session.update(sh);

		UserNotification un = new UserNotification();

		un.setActionId(swapHeaderId);
		un.setActionName("swap");
		un.setBookActionPerformedOn(sh.getSwapDetail().getBookOwner());
		un.setUser(sh.getRequestedSwapDetail().getBookOwner().getUser());
		un.setUserPerformer(sh.getUser());
		un.setActionStatus("Confirm");

		userNotificationDao.save(un);
		pusherServer.sendNotification(un);
		return sh;
	}

	public boolean canSwap(long userId) {
		boolean flag = false;

		String query = "select count(swapHeaderId) from swap_header "
				+ " where swap_header.userId = :userId and (status='Confirm' or status='Approved' or status='Delivered')";

		SQLQuery sqlQuery = getSessionFactory().getCurrentSession().createSQLQuery(query);
		sqlQuery.setLong("userId", userId);
		Object obj = sqlQuery.uniqueResult();
		BigInteger bigIntVal = (BigInteger) obj;

		if (bigIntVal != null && bigIntVal.longValue() < 3) {
			flag = true;
		}

		return flag;
	}
}
