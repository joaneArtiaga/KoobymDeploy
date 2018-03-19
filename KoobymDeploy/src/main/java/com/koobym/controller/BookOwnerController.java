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

import com.koobym.dto.BookActivityObject;
import com.koobym.model.BookOwner;
import com.koobym.service.BookOwnerService;

@RestController
@RequestMapping(value = "/bookOwner")
public class BookOwnerController {

	@Autowired
	private BookOwnerService bookOwnerService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<BookOwner> add(@RequestBody BookOwner bookOwner) {
		ResponseEntity<BookOwner> ent = null;
		bookOwnerService.save(bookOwner);
		if (bookOwner == null) {
			ent = ResponseEntity.badRequest().body(null);
		} else {
			ent = ResponseEntity.ok(bookOwner);
		}
		return ent;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<BookOwner> update(@RequestBody BookOwner bookOwner) {
		ResponseEntity<BookOwner> ent = null;
		bookOwnerService.update(bookOwner);
		if (bookOwner == null) {
			ent = ResponseEntity.badRequest().body(null);
		} else {
			ent = ResponseEntity.ok(bookOwner);
		}
		return ent;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> getRoles() {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.allDistinct());
		return flag;
	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<BookOwner> getRoles(@PathVariable("id") int id) {
		ResponseEntity<BookOwner> flag = ResponseEntity.ok(bookOwnerService.get(new Long(id)));
		return flag;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteUser(@PathVariable("id") int id) {
		bookOwnerService.delete(new Long(id));
		ResponseEntity<Integer> flag = ResponseEntity.ok(id);
		return flag;
	}

	@RequestMapping(value = "/updateBookOwner/{bookOwnerId}/{userId}", method = RequestMethod.GET)
	public ResponseEntity<BookOwner> setBookOwner(@PathVariable("bookOwnerId") int bookOwnerId,
			@PathVariable("userId") long userId) {
		ResponseEntity<BookOwner> flag = ResponseEntity.ok(bookOwnerService.setBookOwner(bookOwnerId, userId));
		return flag;
	}

	@RequestMapping(value = "/myBooksById/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> getMyBooksById(@PathVariable("userId") int userId) {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.getMyBooksById(userId));
		return flag;
	}

	@RequestMapping(value = "/increment/{bookOwnerId}", method = RequestMethod.GET)
	public ResponseEntity<BookOwner> increment(@PathVariable("bookOwnerId") long bookOwnerId) {
		ResponseEntity<BookOwner> flag = ResponseEntity.ok(bookOwnerService.increment(bookOwnerId));
		return flag;
	}

	@RequestMapping(value = "/allBook", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> getStatusById() {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.getStatusById());
		return flag;
	}

	@RequestMapping(value = "/suggestedBooks/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> getStatusById(@PathVariable("userId") int userId) {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.getSuggestedBooks(userId));
		return flag;
	}

	@RequestMapping(value = "/suggestedBooksMerged/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> suggestedBooksMerged(@PathVariable("userId") int userId) {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.mergedSuggested(userId));
		return flag;
	}

	@RequestMapping(value = "/suggestedBooksByUserSimilarity/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> suggestedBooksByUserSimilarity(@PathVariable("userId") int userId) {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity
				.ok(bookOwnerService.getRecommendationByUserSimilarity(userId));
		return flag;
	}

	@RequestMapping(value = "/searchByGenre/{genre}", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> searchByGenre(@PathVariable("genre") String genre) {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.searchByGenre(genre));
		return flag;
	}

	@RequestMapping(value = "/searchByAuthor/{author}", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> searchByAuthor(@PathVariable("author") String author) {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.searchByAuthor(author));
		return flag;
	}

	@RequestMapping(value = "/searchByUserOwner/{userOwner}", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> searchByUserOwner(@PathVariable("userOwner") String userOwner) {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.searchByUserOwner(userOwner));
		return flag;
	}

	@RequestMapping(value = "/searchBooks/{searchKey}", method = RequestMethod.GET)
	public ResponseEntity<List<BookOwner>> searchBooks(@PathVariable("searchKey") String searchKey) {
		ResponseEntity<List<BookOwner>> flag = ResponseEntity.ok(bookOwnerService.searchBookOwner(searchKey));
		return flag;
	}

	@RequestMapping(value = "/bookActivity/own/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Set<BookActivityObject>> getBookActivityOwn(@PathVariable("userId") int userId) {
		ResponseEntity<Set<BookActivityObject>> flag = ResponseEntity
				.ok(bookOwnerService.getUserOwnBookActivities(userId));
		return flag;
	}

	@RequestMapping(value = "/bookActivity/requests/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Set<BookActivityObject>> getBookActivityRequests(@PathVariable("userId") int userId) {
		ResponseEntity<Set<BookActivityObject>> flag = ResponseEntity
				.ok(bookOwnerService.getUserRequestsBookActivities(userId));
		return flag;
	}

}