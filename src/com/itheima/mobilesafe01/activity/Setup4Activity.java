package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {
	private CheckBox cb_safe;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_safe = (CheckBox) findViewById(R.id.cb_safe);

		if (sp.getBoolean("protect", false)) {
			cb_safe.setText("防盗保护已经开启");
			cb_safe.setChecked(true);
		} else {
			cb_safe.setText("防盗保护没有开启");
			cb_safe.setChecked(false);
		}

		cb_safe.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					cb_safe.setText("防盗保护已经开启");
					sp.edit().putBoolean("protect", true).commit();
				} else {
					cb_safe.setText("防盗保护没有开启");
					sp.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}

	@Override
	protected void showPreviousPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_preout, R.anim.tran_prein);
	}

	@Override
	protected void showNextPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_out);
	}

}
