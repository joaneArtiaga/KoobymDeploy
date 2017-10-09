package com.koobym.service;

import java.util.List;
import com.koobym.model.RentalHeader;
import com.koobym.model.UserRental;

public interface RentalHeaderService extends BaseService<RentalHeader, Long> {
	public List<RentalHeader> getListRentalById(int userId);
	public List<RentalHeader> getToDeliverById(int userId);
	public List<RentalHeader> getToReceiveByIdRenter(int userId);
	public List<RentalHeader> getMyRequestsById(int userId);
	public List<RentalHeader> getRequestReceivedById(int userId);
	public List<RentalHeader> getToReturnByIdRenter(int userId);
	public List<RentalHeader> getCompleteByIdRenter(int userId);
	public List<RentalHeader> getToReceiveByIdOwner(int userId);
	public List<RentalHeader> getCompleteByIdOwner(int userId);
	public List<RentalHeader> getToReturnByIdOwner(int userId);
	public RentalHeader setApprovedExam(long rentalHeaderId, String status);
	public RentalHeader setRentalHeader(RentalHeader rentalHeader);
}
