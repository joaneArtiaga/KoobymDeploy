package com.koobym.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private User user;

	@Column(name = "noRenters")
	private int noRenters;
	
	
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
