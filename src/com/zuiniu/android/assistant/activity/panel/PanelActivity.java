/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel;

import com.zuiniu.android.assistant.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Administrator
 * 
 */
public class PanelActivity extends Activity implements View.OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panel);
	}

}
