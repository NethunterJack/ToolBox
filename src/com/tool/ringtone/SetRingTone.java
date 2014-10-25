package com.tool.ringtone;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tool.BaseActivity;
import com.tool.MainActivity;
import com.tool.R;
import com.tool.XyCallBack;
import com.tool.common.ActivityUtils;
import com.tool.common.ToastManager;
import com.tool.menu.CustomMenu;
import com.tool.menu.MusicMenu;
/*
 * �������õ�Activity
 */
public class SetRingTone extends BaseActivity {
	MusicMenu customMenu;
	private static final int INPUT = 0;
	// ����������
	private MediaPlayer player = new MediaPlayer();
	/* ���ݿ� */
	MyDataBaseAdapter m_MyDatabaseAdapter;
	/* listview */
	private ListView mLib;
	private ImageView homeBtn,aboutBtn; 
	private TextView ringTitle;

	String prePath = "/sdcard/";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_ring);
		/* �������ݿ� */
		m_MyDatabaseAdapter = new MyDataBaseAdapter(this);
		m_MyDatabaseAdapter.open();
		mLib = (ListView) findViewById(R.id.lib);
		homeBtn = (ImageView)findViewById(R.id.home);
		aboutBtn = (ImageView)findViewById(R.id.reback);
		aboutBtn.setBackgroundResource(R.drawable.about_selected);
		ringTitle = (TextView)findViewById(R.id.query_tool);
		ringTitle.setText(getResources().getString(R.string.set_ring));
		homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetRingTone.this, MainActivity.class);
				startActivity(intent);
				SetRingTone.this.finish();
			}
		});
		
		aboutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(SetRingTone.this, MainActivity.class);
//				startActivity(intent);
//				SetRingTone.this.finish();
//				overridePendingTransition(R.anim.zoom_enter,
//						R.anim.zoom_exit);
//				ActivityUtils.showDialog(SetRingTone.this, "ȷ��", "����", 
//		                 getResources().getString(R.string.about_setringtone));
				
				final Dialog aboutAuthorDialog = new Dialog(SetRingTone.this,
						R.style.dialog_style);
				//lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			   aboutAuthorDialog.setContentView(R.layout.about_dialog);
				((TextView) aboutAuthorDialog.findViewById(R.id.dialog_title)).setText("����");
				((TextView) aboutAuthorDialog.findViewById(R.id.dialog_message))
				                              .setText(getResources().getString(R.string.about_setringtone));
				
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
		});
		UpdataAdapter();
		// ListView��������
		mLib.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String p = m_MyDatabaseAdapter.getPath(arg2 + 1);
				Log.i("ListView", p + " clicked");
				onStart(p);
				ToastManager.showToastView(getApplicationContext(), "������������");

			}
		});

		mLib.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// ���ݿ�ID��1��ʼ��ListView��Item��ID��0��ʼ�����Լ�1��ʹ���ݿ���ListViewһһ��Ӧ
				final int id = arg2 + 1;
				final String p = m_MyDatabaseAdapter.getPath(id);

				player.stop();
				// �Ի���
				final CharSequence[] items = { getString(R.string.setRingtone),
						getString(R.string.setNotification),
						getString(R.string.setAlarm),
						getString(R.string.delete),
						getString(R.string.neverMind) };

				Dialog dialog = new AlertDialog.Builder(SetRingTone.this)
						.setItems(items, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								switch (item) {
								case 0:
									setMyRingtone(p);
									break;
								case 1:
									setMyNotification(p);
									break;
								case 2:
									setMyAlarm(p);
									break;
								case 3:
									deleteitem(id);
									break;
								case 4:
									ToastManager.showToastView(getApplicationContext(),"ȡ��");
											
									break;
								}
							}
						}).create();
				dialog.show();
				return false;
			}
		});

		ToastManager.showToastView(this, "���Menu����������ļ�");
	}

	// ����ʱ����Adapter
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UpdataAdapter();
	}

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

		customMenu = new MusicMenu(this, R.style.dialog,menuCallBack,1);			
		customMenu.showDialog(R.layout.menu_music, 0, 0);	

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
			menuDimiss();
			goInputFileActivity();			
			break;	
		default:
			break;
		}
	}
	
//	// �ײ������˵�
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		menu.add(0, INPUT, 0, "��SD�����������ļ�");
//		return true;
//	}
//
//	public boolean onOptionsItemSelected(MenuItem item) {
//		super.onOptionsItemSelected(item);
//		switch (item.getItemId()) {
//		case INPUT:
//			goInputFileActivity();
//			break;
//		}
//		return false;
//	}

	public void goInputFileActivity() {
		Intent intent = new Intent();
		intent.setClass(SetRingTone.this, InputFile.class);
		startActivity(intent);
		SetRingTone.this.finish();
	}

	/* ���ݿ�list */
	public void UpdataAdapter() {
		// ��ȡ���ݿ��Cursor
		Cursor cur = m_MyDatabaseAdapter.fetchAllData();

		if (cur != null && cur.getCount() >= 0) {
			Log.i("Database", "done");
			// ListAdapter��ListView�ͺ�̨���ݵ�����
			ListAdapter adapter = new SimpleCursorAdapter(this,
			// ����List��ÿһ�е���ʾģ��
			// ��ʾÿһ�а�������������
					R.layout.liblist_item,
					// ���ݿ��Cursor����
					cur,
					// �����ݿ��TABLE_NUM��TABLE_DATA������ȡ����
					new String[] { MyDataBaseAdapter.TABLE_rNAME,
							MyDataBaseAdapter.TABLE_PATH },
					// ��NAME��PATH��Ӧ��Views
					new int[] { R.id.listitem_title, R.id.listitem_content });

			/* ��adapter��ӵ�m_ListView�� */
			mLib.setAdapter(adapter);
		}
	}

	// �����������
	public void onStart(String p) {
		/*
		 * Uri path= Uri.parse(p); player.stop(); player =
		 * MediaPlayer.create(this, path); player.start();
		 */
		if (prePath.equals(p) && player.isPlaying()) {
			player.stop();
		} else {
			Uri path = Uri.parse(p);
			prePath = p;
			player.stop();
			player = MediaPlayer.create(this, path);
			player.start();
		}

	}

	// ����--����
	public void setMyRingtone(final String p) {
		player.stop();

		File sdfile = new File(p);
		Log.i("File", p);
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile
				.getAbsolutePath());
		Uri newUri = this.getContentResolver().insert(uri, values);

		RingtoneManager.setActualDefaultRingtoneUri(this,
				RingtoneManager.TYPE_RINGTONE, newUri);
		ToastManager.showToastView(getApplicationContext(), "�绰����������");
		System.out.println("setMyRingtone()-------------");
	}

	// ����--��ʾ��
	public void setMyNotification(final String p) {
		player.stop();

		File sdfile = new File(p);
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile
				.getAbsolutePath());
		Uri newUri = this.getContentResolver().insert(uri, values);

		RingtoneManager.setActualDefaultRingtoneUri(this,
				RingtoneManager.TYPE_NOTIFICATION, newUri);
		ToastManager.showToastView(getApplicationContext(),R.string.setNotificationSucceed);
				
		System.out.println("setMyNOTIFICATION-------------");
	}

	// ����--������
	public void setMyAlarm(final String p) {
		player.stop();

		File sdfile = new File(p);
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile
				.getAbsolutePath());
		Uri newUri = this.getContentResolver().insert(uri, values);

		RingtoneManager.setActualDefaultRingtoneUri(this,
				RingtoneManager.TYPE_ALARM, newUri);
		Toast.makeText(getApplicationContext(), R.string.setAlarmSucceed,
				Toast.LENGTH_SHORT).show();
		System.out.println("setMyNOTIFICATION-------------");
	}

	// ���б����ݿ���ɾ������
	public void deleteitem(int id) {
		m_MyDatabaseAdapter.deleteData(id);
		ToastManager.showToastView(this, "�����Ѵ��б����Ƴ�");
		m_MyDatabaseAdapter.updateID(id);
		UpdataAdapter();

	}

	// �ر�Activity����
	public void onDestroy() {
		super.onDestroy();
		try {
			this.finish();
		} catch (Exception e) {
		}
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event){
  		 if (keyCode == KeyEvent.KEYCODE_BACK){
  			 Intent intent = new Intent(SetRingTone.this, MainActivity.class);
  			 startActivity(intent);
  			 SetRingTone.this.finish();
  		 }
  		 return false;
  	 }
}