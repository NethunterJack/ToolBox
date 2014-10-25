package com.tool.search.creditcard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class CreditCardPullService {

	public static List<CreditCard> readXml(InputStream inStream){
		List<CreditCard> cards = null;
		CreditCard card = null;
		try {
			//��ȡXMLPull��������
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			//����XML PULL������
			XmlPullParser parser = factory.newPullParser();
			//����XML Pull���������������ݺͱ���
			parser.setInput(inStream, "UTF-8");
			//��ȡ�¼�����
			int eventCode = parser.getEventType();
			try {
				while (eventCode != XmlPullParser.END_DOCUMENT) {
					switch (eventCode) {
					//�жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼� 
					case XmlPullParser.START_DOCUMENT: // 0 �ĵ���ʼ�¼�
						cards = new ArrayList<CreditCard>();
						break;
					//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�  
					case XmlPullParser.START_TAG: // 2 ��ʼԪ��
						if ("bankinfo".equals(parser.getName())) {
							//��ʼ��cardCards����  
							card = new CreditCard();
							//person.setcard(new Integer(parser.getAttributeValue(0)));
						} else if (null != card) {
							//�жϿ�ʼ��ǩԪ���Ƿ���cardCard
							if ("status".equals(parser.getName())) {
								
							  //�жϿ�ʼ��ǩԪ���Ƿ���location
							}else if ("cardNum".equals(parser.getName())) {
								card.setCardNum(parser.nextText());
							}else if ("area".equals(parser.getName())) {
								card.setArea(parser.nextText());
							}else if ("bank".equals(parser.getName())) {
								card.setBank(parser.nextText());
							}else if ("cardType".equals(parser.getName())) {
								card.setCardType(parser.nextText());				
							}else if ("cardName".equals(parser.getName())) {
								card.setCardName(parser.nextText());				
							}
						}
						break;
						//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�  
					case XmlPullParser.END_TAG: // ����Ԫ��
						if ("bankinfo".equals(parser.getName()) && card != null) {
							//��cardCard��ӵ�cardCards����
							cards.add(card);
							card = null;
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
		return cards;
	}
}
