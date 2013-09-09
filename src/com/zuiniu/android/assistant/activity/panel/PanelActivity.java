/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel;

import java.util.ArrayList;
import java.util.List;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.activity.panel.view.PanelAdapter;
import com.zuiniu.android.assistant.activity.panel.view.PanelView;
import com.zuiniu.android.assistant.datamanager.bean.panel.Slot;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Administrator
 * 
 */
public class PanelActivity extends Activity implements View.OnTouchListener {
	
	private PanelView panel;
	private PanelAdapter adapter;
	private List<Slot> slots = new ArrayList<Slot>();

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.panel);
		
		panel = (PanelView)findViewById(R.id.grid);
		adapter = new PanelAdapter(this, panel);
		panel.setAdapter(adapter);
		
		for (int i = 0; i < 9; i++) {
			Slot slot = new Slot();
			slot.setEmpty(true);
			slots.add(slot);
		}
		
		adapter.refreshData(slots);
	}

}
