package com.tool.convert.ascii;

import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.tool.MainActivity;
import com.tool.R;
import com.tool.common.ActivityUtils;
/*
 * ASCIIת����Activity
 */
public class ConvertASCII extends Activity {
    private Button okBtn,cancleBtn;
    private EditText chText,resultText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.convert_ascii);
		okBtn = (Button)findViewById(R.id.ascii_ok);
		cancleBtn = (Button)findViewById(R.id.ascii_cancle);
		chText = (EditText)findViewById(R.id.ascii_tx);
		resultText = (EditText)findViewById(R.id.ascii_result);
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				String str=chText.getText().toString(); 
				StringBuffer strBuffer = new StringBuffer();
				//����ActivityUtils�ľ�̬�����ж�������ַ��ǹ�Ϊ��
				 if (ActivityUtils.validateNull(str)) {
					//����ActivityUtils�ľ�̬�����ж�������Ƿ�Ϊ�ַ�
					if (ActivityUtils.valiChar(str)) {
						StringTokenizer s = new StringTokenizer(str, ",");
						if (str.length()<2) {
		                    byte[] cc = str.getBytes(); 
			                int a=(int)cc[0];
							strBuffer.append(String.valueOf(a)+" ");
						} else {
							while(s.hasMoreTokens()){
								String string = s.nextToken();
			                    byte[] cc = string.getBytes(); 
				                int a=(int)cc[0];
								strBuffer.append(String.valueOf(a)+" ");					
							}
						}						
					} else {
						ActivityUtils.showDialog(ConvertASCII.this, "ȷ��", "��ʾ", "������ַ�����ȷ");
					}
					
				} else {
					ActivityUtils.showDialog(ConvertASCII.this, "ȷ��", "��ʾ", "������ַ�����Ϊ��");
				}

				resultText.setText(strBuffer);
			}
		});
		
		//����������ݵİ�ť
        cancleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chText.setText("");
			}
		});
	}	
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		 if (keyCode == KeyEvent.KEYCODE_BACK){
			 Intent intent = new Intent(ConvertASCII.this, MainActivity.class);
			 startActivity(intent);
			 ConvertASCII.this.finish();
		 }
		 return false;
	 }
}
