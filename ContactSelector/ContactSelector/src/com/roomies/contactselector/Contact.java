package com.roomies.contactselector;

import java.io.InputStream;

public class Contact {

	private String firstName;
	private String lastName;
	private String phoneNumberOffice;
	private String phoneNumberHome;
	private InputStream thumbnail;
	private long _id;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public InputStream getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(InputStream thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumberOffice() {
		return phoneNumberOffice;
	}

	public void setPhoneNumberOffice(String phoneNumberOffice) {
		this.phoneNumberOffice = phoneNumberOffice;
	}

	public String getPhoneNumberHome() {
		return phoneNumberHome;
	}

	public void setPhoneNumberHome(String phoneNumberHome) {
		this.phoneNumberHome = phoneNumberHome;
	}

	@Override
	public String toString() {
		return "Contact [firstName=" + firstName + ", lastName=" + lastName
				+ ", phoneNumberOffice=" + phoneNumberOffice
				+ ", phoneNumberHome=" + phoneNumberHome + ", thumbnail="
				+ thumbnail + ", _id=" + _id + "]";
	}


}
