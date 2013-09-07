/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel.view;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.datamanager.bean.panel.Slot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.TextUtils;

/**
 * @author Administrator
 *
 */
public class PannelItemLayout extends RelativeLayout {
	
	private ImageView itemBg, itemDel;
	private TextView itemName;
	private Slot slot;
	private PanelView parent;
	 

	public PannelItemLayout(Context context, PanelView parent) {
		super(context);
		// TODO Auto-generated constructor stub
		this.parent = parent;
		
		itemBg = new ImageView(context);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(CENTER_IN_PARENT);
		itemBg.setLayoutParams(layoutParams);
		int bottom = (int)context.getResources().getDimension(R.dimen.slot_icon_buttom_pedding);
		itemBg.setPadding(0, 0, 0, bottom);
		
		addView(itemBg);
		
		itemName = new TextView(context);
		layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(ALIGN_PARENT_BOTTOM);
		layoutParams.addRule(CENTER_HORIZONTAL);
		layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.slot_text_buttom_margin);
		itemName.setLayoutParams(layoutParams);
		itemName.setSingleLine();
		itemName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		itemName.setTextColor(context.getResources().getColor(R.color.penal_cell_text));
		
		int padding = (int)context.getResources().getDimension(R.dimen.slot_text_padding);
		itemName.setFadingEdgeLength(padding);
		itemName.setHorizontalFadingEdgeEnabled(true);
		itemName.setPadding(padding, 0, padding, 0);
		itemName.setCompoundDrawablePadding(padding / 2);
		
		addView(itemName);
		
		itemDel = new ImageView(context);
		itemDel.setImageResource(R.drawable.close);
		itemDel.setVisibility(View.INVISIBLE);
		addView(itemDel);
		
		Drawable drawable = getResources().getDrawable(R.drawable.slot_empty_bg);
		
		if (drawable != null) {
			setLayoutParams(new AbsListView.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
		} else {
			setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
		}
	}
	
	@Override
	public Bitmap getDrawingCache() {
		// TODO Auto-generated method stub
		//setDrawingCacheEnabled(true);
		
		Bitmap bitmap = super.getDrawingCache();
		
		return bitmap;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}

	
	public void init(Slot slot, boolean isShowDel) {
		if (slot == null) {
			return;
		}
		
		this.slot = slot;
		
		if (this.slot.isEmpty()) {
			setBackgroundResource(R.drawable.slot_empty_bg);
			
			itemBg.setVisibility(View.INVISIBLE);
			itemName.setVisibility(View.INVISIBLE);
			
			itemDel.setVisibility(View.INVISIBLE);
		} else {
			setBackgroundResource(R.drawable.slot_bg);
			
			if (isShowDel) {
				itemDel.setVisibility(View.VISIBLE);
			}
			
			// 这里模拟赋值,真的图片应该是联网获取到的。
			itemBg.setImageResource(R.drawable.switcher_air_mode_state_on);
			itemBg.setVisibility(View.VISIBLE);
			
			itemName.setText(slot.getName());
			
			itemName.setTextSize(17);
			itemName.setGravity(Gravity.CENTER);
			itemName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			itemName.setVisibility(View.VISIBLE);
		}
	}
	
	public Slot getSolt() {
		return slot;
	}
}
