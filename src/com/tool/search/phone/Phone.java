package com.tool.search.phone;

import java.io.Serializable;

public class Phone implements Serializable{

	private String phonenum;
	private String location;
	
	public String getPhonenum(){
		return phonenum;
	}
	
	public void setPhonenum(String phonenum){
		this.phonenum = phonenum;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public String toString(){
		return "�ֻ�����:"+this.phonenum+"\r\n"+
	            "��������أ�"+this.location+"\r\n";
	}
}
