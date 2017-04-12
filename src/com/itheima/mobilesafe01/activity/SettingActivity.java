package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;
import com.itheima.mobilesafe01.service.AddressService;
import com.itheima.mobilesafe01.utils.ServiceStatusUtils;
import com.itheima.mobilesafe01.view.SettingClickView;
import com.itheima.mobilesafe01.view.SettingItemView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;
	private SettingItemView sivAddress;
	private SharedPreferences sp;
	private SettingClickView scvAddress;
	private SettingClickView scv_AddressLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		initUpData();
		initAddressView();
		initStyleView();
		initAddressLocation();
	}

	public void initUpData() {
		if (sp.getBoolean("auto_update", true)) {
			sivUpdate.setChecked(true);
			// sivUpdate.setDesc("自动更新已开启");
		} else {
			sivUpdate.setChecked(false);
			// sivUpdate.setDesc("自动更新已关闭");
		}

		sivUpdate.setOnClickListener(new OnClickListener() {
			// 判断是否勾选
			public void onClick(View v) {
				if (sivUpdate.isChecked()) {
					// 不勾选
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("自动更新已关闭");
					sp.edit().putBoolean("auto_update", false).commit();

				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("自动更新已开启");
					sp.edit().putBoolean("auto_update", true).commit();
				}
			}
		});

	}

	public void initAddressView() {
		// 根据归属地服务是否运行来更新checkbox
		boolean serviceRunning = ServiceStatusUtils.isRunningService(this,
				"com.itheima.mobilesafe01.service.AddressService");
		if (serviceRunning) {
			sivAddress.setChecked(true);

		} else {
			sivAddress.setChecked(false);

		}

		sivAddress.setOnClickListener(new OnClickListener() {
			// 判断是否勾选
			public void onClick(View v) {
				if (sivAddress.isChecked()) {
					// 不勾选
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));

				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));// 开启服务

				}
			}
		});

	}
	final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
	
	/**
	 * 修改提示框显示风格
	 */
	public void initStyleView() {
		scvAddress = (SettingClickView) findViewById(R.id.siv_click_style);
		scvAddress.setTitle("自定义边框选择");
		  int style= sp.getInt("set_style", 0);
		scvAddress.setDesc(items[style]);
		scvAddress.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSingleChooseDailog();
			}
		});
		
	}
	/**
	 * 修改归属地显示位置
	 */
   public void initAddressLocation(){
	   scv_AddressLocation = (SettingClickView) findViewById(R.id.siv_address_style);
	   scv_AddressLocation.setTitle("自定义显示位置");
	   scv_AddressLocation.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this,AddressLocationActivity.class));
		}
	});
   }

	/**
	 * 弹出选择风格的单选框
	 */
	protected void showSingleChooseDailog() {
		// TODO Auto-generated method stub
		     AlertDialog.Builder builder=new AlertDialog.Builder(this);
		     builder.setTitle("選擇欄");
		    int style= sp.getInt("set_style", 0);
		     builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					  sp.edit().putInt("set_style", which).commit();
					  dialog.dismiss();
					  scvAddress.setDesc(items[which]);
				}
			});
		     builder.setNegativeButton("取消", null);
		     builder.show();
	}

}
