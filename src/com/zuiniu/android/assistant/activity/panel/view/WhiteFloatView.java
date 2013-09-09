package com.zuiniu.android.assistant.activity.panel.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.activity.panel.PanelActivity;
import com.zuiniu.android.assistant.utils.DensityUtil;

/**
 * 桌面上浮动的图标
 * <P>
 * 可以拖动，点击弹出设置activity
 * 
 * @author guoruiliang
 * 
 */
public class WhiteFloatView extends View {
	private Context mContext;
	private WindowManager mWManager; // WindowManager
	private WindowManager.LayoutParams mWMParams; // WindowManager参数
	private ImageView mImageView;// 浮动图标
	private int mTag = 0;
	private int mOldOffsetX;
	private int mOldOffsetY;
	WindowManager.LayoutParams wmParams;

	private boolean isShow=false;
	
	public WhiteFloatView(Context context) {
		super(context);
		mContext = context;
	}

	public void create() {
		// 设置载入view WindowManager参数
		mWManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		mImageView = new ImageView(mContext);
		mImageView.setImageResource(R.drawable.whitespot);
		mImageView.setOnTouchListener(mTouchListener);
		wmParams = new WindowManager.LayoutParams();
		mWMParams = wmParams;
		wmParams.type = LayoutParams.TYPE_PHONE;// 系统级窗口
		wmParams.flags = 40;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.format = PixelFormat.RGBA_8888; // 透明
		// 以屏幕中心为原点，设置x、y初始值,默认为右边中间
		wmParams.x = DensityUtil.getScreenWidth(mContext) / 2;
		wmParams.y = 0;

	}

	public void show() {
		if(!isShow){
			mWManager.addView(mImageView, wmParams);	
			isShow=true;
		}
		
	}

	public void remove() {
		if(isShow){
			mWManager.removeView(mImageView);	
			isShow=false;
		}
		
	}

	private OnTouchListener mTouchListener = new OnTouchListener() {
		// 触屏监听
		float lastX, lastY;

		public boolean onTouch(View v, MotionEvent event) {
			final int action = event.getAction();

			float x = event.getX();
			float y = event.getY();

			if (mTag == 0) {
				mOldOffsetX = mWMParams.x; // 偏移量
				mOldOffsetY = mWMParams.y; // 偏移量
			}

			if (action == MotionEvent.ACTION_DOWN) {
				lastX = x;
				lastY = y;

			} else if (action == MotionEvent.ACTION_MOVE) {
				mWMParams.x += (int) (x - lastX); // 偏移量
				mWMParams.y += (int) (y - lastY); // 偏移量

				mTag = 1;
				mWManager.updateViewLayout(mImageView, mWMParams);
			}

			else if (action == MotionEvent.ACTION_UP) {
				int newOffsetX = mWMParams.x;
				int newOffsetY = mWMParams.y;
				if (mOldOffsetX == newOffsetX && mOldOffsetY == newOffsetY) {
					Intent intent = new Intent(mContext, PanelActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);

				} else {
					mTag = 0;
				}
			}
			return true;
		}
	};

}
