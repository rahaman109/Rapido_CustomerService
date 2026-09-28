package com.alpha.customerservice.responsedto;

public class CustomerResponseDto {

	private int customerId;
	private String name;
	private long mobile;
	private String mail;

	public CustomerResponseDto(int customerId, String name, long mobile, String mail) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.mobile = mobile;
		this.mail = mail;
	}

	@Override
	public String toString() {
		return "CustomerResponseDto []";
	}

	public CustomerResponseDto() {
		super();
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
