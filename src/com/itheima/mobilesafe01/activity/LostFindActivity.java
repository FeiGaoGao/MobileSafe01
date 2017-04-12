package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private TextView tv_2;
	private ImageView iv_lock;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		if (sp.getBoolean("configed", false)) {
			setContentView(R.layout.activity_lostfind);
			tv_2 = (TextView) findViewById(R.id.tv_2);
			iv_lock = (ImageView) findViewById(R.id.iv_lock);

			sp = getSharedPreferences("config", MODE_PRIVATE);

			String safephone = sp.getString("safephone", "");
			tv_2.setText(safephone);

			if (sp.getBoolean("protect", false)) {
				iv_lock.setImageResource(R.drawable.lock);
			} else {
				iv_lock.setImageResource(R.drawable.unlock);
			}
			
		} else {
			startActivity(new Intent(this, Setup1Activity.class));
			sp.edit().putBoolean("configed", true).commit();
			finish();
		}

	}

	public void reEnter(View v) {
		startActivity(new Intent(this, Setup1Activity.class));
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_out);
		finish();
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, HomeActivity.class));
		finish();
		super.onBackPressed();
	}
}
