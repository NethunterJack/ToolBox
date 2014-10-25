package com.tool.search.id;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.tool.common.ActivityUtils;

/*
 * Pull������Sax���������ƣ������������Ľ�������Android���ں����Ѿ�Ƕ����Pull���������ǲ���Ҫ����ӵ�����jar����֧��Pull��
 * Pull������Sax������һ���ĵط���
 * (1)pull��ȡxml�ļ�  �󴥷���Ӧ���¼����÷������ص�������
 * (2)pull�����ڳ����п��������������Ϳ���ֹͣ������
 */
public class IDcardPullService {
	public static List<IDCard> readXml(InputStream inStream) {
		List<IDCard> ids = null;
		IDCard id = null;
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
						ids = new ArrayList<IDCard>();
						break;
					//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�  
					case XmlPullParser.START_TAG: // 2 ��ʼԪ��
						if ("product".equals(parser.getName())) {
							//��ʼ��IDCards����  
							id = new IDCard();
							//person.setId(new Integer(parser.getAttributeValue(0)));
						} else if (null != id) {
							//�жϿ�ʼ��ǩԪ���Ƿ���IDCard
							if ("code".equals(parser.getName())) {
								id.setCode(parser.nextText());
							  //�жϿ�ʼ��ǩԪ���Ƿ���location
							}else if ("location".equals(parser.getName())) {
								id.setLocation(parser.nextText());
							}else if ("birthday".equals(parser.getName())) {
								id.setBirthday(parser.nextText());
							}else if ("gender".equals(parser.getName())) {
								id.setGender(parser.nextText().equals("m") ? "��" : "Ů");
//								if (parser.nextText()=="m") {
//									id.setGender("��");
//								} else {
//									id.setGender("Ů");
//								}						
							}
						}
						break;
						//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�  
					case XmlPullParser.END_TAG: // ����Ԫ��
						if ("product".equals(parser.getName()) && id != null) {
							//��IDCard��ӵ�IDCards����
							ids.add(id);
							id = null;
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
			System.out.println("22222222222");
		}
		
       return ids;
		
	}
}
