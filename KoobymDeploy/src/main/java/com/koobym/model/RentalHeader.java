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

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "rental_header")
public class RentalHeader {

	@Id
	@Column(name = "rentalHeaderId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long rentalHeaderId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	@JsonProperty(value = "user")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rentalDetailId")
	private RentalDetail rentalDetail;

	@Column(name = "rentalTimeStamp")
	private String rentalTimeStamp;

	@Column(name = "dateApproved")
	private String dateApproved;

	@Column(name = "dateReceived")
	private String dateReceived;
	
	@Column(name = "dateConfirmed")
	private String dateConfirmed;

	@Column(name = "dateRejected")
	private String dateRejected;

	@Column(name = "dateComplete")
	private String dateComplete;

	@Column(name = "dateDeliver")
	private String dateDeliver;
	
	@Column(name = "rentalEndDate")
	private String rentalEndDate;

	@Column(name = "rentalReturnDate")
	private String rentalReturnDate;
	
	@Column(name = "rentalExtraMessage")
	private String rentalExtraMessage;
	
	@Column(name = "totalPrice")
	private float totalPrice;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "locationId")
	private Location location;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "meet_upId")
	private MeetUp meetUp;

	@Column(name = "status")
	private String status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_day_timeId")
	private UserDayTime userDayTime;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "returnMeetUp")
	private MeetUp returnMeetUp;
	
	public String getDateComplete(){
		return dateComplete;
	}
	
	public void setDateComplete(String dateComplete){
		this.dateComplete = dateComplete;
	}
	
	public String getDateReceived(){
		return dateReceived;
	}
	
	public void setDateReceived(String dateReceived){
		this.dateReceived = dateReceived;
	}
	
	public String getDateRejected(){
		return dateRejected;
	}
	
	public void setDateRejected(String dateRejected){
		this.dateRejected = dateRejected;
	}
	
	public String getRentalExtraMessage(){
		return this.rentalExtraMessage;
	}
	
	public void setRentalExtraMessage(String rentalExtraMessage){
		this.rentalExtraMessage = rentalExtraMessage;
	}
	
	public String getRentalReturnDate(){
		return this.rentalReturnDate;
	}
	
	public void setRentalReturnDate(String rentalReturnDate){
		this.rentalReturnDate = rentalReturnDate;
	}

	public MeetUp getReturnMeetUp() {
		return this.returnMeetUp;
	}
	
	public void setReturnMeetUp(MeetUp returnMeetUp) {
		this.returnMeetUp = returnMeetUp;
	}
	
	public String getDateDeliver() {
		return dateDeliver;
	}

	public void setDateDeliver(String dateDeliver) {
		this.dateDeliver = dateDeliver;
	}

	public String getDateConfirmed() {
		return dateConfirmed;
	}

	public void setDateConfirmed(String dateConfirmed) {
		this.dateConfirmed = dateConfirmed;
	}

	public String getRentalEndDate() {
		return rentalEndDate;
	}

	public void setRentalEndDate(String rentalEndDate) {
		this.rentalEndDate = rentalEndDate;
	}

	public void setMeetUp(MeetUp meetUp) {
		this.meetUp = meetUp;
	}

	public MeetUp getMeetUp() {
		return meetUp;
	}

	public void setDateApproved(String dateApproved) {
		this.dateApproved = dateApproved;
	}

	public String getDateApproved() {
		return dateApproved;
	}

	public void setUserDayTime(UserDayTime userDayTime) {
		this.userDayTime = userDayTime;
	}

	public UserDayTime getUserDayTime() {
		return userDayTime;
	}

	public void setRentalDetail(RentalDetail rentalDetail) {
		this.rentalDetail = rentalDetail;
	}

	public RentalDetail getRentalDetail() {
		return rentalDetail;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setRentalHeaderId(long rentalHeaderId) {
		this.rentalHeaderId = rentalHeaderId;
	}

	public long getRentalHeaderId() {
		return rentalHeaderId;
	}

	public void setUserId(User user) {
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
