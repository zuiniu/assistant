/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel.view;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.AdapterView;

/**
 * @author Administrator
 * 
 */
public class PanelView extends GridView implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

	private final Vibrator vibrator;
	private final WindowManager.LayoutParams layoutParams;
	private Context context;

	public PanelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
		layoutParams = new WindowManager.LayoutParams();
		layoutParams.gravity = 51;//Gravity.
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = LayoutParams.MATCH_PARENT;
		layoutParams.flags = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED;
		layoutParams.format = PixelFormat.OPAQUE;
		//layoutParams.windowAnimations = null;
		this.setLayoutParams(layoutParams);
		setOnItemClickListener(this);
	    setOnItemLongClickListener(this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		int count = parent.getCount();
		for(int i =0; i < count; i++) {
			PanelAdapter adapter = (PanelAdapter)parent.getAdapter();
			
			if (adapter != null) {
				adapter.showDelete(true);
			}
		}
		
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		PannelItemLayout itemView = (PannelItemLayout)view;
		
		if (itemView != null) {
			if (itemView.getSolt().isEmpty()) {
				WantToListWindow window = new WantToListWindow(view);
				window.show();
			}
		}
	}
	
	

}
