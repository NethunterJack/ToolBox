package com.tool;

import com.tool.common.BadgeView;
import com.tool.common.ToastManager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*
 * �������������Activity,����������ʾ��ʱ��Ϊ2��
 */
public class SplashActivity extends BaseActivity {

	SharedPreferences isShowIconRef;
	private BadgeView appVersion;
	private Button appVersionView;
	public static final String pName ="com.tool";
    public static int versionCode;
    public static String  versionName;
    boolean flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		//getCode();
		appVersionView = (Button)findViewById(R.id.app_version);
		appVersionView.setText(getCode());
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				 		        				 
			        isShowIconRef = getSharedPreferences("isShowIcon"+versionName, 0);
			        boolean isShowIcon=isShowIconRef.getBoolean("isShowIcon"+versionName, false);
			        if(!isShowIcon){
			            System.out.println("δ������ݷ�ʽ");
			            addShortcut();
			        }else{
			            System.out.println("�Ѵ�����ݷ�ʽ");
			        }
				   
				   Intent it = new Intent(SplashActivity.this, MainActivity.class);
				   startActivity(it);
				   finish();
				}
		}, 2000);        
	}
	
	
	/*
	    * Ϊ���򴴽������ݷ�ʽ
	    */
    private void addShortcut(){
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            //��ݷ�ʽ������
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
            shortcut.putExtra("duplicate", false); //�������ظ�����
            //ָ����ǰ��ActivityΪ��ݷ�ʽ�����Ķ���: �� com.everest.video.VideoPlayer
            //ע��: ComponentName�ĵڶ�������������ϵ��(.)�������ݷ�ʽ�޷�������Ӧ����
            ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , SplashActivity.class));
            //��ݷ�ʽ��ͼ��
            ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.tools);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
            sendBroadcast(shortcut);
            
            isShowIconRef = getSharedPreferences(
                    "isShowIcon"+versionName, 0);
            Editor isShowIconRef_e = isShowIconRef.edit();
            isShowIconRef_e.putBoolean("isShowIcon"+versionName, true);
            isShowIconRef_e.commit();  
            ToastManager.showToastView(SplashActivity.this,"�Ѵ�����ݷ�ʽ");
    }
//	    
//    /*
//     * ���汾��
//     */
    public String getCode() {
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(pName,
                    PackageManager.GET_CONFIGURATIONS);
            versionCode = pinfo.versionCode;
            versionName = pinfo.versionName;
        } catch (NameNotFoundException e) {
            versionName = "1.0";
        }
        
        return versionName;
    }
}
