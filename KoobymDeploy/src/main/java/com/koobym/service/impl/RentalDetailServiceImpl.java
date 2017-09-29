package com.koobym.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koobym.dao.AuthorDao;
import com.koobym.dao.BookOwnerDao;
import com.koobym.dao.GenreDao;
import com.koobym.dao.RentalDetailDao;
import com.koobym.dao.UserDao;
import com.koobym.model.Author;
import com.koobym.model.Book;
import com.koobym.model.Genre;
import com.koobym.model.RentalDetail;
import com.koobym.model.User;
import com.koobym.service.AuthorService;
import com.koobym.service.BookService;
import com.koobym.service.RentalDetailService;
import com.koobym.service.UserService;

@Service
@Transactional
public class RentalDetailServiceImpl extends BaseServiceImpl<RentalDetail, Long> implements RentalDetailService {

	private RentalDetailDao rentalDetailDao;
	
	@Autowired
	private BookOwnerDao bookOwnerDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	public RentalDetailServiceImpl(RentalDetailDao rentalDetailDao) {
		super(rentalDetailDao);
		this.rentalDetailDao = rentalDetailDao;
	}
	
	
	public List<RentalDetail> getSuggestedByGenre(int userId){
		List<RentalDetail> flag = rentalDetailDao.suggestedBooksByGenre(userId);
		
		for(RentalDetail rd: flag) {
			rd.setBookOwner(bookOwnerDao.get(rd.getBookOwner().getBook_OwnerId()));
		}
		
		return flag;
	}


	@Override
	public List<RentalDetail> getMostRented() {
		return rentalDetailDao.mostRentedBooks();
	}

	@Override
	public List<RentalDetail> getRentalById(int userId){
		return rentalDetailDao.getRentalById(userId);
	}

}
