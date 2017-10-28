package com.sh.messages;

public class ShopMsg {
	
	private String shopName;
	
	private String city;
	
	private int sales;
	
	
	
	public ShopMsg() {
		super();
		
	}
	public ShopMsg(String shopName, String city, int sales) {
		super();
		this.shopName = shopName;
		this.city = city;
		this.sales = sales;
		
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getSales() {
		return sales;
	}
	public void setSales(int sales) {
		this.sales = sales;
	}
	
	
	
	
	
	
	

}
