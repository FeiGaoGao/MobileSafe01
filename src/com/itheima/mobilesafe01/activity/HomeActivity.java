package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;
import com.itheima.mobilesafe01.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private GridView gv_home;
	String[] text = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
			"手机杀毒", "缓存清理", "高级工具", "设置中心" };
	int[] png = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe,
			R.drawable.home_apps, R.drawable.home_taskmanager,
			R.drawable.home_netmanager, R.drawable.home_trojan,
			R.drawable.home_sysoptimize, R.drawable.home_tools,
			R.drawable.home_settings };
	private EditText et_password;
	private EditText et_password_confirm;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter());
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 设置点击图标的监听
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					showPasswordDialog();
					break;
				case 7:
					startActivity(new Intent(HomeActivity.this,
							AToolActivity.class));
					
					break;
				case 8:
					startActivity(new Intent(HomeActivity.this,
							SettingActivity.class));
					
					break;

				default:
					break;
				}
			}
		});
	}

	class HomeAdapter extends BaseAdapter {

		public int getCount() {
			// TODO Auto-generated method stub
			return png.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return png[position];
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = View.inflate(HomeActivity.this, R.layout.home_list_item,
					null);
			ImageView iv_item = (ImageView) v.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) v.findViewById(R.id.tv_item);
			iv_item.setImageResource(png[position]);
			tv_item.setText(text[position]);
			return v;
		}

	}

	/**
	 * 显示密码弹窗
	 */
	public void showPasswordDialog() {
		//获取密码
		String savepassword = sp.getString("password", null);
		//如果为空 设置密码
		if(TextUtils.isEmpty(savepassword)){
			showPasswordSetDialog();
		}else{
		//不为空输入密码
		showPasswordInputDialog();
		}
	}

	private void showPasswordInputDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View v = View.inflate(this, R.layout.dailog_input_password, null);
		dialog.setView(v);
		et_password = (EditText) v.findViewById(R.id.et_password);
		Button btn_ok = (Button) v.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = et_password.getText().toString();

				if (!TextUtils.isEmpty(password)) {
					String savepassword = sp.getString("password", null);
					if (MD5Utils.encode(password).equals(savepassword)) {
						dialog.dismiss();

						// 跳转到手机防盗页
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
						finish();
					} else {
						Toast.makeText(HomeActivity.this, "密码错误",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框内容不能为空!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();// 隐藏dialog
			}
		});

		dialog.show();
	}

	public void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View v = View.inflate(this, R.layout.dailog_set_password, null);
		dialog.setView(v, 0, 0, 0, 0);

		et_password = (EditText) v.findViewById(R.id.et_password);
		et_password_confirm = (EditText) v
				.findViewById(R.id.et_password_confirm);
		Button btn_ok = (Button) v.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);

		btn_ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password = et_password.getText().toString();
				String passwordconfirm = et_password_confirm.getText()
						.toString();
				if (!TextUtils.isEmpty((password))
						&& !passwordconfirm.isEmpty()) {
					if (password.equals(passwordconfirm)) {
						sp.edit()
								.putString("password",
										MD5Utils.encode(password)).commit();

						dialog.dismiss();

						// 跳转到手机防盗页
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
						finish();
					} else {
						Toast.makeText(HomeActivity.this, "两次密码不一致!",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框内容不能为空!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();// 隐藏dialog
			}
		});

		dialog.show();
	}
}
