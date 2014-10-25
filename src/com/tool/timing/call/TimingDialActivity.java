package com.tool.timing.call;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tool.MainActivity;
import com.tool.R;
import com.tool.common.ActivityUtils;

public class TimingDialActivity extends Activity implements OnClickListener{
	/** Called when the activity is first created. */

	private static final int CONTACT_REQUEST_CODE = 2;
	private ImageView homeBtn,rebackBtn; 
	private Button mButtonStart,mButtonSave,mButtonStop,chosePhoneNum;
	private EditText phoneEditText;
	private TextView timingCallTitle;

	TextView mTextView;
	Calendar calendar;
	String tmpS;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.timing_call);
		calendar = Calendar.getInstance();

		mTextView = (TextView) findViewById(R.id.phone_view);
		mButtonStart = (Button) findViewById(R.id.set_time);
		mButtonSave = (Button) findViewById(R.id.save_call_time);
		mButtonStop = (Button) findViewById(R.id.cancle_time);
		chosePhoneNum = (Button) findViewById(R.id.chose_num);
		phoneEditText = (EditText) findViewById(R.id.num);
		timingCallTitle = (TextView)findViewById(R.id.query_tool);
		timingCallTitle.setText(getResources().getString(R.string.timing_call));
		homeBtn = (ImageView)findViewById(R.id.home);
		rebackBtn = (ImageView)findViewById(R.id.reback);
		chosePhoneNum.setOnClickListener(this);
		homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimingDialActivity.this, MainActivity.class);
				startActivity(intent);
				TimingDialActivity.this.finish();
			}
		});
		
		rebackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimingDialActivity.this, MainActivity.class);
				startActivity(intent);
				TimingDialActivity.this.finish();
			}
		});

		mButtonStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				calendar.setTimeInMillis(System.currentTimeMillis());
				int mHour = calendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = calendar.get(Calendar.MINUTE);
				new TimePickerDialog(TimingDialActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								calendar.setTimeInMillis(System
										.currentTimeMillis());
								calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								calendar.set(Calendar.MINUTE, minute);
								calendar.set(Calendar.SECOND, 0);
								calendar.set(Calendar.MILLISECOND, 0);

								
								tmpS = format(hourOfDay)
										+ ":" + format(minute)+""+"->";
								mTextView.setText(tmpS);
							}
						}, mHour, mMinute, true).show();
			}
		});

		mButtonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String time = String.valueOf(calendar.getTimeInMillis());
				String phoneNum = phoneEditText.getText().toString();
				if (ActivityUtils.validateNull(phoneNum)) {
					 if (ActivityUtils.validateNull(tmpS)) {
						 mTextView.append(phoneNum);
							Intent intent = new Intent(
									TimingDialActivity.this,
									AlarmReceiver.class);
							intent.putExtra("tel_phone", phoneEditText
									.getText().toString());
							PendingIntent pendingIntent = PendingIntent
									.getBroadcast(TimingDialActivity.this,
											0, intent, 0);
							AlarmManager am;
							am = (AlarmManager) getSystemService(ALARM_SERVICE);
							// am.set(AlarmManager.RTC_WAKEUP, calendar
							// .getTimeInMillis(), pendingIntent);
							am.setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(),
									(24 * 60 * 60 * 1000), pendingIntent);
							SharedPreferences preference = getSharedPreferences("zhuyan",
									Context.MODE_PRIVATE);
							Editor edit = preference.edit();
							edit.putString("tel_phone",  phoneNum);
							edit.commit();
					 }else {
						 ActivityUtils.showDialog(TimingDialActivity.this, "ȷ��", "��ʾ", "�����ʱ�䲻��Ϊ��");
					}				
			  }else {
				  mTextView.append("");
				  ActivityUtils.showDialog(TimingDialActivity.this, "ȷ��", "��ʾ", "������ֻ��ź�ʱ�䶼����Ϊ��");
			 }
		   }			
		});
		
		mButtonStop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TimingDialActivity.this,
						AlarmReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						TimingDialActivity.this, 0, intent, 0);
				AlarmManager am;
				am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.cancel(pendingIntent);
				mTextView.setText("ȡ����ʱ����");
			}
		});
	}

	
	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//
//		SharedPreferences preference = getSharedPreferences("zhuyan",
//				Context.MODE_PRIVATE);
//		phoneEditText.setText(preference.getString("tel_phone","000000"));
//	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferences preference = getSharedPreferences("zhuyan",
				Context.MODE_PRIVATE);
		Editor edit = preference.edit();
		edit.putString("tel_phone", phoneEditText.getText().toString());
		edit.commit();
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event){
 		 if (keyCode == KeyEvent.KEYCODE_BACK){
 			 Intent intent = new Intent(TimingDialActivity.this, MainActivity.class);
 			 startActivity(intent);
 			 TimingDialActivity.this.finish();
 		 }
 		 return false;
 	 }
	 
	//ҳ�洫ֵ����ȡ�ش�ֵ
	    public void onClick(View v) {
	    	//ʵ����Intent����ָ��class
	    	Intent intent = new Intent();
			intent.setClass(TimingDialActivity.this, ContactListView.class);
			
			//ʵ����Bundle
			Bundle bundle = new Bundle();
			//���ͨ�ź���
			String wNumberStr = phoneEditText.getText().toString();
			bundle.putString("wNumberStr", wNumberStr);
			//��Bundle��ӵ�Intent
			intent.putExtras(bundle);
			//CONTACT_REQUEST_CODE=2��Ϊ���ؽ��
			startActivityForResult(intent, CONTACT_REQUEST_CODE);
	    }
	    
	    //��д��ȡҳ��ش�ֵ
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	switch (requestCode) {
	    	case CONTACT_REQUEST_CODE:
				if (resultCode == RESULT_OK) {
					String numberStr = null;
					//��Intent���Bundle
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						//��Bundle��õ绰����
						numberStr = bundle.getString("numberStr");
						System.out.println(numberStr);
						
						//System.out.println("====================>");
					}	
					phoneEditText.setText(numberStr);
				}
				
				break;
			}
	    }
}