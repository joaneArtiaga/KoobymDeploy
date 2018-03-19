package com.koobym.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "book_owner")
public class BookOwner {

	@Id
	@Column(name = "book_ownerId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long book_ownerId;

	@Column(name = "statusDescription")
	private String statusDescription;

	@Column(name = "dateBought")
	private String dateBought;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "bookId")
	private Book book;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	private User user;

	@Column(name = "noRenters")
	private int noRenters;

	@Column(name = "status")
	private String status;

	@Column(name = "bookStat")
	private String bookStat;

	@Transient
	private double rate;

	@Transient
	@JsonInclude
	private double weight;

	@Transient
	private int matches;

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getBookStat() {
		return bookStat;
	}

	public void setBookStat(String bookStat) {
		this.bookStat = bookStat;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setBook_ownerId(long book_ownerId) {
		this.book_ownerId = book_ownerId;
	}

	public void setNoRenters(int noRenters) {
		this.noRenters = noRenters;
	}

	public long getBook_ownerId() {
		return book_ownerId;
	}

	public int getNoRenters() {
		return noRenters;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return this.book;
	}

	public void setBook_OwnerId(long book_ownerId) {
		this.book_ownerId = book_ownerId;
	}

	public long getBook_OwnerId() {
		return this.book_ownerId;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getStatusDescription() {
		return this.statusDescription;
	}

	public void setDateBought(String dateBought) {
		this.dateBought = dateBought;
	}

	public String getDateBought() {
		return this.dateBought;
	}

}
