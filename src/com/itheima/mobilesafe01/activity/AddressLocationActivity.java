package com.itheima.mobilesafe01.activity;

import com.itheima.mobilesafe01.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class AddressLocationActivity extends Activity {
	private TextView tv_top;
	private TextView tv_bottom;
	private ImageView iv_drag;
	private SharedPreferences sp;
	private int winWidth;
	private int winHeight;
	long[] mHits = new long[2];// 数组长度表示要点击的次数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_item);
		tv_top = (TextView) findViewById(R.id.tv_top);
		tv_bottom = (TextView) findViewById(R.id.tv_bottom);
		iv_drag = (ImageView) findViewById(R.id.iv_drag);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		int lastX = sp.getInt("lastX", 0);
		int lastY = sp.getInt("lastY", 0);
		// 获取屏幕宽高
		winWidth = getWindowManager().getDefaultDisplay().getWidth();
		winHeight = getWindowManager().getDefaultDisplay().getHeight();
		// 根据图片位置,决定提示框显示和隐藏
		if (lastY > winHeight / 2) {// 上边显示,下边隐藏
			tv_top.setVisibility(View.VISIBLE);
			tv_bottom.setVisibility(View.INVISIBLE);
		} else {
			tv_top.setVisibility(View.INVISIBLE);
			tv_bottom.setVisibility(View.VISIBLE);
		}

		// 将图片的位置赋值给lp 然后将图片显示在存储的位置
		RelativeLayout.LayoutParams lp = (LayoutParams) iv_drag
				.getLayoutParams();
		lp.leftMargin = lastX; // 左边
		lp.topMargin = lastY; // 上边距
		iv_drag.setLayoutParams(lp);// 重新更新设置位置

		iv_drag.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// 把图片居中
					iv_drag.layout(winWidth / 2 - iv_drag.getWidth() / 2,
							iv_drag.getTop(), winWidth / 2 + iv_drag.getWidth()
									/ 2, iv_drag.getBottom());
				}
			}
		});

		/**
		 * 记录移动的位置
		 */
		iv_drag.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 得到初始化位置
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;

					// 更新左上右下距离
					int l = iv_drag.getLeft() + dx;
					int r = iv_drag.getRight() + dx;

					int t = iv_drag.getTop() + dy;
					int b = iv_drag.getBottom() + dy;
					// 判断是否超出屏幕边界, 注意状态栏的高度
					if (l < 0 || r > winWidth || t < 0 || b > winHeight - 20) {
						break;
					}

					// 根据图片位置,决定提示框显示和隐藏
					if (t > winHeight / 2) {// 上边显示,下边隐藏
						tv_top.setVisibility(View.VISIBLE);
						tv_bottom.setVisibility(View.INVISIBLE);
					} else {
						tv_top.setVisibility(View.INVISIBLE);
						tv_bottom.setVisibility(View.VISIBLE);
					}

					// 更新界面
					iv_drag.layout(l, t, r, b);

					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					sp.edit().putInt("lastX", iv_drag.getLeft()).commit();
					sp.edit().putInt("lastY", iv_drag.getTop()).commit();

					break;
				default:
					break;
				}
				return true;

			}
		});
	}

}
