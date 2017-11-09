package com.koobym.dao;

import java.util.List;

import com.koobym.model.BookOwner;

public interface BookOwnerDao extends BaseDao<BookOwner, Long> {
	
	public boolean isCurrentlyAvailableForRent(long bookOwnerId);
	
	public List<BookOwner> searchBookOwner(String searchKey);

	public BookOwner setBookOwner(long bookOwnerId, long userId);

	public List<BookOwner> getMyBooksById(int userId);

	public BookOwner increment(long bookOwnerId);

	public List<BookOwner> getStatusById();

	public List<BookOwner> allDistinct();

	public List<BookOwner> suggestedBooks(int userId);
}
