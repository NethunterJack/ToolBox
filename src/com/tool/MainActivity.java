package com.tool;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter; 
import android.widget.TextView;

import com.tool.common.AppManager;
import com.tool.common.SendEmail;
import com.tool.common.ToolGuide;
import com.tool.dialog.CustomDialog;
import com.tool.ringtone.SetRingTone;
import com.tool.timing.call.TimingDialActivity;
import com.tool.timing.message.TimerActivity;
import com.tool.timing.profile.Profile;

@TargetApi(3)
public class MainActivity extends BaseActivity {
	CustomDialog customDialog;
	private QuickActionWidget mQuick;//������ؼ� 
    private ImageButton infoImage;
    private ImageView deviceView,wifiView,queryView,convertView,transpondView,timingView;
    private ListView popList;
    private PopupWindow infoPopWindow;
    ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
    public static final String pName ="com.wqs";
    public static int versionCode;
    public static String  versionName;
    SharedPreferences isShowIconRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //������������Activityû����ʾ����
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        //������ͼ�ؼ�
        initView();
        initQuickActionGrid();
        //���ü����¼��ķ���
        setListner();    
        
    }
    
    //������ͼ�ؼ��ķ���
	public void initView() {
		                          
		infoImage = (ImageButton)findViewById(R.id.top_info);
		deviceView = (ImageView)findViewById(R.id.device_view);
		wifiView = (ImageView)findViewById(R.id.wifi_view);
		queryView = (ImageView)findViewById(R.id.query_view);
		convertView = (ImageView)findViewById(R.id.convert_view);
		transpondView = (ImageView)findViewById(R.id.ring_view);
		timingView = (ImageView)findViewById(R.id.timing_view);
		customDialog = new CustomDialog(MainActivity.this,R.style.dialog);
	}
	
	//���ü����¼��ķ���
	public void setListner() {
		                           
		infoImage.setOnClickListener(new infoClickListner());
		deviceView.setOnClickListener(new selectToolClickListner());
		wifiView.setOnClickListener(new selectToolClickListner());
		queryView.setOnClickListener(new selectToolClickListner());
		convertView.setOnClickListener(new selectToolClickListner());
		transpondView.setOnClickListener(new selectToolClickListner());
		timingView.setOnClickListener(new selectToolClickListner());		
	}
	
	//�������ذ�ť
	 public boolean onKeyDown(int keyCode, KeyEvent event){
		 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			 showExitDialog("ע�⣡","�ף�ȷ��Ҫ�˳���?");

		 }
		 return false;
	 }
	
	//����Pop����
	class infoClickListner implements OnClickListener{
		@Override
		public void onClick(View v) {
			mQuick.show(v);
//          	if(infoPopWindow !=null){
//          		infoPopWindow.dismiss();
//          	}
//          	else{
//          		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//          		View view = inflater.inflate(R.layout.info_pop, null);
//          		popList = (ListView)view.findViewById(R.id.info_pop_list);
//          		SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, CreateData(),
//    					R.layout.info_pop_list_item, new String[] { "content" },
//    					new int[] { R.id.content });
//
//    			popList.setAdapter(adapter);
//    			popList.setItemsCanFocus(false);
//    			popList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//    			popList.setOnItemClickListener(new selectItemLisener());
//    			infoPopWindow = new PopupWindow(view,100,LayoutParams.WRAP_CONTENT);
//    			infoPopWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_menu));
//              	infoPopWindow.setFocusable(true);
//              	infoPopWindow.setOutsideTouchable(false);          	
//    		}
//          	infoPopWindow.setAnimationStyle(R.style.AnimationPreview);
//          	infoPopWindow.update();
//          	infoPopWindow.showAsDropDown(infoImage);
////          	infoPopWindow.showAtLocation(findViewById(R.id.top_layout), Gravity.CENTER_HORIZONTAL | Gravity.TOP,
////    				160, 100);
        }
	}		

	/**
     * ��ʼ�������
     */
    private void initQuickActionGrid() {
        mQuick = new QuickActionGrid(this);
        mQuick.addQuickAction(new MyQuickAction(MainActivity.this, R.drawable.author, R.string.about_author));       
        mQuick.addQuickAction(new MyQuickAction(MainActivity.this, R.drawable.info, R.string.about_app)); 
        mQuick.addQuickAction(new MyQuickAction(MainActivity.this, R.drawable.guide, R.string.about_guide)); 
        mQuick.addQuickAction(new MyQuickAction(MainActivity.this, R.drawable.feedback, R.string.about_feedback)); 
        mQuick.setOnQuickActionClickListener(mActionListener);
    }
    
    /**
     * �����item����¼�
     */
    private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {
        	switch (position) {
			case 0:				
				showToolDialog("��������","ѧ��:0901010063\n"+"����:����� \n"+"רҵ:�����\n"+
		                   "QQ:657568424\n"+"Email��lingdududu@163.com");
				break;
			case 1:
				Intent toAbout = new Intent(MainActivity.this, AboutActivity.class);
				startActivity(toAbout);
				
				break;
			case 2:				
				Intent intent = new Intent(MainActivity.this, ToolGuide.class);
				startActivity(intent);
				MainActivity.this.finish();			
				break;
			case 3:			
				Intent sendMail = new Intent(MainActivity.this, SendEmail.class);
				startActivity(sendMail);
				MainActivity.this.finish();
				break;
			default:
				break;
			}
		}
    };
	//����Pop���ڵ�ѡ��Item�¼�
	//0 �����������ߵ���Ϣ�ĶԻ���   1�������������Ϣ�ĶԻ���  2��ת�����ָ�ϵ�Activity
//	class selectItemLisener implements OnItemClickListener{
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			// TODO Auto-generated method stub
//			switch (position) {
//			case 0:				
//				showToolDialog("��������","�Ŷ�����:L.T.L\n"+"��Ա:�����  ������ �Ƽ���\n"+
//		                   "QQ:657568424\n"+"Email��lingdududu@163.com");
//				break;
//			case 1:
//				showToolDialog("�������","�������:СС������\n"+"����:������\n"+"�汾:v1.0\n"+
//		                   "֧��ƽ̨:Android2.1+");
//				break;
//			case 2:				
//				Intent intent = new Intent(MainActivity.this, ToolGuide.class);
//				startActivity(intent);
//				MainActivity.this.finish();			
//				break;
//			case 3:			
//				Intent sendMail = new Intent(MainActivity.this, SendEmail.class);
//				startActivity(sendMail);
//				MainActivity.this.finish();
//				break;
//			default:
//				break;
//			}
//		}
//		
//	}

	//������ҳ�����������ť�¼�
	class selectToolClickListner implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//�ֻ���Ϣ
			case R.id.device_view:
				Intent mobileIntent = new Intent(MainActivity.this, MobileInfo.class);
    			startActivity(mobileIntent);
    			MainActivity.this.finish();
				break;
			//WIFI��Ϣ
            case R.id.wifi_view:
            	Intent wifiIntent = new Intent(MainActivity.this, WIFIInfo.class);
    			startActivity(wifiIntent);
    			MainActivity.this.finish();
				break;
			//��ѯ����
            case R.id.query_view:
            	Intent queryIntent = new Intent(MainActivity.this, QueryTool.class);
    			startActivity(queryIntent);
    			MainActivity.this.finish();
				break;
			//ת������
            case R.id.convert_view:
            	Intent convertIntent = new Intent(MainActivity.this, Convert.class);        	
    			startActivity(convertIntent);
    			MainActivity.this.finish();
				break;
			//��������
            case R.id.ring_view:
            	//showCallToolsDialog();
            	Intent ringIntent = new Intent(MainActivity.this, SetRingTone.class);        	
    			startActivity(ringIntent);
    			MainActivity.this.finish();
				break;
			//��ʱ����
            case R.id.timing_view:
            	showTimingToolsDialog();
				break;
			default:
				break;
			}	
		}		
	}
	
//	//��Map�����������
//	public ArrayList<Map<String, Object>> CreateData() {
//		
//		Map<String, Object> map;
//		map = new HashMap<String, Object>();
//		map.put("content", "��������");
//		items.add(map);
//		map = new HashMap<String, Object>();
//		map.put("content", "�������");
//		items.add(map);
//		map = new HashMap<String, Object>();
//		map.put("content", "���ָ��");
//		items.add(map);
//		map = new HashMap<String, Object>();
//		map.put("content","�������");
//		items.add(map);
//		return items;
//	}

	//�˳�����ĶԻ���
	private void showExitDialog(String pTitle, final String pMsg) {
		
//		AlertDialog dialog;
//		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//		builder.setIcon(R.drawable.tools);
//		builder.setTitle("ȷ���˳�");
//		builder.setMessage("��,ȷ��Ҫ�˳������?");
//		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				AppManager.getAppManager().AppExit(MainActivity.this);
//				//MainActivity.this.finish();
//			}					
//		});
//		
//		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}					
//		});
//		dialog = builder.create();
//		dialog.show();
		
		final Dialog exitDialog = new Dialog(MainActivity.this,
				R.style.dialog_style);
		//lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		exitDialog.setContentView(R.layout.dialog);
		((TextView) exitDialog.findViewById(R.id.dialog_title)).setText(pTitle);
		((TextView) exitDialog.findViewById(R.id.dialog_message)).setText(pMsg);
		
		((Button) exitDialog.findViewById(R.id.dialog_button_cancle))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// write your code to do things after users clicks CANCEL
						exitDialog.dismiss();
					}
				});
		((Button) exitDialog.findViewById(R.id.dialog_button_ok))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// write your code to do things after users clicks OK
						
						exitDialog.dismiss();
						AppManager.getAppManager().AppExit(MainActivity.this);
					}
				});
		exitDialog.show();

   }

   public void showToolDialog(String title,String message){

//		AlertDialog dialog;
//		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//		builder.setIcon(android.R.drawable.ic_menu_info_details);
//		builder.setTitle(title);
//		builder.setMessage(message);
//		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}					
//		});
//		dialog = builder.create();
//		dialog.show();
	   
	   final Dialog aboutAuthorDialog = new Dialog(MainActivity.this,
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
					}
				});
		aboutAuthorDialog.show();
   }
    
    //���������ʱ���߰�ť�����Ի����¼�
    public void showTimingToolsDialog(){
    	final CharSequence[] dialog_items = { getString(R.string.timing_call),
  				getString(R.string.timing_message),
  				getString(R.string.timing_profile),
  				getString(R.string.cancle)};
				Dialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setItems(dialog_items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:						
							Intent callIntent = new Intent(MainActivity.this, TimingDialActivity.class);
			    			startActivity(callIntent);
			    			MainActivity.this.finish();
							break;
						case 1:						
							Intent messageIntent = new Intent(MainActivity.this, TimerActivity.class);
			    			startActivity(messageIntent);
			    			MainActivity.this.finish();
							break;
						case 2:							
			            	Intent profileIntent = new Intent(MainActivity.this, Profile.class);
			    			startActivity(profileIntent);
			    			MainActivity.this.finish();			 
							break;
						case 3:
							dialog.dismiss();
							break;
						
						}
					}
				}).create();
		   dialog.show();
   	}
}