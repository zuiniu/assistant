/**
 * 
 */
package com.zuiniu.android.assistant.view;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

/**
 * @author Administrator
 *
 */
public class CustomPopupWindow {
	protected final View anchor;
	protected final PopupWindow window;
	private View root;
	protected final WindowManager windowManager;

	public CustomPopupWindow(View anchor) {
		this.anchor = anchor;
		this.window = new PopupWindow(anchor.getContext());

		// 设置触摸拦截
		window.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					// 如果触摸在外面，关闭窗口
					CustomPopupWindow.this.window.dismiss();
					// 截获的触摸事件将不再往里层冒泡
					return true;
				}

				return false;
			}
		});

		windowManager = (WindowManager) anchor.getContext().getSystemService(
				Context.WINDOW_SERVICE);
	}

	private void showWindows(int x, int y) {
		if (root == null) {
			throw new IllegalStateException(
					"setContentView was not called with a view to display.");
		}
		// 定义宽度
		window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		// 定义高度
		window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		// 可以触摸
		window.setTouchable(true);
		// 可以获取焦点
		window.setFocusable(true);
		// 窗口外包也可以触摸
		window.setOutsideTouchable(true);
		// 设置窗口的内容
		window.setContentView(root);
		// 按制定位置显示窗口
		window.showAtLocation(this.anchor, Gravity.NO_GRAVITY, x, y);
	}

	/** 弹出popwindow */
	public void show(int x, int y) {
		this.root = getContentView();
		window.setContentView(root);
		showWindows(x, y);
	}
	
	public void show() {
		show(30, 130);
	}

	/** 自定义view，子类重写定义 */
	protected View getContentView() {
		return anchor;
	}

	/** 关闭popwindow */
	public void dismiss() {
		window.dismiss();
	}
}
