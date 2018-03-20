package com.koobym.dao.impl;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.koobym.dao.UserDayTimeDao;
import com.koobym.model.UserDayTime;

@Repository
public class UserDayTimeDaoImpl extends BaseDaoImpl<UserDayTime, Long> implements UserDayTimeDao {

	public UserDayTimeDaoImpl() {
		super(UserDayTime.class);
	}

	public void saveOrUpdate(UserDayTime userDayTime) {
		Session session = getSessionFactory().getCurrentSession();
		session.clear();
		session.saveOrUpdate(userDayTime);
		session.flush();
	}
	
	@Override
	public void delete(UserDayTime userDayTime) {
		Session session = getSessionFactory().getCurrentSession();
		String squery = "delete from user_day_time where userDayTimeId = :userDayTimeId";
		SQLQuery query = session.createSQLQuery(squery);
		query.setLong("userDayTimeId", userDayTime.getUserDayTimeId());
		query.executeUpdate();
	}
}
