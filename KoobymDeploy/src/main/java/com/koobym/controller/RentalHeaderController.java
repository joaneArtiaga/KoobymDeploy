package com.koobym.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.koobym.model.AuctionHeader;
import com.koobym.model.RentalHeader;
import com.koobym.model.UserRental;
import com.koobym.service.RentalHeaderService;

@RestController
@RequestMapping(value = "/rentalHeader")
public class RentalHeaderController {

	@Autowired
	private RentalHeaderService rentalHeaderService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<RentalHeader> add(@RequestBody RentalHeader rentalHeader) {
		ResponseEntity<RentalHeader> ent = null;
		rentalHeaderService.addNewRentalHeader(rentalHeader);
		if (rentalHeader == null) {
			ent = ResponseEntity.badRequest().body(null);
		} else {
			ent = ResponseEntity.ok(rentalHeader);
		}
		return ent;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<RentalHeader> update(@RequestBody RentalHeader rentalHeader) {
		ResponseEntity<RentalHeader> ent = null;
		rentalHeaderService.update(rentalHeader);
		if (rentalHeader == null) {
			ent = ResponseEntity.badRequest().body(null);
		} else {
			ent = ResponseEntity.ok(rentalHeader);
		}
		return ent;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getRoles() {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.list());
		return flag;
	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<RentalHeader> getRoles(@PathVariable("id") int id) {
		ResponseEntity<RentalHeader> flag = ResponseEntity.ok(rentalHeaderService.get(new Long(id)));
		return flag;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteUser(@PathVariable("id") int id) {
		rentalHeaderService.delete(new Long(id));
		ResponseEntity<Integer> flag = ResponseEntity.ok(id);
		return flag;
	}
	
	@RequestMapping(value = "/rentalById/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getRentalId(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getListRentalById(userId));
		return flag;
	}
	
	@RequestMapping(value = "/toDeliverById/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getToDeliverById(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getToDeliverById(userId));
		return flag;
	}
	
	@RequestMapping(value = "/toReceiveByIdRenter/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getToReceiveByIdRenter(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getToReceiveByIdRenter(userId));
		return flag;
	}
	
	@RequestMapping(value = "/myRequestById/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getMyRequestsById(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getMyRequestsById(userId));
		return flag;
	}
	
	@RequestMapping(value = "/requestReceivedById/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getRequestReceivedById(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getRequestReceivedById(userId));
		return flag;
	}
	
	@RequestMapping(value = "/toReturnByIdRenter/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getToReturnByIdRenter(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getToReturnByIdRenter(userId));
		return flag;
	}
	
	@RequestMapping(value = "/toReturnByIdOwner/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getToReturnByIdOwner(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getToReturnByIdOwner(userId));
		return flag;
	}
	
	@RequestMapping(value = "/completeByIdRenter/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getCompleteByIdRenter(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getCompleteByIdRenter(userId));
		return flag;
	}
	
	@RequestMapping(value = "/toReceiveByIdOwner/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getToReceiveByIdOwner(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getToReceiveByIdOwner(userId));
		return flag;
	}
	
	@RequestMapping(value = "/completeByIdOwner/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getCompleteByIdOwner(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getCompleteByIdOwner(userId));
		return flag;
	}
	
	@RequestMapping(value = "/rejectedByIdOwner/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getRejectedByIdOwner(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getRejectedByIdOwner(userId));
		return flag;
	}
	
	@RequestMapping(value = "/rejectedByIdRenter/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getRejectedByIdRenter(@PathVariable("userId") int userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getRejectedByIdRenter(userId));
		return flag;
	}

	@RequestMapping(value = "/updateStatus/{rentalHeaderId}/{status}/{dateApproved}", method = RequestMethod.GET)
	public ResponseEntity <RentalHeader> setApprovedExam(@PathVariable("rentalHeaderId") long rentalHeaderId, @PathVariable("status") String status, @PathVariable("dateApproved") String dateApproved) {
		ResponseEntity <RentalHeader> flag = ResponseEntity.ok(rentalHeaderService.setApprovedExam(rentalHeaderId, status, dateApproved));
		return flag;
	}
	
	@RequestMapping(value = "/addRentalHeader/{rentalHeader}", method = RequestMethod.GET)
	public ResponseEntity <RentalHeader> setRentalHeader(@PathVariable("rentalHeader") RentalHeader rentalHeader) {
		ResponseEntity <RentalHeader> flag = ResponseEntity.ok(rentalHeaderService.setRentalHeader(rentalHeader));
		return flag;
	}
	
	@RequestMapping(value = "/checkExist/{userId}/{rentalDetailId}", method = RequestMethod.GET)
	public ResponseEntity <RentalHeader> checkExist(@PathVariable("userId") long userId, @PathVariable("rentalDetailId") long rentalDetailId) {
		ResponseEntity <RentalHeader> flag = ResponseEntity.ok(rentalHeaderService.checkExist(userId, rentalDetailId));
		return flag;
	}
	
	@RequestMapping(value = "/getRentalDetail/{rentalDetailId}", method = RequestMethod.GET)
	public ResponseEntity <List<RentalHeader>> getCompleteByRentalDetail(@PathVariable("rentalDetailId") long rentalDetailId) {
		ResponseEntity <List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getCompleteByRentalDetail(rentalDetailId));
		return flag;
	}
	
	@RequestMapping(value = "/getCount/{bookOwnerId}", method = RequestMethod.GET)
	public ResponseEntity <Long> numberOfCompletedRentsByBookOwnerId(@PathVariable("bookOwnerId") long bookOwnerId) {
		ResponseEntity <Long> flag = ResponseEntity.ok(rentalHeaderService.numberOfCompletedRentsByBookOwnerId(bookOwnerId));
		return flag;
	}
	
	@RequestMapping(value = "/getRentalByBookOwner/{bookOwnerId}", method = RequestMethod.GET)
	public ResponseEntity <List<RentalHeader>> getRentalHeader(@PathVariable("bookOwnerId") long bookOwnerId) {
		ResponseEntity <List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getRentalHeader(bookOwnerId));
		return flag;
	}
	
	@RequestMapping(value = "/updateMeetUp/{rentalHeaderId}/{meetUpId}", method = RequestMethod.GET)
	public ResponseEntity <RentalHeader> setMeetUp(@PathVariable("rentalHeaderId") long rentalHeaderId, @PathVariable("meetUp") String status, @PathVariable("dateApproved") String dateApproved) {
		ResponseEntity <RentalHeader> flag = ResponseEntity.ok(rentalHeaderService.setApprovedExam(rentalHeaderId, status, dateApproved));
		return flag;
	}
	
	@RequestMapping(value = "/setReturnById/{rentalHeaderId}/{meetUpId}", method = RequestMethod.GET)
	public ResponseEntity<RentalHeader> setReturnById(@PathVariable("rentalHeaderId") long rentalHeaderId, @PathVariable("meetUpId") long meetUpId) {
		ResponseEntity<RentalHeader> flag = ResponseEntity.ok(rentalHeaderService.setReturnMeetUp(rentalHeaderId, meetUpId));
		return flag;
	}
	
	@RequestMapping(value = "/getAllReceive/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RentalHeader>> getAllReceive(@PathVariable("userId") long userId) {
		ResponseEntity<List<RentalHeader>> flag = ResponseEntity.ok(rentalHeaderService.getToReceive(userId));
		return flag;
	}
	
	@RequestMapping(value = "/returnToReceive/{rentalHeaderId}", method = RequestMethod.GET)
	public ResponseEntity<RentalHeader> returnToReceive(@PathVariable("rentalHeaderId") long rentalHeaderId) {
		ResponseEntity<RentalHeader> flag = ResponseEntity.ok(rentalHeaderService.setReturnToReceive(rentalHeaderId));
		return flag;
	}
}
	