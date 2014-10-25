package com.tool.timing.message;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tool.QueryTool;
import com.tool.R;
import com.tool.common.ActivityUtils;
import com.tool.ringtone.SetRingTone;

public class TimerActivity extends Activity {
    /** Called when the activity is first created. */
	private static final int CONTACT_REQUEST_CODE = 2;
	private TextView timeTx,timingMessageTitle;
    private Button setTimeBtn;
    private Button saveTimeBtn;
    private Button cancleTimeBtn;
    private Button addNumBtn;
    private ImageView homeBtn,rebackBtn; 
    private EditText et1;
    private EditText et2;   
    private static final String save="saved_pref";
	private static final String num="number";
	private static final String msn="message";
    Calendar c=Calendar.getInstance();
    String tmpS;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.timing_message);
        findView();
        
        addNumBtn.setOnClickListener(addnum);
        saveTimeBtn.setOnClickListener(savetext);
        setTimeBtn.setOnClickListener(settime); 
        cancleTimeBtn.setOnClickListener(cancletime); 
        
        homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimerActivity.this, MainActivity.class);
				startActivity(intent);
				TimerActivity.this.finish();
			}
		});
		
		rebackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TimerActivity.this, MainActivity.class);
				startActivity(intent);
				TimerActivity.this.finish();
			}
		});
   
    }
     public void findView(){
    	timingMessageTitle = (TextView)findViewById(R.id.query_tool);
    	timingMessageTitle.setText(getResources().getString(R.string.timing_message));
    	timeTx = (TextView)findViewById(R.id.message_view);
    	addNumBtn = (Button)findViewById(R.id.chose_message_num);
        setTimeBtn = (Button)findViewById(R.id.set_message_time);
        saveTimeBtn = (Button)findViewById(R.id.save_message_time);
        cancleTimeBtn = (Button)findViewById(R.id.cancle_message_time);
        homeBtn = (ImageView)findViewById(R.id.home);
		rebackBtn = (ImageView)findViewById(R.id.reback);
        et1=(EditText)findViewById(R.id.editnum);
        et2=(EditText)findViewById(R.id.editSMS);
        //timeTx=(TextView)findViewById(R.id.time);
    }
     
     Button.OnClickListener addnum=new Button.OnClickListener(){
    	 public void onClick(View arg0) {
    		 Intent intent = new Intent();
    			intent.setClass(TimerActivity.this, Contact_ListView.class);
    			
    			//ʵ����Bundle
    			Bundle bundle = new Bundle();
    			//���ͨ�ź���
    			String wNumberStr = et1.getText().toString();
    			bundle.putString("wNumberStr", wNumberStr);
    			//��Bundle��ӵ�Intent
    			intent.putExtras(bundle);
    			//CONTACT_REQUEST_CODE=2��Ϊ���ؽ��
    			startActivityForResult(intent, CONTACT_REQUEST_CODE);
    	 }
     };
       
      Button.OnClickListener settime=new Button.OnClickListener(){

             public void onClick(View arg0) {
                c.setTimeInMillis(System.currentTimeMillis());
 				int hours = c.get(Calendar.HOUR_OF_DAY);
 				int minutes = c.get(Calendar.MINUTE);
                 new TimePickerDialog(TimerActivity.this,
                 new TimePickerDialog.OnTimeSetListener(){
                     public void onTimeSet(TimePicker v, int hour, int minute) {
                    	 
                           c.setTimeInMillis(System.currentTimeMillis());
                           c.set(Calendar.HOUR_OF_DAY,hour);
                           c.set(Calendar.MINUTE,minute);
                           c.set(Calendar.SECOND,0);
                           c.set(Calendar.MILLISECOND,0);
//                           String tmpS = et1.getText()
//                        		   		     .toString()
//                        		   		     + "->"
//										     + format(hour)
//										     + ":" + format(minute);
                           tmpS = format(hour)
								          + ":" + format(minute)+""+"->";
						   if (ActivityUtils.validateNull(tmpS)) {
							   timeTx.setText(tmpS);  
						} else {
							ActivityUtils.showDialog(TimerActivity.this, "ȷ��", "��ʾ", "������ֻ��ź�ʱ�䶼����Ϊ��");  
						}	     
                                                   
                     }
                 },hours,minutes,true).show();
                 
             }            
     };
       
//     Button.OnClickListener settime=new Button.OnClickListener(){
//
//         public void onClick(View arg0) {
//             //c.setTimeInMillis(System.currentTimeMillis());
//        	 c.set(Calendar.YEAR, Integer.parseInt((etx1.getText().toString())));
//        	 c.set((Calendar.MONTH)+1, Integer.parseInt((etx2.getText().toString())));
//        	 c.set(Calendar.DAY_OF_MONTH, Integer.parseInt((etx3.getText().toString())));
//             c.set(Calendar.HOUR_OF_DAY, Integer.parseInt((etx4.getText().toString())));
//             c.set(Calendar.MINUTE,Integer.parseInt((etx5.getText().toString())));
//         }     
// };
     Button.OnClickListener savetext=new Button.OnClickListener(){

         public void onClick(View arg0) {
        	 String time = String.valueOf(c.getTimeInMillis()+" "+tmpS);
        	 System.out.println(time);
        	 String ednum=et1.getText().toString();
             String edsms=et2.getText().toString();
             if (ActivityUtils.validateNull(ednum)) {
            	 if (ActivityUtils.validateNull(tmpS)) {
            		 if (ActivityUtils.validateNull(edsms)) {
            			 timeTx.append(ednum);
                    	 SharedPreferences sp=getSharedPreferences(save,0);
                         
                         sp.edit().putString(num, ednum).putString(msn, edsms).commit();
                                   
                         Intent intent = new Intent(TimerActivity.this, TimerRecv.class);
                         PendingIntent sender=PendingIntent.getBroadcast(
                                                           TimerActivity.this,0, intent, 0);
                         AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
                         am.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),sender);
					} else {
						ActivityUtils.showDialog(TimerActivity.this, "ȷ��", "��ʾ", "�������Ϣ����Ϊ��");
					}
				} else {
					ActivityUtils.showDialog(TimerActivity.this, "ȷ��", "��ʾ", "�����ʱ�䲻��Ϊ��");
				}  		 
			}              
		    else {
				timeTx.append("");
				ActivityUtils.showDialog(TimerActivity.this, "ȷ��", "��ʾ", "������ֻ��ź�ʱ�䶼����Ϊ��");				
			}            
         }
     };   
     
     Button.OnClickListener cancletime=new Button.OnClickListener(){
    	 public void onClick(View arg0) {
    		 Intent intent = new Intent(TimerActivity.this, TimerRecv.class);
             PendingIntent sender=PendingIntent.getBroadcast(
                                               TimerActivity.this,0, intent, 0);
             AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
             am.cancel(sender);
             timeTx.setText("ȡ����ʱ��Ϣ");
    	 }
     };
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
 				}
 				et1.setText(numberStr);
 			}
 			break;
 		}
     }
     
     private String format(int x) {
 		String s = "" + x;
 		if (s.length() == 1)
 			s = "0" + s;
 		return s;
 	}
     
     public boolean onKeyDown(int keyCode, KeyEvent event){
  		 if (keyCode == KeyEvent.KEYCODE_BACK){
  			 Intent intent = new Intent(TimerActivity.this, MainActivity.class);
  			 startActivity(intent);
  			 TimerActivity.this.finish();
  		 }
  		 return false;
  	 }
}