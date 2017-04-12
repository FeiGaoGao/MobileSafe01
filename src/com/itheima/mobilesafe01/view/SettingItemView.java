package com.itheima.mobilesafe01.view;

import com.itheima.mobilesafe01.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {
	public static String namespace = "http://schemas.android.com/apk/res/com.itheima.mobilesafe01";
	private TextView tv_title;
	private TextView tv_desc;
	private CheckBox cb_status;
	private String mTitle;
	private String mDescOn;
	private String mDescOff;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTitle = attrs.getAttributeValue(namespace, "title");
		mDescOn = attrs.getAttributeValue(namespace, "desc_on");
		mDescOff = attrs.getAttributeValue(namespace, "desc_off");
		initView();

	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	public void initView() {
		View.inflate(getContext(), R.layout.setting_list_item, this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		setTitle(mTitle);

	}

	public void setTitle(String title) {
		tv_title.setText(title);
	}

	public void setDesc(String desc) {
		tv_desc.setText(desc);
	}

	public boolean isChecked() {
		return cb_status.isChecked();
	}

	public void setChecked(boolean checked) {
		cb_status.setChecked(checked);

		// 根据选择的状态,更新文本描述
		if (checked) {
			setDesc(mDescOn);
		} else {
			setDesc(mDescOff);
		}

	}

	

}
