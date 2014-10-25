package com.tool.search.ip;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class IpSearch {

	static StringBuilder sb;
	static String baseUri = "http://www.youdao.com/smartresult-xml/search.s?type=ip&q=";
	public static String ipSearch(String str){
		
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
		    List<Ip> ips = IpPullService.readXml(in);
			
			for (Ip ip : ips) {
				sb.append(ip.toString()).append("\n");
			}	
			
			in.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();		
	}
}
