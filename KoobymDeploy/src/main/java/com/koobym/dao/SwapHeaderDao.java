package com.koobym.dao;

import java.util.List;


import java.util.Set;

import com.koobym.model.BookOwner;
import com.koobym.model.SwapHeader;

public interface SwapHeaderDao extends BaseDao<SwapHeader, Long> {
	public List<SwapHeader> getToDeliverById(int userId);
	public List<SwapHeader> getToReceiveByIdRenter(int userId);
	public SwapHeader setApprovedExam(long swapHeaderId, String status);
	
}
