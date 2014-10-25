package com.tool.search.ip;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/*
 * Pull������Sax���������ƣ������������Ľ�������Android���ں����Ѿ�Ƕ����Pull���������ǲ���Ҫ����ӵ�����jar����֧��Pull��
 * Pull������Sax������һ���ĵط���
 * (1)pull��ȡxml�ļ�  �󴥷���Ӧ���¼����÷������ص�������
 * (2)pull�����ڳ����п��������������Ϳ���ֹͣ������
 */
public class IpPullService {
	public static List<Ip> readXml(InputStream inStream) throws Exception {
		List<Ip> ips = null;
		Ip ip = null;
		try {
			//��ȡXMLPull��������
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			//����XML PULL������
			XmlPullParser parser = factory.newPullParser();
			//����XML Pull���������������ݺͱ���
			parser.setInput(inStream, "GBK");
			//��ȡ�¼�����
			int eventCode = parser.getEventType();
			try {
				while (eventCode != XmlPullParser.END_DOCUMENT) {
					switch (eventCode) {
					//�жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼� 
					case XmlPullParser.START_DOCUMENT: // 0 �ĵ���ʼ�¼�
						ips = new ArrayList<Ip>();
						break;
					//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�  
					case XmlPullParser.START_TAG: // 2 ��ʼԪ��
						if ("product".equals(parser.getName())) {
							//��ʼ��ips����  
							ip = new Ip();
							//person.setId(new Integer(parser.getAttributeValue(0)));
						} else if (null != ip) {
							//�жϿ�ʼ��ǩԪ���Ƿ���ip
							if ("ip".equals(parser.getName())) {
								ip.setIp(parser.nextText());
							  //�жϿ�ʼ��ǩԪ���Ƿ���location
							} else if ("location".equals(parser.getName())) {
								ip.setLocation(parser.nextText());
							}
						}
						break;
						//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�  
					case XmlPullParser.END_TAG: // ����Ԫ��
						if ("product".equals(parser.getName()) && ip != null) {
							//��ip��ӵ�ips����
							ips.add(ip);
							ip = null;
						}
						break;
					}
					//������һ��Ԫ�ز�������Ӧ�¼�  
					eventCode = parser.next();
				}
			} catch (IOException e) {
				// TODO: handle exception
				System.out.println("11111111111111");
			}
		} catch (XmlPullParserException e) {
			// TODO: handle exception
			System.out.println("2222222222222");
		}		
		return ips;
	}
}
