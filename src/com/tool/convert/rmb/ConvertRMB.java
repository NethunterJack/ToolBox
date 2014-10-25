package com.tool.convert.rmb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tool.R;
import com.tool.common.ActivityUtils;

public class ConvertRMB extends Activity {

	private ImageView convertBtn;
	private EditText rmbTx;
	private TextView conertResultTx;
	private static final int MESSAGETYPE = 101;
    private ProgressDialog progressDialog = null; 
    String resultData;
    StringBuilder sb;
    StringBuffer stringBuffer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.convert_rmb);
		convertBtn = (ImageView)findViewById(R.id.convert_search_btn);
        rmbTx = (EditText)findViewById(R.id.rmb_tx);
        rmbTx.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        conertResultTx = (TextView)findViewById(R.id.convert_result);
        
        convertBtn.setOnClickListener(new ConvertURLListener());
	}	
	
	class ConvertURLListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			resultData="";
			String str=ActivityUtils.deleteSpace(rmbTx.getText().toString());
			System.out.println(str+"--------->");
			// TODO Auto-generated method stub
			if(ActivityUtils.validateNull(str)){
//				try {
//					Integer.parseInt(str);
//					convertRMB(str);
//				} catch (Exception e) {
//					// TODO: handle exception
//					ActivityUtils.showDialog(ConvertRMB.this, "ȷ��", "��ʾ", "����Ĳ���ȷ");
//				}
				convertRMB(str);
			}else {
				
				ActivityUtils.showDialog(ConvertRMB.this, "ȷ��", "��ʾ", "������������Ŀ����Ϊ��");
			}			
		}
	}
	
    public void convertRMB(final String str) {
    	
		final String baseUri = "http://api.liqwei.com/currency/?exchange=CNY|USD&count=";
		progressDialog = ProgressDialog.show(ConvertRMB.this, "ת��", "����ת��,���Ժ�......");  
			 new Thread() {
	                public void run() {                        
	                	String readline = "";
	        			try {
	        				URL url = new URL(baseUri+str);
	        				Log.i("TAG", url.toString());
	        				//ʹ��HttpURLConnection������  
	        				HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection(); 
	        				InputStreamReader is = new InputStreamReader(
	        						httpConnection.getInputStream());
            				BufferedReader br = new BufferedReader(is);

            				while (((readline = br.readLine()) != null)) {

            					resultData += readline + "\n";// ע�⣺�洢����ʹ�õ���ȫ�ֱ�������Ϊ��handler��ҲҪʹ�õ�
            				}
            				resultData ="����ĵõ�����ԪΪ:\n"+resultData;
	        				// ��Դ�����������Ҫ��������ʾ�����ݴ�������Ϣ��Handler
	        				Message msg_listData = new Message();
	                        msg_listData.what = MESSAGETYPE;
	                        msg_listData.obj = resultData;
	                        idHandler.sendMessage(msg_listData);
	                        
	        				// �ر���Դ
	                        br.close();
	                        is.close();
	        				httpConnection.disconnect();
	        			} catch (Exception e) {
	        				throw new RuntimeException(e.getMessage(),e);
	        			}                                                      
	                }
	        }.start();
		} 

      Handler idHandler = new Handler() {                
    	
        public void handleMessage(Message message) {
        	    
        	    if (message.obj=="") {
            		if(progressDialog.isShowing()){
            			progressDialog.dismiss(); //�رս�����
            			showExcetionDialog("ע��", "��ȡ���ݳ��ִ���\n"+"�����������Ƿ���ȷ");         			
            		}
               		return;
    			}
                switch (message.what) {
                case MESSAGETYPE:                                        
                	    conertResultTx.setText(resultData);                
                        progressDialog.dismiss(); //�رս�����
                        break;
                }
            }
        };
        
        public void showExcetionDialog(String title,String message){

    		AlertDialog dialog;
    		AlertDialog.Builder builder = new AlertDialog.Builder(ConvertRMB.this);
    		builder.setTitle(title);
    		builder.setMessage(message);
    		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				conertResultTx.setText("û�в�ѯ����ؽ��");
    			}					
    		});
    		dialog = builder.create();
    		dialog.show();
       }
}
