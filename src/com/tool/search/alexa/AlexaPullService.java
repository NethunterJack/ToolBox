package com.tool.search.alexa;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public class AlexaPullService {

	public static List<Alexa> readXml(InputStream inStream) throws Exception {
		List<Alexa> alexas = null;
		boolean first = true;
		//��ȡXMLPull��������
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		//����XML PULL������
		XmlPullParser parser = factory.newPullParser();
		//����XML Pull���������������ݺͱ���
		parser.setInput(inStream, "UTF-8");
		//��ȡ�¼�����
		int eventCode = parser.getEventType();
		Alexa alexa = null;
		while (eventCode != XmlPullParser.END_DOCUMENT) {
			switch (eventCode) {
			//�жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼� 
			case XmlPullParser.START_DOCUMENT: // 0 �ĵ���ʼ�¼�
				alexas = new ArrayList<Alexa>();
				break;
			//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�  
			case XmlPullParser.START_TAG: // 2 ��ʼԪ��
				if ("SD".equals(parser.getName()) && first ) {
					//��ʼ��ips����  
					alexa = new Alexa();
					first = false;
				} else if (null != alexa) {
					//�жϿ�ʼ��ǩԪ���Ƿ���ip
					if ("TITLE".equals(parser.getName())) {			
					    alexa.setTitle(parser.getAttributeValue(0));				
					}else if("SPEED".equals(parser.getName())){
						alexa.setMsSpeed(parser.getAttributeValue(0));
						alexa.setSecond(parser.getAttributeValue(1));
					}else if ("LINKSIN".equals(parser.getName())) {			
					    alexa.setReferrers(parser.getAttributeValue(0));				
					}else if ("POPULARITY".equals(parser.getName())) {
						alexa.setRank(parser.getAttributeValue(1));
					}
							
				}
				break;
				//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�  
			case XmlPullParser.END_TAG: // ����Ԫ��
				if ("ALEXA".equals(parser.getName()) && alexa != null) {
					//��ip��ӵ�ips����
					alexas.add(alexa);
					alexa = null;
				}
				break;
			}
			//������һ��Ԫ�ز�������Ӧ�¼�  
			eventCode = parser.next();
		}
		return alexas;
	}
}
