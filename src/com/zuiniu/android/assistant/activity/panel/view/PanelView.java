/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel.view;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.view.CustomPopupWindow;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author Administrator
 * 
 */
public class PanelView extends GridView implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {

	private final Vibrator vibrator;
	private final WindowManager.LayoutParams layoutParams;
	private Context context;
	private CustomPopupWindow mWindow;
	private ImageView image;

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
		setOnItemSelectedListener(this);
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d("PanelView", "你选择了第" + position + "个子项");
		
		/*
		 * 事实上知道了在哪个监听上去完成的话，剩下的情况就简单了，跟cocos2d的子精灵添加定位也差不了多少，动画和图片大小。可以看情况增加。
		 * 事实上我对代码来控制视图的方式更加了解和清晰，如果使用xml的方式也可以在里面增加framelayout然后下层是放的gridview,上层先定位好图片
		 * 然后对应的图片显示隐藏就可以了，这样代码比较简单，不需要定位。
		 * 
		 * 我对android比较陌生的是
		 * 1、看到相应的东西要马上响应用什么布局，如何布局，对应的监听应该使用哪个才合适，这些敏感度需要提高。
		 * 2、后台相关操作如果定义service、alarm、broadcast receiver该如何灵活定义配合使用(当然在近期的阅读书籍和示例锻炼中已经有一定的认识和提高了)
		 * 3、针对一些android特有比较奇特的控件开发和使用，提示等功能等整体认识。
		 * 
		 * 我比较有自信优势的是：
		 * 我的编码和学习能力，还有就是对应之前我学过gdi+和一些游戏类框架和类库，对于一些视图(游戏叫精灵和层)创建位置动画等那个东西我搞多了实在太熟悉了，遍地一想就觉得搞过的。
		 */
		RelativeLayout layout = (RelativeLayout)parent.getParent();

		int[] location = new int[2];
		view.getLocationInWindow(location);
		
		if (layout != null) {
			Log.d("PanelView", "有");
			
			if (image != null) {
				layout.removeView(image);
				image = null;
			}
			
			image = new ImageView(context);
			image.setImageResource(R.drawable.switcher_air_mode_state_on);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(location[0], location[1], 0, 0);
			layout.addView(image, params);
		} else {
			Log.d("PanelView", "没有");
		}
		
		/*
		// 刚开始尝试使用popupWindow,那样不行，会产生popupWindow抢占了焦点的问题。
		if (mWindow != null) {
			mWindow.dismiss();
			mWindow = null;
		}
		
		mWindow = new CustomPopupWindow(image);
		
		int[] location = new int[2];
		
		//view.getLocationOnScreen(location);
		view.getLocationInWindow(location);
		
		mWindow.show(location[0], location[1]);
		*/
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		Log.d("PanelView", "你没选择任何东西");
		
		if (mWindow != null) {
			mWindow.dismiss();
			mWindow = null;
		}
		
		if (image != null) {
			RelativeLayout layout = (RelativeLayout)parent.getParent();
			if (layout != null) {
				layout.removeView(image);
			}
			
			image = null;
		}
	}
	
	

}
