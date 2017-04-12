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
	long[] mHits = new long[2];// ���鳤�ȱ�ʾҪ����Ĵ���

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
		// ��ȡ��Ļ���
		winWidth = getWindowManager().getDefaultDisplay().getWidth();
		winHeight = getWindowManager().getDefaultDisplay().getHeight();
		// ����ͼƬλ��,������ʾ����ʾ������
		if (lastY > winHeight / 2) {// �ϱ���ʾ,�±�����
			tv_top.setVisibility(View.VISIBLE);
			tv_bottom.setVisibility(View.INVISIBLE);
		} else {
			tv_top.setVisibility(View.INVISIBLE);
			tv_bottom.setVisibility(View.VISIBLE);
		}

		// ��ͼƬ��λ�ø�ֵ��lp Ȼ��ͼƬ��ʾ�ڴ洢��λ��
		RelativeLayout.LayoutParams lp = (LayoutParams) iv_drag
				.getLayoutParams();
		lp.leftMargin = lastX; // ���
		lp.topMargin = lastY; // �ϱ߾�
		iv_drag.setLayoutParams(lp);// ���¸�������λ��

		iv_drag.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();// ������ʼ�����ʱ��
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// ��ͼƬ����
					iv_drag.layout(winWidth / 2 - iv_drag.getWidth() / 2,
							iv_drag.getTop(), winWidth / 2 + iv_drag.getWidth()
									/ 2, iv_drag.getBottom());
				}
			}
		});

		/**
		 * ��¼�ƶ���λ��
		 */
		iv_drag.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// �õ���ʼ��λ��
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// �����ƶ�ƫ����
					int dx = endX - startX;
					int dy = endY - startY;

					// �����������¾���
					int l = iv_drag.getLeft() + dx;
					int r = iv_drag.getRight() + dx;

					int t = iv_drag.getTop() + dy;
					int b = iv_drag.getBottom() + dy;
					// �ж��Ƿ񳬳���Ļ�߽�, ע��״̬���ĸ߶�
					if (l < 0 || r > winWidth || t < 0 || b > winHeight - 20) {
						break;
					}

					// ����ͼƬλ��,������ʾ����ʾ������
					if (t > winHeight / 2) {// �ϱ���ʾ,�±�����
						tv_top.setVisibility(View.VISIBLE);
						tv_bottom.setVisibility(View.INVISIBLE);
					} else {
						tv_top.setVisibility(View.INVISIBLE);
						tv_bottom.setVisibility(View.VISIBLE);
					}

					// ���½���
					iv_drag.layout(l, t, r, b);

					// ���³�ʼ���������
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
