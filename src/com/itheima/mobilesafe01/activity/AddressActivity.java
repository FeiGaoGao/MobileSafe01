package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;
import com.itheima.mobilesafe01.db.Dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class AddressActivity extends Activity {
	private EditText et_number;
	private TextView tv_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		et_number = (EditText) findViewById(R.id.et_number);
		tv_result = (TextView) findViewById(R.id.tv_result);
		et_number.addTextChangedListener(new TextWatcher() {
			
			
			//文本变化时的监听
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			     	String address = AddressDao.getAddress(s.toString());
			     	tv_result.setText(address);
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void query(View v) {
		String number = et_number.getText().toString().trim();
		if (!TextUtils.isEmpty(number)) {
			 String number1=AddressDao.getAddress(number);
		      tv_result.setText(number1);
		}else{
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

			et_number.startAnimation(shake);
			vibrate();
		}
		     
	}
	/**
	 * 手机震动, 需要权限 android.permission.VIBRATE   插补器原理
	 */
	private void vibrate() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// vibrator.vibrate(2000);震动两秒
		vibrator.vibrate(new long[] { 1000, 2000, 1000, 3000 }, -1);// 先等待1秒,再震动2秒,再等待1秒,再震动3秒,
																	// 参2等于-1表示只执行一次,不循环,
																	// 参2等于0表示从头循环,
																	// 参2表示从第几个位置开始循环
		// 取消震动vibrator.cancel()
	}
}
