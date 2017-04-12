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
					// �Ƴ��Ѱ󶨵�sim��
				} else {
					// ����sim����Ϣ
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();// ��ȡsim�����к�
					System.out.println("sim�����к�:" + simSerialNumber);
					tv_siv.setChecked(true);
					// �ύsim�������к�
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
		// �л�����
		overridePendingTransition(R.anim.tran_preout, R.anim.tran_prein);
	}

	@Override
	protected void showNextPage() {
		// TODO Auto-generated method stub
		String sim = sp.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			Toast.makeText(this, "���sim��", 0).show();
			return;
		}
		startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
		finish();
		// �л�����
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_out);
	}

}
