package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends BaseSetupActivity {
             @Override
            protected void onCreate(Bundle savedInstanceState) {
            	// TODO Auto-generated method stub
            	super.onCreate(savedInstanceState);
            	setContentView(R.layout.activity_setup1);
            }
            
			@Override
			protected void showPreviousPage() {
				// TODO Auto-generated method stub
				
			}
			@Override
			protected void showNextPage() {
				// TODO Auto-generated method stub
				 startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
            	 finish();
            	 overridePendingTransition(R.anim.tran_enter, R.anim.tran_out);
			}
}
