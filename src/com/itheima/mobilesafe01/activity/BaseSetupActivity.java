package com.itheima.mobilesafe01.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public abstract class BaseSetupActivity extends Activity {
	private GestureDetector mDetector;
	private SharedPreferences sp;
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
           sp = getSharedPreferences("config",MODE_PRIVATE);

	mDetector = new GestureDetector(this,
			new SimpleOnGestureListener() {
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					// ���һ�,��һҳ
					if (e2.getRawX() - e1.getRawX() > 200) {
						showPreviousPage();
						return true;
					}

					// ����, ��һҳ
					if (e1.getRawX() - e2.getRawX() > 200) {
						showNextPage();
						return true;
					}
					return super.onFling(e1, e2, velocityX, velocityY);
				}

			});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	protected abstract void showPreviousPage();

	protected abstract void showNextPage();

	// �����һҳ��ť
	public void next(View v) {
		showNextPage();
	}

	// �����һҳ��ť
	public void previous(View v) {
		showPreviousPage();
	}
}
