package com.itheima.mobilesafe01.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.mobilesafe01.R;
import com.itheima.mobilesafe01.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class SplashActivity extends Activity {

	private TextView tv_version;
	private HttpURLConnection conn = null;

	// ��JSON�л�ȡ����Ϣ
	private String versionName;
	private int versionCode;
	private String description;
	private String downloadUrl;

	// �汾��Ϣ
	private int versionCode2;
	private String versionName2;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdataDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "�������", 0).show();
				enterHome();
				break;
			case CODE_IO_ERROR:
				Toast.makeText(SplashActivity.this, "�������", 0).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "�汾����", 0).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
			default:
				break;
			}
		};
	};
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_IO_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;
	private TextView tv_download;
	private SharedPreferences sp;
	private RelativeLayout rl_root;
	private InputStream fis;
	private FileOutputStream out;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		copyDB();
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("�汾��Ϊ" + getVersionName());
		tv_download = (TextView) findViewById(R.id.tv_download);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		if (sp.getBoolean("auto_update", false)) {
			checkVersion();
		} else {
			handler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);// ����һ���ӳٵ���Ϣ
		}
		AlphaAnimation  alph= new AlphaAnimation(0.3f, 1f);
		alph.setDuration(2000);
		 rl_root.startAnimation(alph);
	}

	/**
	 * ��ȡ�汾��
	 * 
	 * @return
	 */
	public String getVersionName() {
		PackageManager packagemanager = getPackageManager();
		try {
			PackageInfo packageInfo = packagemanager.getPackageInfo(
					getPackageName(), 0);
			versionName2 = packageInfo.versionName;
			versionCode2 = packageInfo.versionCode;
			// System.out.println("�汾��:"+ versionName +";�汾��" + versionCode);
			return versionName2;
		} catch (NameNotFoundException e) {
			// �汾�Ų������쳣
			e.printStackTrace();
		}
		return null;
	}

	public int getVersionCode2() {
		PackageManager packagemanager = getPackageManager();
		try {
			PackageInfo packageInfo = packagemanager.getPackageInfo(
					getPackageName(), 0);
			versionCode2 = packageInfo.versionCode;
			return versionCode2;
		} catch (NameNotFoundException e) {
			// �����������쳣
			e.printStackTrace();
		}
		return -1;
	}

	public void checkVersion() {
		final long startTime = System.currentTimeMillis();
		final Message msg = Message.obtain();
		final Thread thread = new Thread() {
			public void run() {
				try {
					URL url = new URL("http://192.168.1.104:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					conn.connect();

					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream is = conn.getInputStream();
						String readFromStream = StreamUtils.readFromStream(is);
						// System.out.println(readFromStream);
						JSONObject json = new JSONObject(readFromStream);
						versionName = json.getString("versionName");
						versionCode = json.getInt("versionCode");
						description = json.getString("description");
						downloadUrl = json.getString("downloadUrl");
						if (versionCode > getVersionCode2()) {

							// ��������VersionCode���ڱ��ص�VersionCode
							// ˵���и���, ���������Ի���
							msg.what = CODE_UPDATE_DIALOG;
						} else {
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = CODE_IO_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long usedTime = endTime - startTime;
					if (usedTime < 2000) {
						try {
							Thread.sleep(2000 - usedTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
					if (conn != null) {
						conn.disconnect();// �ر���������
					}
				}
			};
		};
		thread.start();
	}

	/**
	 * չʾ�Ի���
	 */
	protected void showUpdataDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���°汾" + versionName);
		builder.setMessage(description);
		builder.setPositiveButton("��������", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// ���������������
				download();

			}
		});
		builder.setNegativeButton("ȡ������", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});

		// ����û��㷵�� �����������

		builder.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		builder.show();
	}

	protected void download() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			tv_download.setVisibility(View.VISIBLE);
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			// xUtils
			HttpUtils xUtils = new HttpUtils();
			xUtils.download(downloadUrl, target, new RequestCallBack<File>() {

				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					// TODO Auto-generated method stub
					super.onLoading(total, current, isUploading);
					System.out.println("�ܴ�С" + total + "���ؽ���" + current);
					tv_download.setText("���ؽ���:" + current * 100 / total + "%");
				}

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("���سɹ�");
					// ��ת��ϵͳ����ҳ��
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					startActivityForResult(intent, 0);// ����û�ȡ����װ�Ļ�,
					// �᷵�ؽ��,�ص�����onActivityResult
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(SplashActivity.this, "����ʧ��", 0);
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "�ڴ濨������", 0);
		}

	}

	// ����û�ȡ����װ�Ļ�,
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����������
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	/**
	 * ��ȡ���ݿ�
	 */
	public  void  copyDB(){
	String 	dbName="address.db";
	     
            File  file=new File(getFilesDir(), dbName);//�ļ���ַ
            if (file.exists()) {
				System.out.println("���ݿ��Ѵ���");
				return;
			}
            try {
				fis = getAssets().open(dbName);
				 out = new FileOutputStream(file);
				 int len=0;
				 byte [] b=new byte[1024];
				 while((len=fis.read(b)) != -1){
					 out.write(b, 0, len);
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					fis.close();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
          
	
	}
	
	
	
	
}
