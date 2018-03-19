package com.koobym.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koobym.dao.BookDao;
import com.koobym.dao.BookOwnerDao;
import com.koobym.dao.RentalHeaderDao;
import com.koobym.dao.SwapHeaderDao;
import com.koobym.dao.UserDao;
import com.koobym.dto.BookActivityObject;
import com.koobym.dto.Transaction;
import com.koobym.model.BookOwner;
import com.koobym.model.Genre;
import com.koobym.model.RentalHeader;
import com.koobym.model.SwapHeader;
import com.koobym.model.User;
import com.koobym.service.BookOwnerRatingService;
import com.koobym.service.BookOwnerService;

@Service
@Transactional
public class BookOwnerServiceImpl extends BaseServiceImpl<BookOwner, Long> implements BookOwnerService {

	private BookOwnerDao bookOwnerDao;
	private BookDao bookDao;
	private UserDao userDao;
	private RentalHeaderDao rentalHeaderDao;
	private SwapHeaderDao swapHeaderDao;

	@Autowired
	private BookOwnerRatingService bookOwnerRatingService;

	@Autowired
	public BookOwnerServiceImpl(BookOwnerDao bookOwnerDao, RentalHeaderDao rentalHeaderDao, SwapHeaderDao swapHeaderDao,
			BookDao bookDao, UserDao userDao) {
		super(bookOwnerDao);
		this.bookOwnerDao = bookOwnerDao;
		this.rentalHeaderDao = rentalHeaderDao;
		this.swapHeaderDao = swapHeaderDao;
		this.bookDao = bookDao;
		this.userDao = userDao;
	}

	public List<BookOwner> allDistinct() {
		return bookOwnerDao.allDistinct();
	}

	@Override
	public BookOwner setBookOwner(long bookOwnerId, long userId) {
		return bookOwnerDao.setBookOwner(bookOwnerId, userId);
	}

	@Override
	public List<BookOwner> getMyBooksById(int userId) {
		List<BookOwner> flag = bookOwnerDao.getMyBooksById(userId);
		for (BookOwner bo : flag) {
			bo.setRate(bookOwnerRatingService.averageRatingOfBookOwner(bo.getBook_ownerId()));
		}
		return flag;
	}

	@Override
	public BookOwner increment(long bookOwnerId) {
		return bookOwnerDao.increment(bookOwnerId);
	}

	@Override
	public List<BookOwner> getStatusById() {
		return bookOwnerDao.getStatusById();
	}

	@Override
	public List<Transaction> bookTransactions(long bookOwnerId) {
		List<Transaction> transactions = new ArrayList<Transaction>();

		List<RentalHeader> rentalHeaders = rentalHeaderDao.getListRentalByBookOwnerId(bookOwnerId);
		Transaction trans;
		for (RentalHeader rh : rentalHeaders) {
			trans = new Transaction();
			trans.setTransactionId(rh.getRentalHeaderId());
			trans.setTransactionStatus(rh.getStatus());
			trans.setTransactionType("rent");
			trans.setUser(rh.getUserId());
			transactions.add(trans);
		}

		List<SwapHeader> swapHeaders = swapHeaderDao.getListSwapHeaderByBookOwnerId(bookOwnerId);
		for (SwapHeader sh : swapHeaders) {
			trans = new Transaction();
			trans.setTransactionId(sh.getSwapHeaderId());
			trans.setTransactionStatus(sh.getStatus());
			trans.setTransactionType("swap");
			trans.setUser(sh.getUser());
			transactions.add(trans);
		}

		return transactions;
	}

	public List<BookOwner> getSuggestedBooks(int userId) {
		List<BookOwner> flags = bookOwnerDao.suggestedBooks(userId);
		List<BookOwner> toReturn = new ArrayList<BookOwner>();
		for (BookOwner flag : flags) {
			if (flag.getStatus() != null && !"none".equals(flag.getStatus())) {
				flag.setBook(bookDao.get(flag.getBook().getBookId()));
				flag.setUser(userDao.get(flag.getUser().getUserId()));
				toReturn.add(flag);
			}
		}

		return toReturn;
	}

	public List<BookOwner> getRecommendationByUserSimilarity(int userId) {
		List<BookOwner> flag = bookOwnerDao.allDistinctAvailable(userId);
		for (BookOwner bo : flag) {
			bo.setRate(bookOwnerRatingService.averageRatingOfBookOwner(bo.getBook_ownerId()));
		}
		performRecommendation(userId, flag);
		sortByWeight(flag);
		return flag;
	}

	public void performRecommendation(int userId, List<BookOwner> books) {
		User user = userDao.get(new Long(userId));
		for (BookOwner bo : books) {
			int genreMatch = genreMatches(user, bo.getUser());
			double weight = weightCalculation(getAge(bo.getUser().getBirthdate()), genreMatch,
					getAge(user.getBirthdate()), user.getGenres().size());
			bo.setWeight(weight);
		}
	}

	private void sortByWeight(List<BookOwner> bo) {
		Collections.sort(bo, new Comparator<BookOwner>() {

			@Override
			public int compare(BookOwner o1, BookOwner o2) {
				int flag = 0;
				if (o1.getWeight() > o2.getWeight()) {
					flag = 1;
				} else if (o1.getWeight() < o2.getWeight()) {
					flag = -1;
				} else {
					if (o1.getRate() < o2.getRate()) {
						flag = -1;
					} else if (o1.getRate() > o2.getRate()) {
						flag = 1;
					}
				}
				return flag;
			}
		});
	}

	private double weightCalculation(int userMatcherAge, int genreMatch, int userAge, int userGenres) {
		double flag = 0;

		double agePow = Math.pow(userAge - userMatcherAge, 2);
		double genrePow = Math.pow(genreMatch - userGenres, 2);
		flag = Math.sqrt(agePow + genrePow);

		System.out.println("bullcrap\t" + agePow + "\t" + genrePow + "\t" + flag);

		return flag;
	}

	private int getAge(String birthday) {
		int flag = 0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse(birthday, formatter);
		LocalDate now = LocalDate.now();
		flag = Period.between(ld, now).getYears();

		return flag;
	}

	private int genreMatches(User user1, User user2) {
		int flag = 0;

		for (Genre g1 : user1.getGenres()) {
			for (Genre g2 : user2.getGenres()) {
				if (g1.getGenreId() == g2.getGenreId()) {
					flag++;
				}
			}
		}

		return flag;
	}

	private int genreMatchesBook(User user, BookOwner book) {
		int flag = 0;

		for (Genre g1 : user.getGenres()) {
			for (Genre g2 : book.getBook().getGenres()) {
				if (g1.getGenreId() == g2.getGenreId()) {
					flag++;
				}
			}
		}

		return flag;
	}

	public List<BookOwner> mergedSuggested(int userId) {
		List<BookOwner> flags = bookOwnerDao.allDistinctAvailable(userId);

		for (BookOwner bo : flags) {
			bo.setRate(bookOwnerRatingService.averageRatingOfBookOwner(bo.getBook_ownerId()));
		}

		performRecommendationMerged(userId, flags);
		sortByWeight(flags);

		return flags;
	}

	public void performRecommendationMerged(int userId, List<BookOwner> books) {
		User user = userDao.get(new Long(userId));
		for (BookOwner bo : books) {
			int genreMatch = genreMatches(user, bo.getUser());
			int genreMatchesBooks = genreMatchesBook(user, bo);
			double weight = weightCalculationMerged(getAge(bo.getUser().getBirthdate()), genreMatch, genreMatchesBooks,
					getAge(user.getBirthdate()), user.getGenres().size());
			bo.setWeight(weight);
		}
	}

	private double weightCalculationMerged(int userMatcherAge, int userGenreMatch, int bookGenreMatch, int userAge,
			int userGenres) {
		double flag = 0;

		double agePow = Math.pow(userAge - userMatcherAge, 2);
		double genrePow = Math.pow(userGenreMatch - userGenres, 2);
		double bookGenrePow = Math.pow(bookGenreMatch - userGenres, 2);
		flag = Math.sqrt(agePow + genrePow + bookGenrePow);

		System.out.println("bullcrap\t" + agePow + "\t" + genrePow + "\t" + bookGenrePow + "\t" + flag);

		return flag;
	}

	public List<BookOwner> searchByGenre(String genre) {
		List<BookOwner> flags = bookOwnerDao.searchByGenre(genre);
		List<BookOwner> toReturn = new ArrayList<BookOwner>();
		for (BookOwner flag : flags) {
			if (flag.getStatus() != null && !"none".equals(flag.getStatus())) {
				flag.setBook(bookDao.get(flag.getBook().getBookId()));
				flag.setUser(userDao.get(flag.getUser().getUserId()));
				toReturn.add(flag);
			}
		}
		return toReturn;
	}

	public List<BookOwner> searchByAuthor(String author) {
		List<BookOwner> flags = bookOwnerDao.searchByAuthor(author);
		List<BookOwner> toReturn = new ArrayList<BookOwner>();
		for (BookOwner flag : flags) {
			if (flag.getStatus() != null && !"none".equals(flag.getStatus())) {
				flag.setBook(bookDao.get(flag.getBook().getBookId()));
				flag.setUser(userDao.get(flag.getUser().getUserId()));
				toReturn.add(flag);
			}
		}
		return toReturn;
	}

	public List<BookOwner> searchByUserOwner(String userOwner) {
		List<BookOwner> flags = bookOwnerDao.searchByUserOwner(userOwner);
		List<BookOwner> toReturn = new ArrayList<BookOwner>();
		for (BookOwner flag : flags) {
			if (flag.getStatus() != null && !"none".equals(flag.getStatus())) {
				flag.setBook(bookDao.get(flag.getBook().getBookId()));
				flag.setUser(userDao.get(flag.getUser().getUserId()));
				toReturn.add(flag);
			}
		}
		return toReturn;
	}

	public Set<BookActivityObject> getUserOwnBookActivities(int userId) {
		List<RentalHeader> rentalHeaders = rentalHeaderDao.getOngoingByOwner(userId);
		List<SwapHeader> swapHeaders = swapHeaderDao.getOngoingSwaps(userId);

		Set<BookActivityObject> bookActivityObjects = new TreeSet<BookActivityObject>();
		BookActivityObject baj;
		for (RentalHeader rentalHeader : rentalHeaders) {
			baj = new BookActivityObject();
			baj.setUser(rentalHeader.getUserId());
			baj.setBookOwner(rentalHeader.getRentalDetail().getBookOwner());
			baj.setBookActivityId(rentalHeader.getRentalHeaderId());
			baj.setBookStatus("rent");
			baj.setStatus(rentalHeader.getStatus());
			baj.setDateRequest(rentalHeader.getRentalTimeStamp());

			bookActivityObjects.add(baj);
		}

		for (SwapHeader swapHeader : swapHeaders) {
			baj = new BookActivityObject();
			baj.setUser(swapHeader.getUser());
			baj.setBookOwner(swapHeader.getSwapDetail().getBookOwner());
			baj.setBookStatus("swap");
			baj.setStatus(swapHeader.getStatus());
			baj.setBookActivityId(swapHeader.getSwapHeaderId());
			baj.setDateRequest(swapHeader.getDateTimeStamp());

			bookActivityObjects.add(baj);
		}

		return bookActivityObjects;
	}

	public Set<BookActivityObject> getUserRequestsBookActivities(int userId) {
		List<RentalHeader> rentalHeaders = rentalHeaderDao.getOngoingRequestsByUser(userId);
		List<SwapHeader> swapHeaders = swapHeaderDao.getOngoingSwapRequestsByUser(userId);

		Set<BookActivityObject> bookActivityObjects = new TreeSet<BookActivityObject>();
		BookActivityObject baj;
		for (RentalHeader rentalHeader : rentalHeaders) {
			baj = new BookActivityObject();
			baj.setUser(rentalHeader.getUserId());
			baj.setBookOwner(rentalHeader.getRentalDetail().getBookOwner());
			baj.setBookActivityId(rentalHeader.getRentalHeaderId());
			baj.setBookStatus("rent");
			baj.setStatus(rentalHeader.getStatus());
			baj.setDateRequest(rentalHeader.getRentalTimeStamp());

			bookActivityObjects.add(baj);
		}

		for (SwapHeader swapHeader : swapHeaders) {
			baj = new BookActivityObject();
			baj.setUser(swapHeader.getUser());
			baj.setBookOwner(swapHeader.getSwapDetail().getBookOwner());
			baj.setBookStatus("swap");
			baj.setStatus(swapHeader.getStatus());
			baj.setBookActivityId(swapHeader.getSwapHeaderId());
			baj.setDateRequest(swapHeader.getDateTimeStamp());

			bookActivityObjects.add(baj);
		}

		return bookActivityObjects;
	}

	public List<BookOwner> searchBookOwner(String searchKey) {
		List<BookOwner> searchResult = bookOwnerDao.searchBookOwner(searchKey);
		List<BookOwner> toReturn = new ArrayList<BookOwner>();
		for (BookOwner bo : searchResult) {
			if (bo.getStatus() != null && !"none".equals(bo.getStatus())
					&& bookOwnerDao.isCurrentlyAvailableForRent(bo.getBook_OwnerId())) {
				bo.setRate(bookOwnerRatingService.averageRatingOfBookOwner(bo.getBook_OwnerId()));
				toReturn.add(bo);
			}
		}
		return toReturn;
	}
}
