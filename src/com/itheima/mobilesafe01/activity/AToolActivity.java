package com.itheima.mobilesafe01.activity;


import com.itheima.mobilesafe01.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AToolActivity extends Activity {
          @Override
        protected void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_atools);
        }
          public void numberAddressQuery(View v ){
        	  startActivity(new Intent(this, AddressActivity.class));
          }
}
