package com.itheima.mobilesafe01.service;

import com.itheima.mobilesafe01.R;
import com.itheima.mobilesafe01.db.Dao.AddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * �����������
 * 
 * @author sunzhaung
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private OutCallReceive ocr;
	private WindowManager mWM;
	private View v;
	private TextView textView1;
	private SharedPreferences sp;
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// TelephonyManager�����绰
		tm.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);// ������绰��״̬
		ocr = new OutCallReceive();
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(ocr, filter);
		sp = getSharedPreferences("config", MODE_PRIVATE);
	}

	class MyListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// System.out.println("�绰����");
				String incoming = AddressDao.getAddress(incomingNumber);
				showToast(incoming);
				// Toast.makeText(AddressService.this, incoming,
				// Toast.LENGTH_LONG).show();
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (mWM != null && v != null) {
					mWM.removeView(v);
				}// ��window���Ƴ�view
				break;
			default:
				break;
			}

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tm.listen(new MyListener(), PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(ocr);
	}

	/**
	 * ����ȥ��Ĺ㲥������ ��ҪȨ��: android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 */
	class OutCallReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String resultData = getResultData();
			String address = AddressDao.getAddress(resultData);
			// Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);
		}

	}

	/**
	 * �Զ�������ظ���
	 */
	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
															// ����������Ӧ�ó���֮�ϣ�״̬��֮�¡�
		params.gravity = Gravity.LEFT + Gravity.TOP;// ������λ������Ϊ���Ϸ�,
													// Ҳ����(0,0)�����Ϸ���ʼ,������Ĭ�ϵ�����λ��
		params.setTitle("Toast");
		v = View.inflate(this, R.layout.toast_address, null);
		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = sp.getInt("set_style", 0);
		
		int lastX = sp.getInt("lastX", 0);
		int lastY = sp.getInt("lastY", 0);

		// ���ø�����λ��, �������Ϸ���ƫ����
		params.x = lastX;
		params.y = lastY;

		v.setBackgroundResource(bgs[style]);
		textView1 = (TextView) v.findViewById(R.id.textView1);
		textView1.setText(text);
		// tv.setTextColor(Color.BLUE);
		mWM.addView(v, params);// �����ͼ��window��

	}
}
