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
	 * 发送防盗短信
	 * */
	
	private DevicePolicyManager mDPM;
	
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 mDPM =(DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
		 mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);
		Object[] objects = (Object[]) intent.getExtras().get("pdus");

		for (Object object : objects) {// 短信最多140字节,
										// 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			String originatingAddress = message.getOriginatingAddress();// 短信来源号码
			String messageBody = message.getMessageBody();// 短信内容

			System.out.println(originatingAddress + ":" + messageBody);

			if ("#*alarm*#".equals(messageBody)) {
				
				// 播放报警音乐, 即使手机调为静音,也能播放音乐, 因为使用的是媒体声音的通道,和铃声无关
				Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
						mDeviceAdminSample);
				intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
						"哈哈哈, 我们有了超级设备管理器, 好NB!");
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();

				abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
			}else if("#*location*#".equals(messageBody)){
				    
			}else if("#*lockscreen*#".equals(messageBody)){
				
//				 Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//					intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
//							mDeviceAdminSample);
//					intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//							"哈哈哈, 我们有了超级设备管理器, 好NB!");
//				
//				context.startActivity(intent1);
				if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
					mDPM.lockNow();// 立即锁屏
					mDPM.resetPassword("123456", 0);
				} else {
					Toast.makeText(context , "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
				}
				abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
			}else if("#*wipedata*#".equals(messageBody)){
				if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
					mDPM.wipeData(0);// 清除数据,恢复出厂设置
				} else {
					Toast.makeText(context, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
				}
				abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
			}
		}
	}
	
}
