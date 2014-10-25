package com.tool.search.phone;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class PhonePullService {

	public static List<Phone> readXml(InputStream inStream){
		List<Phone> phones = null;
		Phone phone = null;
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
							phones = new ArrayList<Phone>();
							break;
						//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�  
						case XmlPullParser.START_TAG: // 2 ��ʼԪ��
							if ("product".equals(parser.getName())) {
								//��ʼ��ips����  
								phone = new Phone();
								//person.setId(new Integer(parser.getAttributeValue(0)));
							} else if (null != phone) {
								//�жϿ�ʼ��ǩԪ���Ƿ���ip
								if ("phonenum".equals(parser.getName())) {
									phone.setPhonenum(parser.nextText());
								  //�жϿ�ʼ��ǩԪ���Ƿ���location
								} else if ("location".equals(parser.getName())) {
									phone.setLocation(parser.nextText());
								}
							}
							break;
							//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�  
						case XmlPullParser.END_TAG: // ����Ԫ��
							if ("product".equals(parser.getName()) && phone != null) {
								//��ip��ӵ�ips����
								phones.add(phone);
								phone = null;
							}
							break;
						}
						//������һ��Ԫ�ز�������Ӧ�¼�  
						eventCode = parser.next();
					}
					
			} catch (IOException e) {
				// TODO: handle exception
			}
		} catch (XmlPullParserException e) {
			// TODO: handle exception
			System.out.println("22222222222");
		}
		
		return phones;
	}
}
