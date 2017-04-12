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
			// sivUpdate.setDesc("�Զ������ѿ���");
		} else {
			sivUpdate.setChecked(false);
			// sivUpdate.setDesc("�Զ������ѹر�");
		}

		sivUpdate.setOnClickListener(new OnClickListener() {
			// �ж��Ƿ�ѡ
			public void onClick(View v) {
				if (sivUpdate.isChecked()) {
					// ����ѡ
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("�Զ������ѹر�");
					sp.edit().putBoolean("auto_update", false).commit();

				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("�Զ������ѿ���");
					sp.edit().putBoolean("auto_update", true).commit();
				}
			}
		});

	}

	public void initAddressView() {
		// ���ݹ����ط����Ƿ�����������checkbox
		boolean serviceRunning = ServiceStatusUtils.isRunningService(this,
				"com.itheima.mobilesafe01.service.AddressService");
		if (serviceRunning) {
			sivAddress.setChecked(true);

		} else {
			sivAddress.setChecked(false);

		}

		sivAddress.setOnClickListener(new OnClickListener() {
			// �ж��Ƿ�ѡ
			public void onClick(View v) {
				if (sivAddress.isChecked()) {
					// ����ѡ
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));

				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));// ��������

				}
			}
		});

	}
	final String[] items = new String[] { "��͸��", "������", "��ʿ��", "������", "ƻ����" };
	
	/**
	 * �޸���ʾ����ʾ���
	 */
	public void initStyleView() {
		scvAddress = (SettingClickView) findViewById(R.id.siv_click_style);
		scvAddress.setTitle("�Զ���߿�ѡ��");
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
	 * �޸Ĺ�������ʾλ��
	 */
   public void initAddressLocation(){
	   scv_AddressLocation = (SettingClickView) findViewById(R.id.siv_address_style);
	   scv_AddressLocation.setTitle("�Զ�����ʾλ��");
	   scv_AddressLocation.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this,AddressLocationActivity.class));
		}
	});
   }

	/**
	 * ����ѡ����ĵ�ѡ��
	 */
	protected void showSingleChooseDailog() {
		// TODO Auto-generated method stub
		     AlertDialog.Builder builder=new AlertDialog.Builder(this);
		     builder.setTitle("�x���");
		    int style= sp.getInt("set_style", 0);
		     builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					  sp.edit().putInt("set_style", which).commit();
					  dialog.dismiss();
					  scvAddress.setDesc(items[which]);
				}
			});
		     builder.setNegativeButton("ȡ��", null);
		     builder.show();
	}

}
