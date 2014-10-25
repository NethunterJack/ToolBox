package com.tool.search.phone;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PhoneSearch {

	static StringBuilder sb;
	static String baseUri = "http://www.youdao.com/smartresult-xml/search.s?type=mobile&q=";
	public static String phoneSearch(String str){
		
		try {
			sb =new StringBuilder("");
			//ʵ����URL
			URL url = new URL(baseUri+str);
			//ʹ��HttpURLConnection������  
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection(); 
			httpConnection.setRequestMethod("GET"); 
			httpConnection.setRequestProperty("Charset", "GBK");
			//�õ�����������
		    InputStream in = httpConnection.getInputStream();
		    List<Phone> phones = PhonePullService.readXml(in);
			
			for (Phone phone : phones) {
				sb.append(phone.toString()).append("\n");
			}	
			
			in.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();		
	}
}
