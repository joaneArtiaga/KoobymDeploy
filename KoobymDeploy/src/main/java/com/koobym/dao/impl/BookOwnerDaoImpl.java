package com.koobym.dao.impl;

import java.math.BigInteger;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.koobym.dao.BookOwnerDao;
import com.koobym.model.Book;
import com.koobym.model.BookOwner;
import com.koobym.model.User;

@Repository
public class BookOwnerDaoImpl extends BaseDaoImpl<BookOwner, Long> implements BookOwnerDao {

	public BookOwnerDaoImpl() {
		super(BookOwner.class);
	}

	public BookOwner setBookOwner(long bookOwnerId, long userId) {
		BookOwner bookOwner = new BookOwner();

		Session session = getSessionFactory().getCurrentSession();
		String squery = "update book_owner set userId = :userId where book_ownerId = :bookOwnerId";

		SQLQuery query = session.createSQLQuery(squery);
		query.setLong("userId", userId);
		query.setLong("bookOwnerId", bookOwnerId);
		query.executeUpdate();

		bookOwner = get(bookOwnerId);

		return bookOwner;
	}

	public BookOwner increment(long bookOwnerId) {
		BookOwner bookOwner = get(bookOwnerId);
		System.out.println(bookOwner.getNoRenters());
		bookOwner.setNoRenters(bookOwner.getNoRenters() + 1);
		System.out.println(bookOwner.getNoRenters());
		update(bookOwner);
		return bookOwner;
	}

	public List<BookOwner> getMyBooksById(int userId) {
		List<BookOwner> flag = new ArrayList<BookOwner>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BookOwner.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.createAlias("book", "book");
		criteria = criteria.add(Restrictions.eq("user.userId", new Long(userId)));
		criteria = criteria.add(Restrictions.or(Restrictions.eq("book.status", "Available"),
				Restrictions.eq("book.status", "Not Available")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<BookOwner>) criteria.list();
		return flag;
	}

	public List<BookOwner> getStatusById() {
		List<BookOwner> flag = new ArrayList<BookOwner>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BookOwner.class);
		criteria = criteria.createAlias("user", "user");
		criteria = criteria.add(Restrictions.or(Restrictions.eq("status", "Rent"), Restrictions.eq("status", "Swap")));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<BookOwner>) criteria.list();
		return flag;
	}

	public List<BookOwner> allDistinct() {
		List<BookOwner> flag = new ArrayList<BookOwner>();

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BookOwner.class);
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<BookOwner>) criteria.list();
		return flag;
	}

	public List<BookOwner> suggestedBooks(int userId) {
		List<BookOwner> flag = new ArrayList<BookOwner>();
		Session session = getSessionFactory().getCurrentSession();
		String squery = "select * from book_owner join (SELECT bookId as bi, count(genre_bookId) "
				+ " FROM genre_book JOIN genre_user ON genre_book.genreId = genre_user.genreId  WHERE genre_user.userId = :userId"
				+ " GROUP BY bookId ORDER BY 2 desc) as suggested_books on book_owner.bookId = suggested_books.bi"
				+ " LEFT JOIN (select avg(rate.rateNumber) as rate, book_owner_rating.book_ownerId as boi from rate"
				+ " JOIN book_owner_rating on rate.rateId = book_owner_rating.rateId group by"
				+ " book_owner_rating.book_ownerId) as ratings on ratings.boi = book_owner.book_ownerId where userId != :userId "
				+ " and book_owner.bookStat = 'Available' order by rate desc";

		SQLQuery query = session.createSQLQuery(squery);
		query.setInteger("userId", userId);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		Map<String, Object> row = null;
		List<Object> data = query.list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		BookOwner temp;
		for (Object object : data) {
			row = (Map<String, Object>) object;
			temp = new BookOwner();

			temp.setBook_OwnerId((int) row.get("book_ownerId"));
			if (isCurrentlyAvailableForRent(temp.getBook_OwnerId())) {
				temp.setBook(new Book());
				temp.getBook().setBookId((int) row.get("bookId"));
				Hibernate.initialize(temp.getBook());
				temp.setUser(new User());
				temp.getUser().setUserId((int) row.get("userId"));
				Hibernate.initialize(temp.getUser());
				temp.setStatusDescription((String) row.get("statusDescription"));
				Date dateBought = (Date) row.get("dateBought");
				if (dateBought != null) {
					temp.setDateBought(format.format(new java.util.Date(dateBought.getTime())));
				}
				temp.setNoRenters((int) row.get("noRenters"));
				temp.setStatus((String) row.get("status"));
				Double rate = (Double) row.get("rate");
				if (rate != null) {
					temp.setRate(rate);
				} else {
					temp.setRate(0);
				}
				flag.add(temp);
			}
		}
		return flag;
	}

	public List<BookOwner> searchByGenre(String genre) {
		List<BookOwner> flag = new ArrayList<BookOwner>();
		Session session = getSessionFactory().getCurrentSession();
		String squery = " select bo.book_ownerId, bo.bookId, bo.userId, bo.statusDescription, bo.dateBought, bo.noRenters, bo.status, bo.bookStat, rating.rate"
				+ " from book_owner bo inner join genre_book gb on gb.bookId = bo.bookId inner join genre g on g.genreID = gb.genreId"
				+ " left join (select avg(r.rateNumber) as rate, bor.book_ownerId from rate r inner join book_owner_rating bor on r.rateId = bor.rateId group by bor.book_ownerId) as rating"
				+ " on rating.book_ownerId = bo.book_ownerId"
				+ " where g.genreName like :genre and bo.bookStat='Available' order by rating.rate desc";

		SQLQuery query = session.createSQLQuery(squery);
		genre = "%" + genre + "%";
		query.setString("genre", genre);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		Map<String, Object> row = null;
		List<Object> data = query.list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		BookOwner temp;
		for (Object object : data) {
			row = (Map<String, Object>) object;
			temp = new BookOwner();

			temp.setBook_OwnerId((int) row.get("book_ownerId"));
			if (isCurrentlyAvailableForRent(temp.getBook_OwnerId())) {
				temp.setBook(new Book());
				temp.getBook().setBookId((int) row.get("bookId"));
				Hibernate.initialize(temp.getBook());
				temp.setUser(new User());
				temp.getUser().setUserId((int) row.get("userId"));
				Hibernate.initialize(temp.getUser());
				temp.setStatusDescription((String) row.get("statusDescription"));
				Date dateBought = (Date) row.get("dateBought");
				if (dateBought != null) {
					temp.setDateBought(format.format(new java.util.Date(dateBought.getTime())));
				}
				temp.setNoRenters((int) row.get("noRenters"));
				temp.setStatus((String) row.get("status"));
				Double rate = (Double) row.get("rate");
				if (rate != null) {
					temp.setRate(rate);
				} else {
					temp.setRate(0);
				}
				flag.add(temp);
			}
		}
		return flag;
	}

	public List<BookOwner> searchByAuthor(String author) {
		List<BookOwner> flag = new ArrayList<BookOwner>();
		Session session = getSessionFactory().getCurrentSession();
		String squery = "select bo.book_ownerId, bo.bookId, bo.userId, bo.statusDescription, bo.dateBought, bo.noRenters, bo.status, bo.bookStat, rating.rate  from book_owner bo inner join author_book ab on ab.bookId = bo.bookId inner join author a on a.authorId = ab.authorId"
				+ " left join (select avg(r.rateNumber) as rate, bor.book_ownerId from rate r inner join book_owner_rating bor on r.rateId = bor.rateId group by bor.book_ownerId) as rating"
				+ " on rating.book_ownerId = bo.book_ownerId"
				+ " where a.authorFname like :author and bo.bookStat='Available'  order by rating.rate desc";

		SQLQuery query = session.createSQLQuery(squery);
		author = "%" + author + "%";
		query.setString("author", author);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		Map<String, Object> row = null;
		List<Object> data = query.list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		BookOwner temp;
		for (Object object : data) {
			row = (Map<String, Object>) object;
			temp = new BookOwner();

			temp.setBook_OwnerId((int) row.get("book_ownerId"));
			if (isCurrentlyAvailableForRent(temp.getBook_OwnerId())) {
				temp.setBook(new Book());
				temp.getBook().setBookId((int) row.get("bookId"));
				Hibernate.initialize(temp.getBook());
				temp.setUser(new User());
				temp.getUser().setUserId((int) row.get("userId"));
				Hibernate.initialize(temp.getUser());
				temp.setStatusDescription((String) row.get("statusDescription"));
				Date dateBought = (Date) row.get("dateBought");
				if (dateBought != null) {
					temp.setDateBought(format.format(new java.util.Date(dateBought.getTime())));
				}
				temp.setNoRenters((int) row.get("noRenters"));
				temp.setStatus((String) row.get("status"));
				Double rate = (Double) row.get("rate");
				if (rate != null) {
					temp.setRate(rate);
				} else {
					temp.setRate(0);
				}
				flag.add(temp);
			}
		}
		return flag;
	}

	public List<BookOwner> searchByUserOwner(String userOwnerName) {
		List<BookOwner> flag = new ArrayList<BookOwner>();
		Session session = getSessionFactory().getCurrentSession();
		String squery = "select bo.book_ownerId, bo.bookId, bo.userId, bo.statusDescription, bo.dateBought, bo.noRenters, bo.status, bo.bookStat, rating.rate  from book_owner bo"
				+ " inner join user u on u.userId = bo.userId"
				+ " left join (select avg(r.rateNumber) as rate, bor.book_ownerId from rate r inner join book_owner_rating bor on r.rateId = bor.rateId group by bor.book_ownerId) as rating"
				+ " on rating.book_ownerId = bo.book_ownerId"
				+ " where u.userFname like :userOwnerName or u.userLname like :userOwnerName "
				+ " and bo.bookStat='Available' order by rating.rate desc;";

		SQLQuery query = session.createSQLQuery(squery);
		userOwnerName = "%" + userOwnerName + "%";
		query.setString("userOwnerName", userOwnerName);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		Map<String, Object> row = null;
		List<Object> data = query.list();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		BookOwner temp;
		for (Object object : data) {
			row = (Map<String, Object>) object;
			temp = new BookOwner();

			temp.setBook_OwnerId((int) row.get("book_ownerId"));
			if (isCurrentlyAvailableForRent(temp.getBook_OwnerId())) {
				temp.setBook(new Book());
				temp.getBook().setBookId((int) row.get("bookId"));
				Hibernate.initialize(temp.getBook());
				temp.setUser(new User());
				temp.getUser().setUserId((int) row.get("userId"));
				Hibernate.initialize(temp.getUser());
				temp.setStatusDescription((String) row.get("statusDescription"));
				Date dateBought = (Date) row.get("dateBought");
				if (dateBought != null) {
					temp.setDateBought(format.format(new java.util.Date(dateBought.getTime())));
				}
				temp.setNoRenters((int) row.get("noRenters"));
				temp.setStatus((String) row.get("status"));
				Double rate = (Double) row.get("rate");
				if (rate != null) {
					temp.setRate(rate);
				} else {
					temp.setRate(0);
				}
				flag.add(temp);
			}
		}
		return flag;
	}

	public boolean isCurrentlyAvailableForRent(long bookOwnerId) {
		boolean flag = false;

		String sQuery = "select count(rentalHeaderId) from rental_header join rental_detail on "
				+ "rental_header.rentalDetailId = rental_detail.rental_detailId where rental_detail.bookOwnerId = :bookOwnerId and "
				+ "rental_header.status != 'Rejected' and rental_header.status != 'Complete' and rental_header.status != 'Request'";

		SQLQuery sqlQuery = getSessionFactory().getCurrentSession().createSQLQuery(sQuery);
		sqlQuery.setLong("bookOwnerId", bookOwnerId);
		Object obj = sqlQuery.uniqueResult();
		BigInteger bigIntVal = (BigInteger) obj;

		if (bigIntVal.intValue() == 0) {
			flag = true;
		}

		return flag;
	}

	public List<BookOwner> searchBookOwner(String searchKey) {
		List<BookOwner> flag = new ArrayList<BookOwner>();
		searchKey = "%" + searchKey + "%";

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BookOwner.class);
		criteria = criteria.createAlias("book", "book");
		criteria = criteria.add(Restrictions.eq("bookStat", "Available"));
		criteria = criteria.add(Restrictions.like("book.bookTitle", searchKey));
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		flag = (List<BookOwner>) criteria.list();
		return flag;
	}

}
