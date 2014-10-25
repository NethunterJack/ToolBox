package com.tool.convert.bmi;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tool.R;
import com.tool.common.ActivityUtils;
/*
 * BMIת����Activity
 */
public class ConvertBMI extends Activity {
	private TextView resultText;
	private Button countButton;
	private EditText heighText;
	private RadioButton maleBtn, femaleBtn; 
	String sex = "";
	double height;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convert_bmi);
        //���ô�����ͼ�ĺ���
        creadView();
        //�����Ա�ѡ��ĺ���
        sexChoose();
        //����Buttonע��������ĺ���
        setListener();
   }
    
    //��ӦButton�¼��ĺ���
    private void setListener() {
    	countButton.setOnClickListener(countListner);

    }

    private OnClickListener countListner = new OnClickListener() {
		String result="";
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			try {
        		//if ((heighText.getText().toString())!=null) 
            	Double.parseDouble(heighText.getText().toString());
            	result="����һλ"+sexChoose()+"\n"
	               +"������Ϊ"+Double.parseDouble(heighText.getText().toString())+"cm"
			       +"\n��ı�׼����Ϊ"+getWeight(sexChoose(), height)+"kg";
            	resultText.setText(result);				
			} catch (Exception e) {
				// TODO: handle exception
				ActivityUtils.showDialog(ConvertBMI.this, "ȷ��", "��ʾ", "��������غ��Ա𶼲���Ϊ��");
			}
		}
	};
	    
	//�Ա�ѡ��ĺ���
    private String sexChoose(){ 	
    	if (maleBtn.isChecked()) {
        	sex = "����";
        } 
    	else if(femaleBtn.isChecked()){
        	sex = "Ů��";
        }
		return sex;  	
    }
    
    //������ͼ�ĺ���
    public void creadView(){
    	resultText = (TextView)findViewById(R.id.result);
    	countButton = (Button)findViewById(R.id.btn);
    	heighText = (EditText)findViewById(R.id.etx);
    	maleBtn = (RadioButton)findViewById(R.id.male);
    	femaleBtn = (RadioButton)findViewById(R.id.female); 	
    	//txt.setBackgroundResource(R.drawable.bg);
    }
    
    //��׼���ظ�ʽ������ĺ���
    private String format(double num) {
    	NumberFormat formatter = new DecimalFormat("0.00");
    	String str = formatter.format(num);
    	return str;
    	}
    
    //�õ���׼���صĺ���
    private String getWeight(String sex, double height) {
    	height = Double.parseDouble(heighText.getText().toString());
	  	String weight = "";
	   	if (sex.equals("����")) {
	   	      weight =format((height - 80) * 0.7);
	   	} 
	   	else {
	   	      weight = format((height - 70) * 0.6);
	    }
	   	return weight;
	   }
   }  
