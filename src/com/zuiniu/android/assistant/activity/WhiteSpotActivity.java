/**
 * 
 */
package com.zuiniu.android.assistant.activity;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.activity.panel.PanelActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * @author Administrator
 * 
 */
public class WhiteSpotActivity extends Activity implements View.OnClickListener {
	private static final String tag = WhiteSpotActivity.class.getSimpleName();

	private LinearLayout theme_apply_single;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(tag, "你点击了按钮");
		switch (v.getId()) {
		case R.id.theme_apply_single:
			Intent intent = new Intent(WhiteSpotActivity.this, PanelActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 设置窗口全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.preview);

		theme_apply_single = (LinearLayout) findViewById(R.id.theme_apply_single);
		theme_apply_single.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
