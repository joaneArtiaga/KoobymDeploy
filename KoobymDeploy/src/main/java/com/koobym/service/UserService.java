package com.koobym.service;

import java.util.Set;

import com.koobym.model.User;
import com.koobym.model.UserDayTime;

public interface UserService extends BaseService<User, Long> {

	public User register(User user);

	public User login(User user);

	public User checkFbUser(String userFbId);

	public void updateUserDayTimes(User user, Set<UserDayTime> userDayTimes);
}
