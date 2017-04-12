package com.itheima.mobilesafe01.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompltetRecriver extends BroadcastReceiver {

	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String sim = sp.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = tm.getSimSerialNumber()+"1";
			if (sim.equals(currentSim)) {
				System.out.println("安全");
            
			}else{
				System.out.println("sim卡已经变化, 发送报警短信!!!");
				   String safephone= sp.getString("safephone", "");  
				   SmsManager smsManager = SmsManager.getDefault();
				   smsManager.sendTextMessage(safephone, null, "sim  change", null, null);
			}
		}

	}

}
