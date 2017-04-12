package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;
import com.itheima.mobilesafe01.R.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Setup3Activity extends BaseSetupActivity {
	private EditText et_safephone;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_safephone = (EditText) findViewById(R.id.et_safephone);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		String phone = sp.getString("safephone", "");
		et_safephone.setText(phone);
	}

	public void selectContact(View v) {
		Intent intent = new Intent(this, ContactActivity.class);
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll("-", "").replaceAll(" ", "");
			et_safephone.setText(phone);

		}
	
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_preout, R.anim.tran_prein);
	}

	@Override
	protected void showNextPage() {
		// TODO Auto-generated method stub
		String string = et_safephone.getText().toString().trim();
		if (TextUtils.isEmpty(string)) {
			Toast.makeText(this, "安全号码不能为空", 0).show();
			return;
		}
		sp.edit().putString("safephone", string).commit();
		startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_out);
	}
}
