package com.koobym.dao.impl;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.koobym.dao.AuctionCommentDetailDao;
import com.koobym.model.AuctionCommentDetail;

@Repository
public class AuctionCommentDetailDaoImpl extends BaseDaoImpl<AuctionCommentDetail, Long> implements AuctionCommentDetailDao {

	public AuctionCommentDetailDaoImpl() {
		super(AuctionCommentDetail.class);
	}
	
	
	public List<AuctionCommentDetail> getAuctionCommentDetailOfAuctionDetail(long auctionDetailId){
		List<AuctionCommentDetail> flag = null;
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AuctionCommentDetail.class);
		criteria = criteria.add(Restrictions.eq("auctionDetail.auctionDetailId", auctionDetailId));
		flag = (List<AuctionCommentDetail>) criteria.list();
		
		return flag;
	}
	
}
