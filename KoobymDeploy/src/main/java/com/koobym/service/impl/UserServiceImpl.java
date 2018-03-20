package com.koobym.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koobym.dao.UserDao;
import com.koobym.dao.UserDayTimeDao;
import com.koobym.model.User;
import com.koobym.model.UserDayTime;
import com.koobym.service.UserService;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

	private UserDao userDao;
	private UserDayTimeDao userDayTimeDao;

	@Autowired
	public UserServiceImpl(UserDao userDao, UserDayTimeDao userDayTimeDao) {
		super(userDao);
		this.userDao = userDao;
		this.userDayTimeDao = userDayTimeDao;
	}

	@Override
	public User register(User user) {
		User userR = null;
		try {
			Set<UserDayTime> userDayTimes = user.getUserDayTimes();
			user.setUserDayTimes(null);
			userR = userDao.register(user);
			for (UserDayTime udt : userDayTimes) {
				udt.setUserId(userR.getUserId());
				userDayTimeDao.saveOrUpdate(udt);
			}

		} catch (Exception e) {
			userR = null;
		}
		return userR;
	}

	@Override
	public void update(User user) {
		user.setUserDayTimes(null);
		userDao.update(user);
	}

	public void updateUserDayTimes(User user, Set<UserDayTime> userDayTimes) {
		User userFromGet = get(user.getUserId());
		Set<UserDayTime> udts = new HashSet<UserDayTime>();
		Set<UserDayTime> originalUserDayTimes = userFromGet.getUserDayTimes();

		if (originalUserDayTimes != null) {
			for (UserDayTime udt : originalUserDayTimes) {
				userDayTimeDao.delete(udt);
			}
		}
		if (userDayTimes != null) {
			for (UserDayTime udt : userDayTimes) {
				udt.setUserDayTimeId(0);
				userDayTimeDao.saveOrUpdate(udt);
				udts.add(udt);
			}
		}
		user.setUserDayTimes(udts);
	}

	@Override
	public User login(User user) {
		User userLoggedIn = null;
		try {
			userLoggedIn = userDao.login(user);
		} catch (Exception e) {
			userLoggedIn = null;
		}
		return userLoggedIn;
	}

	@Override
	public User checkFbUser(String userFbId) {
		User userFB = null;
		try {
			System.out.println("in service");
			userFB = userDao.checkFbUser(userFbId);
		} catch (Exception e) {
			userFB = null;
			e.printStackTrace();
		}
		return userFB;
	}

}
