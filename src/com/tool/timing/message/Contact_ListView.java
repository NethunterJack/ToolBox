package com.tool.timing.message;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tool.MainActivity;
import com.tool.R;
import com.tool.common.ToastManager;
import com.tool.timing.call.TimingDialActivity;

@TargetApi(Build.VERSION_CODES.ECLAIR)
public class Contact_ListView extends Activity {
	
	ListView listView;
	AutoCompleteTextView textView;
	TextView emptytextView;
	private TextView contactTitile;
	private ImageView homeBtn,rebackBtn; 
	protected CursorAdapter mCursorAdapter;
	protected Cursor mCursor = null;
	protected Contact_Adapter ca;
	ArrayList<Contact_Info> contactList = new ArrayList<Contact_Info>();
	//ѡ�е��ֻ���
	protected String numberStr = "";
	protected String[] autoContact = null;
	protected String[] wNumStr = null;
	private static final int DIALOG_KEY = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_view2);

		listView = (ListView) findViewById(R.id.list);
		textView = (AutoCompleteTextView) findViewById(R.id.edit);
		emptytextView = (TextView) findViewById(R.id.empty);
		Button btn_add = (Button) findViewById(R.id.btn_add);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        contactTitile = (TextView)findViewById(R.id.query_tool);
		contactTitile.setText("��ϵ�˺���");
        homeBtn = (ImageView)findViewById(R.id.home);
		rebackBtn = (ImageView)findViewById(R.id.reback);

		homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Contact_ListView.this, MainActivity.class);
				startActivity(intent);
				Contact_ListView.this.finish();
			}
		});
		
		rebackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Contact_ListView.this.finish();
			}
		});
        
        emptytextView.setVisibility(View.GONE);
        
        //��ȡǰҳ��ֵ,������ֶ���д���ֻ�����ͨѶ¼��,��Ĭ�Ϲ���
        //����ֶ���д���ֻ��Ų���ͨѶ¼��,���ڻش�ֵ��ʱ�����ȥ(�������ֻ���ʽ��ȥ��)
        
        /*ȡ��Intent�е�Bundle����*/
		Intent intent = this.getIntent();
		Bundle  bundle = intent.getExtras();
		
		//ȡ��Bundle�����е�����
		String wNumberStr = bundle.getString("wNumberStr").replace("��", ",");
		wNumStr = wNumberStr.split(",");
		
		//��������
		new GetContactTask().execute("");

		// �б����¼�����
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				LinearLayout ll = (LinearLayout) view;
				CheckBox cb = (CheckBox) ll.getChildAt(0).findViewById(
						R.id.check);
				//ѡ�������ѡ���ַ�����,ȡ������ַ�����ɾ��
				if (cb.isChecked()) {
					cb.setChecked(false);
					numberStr = numberStr.replace(","
							+ contactList.get(position).getUserNumber(), "");
					contactList.get(position).isChecked = false;
				} else {
					cb.setChecked(true);
					numberStr += ","
							+ contactList.get(position).getUserNumber();
					contactList.get(position).isChecked = true;
				}
			}
		});	
        
        btn_add.setOnClickListener(btnClick);
        btn_back.setOnClickListener(btnClick);
	}
	
	//��ť����
	private OnClickListener btnClick = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_add:
					//���ȷ�Ͻ�ѡ�е��ֻ��Żش�
					Log.i("eoe", numberStr);
					Intent intent = getIntent();
					Bundle bundle = new Bundle();
					String bundleStr = numberStr;
					if (bundleStr != "") {
						bundleStr = bundleStr.substring(1);
					}
					bundle.putString("numberStr", bundleStr);
					intent.putExtras(bundle);
					//����Intent�ص���һ��Activity
					setResult(RESULT_OK, intent);
					finish();
				break;				
				case R.id.btn_back:
					finish();
				break;
			}
		}
	};
	
	//����AUTOTEXT���ݱ仯,�����ַ���ѡ����ϵ��[��ϵ��(�ֻ���)]�������,���ù���,������ѡ���ֻ�����
	private TextWatcher mTextWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int
                before, int after) { }

        public void onTextChanged(CharSequence s, int start,
                int before, int after) {

                	String autoText = s.toString();
        	if(autoText.length()>=13){
        		Pattern pt=Pattern.compile("\\(([1][3,5,8]+\\d{9})\\)");
        		Matcher mc = pt.matcher(autoText);
        		if(mc.find()){
        			String sNumber = mc.group(1);
        			DealWithAutoComplete(contactList,sNumber);
        			//ˢ���б�
        			ToastManager.showToastView(Contact_ListView.this, "��ѡ���������Ľ��!");
        			ca.setItemList(contactList);
        			ca.notifyDataSetChanged();
        		}
        	}
        }

        public void afterTextChanged(Editable s) { }

	};
	
	//��ȡͨѶ¼����
	@TargetApi(3)
	private class GetContactTask extends AsyncTask<String, String, String> {
		public String doInBackground(String... params) {
		 
			//�ӱ����ֻ��л�ȡ
			GetLocalContact();
			//��SIM���л�ȡ
			GetSimContact("content://icc/adn");
			GetSimContact("content://sim/adn");
		  return "";
		}

		@Override
		protected void onPreExecute() {
			showDialog(DIALOG_KEY);
		}

		@Override
		public void onPostExecute(String Re) {
			//��LISTVIEW
			if(contactList.size()==0){
				emptytextView.setVisibility(View.VISIBLE);
			}
			else{
				//������ƴ��˳������
				Comparator comp = new Mycomparator(); 
		        Collections.sort(contactList,comp);  
	
				//numberStr = GetNotInContactNumber(wNumStr, contactList) + numberStr;
				ca = new Contact_Adapter(Contact_ListView.this, contactList);
				listView.setAdapter(ca);
				listView.setTextFilterEnabled(true);
				//�༭AUTOCOMPLETE����
				autoContact = new String[contactList.size()];
				for(int c=0;c<contactList.size();c++){
					autoContact[c]=contactList.get(c).contactName+"("+contactList.get(c).userNumber+")";
				}
				//��AUTOCOMPLETE
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(Contact_ListView.this,
		                android.R.layout.simple_dropdown_item_1line, autoContact);
		        textView.setAdapter(adapter);
		        textView.addTextChangedListener(mTextWatcher);
			}
	        removeDialog(DIALOG_KEY);
		}
	}
	
	// ����"�鿴"�Ի���
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("��ȡͨѶ¼��...���Ժ�");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}
	
	//�ӱ�����ȡ��
	private void GetLocalContact(){
		//�õ�ContentResolver����  
	     ContentResolver cr = getContentResolver();    
	      //ȡ�õ绰���п�ʼһ��Ĺ��  
	    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (cursor.moveToNext()) {
			Contact_Info cci = new Contact_Info();
			//ȡ����ϵ������
			 int nameFieldColumnIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME); 
			 cci.contactName = cursor.getString(nameFieldColumnIndex);
			 // ȡ����ϵ��ID  
		    int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));  
	         Cursor phone=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?", new String[]{Integer.toString(id)}, null);//����ContactsContract.CommonDataKinds.Phone�и��ݲ�ѯ��Ӧid��ϵ�˵����е绰��

		  
		    // ȡ�õ绰����(���ܴ��ڶ������)  
		    while (phone.moveToNext())  
		    {   String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                cci.userNumber = GetNumber(strPhoneNumber);
				cci.isChecked = false;
				
					if (!IsContain(contactList, cci.userNumber)) {
						if (IsUserNumber(strPhoneNumber)) {
						 
					 
						 
						contactList.add(cci); }
				}
		    }  
		    
		    phone.close(); 
		}

	} 
	
		
//		cursor.close();
//		ContentResolver cr = getContentResolver();
//		cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
//
//				  while (cursor.moveToNext()) {
//
//				   ContactInfo cci = new ContactInfo();
//
//				   //ȡ����ϵ������
//
//				   int nameFieldColumnIndex = cursor.getColumnIndex(People.NAME);
//
//				   cci.contactName = cursor.getString(nameFieldColumnIndex);
//
//				   //ȡ�õ绰����
//
//				   int numberFieldColumnIndex = cursor.getColumnIndex(People.NUMBER);
//
//				   cci.userNumber = cursor.getString(numberFieldColumnIndex);
//
//				   cci.userNumber = GetNumber(cci.userNumber);
//
//				   cci.isChecked = false;
//
//				  
//
//				    if (!IsContain(contactList, cci.userNumber)) {
//                        contactList.add(cci);  }
//            }
// cursor.close(); }
	
	//��SIM����ȡ��
	private void GetSimContact(String add){
		//��ȡSIM���ֻ���,�����ֿ���:content://icc/adn��content://sim/adn
		try { 
			Intent intent = new Intent();
			intent.setData(Uri.parse(add));
			Uri uri = intent.getData();
			mCursor = getContentResolver().query(uri, null, null, null, null);
			if (mCursor != null) {
				while (mCursor.moveToNext()) {
					Contact_Info sci = new Contact_Info();
					// ȡ����ϵ������
					int nameFieldColumnIndex = mCursor.getColumnIndex("name");
					sci.contactName = mCursor.getString(nameFieldColumnIndex);
					
					
					
					// ȡ�õ绰����
					int numberFieldColumnIndex = mCursor
							.getColumnIndex("number");
					sci.userNumber = mCursor.getString(numberFieldColumnIndex);

					sci.userNumber = GetNumber(sci.userNumber);
					sci.isChecked = false;
					
				 
						if (!IsContain(contactList, sci.userNumber)) {////�Ƿ���LIST��ֵ
				        contactList.add(sci); }
					 
				}
				mCursor.close();
			}
		} catch (Exception e) {
			Log.i("eoe", e.toString());
		}
	}
	
	//�Ƿ���LIST��ֵ
	private boolean IsContain(ArrayList<Contact_Info> list,String un){
		for(int i=0; i<list.size(); i++){
			if(un.equals(list.get(i).userNumber)){
				return true;
			}
		}
		return false;
	}
	
 
	
 
	//����������ѡ�е��ֻ���
	private void DealWithAutoComplete(ArrayList<Contact_Info> list,String un){
		for(int i=0; i<list.size(); i++){
			if(un.equals(list.get(i).userNumber)){
				if(!list.get(i).isChecked){
					list.get(i).isChecked = true;
				   textView.setText("");
				}
			}
		}
	} 
	//ͨѶ¼������ƴ������
	public class Mycomparator implements Comparator{ 
	    public int compare(Object o1,Object o2) { 
	    	Contact_Info c1=(Contact_Info)o1; 
	    	Contact_Info c2=(Contact_Info)o2; 
	    	Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
	    	return cmp.compare(c1.contactName, c2.contactName);       
	       } 

	} 
	
	
	//�Ƿ�Ϊ�ֻ�����  �е�ͨѶ¼��ʽΪ135-1568-1234 
	public static boolean IsUserNumber(String num){
		boolean re = false;
		if(num.length()>=11)
		{
			if(num.startsWith("1")){
				re = true;
			} }
		return re;
	}
	
	//��ԭ11λ�ֻ���  ����ȥ����-��
	public static String GetNumber(String num2){
		String num=num2;
		if(num!=null)
		{  
			num=num.replaceAll("-", "");
		if (num.startsWith("+86"))
        {
			num = num.substring(3);
        }
        else if (num.startsWith("86")){
        	num = num.substring(2);
        }
        else if (num.startsWith("86")){
        	num = num.substring(2);
        } }
		else{
			num=""; }
		return num;
	}
	
	public void showTimingToolsDialog(){
	final CharSequence[] dialog_items = {"ȷ�����","ȡ��"};
			Dialog dialog = new AlertDialog.Builder(Contact_ListView.this)
			.setItems(dialog_items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
					case 0:
						Log.i("eoe", numberStr);
						Intent intent = getIntent();
						Bundle bundle = new Bundle();
						String bundleStr = numberStr;
						if (bundleStr != "") {
							bundleStr = bundleStr.substring(0);
						}
						bundle.putString("numberStr", bundleStr);
						intent.putExtras(bundle);
						//����Intent�ص���һ��Activity
						setResult(RESULT_OK, intent);
						finish();
						break;
					
					case 1:
						dialog.dismiss();
						break;
					
					}
				}
			}).create();
	   dialog.show();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event){
		 if (keyCode == KeyEvent.KEYCODE_BACK){
			 Intent intent = new Intent(Contact_ListView.this, TimingDialActivity.class);
			 startActivity(intent);
			Contact_ListView.this.finish();
			overridePendingTransition(R.anim.zoom_enter,
					R.anim.zoom_exit);
		 }
		 return false;
	 }
}