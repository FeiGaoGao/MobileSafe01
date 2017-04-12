package com.itheima.mobilesafe01.receiver;

import com.itheima.mobilesafe01.R;


import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Toast;

public class sMsReceiver extends BroadcastReceiver {
                
	private ComponentName mDeviceAdminSample;
	/**
	 * ���ͷ�������
	 * */
	
	private DevicePolicyManager mDPM;
	
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 mDPM =(DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);// ��ȡ�豸���Է���
		 mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);
		Object[] objects = (Object[]) intent.getExtras().get("pdus");

		for (Object object : objects) {// �������140�ֽ�,
										// �����Ļ�,���Ϊ�������ŷ���,������һ������,��Ϊ���ǵĶ���ָ��ܶ�,����forѭ��ִֻ��һ��
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			String originatingAddress = message.getOriginatingAddress();// ������Դ����
			String messageBody = message.getMessageBody();// ��������

			System.out.println(originatingAddress + ":" + messageBody);

			if ("#*alarm*#".equals(messageBody)) {
				
				// ���ű�������, ��ʹ�ֻ���Ϊ����,Ҳ�ܲ�������, ��Ϊʹ�õ���ý��������ͨ��,�������޹�
				Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
						mDeviceAdminSample);
				intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
						"������, �������˳����豸������, ��NB!");
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();

				abortBroadcast();// �ж϶��ŵĴ���, �Ӷ�ϵͳ����app���ղ���������
			}else if("#*location*#".equals(messageBody)){
				    
			}else if("#*lockscreen*#".equals(messageBody)){
				
//				 Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//					intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
//							mDeviceAdminSample);
//					intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//							"������, �������˳����豸������, ��NB!");
//				
//				context.startActivity(intent1);
				if (mDPM.isAdminActive(mDeviceAdminSample)) {// �ж��豸�������Ƿ��Ѿ�����
					mDPM.lockNow();// ��������
					mDPM.resetPassword("123456", 0);
				} else {
					Toast.makeText(context , "�����ȼ����豸������!", Toast.LENGTH_SHORT).show();
				}
				abortBroadcast();// �ж϶��ŵĴ���, �Ӷ�ϵͳ����app���ղ���������
			}else if("#*wipedata*#".equals(messageBody)){
				if (mDPM.isAdminActive(mDeviceAdminSample)) {// �ж��豸�������Ƿ��Ѿ�����
					mDPM.wipeData(0);// �������,�ָ���������
				} else {
					Toast.makeText(context, "�����ȼ����豸������!", Toast.LENGTH_SHORT).show();
				}
				abortBroadcast();// �ж϶��ŵĴ���, �Ӷ�ϵͳ����app���ղ���������
			}
		}
	}
	
}
