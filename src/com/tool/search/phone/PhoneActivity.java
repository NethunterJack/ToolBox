package com.tool.search.phone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tool.BaseActivity;
import com.tool.MainActivity;
import com.tool.QueryTool;
import com.tool.R;
import com.tool.XyCallBack;
import com.tool.common.ActivityUtils;
import com.tool.common.AppManager;
import com.tool.common.LogManager;
import com.tool.common.ToastManager;
import com.tool.common.ToolGuide;
import com.tool.menu.CustomMenu;

@TargetApi(5)
public class PhoneActivity extends BaseActivity {

	PhoneActivity ph;
	private static CustomMenu customMenu;
	private PhoneDBAdapter db;
	private Cursor cursor;
	private static final int DELETE = Menu.FIRST;
	private static final int SAVETOSD = Menu.FIRST+1;
	private static final int ABOUT= Menu.FIRST+2;
	private ImageView phoneQueryBtn,homeBtn,rebackBtn; 
	private EditText phoneEdit;
	private TextView queryTitile,phoneResultTxView;
	private static final int MESSAGETYPE = 101;
    private ProgressDialog progressDialog = null; 
    String fileNameStr,titleNameStr;
    StringBuilder sb;
    StringBuffer stringBuffer;
    String[] string;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.query_item);
		ph = this;
		phoneQueryBtn = (ImageView)findViewById(R.id.query_search_btn);
		homeBtn = (ImageView)findViewById(R.id.home);
		rebackBtn = (ImageView)findViewById(R.id.reback);
		phoneEdit = (EditText)findViewById(R.id.query_search_content);
		queryTitile = (TextView)findViewById(R.id.query_tool);
		phoneResultTxView = (TextView)findViewById(R.id.textView);
		phoneEdit.setHint(R.string.phone_hint);
		queryTitile.setText(R.string.query_phone);
		stringBuffer = new StringBuffer();
		db = new PhoneDBAdapter(ph).open();
		cursor = db.getAllTitles();
		string = new String[2];
		
		homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PhoneActivity.this, MainActivity.class);
				startActivity(intent);
				PhoneActivity.this.finish();
			}
		});
		
		rebackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PhoneActivity.this, QueryTool.class);
				startActivity(intent);
				PhoneActivity.this.finish();
			}
		});
		
		phoneQueryBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str = phoneEdit.getText().toString().trim();
				if(ActivityUtils.isNetworkAvailable(PhoneActivity.this)==false){
					//Toast.makeText(PhoneActivity.this, "��ȷ�������Ƿ��Ѿ�����",Toast.LENGTH_LONG).show();
					ToastManager.showToastView(PhoneActivity.this, "��ȷ�������Ƿ��Ѿ�����");
				}
				else if(ActivityUtils.isNetworkAvailable(PhoneActivity.this)==true){
					if(ActivityUtils.validateNull(str)){
						if(str.length()==11){
							queryPhoneData(str);
						}else {
							ActivityUtils.showDialog(PhoneActivity.this, "ȷ��", "��ʾ", "��������ȷ��11λ�ֻ���");
						}
												
					}else{
						
		    			ActivityUtils.showDialog(PhoneActivity.this, "ȷ��", "��ʾ", "������ֻ��Ų���Ϊ��");
		    		}				 
			    }
			}
		});
	}
	
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, DELETE, 1, "�������");
//		menu.add(0, SAVETOSD, 2, "��������");
//		menu.add(0, ABOUT, 3, "�����ֻ�����");
//		
//		return super.onCreateOptionsMenu(menu);
//	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_MENU==keyCode){
			if (!menuDimiss()) {
				popMenuWindow();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
	public boolean menuDimiss() {
		if (customMenu != null && customMenu.isShowing()) {
			customMenu.dismiss();
			return true;
		}
		return false;
	}
	
	public void popMenuWindow() {

		customMenu = new CustomMenu(this, R.style.dialog,menuCallBack,0,cursor);			
		customMenu.showDialog(R.layout.menu, 0, 0);	

	}
	
	XyCallBack menuCallBack = new XyCallBack() {
		@Override
		public void execute(Object... obj) {
			if (obj != null && obj.length == 1) {
				Integer pos = (Integer) obj[0];					
					clickMenu(pos);
				
			}
		}
	};
	
	public void clickMenu(int pos) {
		switch (pos) {
		case 0:
			//��ղ�ѯ����
			phoneEdit.setText("");
			phoneResultTxView.setText("");
			menuDimiss();
			break;
		case 1:
		    //�������ݵĴ���		
			showSaveDialog();
			LogManager.d("debug", "showSaveDialog");
			menuDimiss();
			break;
		case 2:
		    //����ղصĴ���	
			
			menuDimiss();
			store();
			break;
		
		case 3:
			//�鿴�ղصĴ���
			menuDimiss();
			Intent intent = new Intent(PhoneActivity.this, PhoneStoreList.class);
			startActivity(intent);	
			break;
		
		default:
			break;
		}
	}
		
	
	public void queryPhoneData(final String str) {
        
		final String baseUri = "http://www.youdao.com/smartresult-xml/search.s?type=mobile&q=";
		progressDialog = ProgressDialog.show(PhoneActivity.this, "��ѯ", "���ڲ�ѯ,���Ժ�......");  
			 new Thread() {
	                public void run() {                        
	                	//String readline = "";
	        			try {
	        				
	        			    sb = new StringBuilder("");
	        				URL url = new URL(baseUri+str);
	        				Log.i("TAG", url.toString());
	        				//ʹ��HttpURLConnection������  
	        				HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection(); 
	        				httpConnection.setRequestMethod("GET"); 
	        				httpConnection.setRequestProperty("Charset", "GBK");
	        				
	        				//�õ�����������
	        			    InputStream in = httpConnection.getInputStream();
	        			    List<Phone> phones = PhonePullService.readXml(in);
	        			    System.out.println(String.valueOf(phones)+"=========>");
	        			    
	        				for (Phone phone : phones) {
	        					sb.append(phone.toString()).append("\n");
	        					LogManager.d("debug", "========"+phone.getPhonenum()+"==========="+phone.getLocation());
	        					string[0]=phone.getPhonenum();
	        					string[1]=phone.getLocation();
	        				}
	        				
	        				// ��Դ�����������Ҫ��������ʾ�����ݴ�������Ϣ��Handler
	        				Message msg_listData = new Message();
	                        msg_listData.what = MESSAGETYPE;
	                        msg_listData.obj = String.valueOf(phones);
	                        idHandler.sendMessage(msg_listData);

	        				// �ر���Դ
	        				in.close();
	        				httpConnection.disconnect();
	        			} catch (Exception e) {
	        				ActivityUtils.showDialog(PhoneActivity.this, "ȷ��", "��ʾ", "������ֻ��Ų���Ϊ��");
	        			}                                                      
	                }
	        }.start();
		} 

      Handler idHandler = new Handler() {                

        public void handleMessage(Message message) {
        	if (message.obj=="[]" || message.obj==null) {
        		if(progressDialog.isShowing()){
        			progressDialog.dismiss(); //�رս�����     			
        		}
        		showExcetionDialog("ע��", "��ȡ���ݳ��ִ���\n"+"������������Ƿ���ȷ");    
           		return;
			}
            switch (message.what) {
            case MESSAGETYPE:
				phoneResultTxView.setText(sb.toString());          	                 
                progressDialog.dismiss(); //�رս�����
                break;
            }
        }
    };
        
//    public boolean onOptionsItemSelected(MenuItem item) {
//		switch(item.getItemId()){
//			case DELETE:{
//				phoneEdit.setText("");
//				phoneResultTxView.setText("");
//				break;
//			}
//			case SAVETOSD:{
//			    showSaveDialog();			
//				break;
//			}	
//			case ABOUT:{
//				ActivityUtils.showDialog(PhoneActivity.this, "ȷ��", "����", 
//						                 getResources().getString(R.string.about_phone));
//				break;
//			}
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
    public void saveToSDCard(String str,String fileName,String titleName) throws Exception{ 
				
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
        	File file = new File(Environment.getExternalStorageDirectory(),fileName+".txt");
            try {
            		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");      
                	String date = sDateFormat.format(new Date());  
					String message = "����:\r"+titleName+"\r\n"+"��ѯʱ��:\t"+date+"\r\n"+str+"\n";
                    FileOutputStream outStream = new FileOutputStream(file,true);
                    outStream.write(message.getBytes());
                    outStream.close();         
                    ToastManager.showToastView(PhoneActivity.this, "д���ļ��ɹ�");
            } catch (Exception e) {
            	ToastManager.showToastView(PhoneActivity.this, "д���ļ�ʧ��");
            }
        } else {
            // ��ʱSDcard�����ڻ��߲��ܽ��ж�д������
        	ToastManager.showToastView(PhoneActivity.this,
                    "��ʱSDcard�����ڻ��߲��ܽ��ж�д����");
        }
     }
    
    private void showSaveDialog() {
    	
//		LayoutInflater factory = LayoutInflater.from(PhoneActivity.this);
//		final View view = factory.inflate(R.layout.save_result_dialog, null);// ����Զ���Ի���
//		final EditText fileName = (EditText)view.findViewById(R.id.file_name);
//		final EditText titleName = (EditText)view.findViewById(R.id.title_name);
//
//		AlertDialog.Builder saveResultDialog = new AlertDialog.Builder(
//				PhoneActivity.this);
//		saveResultDialog.setIcon(android.R.drawable.ic_dialog_info);
//		saveResultDialog.setTitle("�����ѯ���");
//		saveResultDialog.setView(view);
//		saveResultDialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				fileNameStr = fileName.getText().toString();
//				titleNameStr = titleName.getText().toString();
//				System.out.println(String.valueOf(fileNameStr.length()));
//				if (phoneResultTxView.getText().toString()!="") {
//					if (fileNameStr.length() != 0 && titleNameStr.length()!= 0) {					
//						try {
//							saveToSDCard(phoneResultTxView.getText().toString(),fileNameStr,titleNameStr);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					} else {
//						ActivityUtils.showDialog(PhoneActivity.this,"ȷ��", "��ʾ","������ļ����ͱ��ⶼ����Ϊ��");
//					}				
//				} else {
//					
//					ActivityUtils.showDialog(PhoneActivity.this,"ȷ��", "��ʾ", "û����Ҫ���������");
//				}			
//			}
//		});
//		saveResultDialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				// Toast.makeText(dialog_demo.this, "��ѡ����ȡ��",
//				// Toast.LENGTH_LONG).show();
//			}
//		});
//		saveResultDialog.create().show();
		
		
		final Dialog saveResultDialog = new Dialog(PhoneActivity.this,
				R.style.dialog_style);
		//lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		saveResultDialog.setContentView(R.layout.save_result_dialog);
		((TextView) saveResultDialog.findViewById(R.id.dialog_title)).setText("�����ѯ���");
		final EditText fileName = (EditText)saveResultDialog.findViewById(R.id.file_name);
		final EditText titleName = (EditText)saveResultDialog.findViewById(R.id.title_name);
		
		((Button) saveResultDialog.findViewById(R.id.dialog_button_cancle))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// write your code to do things after users clicks CANCEL
						saveResultDialog.dismiss();
						
					}
				});
		((Button) saveResultDialog.findViewById(R.id.dialog_button_ok))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// write your code to do things after users clicks OK
						
						saveResultDialog.dismiss();
						fileNameStr = fileName.getText().toString();
						titleNameStr = titleName.getText().toString();
						System.out.println(String.valueOf(fileNameStr.length()));
						if (phoneResultTxView.getText().toString()!="") {
							if (fileNameStr.length() != 0 && titleNameStr.length()!= 0) {					
								try {
									saveToSDCard(phoneResultTxView.getText().toString(),fileNameStr,titleNameStr);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								ActivityUtils.showDialog(PhoneActivity.this,"ȷ��", "��ʾ","������ļ����ͱ��ⶼ����Ϊ��");
							}				
						} else {
							
							ActivityUtils.showDialog(PhoneActivity.this,"ȷ��", "��ʾ", "û����Ҫ���������");
						}			
					}
				});
		saveResultDialog.show();
       }
    
    public boolean onKeyDown(int keyCode, KeyEvent event){
  		 if (keyCode == KeyEvent.KEYCODE_BACK){
  			 Intent intent = new Intent(PhoneActivity.this, QueryTool.class);
  			 startActivity(intent);
  			 PhoneActivity.this.finish();
  		 }
  		 return false;
  	 }
    
    public void showExcetionDialog(String title,String message){

//		AlertDialog dialog;
//		AlertDialog.Builder builder = new AlertDialog.Builder(PhoneActivity.this);
//		builder.setTitle(title);
//		builder.setMessage(message);
//		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				phoneResultTxView.setText("û�в�ѯ����ؽ��");
//			}					
//		});
//		dialog = builder.create();
//		dialog.show();
    	

 	   final Dialog aboutAuthorDialog = new Dialog(PhoneActivity.this,
 				R.style.dialog_style);
 		//lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 	   aboutAuthorDialog.setContentView(R.layout.about_dialog);
 		((TextView) aboutAuthorDialog.findViewById(R.id.dialog_title)).setText(title);
 		((TextView) aboutAuthorDialog.findViewById(R.id.dialog_message)).setText(message);
 		
 		((Button) aboutAuthorDialog.findViewById(R.id.dialog_button_ok))
 				.setOnClickListener(new OnClickListener() {

 					@Override
 					public void onClick(View v) {
 						// write your code to do things after users clicks CANCEL
 						aboutAuthorDialog.dismiss();
 						phoneResultTxView.setText("û�в�ѯ����ؽ��");
 					}
 				});
 		aboutAuthorDialog.show();
   }
    
    /** �ղ� */
	private void store() {
		
		LogManager.i("debug", "=========="+string[0]+"===="+string[1]);
		if (string[1] != null && phoneResultTxView.getText()!="û�в�ѯ����ؽ��") {
			
			if (cursor.moveToFirst()) {
				do {
					if (string[0].equals(cursor.getString(cursor
							.getColumnIndex(PhoneDBAdapter.KEY_MOBILE)))) {
						ToastManager.showToastView(PhoneActivity.this, "��Ϣ�Ѵ��ڣ�����Ҫ����");
						return;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.insertTitle(string);
			ToastManager.showToastView(PhoneActivity.this, "����ɹ�");
		} else {
			ToastManager.showToastView(PhoneActivity.this, "û����Ϣ��Ҫ����");
		}
	}
}



