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

@Entity
@Table(name="rental_header")
public class RentalHeader {
	
	@Id
	@Column(name = "rentalHeaderId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long rentalHeaderId;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private User user;
		
	@Column(name="rentalTimeStamp")
	private String rentalTimeStamp;

	@Column(name="totalPrice")
	private float totalPrice; 
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "locationId")
	private Location location;
	
	public void setRentalHeaderId(long rentalHeaderId) {
		this.rentalHeaderId = rentalHeaderId;
	}
	
	public long getRentalHeaderId() {
		return rentalHeaderId;
	}
	
	public void setUserId(User user){
		this.user = user;
	}
	
	public User getUserId() {
		return user;
	}
	
	public void setRentalTimeStamp(String rentalTimeStamp) {
		this.rentalTimeStamp = rentalTimeStamp;
	}
	
	public String getRentalTimeStamp() {
		return rentalTimeStamp;
	}
	
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public float getTotalPrice() {
		return totalPrice;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
	
	

}
