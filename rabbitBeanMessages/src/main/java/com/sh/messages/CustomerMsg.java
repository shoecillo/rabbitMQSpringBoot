package com.sh.messages;

public class CustomerMsg{
	
	private String custName;
	private String custOccupation;
	private String description;
	
	public CustomerMsg() {
		super();
		
	}
	
	public CustomerMsg(String custName, String custOccupation, String description) {
		super();
		this.custName = custName;
		this.custOccupation = custOccupation;
		this.description = description;
	}

	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustOccupation() {
		return custOccupation;
	}
	public void setCustOccupation(String custOccupation) {
		this.custOccupation = custOccupation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	

}
