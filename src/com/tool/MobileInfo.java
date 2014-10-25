package com.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import com.tool.common.ActivityUtils;
import com.tool.common.ToolGuide;
/*
 * �ֻ���Ϣģ���Activity,ʹ�õ���ViewPager
 */
public class MobileInfo extends BaseActivity {
	// ViewPager��google SDk���Դ���һ�����Ӱ���һ���࣬��������ʵ����Ļ����л���
	// android-support-v4.jar
	private ViewPager mPager;//ҳ������
	private List<View> listViews; // Tabҳ���б�
	private ImageView cursor;// ����ͼƬ
	private TextView t1, t2, t3,t4;// ҳ��ͷ��
	private TextView imsiTx,imeiTx,isoTx,stateTx,operatorName,phoneType,deviceSoft,deviceID;
	private TextView cpuInfoTx,hardPanInfoTx,displayInfoTx,memoryInfoTx,mobileInfoTitle;
	private ImageView image,homeBtn,rebackBtn;// ���״̬ͼƬ
	private TextView textCD;// ��س��״̬
	private TextView textRL;// ���ʣ������
	private TextView textZT;// ���״̬
	private TextView textDY;// ��ص�ѹmV
	private TextView textWD;// ����¶�
	private TextView textLX;// �������
	private TextView phoneModel,sdkVersion,firVersion,osVersion;
	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	static TelephonyManager tm;
	String simStateStr,phoneTypeStr;
	TableLayout simLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main2);
		InitImageView();
		InitTextView();
		InitViewPager();
	}

	/*
	 * ��ʼ��ͷ��
	 */
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);
		t4 = (TextView) findViewById(R.id.text4);
		mobileInfoTitle= (TextView) findViewById(R.id.query_tool);
		mobileInfoTitle.setText("�ֻ���Ϣ");

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
		t4.setOnClickListener(new MyOnClickListener(3));
	}

	/*
	 * ��ʼ��ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		tm = (TelephonyManager) MobileInfo.this.getSystemService(Context.TELEPHONY_SERVICE);
		LayoutInflater mInflater = getLayoutInflater();
	    View view = mInflater.inflate(R.layout.mobile_info_item, null);
	    simLayout = (TableLayout)view.findViewById(R.id.sim_layout);
//	    simLayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ActivityUtils.showDialog(MobileInfo.this,"ȷ��","���ʽ���", getResources().getString(R.string.sim_noun));
//			}
//		});
	    
	    imsiTx = (TextView)view.findViewById(R.id.imsi_tx);
	    imeiTx = (TextView)view.findViewById(R.id.imei_tx);
	    isoTx = (TextView)view.findViewById(R.id.iso_tx);
	    stateTx = (TextView)view.findViewById(R.id.state_tx);
	    operatorName = (TextView)view.findViewById(R.id.operator_name);
	    
	    phoneType = (TextView)view.findViewById(R.id.type_tx);
	    deviceSoft = (TextView)view.findViewById(R.id.device_soft_tx);
	    deviceID = (TextView)view.findViewById(R.id.device_id_tx);
	    
	    imsiTx.setText(tm.getSubscriberId());
	    imeiTx.setText(tm.getSimSerialNumber());
	    isoTx.setText(tm.getNetworkCountryIso());
	    stateTx.setText(getTheSimState());
	    operatorName.setText(tm.getSimOperatorName());
	    
	    deviceSoft.setText(tm.getDeviceSoftwareVersion());
	    phoneType.setText(getThePhoneType());	    
	    deviceID.setText(tm.getDeviceId());
	    
		listViews.add(view);
		
		LayoutInflater hInflater = getLayoutInflater();
	    View hview = hInflater.inflate(R.layout.hardware_info, null);
		cpuInfoTx = (TextView)hview.findViewById(R.id.cpu_info_tx);
		hardPanInfoTx = (TextView)hview.findViewById(R.id.hardpan_info_tx);
		displayInfoTx = (TextView)hview.findViewById(R.id.display_info_tx);
		memoryInfoTx = (TextView)hview.findViewById(R.id.memory_info_tx);
		
		cpuInfoTx.setText(getCpuInfo());
		hardPanInfoTx.setText(getHardPanInfo());
		displayInfoTx.setText(getDisplayInfo(this));
		memoryInfoTx.setText(getMemoryInfo());
		
		listViews.add(hview);
		
		LayoutInflater oInflater = getLayoutInflater();
	    View oview = oInflater.inflate(R.layout.os_info, null);
	    phoneModel = (TextView)oview.findViewById(R.id.phone_model);
	    sdkVersion = (TextView)oview.findViewById(R.id.sdk_version);
	    firVersion = (TextView)oview.findViewById(R.id.firmware_version);
	    osVersion = (TextView)oview.findViewById(R.id.os_version);
	    phoneModel.setText(Build.MODEL);
	    sdkVersion.setText(Build.VERSION.SDK);
	    firVersion.setText(Build.VERSION.RELEASE);    
	    osVersion.setText(getOsVersion());
	    
		listViews.add(oview);
		
		LayoutInflater bInflater = getLayoutInflater();
	    View bview = bInflater.inflate(R.layout.battery_info, null);
	    textCD = (TextView) bview.findViewById(R.id.textCD);
		textRL = (TextView) bview.findViewById(R.id.textRL);
		textZT = (TextView) bview.findViewById(R.id.textZT);
		textDY = (TextView) bview.findViewById(R.id.textDY);
		textWD = (TextView) bview.findViewById(R.id.textWD);
		textLX = (TextView) bview.findViewById(R.id.textLX);
		image = (ImageView) bview.findViewById(R.id.imageView1);
		registerReceiver(myBroadcastReciver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		listViews.add(bview);
		
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	//����ֻ�SIM״̬�ķ���
    public String getTheSimState(){
    	
    	switch (tm.getSimState()) {
		case 0:
			simStateStr = "SIM��δ֪״̬ ";
			break;
		case 1:
			simStateStr = "SIM��δ�ҵ� ";
			break;
		case 2:
			simStateStr = "��Ҫ�û���PIN�����";
			break;
		case 3:
			simStateStr = "��Ҫ�û���PUK�����";
			break;
		case 4:
			simStateStr = "��Ҫ�����PIN�����";
			break;
		case 5:
			simStateStr = "SIM������";
			break;
		default:
			break;
		}	 	
    	return simStateStr;
    }
    
    //��ȡ�ֻ��ź����͵ķ���
    public String getThePhoneType(){
    	switch (tm.getPhoneType()) {
		case 0:
			phoneTypeStr = "���ź�";
			break;
		case 1:
			phoneTypeStr = "GSM�ź� ";
			break;
		case 2:
			phoneTypeStr = "CDMA�ź�";
			break;
		default:
			break;
		}	 	
    	return phoneTypeStr;
    }
    
    //��ȡCPU��Ϣ�ķ���
	private String getCpuInfo() {
		ProcessBuilder cmd;
		String result = "";

		try {
			String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
			cmd = new ProcessBuilder(args);

			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				System.out.println(new String(re));
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

    //��ȡӲ����Ϣ�ķ���
	private String getHardPanInfo() {
		ProcessBuilder cmd;
		String result = "";

		try {
			String[] args = { "/system/bin/df","/system/bin"};
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				System.out.println(new String(re));
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	//��ȡ�ڴ���Ϣ��
	private String getMemoryInfo() {
		ProcessBuilder cmd;
		String result = "";

		try {
			String[] args = {"/system/bin/cat", "/proc/meminfo"}; 
			cmd = new ProcessBuilder(args);

			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				System.out.println(new String(re));
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	//��ȡ��ʾ����Ϣ�ķ���
	public static String getDisplayInfo(Context cx) {

		String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		float density = dm.density;
		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;
		str += "The absolute width: " + String.valueOf(screenWidth) + "pixels "+"\n";
		str += "The absolute heightin: " + String.valueOf(screenHeight)
				+ "pixels "+"\n";
		str += "The logical density of the display. : "
				+ String.valueOf(density) + " "+"\n";
		str += "X dimension : " + String.valueOf(xdpi) + "pixels per inch "+"\n";
		str += "Y dimension : " + String.valueOf(ydpi) + "pixels per inch ";
		return str;
	}
	
	//��ȡ����ϵͳ�汾�ķ���
	private String getOsVersion() {
		ProcessBuilder cmd;
		String result = "";

		try {
			String[] args = { "/system/bin/cat", "/proc/version"};
			cmd = new ProcessBuilder(args);

			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				System.out.println(new String(re));
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	//������ر仯�Ĺ㲥
	private BroadcastReceiver myBroadcastReciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// ���ʣ������
			//boolean flag = true;
			int level = (int) (intent
					.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
					/ (float) intent.getIntExtra(BatteryManager.EXTRA_SCALE,
							100) * 100);
			textRL.setText(level + "%");
			// ��ص�ǰʹ��״̬
			image.setImageResource(intent.getIntExtra(
					BatteryManager.EXTRA_ICON_SMALL, 0));
			switch (intent.getIntExtra(BatteryManager.EXTRA_STATUS, 1)) {
			case BatteryManager.BATTERY_STATUS_CHARGING:
				if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 1) == BatteryManager.BATTERY_PLUGGED_AC)
					textCD.setText("ʹ�ó���������");
				else
					textCD.setText("ʹ��USB�����");
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				textCD.setText("�ŵ���");
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				textCD.setText("�ѳ���");
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				textCD.setText("δ����");
				break;
			}
			
			// ���״̬
			switch (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 1)) {
			case BatteryManager.BATTERY_HEALTH_DEAD:
				textZT.setText("������𻵣�");
				textZT.setTextColor(Color.RED);
				break;
			case BatteryManager.BATTERY_HEALTH_GOOD:
				textZT.setText("����");
				break;
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				textZT.setText("��ѹ����");
				break;
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				textZT.setText("�¶ȹ���");
				break;
			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
				textZT.setText("δ֪");
				break;
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
				textZT.setText("δ֪����");
				break;
			}
			
			// ��ص�ѹ
			textDY.setText(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 1)+"mV");
			// ����¶�
			textWD.setText((intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 1)/10.0)+"��");
			// �������
			textLX.setText(intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));

		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myBroadcastReciver);
	}
	
	/*
	 * ��ʼ������
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		homeBtn = (ImageView) findViewById(R.id.home);
		homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MobileInfo.this, MainActivity.class);
				startActivity(intent);
				MobileInfo.this.finish();
			}
		});
		rebackBtn = (ImageView) findViewById(R.id.reback);
		rebackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MobileInfo.this, MainActivity.class);
				startActivity(intent);
				MobileInfo.this.finish();
			}
		});
		// ��ȡͼƬ���
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// ��ȡ�ֱ��ʿ��
		int screenW = dm.widthPixels;
		offset = (screenW / 4 - bmpW) /3;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		// ���ö�����ʼλ��
		cursor.setImageMatrix(matrix);
	}

	/*
	 * ViewPager������
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/*
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/*
	 * ҳ���л�����
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����
		int three = two+bmpW;

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				}else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		 if (keyCode == KeyEvent.KEYCODE_BACK){
			 Intent intent = new Intent(MobileInfo.this, MainActivity.class);
			 startActivity(intent);
			 MobileInfo.this.finish();
		 }
		 return false;
	 }
}