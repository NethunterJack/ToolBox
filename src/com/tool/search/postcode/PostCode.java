package com.tool.search.postcode;

public class PostCode {

	private String province;
	private String city;
	private String location;
	private String phone;
	private String zipcode;
	public String getProvince() {
		return province;
	}
	public String getCity() {
		return city;
	}
	public String getLocation() {
		return location;
	}
	public String getPhone() {
		return phone;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String toString(){
		return "ʡ��:"+this.province+"\r\n"+
				"����:"+this.city+"\r\n"+
				"����:"+this.phone+"\r\n"+
				"�ʱ�:"+this.zipcode+"\r\n"+
				"������:"+this.location+"\r\n";
	}
}
