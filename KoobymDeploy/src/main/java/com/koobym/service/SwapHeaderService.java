package com.koobym.service;

import java.util.List;

import com.koobym.model.BookOwner;
import com.koobym.model.SwapHeader;

public interface SwapHeaderService extends BaseService<SwapHeader, Long> {
	public List<SwapHeader> getToDeliverById(int userId);

	public List<SwapHeader> getToReceiveByIdRenter(int userId);

	public SwapHeader setApprovedExam(String status, long swapHeaderId, String date);

	public SwapHeader swapRequested(SwapHeader swapHeader);

	public List<SwapHeader> getRequestedSwaps(long userId);

	public List<SwapHeader> getToApproveSwaps(long userId);

	public List<SwapHeader> getCompleteById(int userId);

	public List<SwapHeader> getApprovedSwaps(long userId);

	public List<SwapHeader> getRejectedByIdOwner(int userId);

	public List<SwapHeader> getRejectedByIdRenter(int userId);

	public List<SwapHeader> getCompleteAllById(int userId);

	public SwapHeader addNewSwapHeader(SwapHeader swapHeader);

	public SwapHeader swapOwner(long userId);

	public SwapHeader setDelivered(long swapHeaderId);

	public SwapHeader setComplete(long swapHeaderId, long userRatingId);

	public List<SwapHeader> history(long userId);

	public List<SwapHeader> getRequests(long userId);

	public SwapHeader approveRequest(long swapHeaderId);

	public SwapHeader rejectedRequest(long swapHeaderId);

	public List<SwapHeader> swapNotifyScheuler();

	public SwapHeader checkExist(long userId, long swapDetailId);

	public SwapHeader updateConfirm(long swapHeaderId);

	public boolean canSwap(long userId);
}
