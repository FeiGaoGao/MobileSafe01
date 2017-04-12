package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;
import com.itheima.mobilesafe01.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {
	private SettingItemView tv_siv;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		String sim = sp.getString("sim", null);

		tv_siv = (SettingItemView) findViewById(R.id.siv_sim);
		if (!TextUtils.isEmpty(sim)) {
			tv_siv.setChecked(true);
		} else {
			tv_siv.setChecked(false);
		}
		tv_siv.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (tv_siv.isChecked()) {
					tv_siv.setChecked(false);
					sp.edit().remove("sim").commit();
					// 移除已绑定的sim卡
				} else {
					// 保存sim卡信息
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();// 获取sim卡序列号
					System.out.println("sim卡序列号:" + simSerialNumber);
					tv_siv.setChecked(true);
					// 提交sim卡的序列号
					sp.edit().putString("sim", simSerialNumber).commit();
				}
			}
		});
	}

	@Override
	protected void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
		finish();
		// 切换动画
		overridePendingTransition(R.anim.tran_preout, R.anim.tran_prein);
	}

	@Override
	protected void showNextPage() {
		// TODO Auto-generated method stub
		String sim = sp.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			Toast.makeText(this, "请绑定sim卡", 0).show();
			return;
		}
		startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
		finish();
		// 切换动画
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_out);
	}

}
