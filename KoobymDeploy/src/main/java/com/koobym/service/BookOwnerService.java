package com.koobym.service;

import java.util.List;
import java.util.Set;

import com.koobym.dto.BookActivityObject;
import com.koobym.dto.Transaction;
import com.koobym.model.BookOwner;

public interface BookOwnerService extends BaseService<BookOwner, Long> {
	public List<BookOwner> searchBookOwner(String searchKey);

	public Set<BookActivityObject> getUserRequestsBookActivities(int userId);

	public List<BookOwner> allDistinct();

	public BookOwner setBookOwner(long bookOwnerId, long userId);

	public List<BookOwner> getMyBooksById(int userId);

	public BookOwner increment(long bookOwnerId);

	public List<BookOwner> getStatusById();

	public List<Transaction> bookTransactions(long bookOwnerId);

	public List<BookOwner> getSuggestedBooks(int userId);

	public List<BookOwner> searchByAuthor(String author);

	public List<BookOwner> searchByGenre(String genre);

	public List<BookOwner> searchByUserOwner(String userOwnerName);

	public Set<BookActivityObject> getUserOwnBookActivities(int userId);

	public List<BookOwner> getRecommendationByUserSimilarity(int userId);

	public List<BookOwner> mergedSuggested(int userId);
}
