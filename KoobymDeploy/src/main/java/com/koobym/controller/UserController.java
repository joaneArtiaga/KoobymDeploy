package com.koobym.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.koobym.model.User;
import com.koobym.model.UserDayTime;
import com.koobym.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<User> login(@RequestBody User user) {
		ResponseEntity<User> ent = null;
		User userLoggedIn = userService.login(user);
		ent = ResponseEntity.ok(userLoggedIn);
		return ent;
	}

	@RequestMapping(value = "/checkFbUser/{userFbId}", method = RequestMethod.GET)
	public ResponseEntity<User> checkFbUser(@PathVariable("userFbId") String userFbId) {
		ResponseEntity<User> ent = null;
		System.out.println(userFbId);
		User userLoggedIn = userService.checkFbUser(userFbId);
		ent = ResponseEntity.ok(userLoggedIn);
		return ent;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<User> add(@RequestBody User user) {
		ResponseEntity<User> ent = null;
		userService.register(user);
		if (user == null) {
			ent = ResponseEntity.badRequest().body(null);
		} else {
			ent = ResponseEntity.ok(user);
		}
		return ent;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<User> update(@RequestBody User user) {
		ResponseEntity<User> ent = null;
		Set<UserDayTime> userDayTimes = user.getUserDayTimes();
		userService.update(user);
		userService.updateUserDayTimes(user, userDayTimes);
		if (user == null) {
			ent = ResponseEntity.badRequest().body(null);
		} else {
			ent = ResponseEntity.ok(user);
		}
		return ent;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getRoles() {
		ResponseEntity<List<User>> flag = ResponseEntity.ok(userService.list());
		return flag;
	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getRoles(@PathVariable("id") int id) {
		ResponseEntity<User> flag = ResponseEntity.ok(userService.get(new Long(id)));
		return flag;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteUser(@PathVariable("id") int id) {
		userService.delete(new Long(id));
		ResponseEntity<Integer> flag = ResponseEntity.ok(id);
		return flag;
	}
}