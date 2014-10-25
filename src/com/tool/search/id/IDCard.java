package com.tool.search.id;

import java.io.Serializable;

public class IDCard implements Serializable{

	private String code;
	private String location;
	private String birthday;
	private String gender;
	
	public String getCode(){
		return code;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public String getBirthday(){
		return birthday;
	}
	
	public void setBirthday(String birthday){
		this.birthday = birthday;
	}
	
	public String getGender(){
		return gender;
	}
	
	public void setGender(String gender){
		this.gender = gender;
	}
	
	public String toString(){
		return "���֤��:"+this.code+"\r\n"+
	            "��ַ:"+this.location+"\r\n"+
				"��������:"+this.birthday+"\r\n"+
	            "�Ա�:"+this.gender+"\r\n";
	}
}
