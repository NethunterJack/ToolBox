package com.tool.search.postcode;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

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
import com.tool.common.LogManager;
import com.tool.common.ToastManager;
import com.tool.menu.CustomMenu;
import com.tool.search.phone.PhoneActivity;
import com.tool.search.phone.PhoneDBAdapter;
import com.tool.search.phone.PhoneStoreList;

public class PostCodeActivity extends BaseActivity {

	PostCodeActivity th;
	private PostCodeDBAdapter db;
	private Cursor cursor;
	private static CustomMenu customMenu;
	private static final int DELETE = Menu.FIRST;
	private static final int SAVETOSD = Menu.FIRST+1;
	private static final int ABOUT= Menu.FIRST+2;
	private ImageView postCodeQueryBtn,homeBtn,rebackBtn; 
	private EditText postCodeEdit;
	private TextView queryTitile,postCodeResultTxView;
	private static final int MESSAGETYPE = 101;
    private ProgressDialog progressDialog = null; 
    String fileNameStr,titleNameStr;
    StringBuilder sb;
    PostCode postCode;
    String page=null;
    String[] string;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.query_item);
		th = this;
		postCodeQueryBtn = (ImageView)findViewById(R.id.query_search_btn);
		homeBtn = (ImageView)findViewById(R.id.home);
		rebackBtn = (ImageView)findViewById(R.id.reback);
		postCodeEdit = (EditText)findViewById(R.id.query_search_content);
		queryTitile = (TextView)findViewById(R.id.query_tool);
		postCodeResultTxView = (TextView)findViewById(R.id.textView);
		postCodeEdit.setHint(R.string.post_code_hint);
		queryTitile.setText(R.string.query_postcode);
		db = new PostCodeDBAdapter(th).open();
		cursor = db.getAllTitles();
		string = new String[3];
		homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PostCodeActivity.this, MainActivity.class);
				startActivity(intent);
				PostCodeActivity.this.finish();
			}
		});
		
		rebackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PostCodeActivity.this, QueryTool.class);
				startActivity(intent);
				PostCodeActivity.this.finish();
			}
		});
		
		postCodeQueryBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = postCodeEdit.getText().toString().trim();
				if(ActivityUtils.isNetworkAvailable(PostCodeActivity.this)==false){
					ToastManager.showToastView(PostCodeActivity.this, "��ȷ�������Ƿ��Ѿ�����");
				}
				else if(ActivityUtils.isNetworkAvailable(PostCodeActivity.this)==true){
					if(ActivityUtils.validateNull(str)){
						if (str.length()!=3&&str.length()!=4) {
							ActivityUtils.showDialog(PostCodeActivity.this, "ȷ��", "��ʾ", "�����������λ������ȷ");
						} else {
							queryPostCodeData(str);	
						}
											
					}else{
						
		    			ActivityUtils.showDialog(PostCodeActivity.this, "ȷ��", "��ʾ", "��������Ų���Ϊ��");
		    		}				 
			    }
			}
		});
	}
		
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, DELETE, 1, "�������");
//		menu.add(0, SAVETOSD, 2, "��������");
//		menu.add(0, ABOUT, 3, "���򷵻�");
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
			postCodeEdit.setText("");
			postCodeResultTxView.setText("");
			menuDimiss();
			break;
		case 1:
		    //�������ݵĴ���		
			showSaveDialog();
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
			Intent intent = new Intent(PostCodeActivity.this, PostCodeStoreList.class);
			startActivity(intent);	
			break;
		
		default:
			break;
		}
	}
		
   public void queryPostCodeData(final String str) {

		progressDialog = ProgressDialog.show(PostCodeActivity.this, "��ѯ", "���ڲ�ѯ,���Ժ�......");  
			 new Thread() {
	                public void run() {                        

	        				Message msg_listData = new Message();
	                        msg_listData.what = MESSAGETYPE;
	                        msg_listData.obj = getAgStringAdress(str);
	                        System.out.println(getAgStringAdress(str)+"===========>");
	                        idHandler.sendMessage(msg_listData);
	                }
	        }.start();
		} 

      Handler idHandler = new Handler() {                

        public void handleMessage(Message message) {
        	if (message.obj==null) {
        		if(progressDialog.isShowing()){
        			progressDialog.dismiss(); //�رս�����
        			         			
        		}
        		showExcetionDialog("ע��", "��ȡ���ݳ��ִ���\n"+"�����������Ƿ���ȷ");
           		return;
			}
            switch (message.what) {
            case MESSAGETYPE:                                        
            	    postCodeResultTxView.setText(message.obj.toString());  
            	    System.out.println(postCode.toString());
                    progressDialog.dismiss(); //�رս�����
                    break;
            }
        }
    };
   
//    public boolean onOptionsItemSelected(MenuItem item) {
//		switch(item.getItemId()){
//			case DELETE:{
//				postCodeEdit.setText("");
//				postCodeResultTxView.setText("");
//				break;
//			}
//			case SAVETOSD:{
//			    showSaveDialog();			
//				break;
//			}	
//			case ABOUT:{
//				ActivityUtils.showDialog(PostCodeActivity.this, "ȷ��", "����", 
//		                 getResources().getString(R.string.about_postcode));
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
                    ToastManager.showToastView(PostCodeActivity.this, "д���ļ��ɹ�");
            } catch (Exception e) {
            	ToastManager.showToastView(PostCodeActivity.this, "д���ļ�ʧ��");
            }
        } else {
            // ��ʱSDcard�����ڻ��߲��ܽ��ж�д������
        	ToastManager.showToastView(PostCodeActivity.this,
                    "��ʱSDcard�����ڻ��߲��ܽ��ж�д����");
        }
     }
        
        private void showSaveDialog() {
//        	LayoutInflater factory = LayoutInflater.from(PostCodeActivity.this);
//    		final View view = factory.inflate(R.layout.save_result_dialog, null);// ����Զ���Ի���
//    		final EditText fileName = (EditText)view.findViewById(R.id.file_name);
//    		final EditText titleName = (EditText)view.findViewById(R.id.title_name);
//
//    		AlertDialog.Builder saveResultDialog = new AlertDialog.Builder(
//    				PostCodeActivity.this);
//    		saveResultDialog.setIcon(android.R.drawable.ic_dialog_info);
//    		saveResultDialog.setTitle("�����ѯ���");
//    		saveResultDialog.setView(view);
//    		saveResultDialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//    			public void onClick(DialogInterface dialog, int whichButton) {
//    				fileNameStr = fileName.getText().toString();
//    				titleNameStr = titleName.getText().toString();
//    				System.out.println(String.valueOf(fileNameStr.length()));
//    				if (postCodeResultTxView.getText().toString()!="") {
//    					if (fileNameStr.length() != 0 && titleNameStr.length()!= 0) {					
//    						try {
//    							saveToSDCard(postCodeResultTxView.getText().toString(),fileNameStr,titleNameStr);
//    						} catch (Exception e) {
//    							// TODO Auto-generated catch block
//    							e.printStackTrace();
//    						}
//    					} else {
//    						ActivityUtils.showDialog(PostCodeActivity.this,"ȷ��", "��ʾ","������ļ����ͱ��ⶼ����Ϊ��");
//    					}				
//    				} else {
//    					
//    					ActivityUtils.showDialog(PostCodeActivity.this,"ȷ��", "��ʾ", "û����Ҫ���������");
//    				}			
//    			}
//    		});
//    		saveResultDialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
//    			public void onClick(DialogInterface dialog, int whichButton) {
//    				// Toast.makeText(dialog_demo.this, "��ѡ����ȡ��",
//    				// Toast.LENGTH_LONG).show();
//    			}
//    		});
//    		saveResultDialog.create().show();
        	
        	final Dialog saveResultDialog = new Dialog(PostCodeActivity.this,
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
    						if (postCodeResultTxView.getText().toString()!="") {
    							if (fileNameStr.length() != 0 && titleNameStr.length()!= 0) {					
    								try {
    									saveToSDCard(postCodeResultTxView.getText().toString(),fileNameStr,titleNameStr);
    								} catch (Exception e) {
    									// TODO Auto-generated catch block
    									e.printStackTrace();
    								}
    							} else {
    								ActivityUtils.showDialog(PostCodeActivity.this,"ȷ��", "��ʾ","������ļ����ͱ��ⶼ����Ϊ��");
    							}				
    						} else {
    							
    							ActivityUtils.showDialog(PostCodeActivity.this,"ȷ��", "��ʾ", "û����Ҫ���������");
    						}			
    					}
    				});
    		saveResultDialog.show();
        }    
        public boolean onKeyDown(int keyCode, KeyEvent event){
      		 if (keyCode == KeyEvent.KEYCODE_BACK){
      			 Intent intent = new Intent(PostCodeActivity.this, QueryTool.class);
      			 startActivity(intent);
      			PostCodeActivity.this.finish();
      		 }
      		 return false;
      	 }
        
        public void showExcetionDialog(String title,String message){

//    		AlertDialog dialog;
//    		AlertDialog.Builder builder = new AlertDialog.Builder(PostCodeActivity.this);
//    		builder.setTitle(title);
//    		builder.setMessage(message);
//    		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
//    			@Override
//    			public void onClick(DialogInterface dialog, int which) {
//    				// TODO Auto-generated method stub
//    				postCodeResultTxView.setText("û�в�ѯ����ؽ��");
//    			}					
//    		});
//    		dialog = builder.create();
//    		dialog.show();
        	
        	final Dialog aboutAuthorDialog = new Dialog(PostCodeActivity.this,
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
     						postCodeResultTxView.setText("û�в�ѯ����ؽ��");
     					}
     				});
     		aboutAuthorDialog.show();
       }
        
      public PostCode getAgStringAdress(String str){
    	  final String baseUri = "http://shinylife.net/api/BaiBaoXianApi.ashx?t=zip&q=";

    	  try {
				String url = baseUri+str;
				Log.i("TAG", url);
				HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet(url);
	            HttpResponse response = client.execute(request);
          	page = EntityUtils.toString(response.getEntity()); 
          	
          	if (page != null && page.length() > 0) {
          		postCode = new PostCode();
  				JSONObject jb = new JSONObject(page);
  				postCode.setCity(jb.getString("city"));
  				postCode.setLocation(jb.getString("location"));
  				postCode.setPhone(jb.getString("phone"));
  				postCode.setZipcode(jb.getString("zipcode"));
  				postCode.setProvince(jb.getString("province"));  
  				return postCode;
  			}
      }catch (Exception e) {
			e.printStackTrace();
	  }   
	  return null;
    }
      
      /** �ղ� */
  	private void store() {
  		string[0]=postCode.getZipcode();
  		string[1]=postCode.getLocation();
  		string[2]=postCode.getPhone();
  		LogManager.i("debug", "=========="+string[0]+"===="+string[1]);
  		if (string[1] != null && postCodeResultTxView.getText()!="û�в�ѯ����ؽ��") {
  			
  			if (cursor.moveToFirst()) {
  				do {
  					if (string[0].equals(cursor.getString(cursor
  							.getColumnIndex(PostCodeDBAdapter.KEY_ZIPCODE)))) {
  						ToastManager.showToastView(PostCodeActivity.this, "��Ϣ�Ѵ��ڣ�����Ҫ����");
  						return;
  					}
  				} while (cursor.moveToNext());
  			}
  			cursor.close();
  			db.insertTitle(string);
  			ToastManager.showToastView(PostCodeActivity.this, "����ɹ�");
  		} else {
  			ToastManager.showToastView(PostCodeActivity.this, "û����Ϣ��Ҫ����");
  		}
  	}
}
