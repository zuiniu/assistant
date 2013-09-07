/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel.view;

import java.util.ArrayList;
import java.util.List;

import com.zuiniu.android.assistant.datamanager.bean.panel.Slot;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Administrator
 *
 */
public class PanelAdapter extends BaseAdapter {
	private List<Slot> slots = new ArrayList<Slot>();
	private PanelView parent;
	private Activity context;
	private boolean isShowDel = false;
	
	public PanelAdapter(Activity context, PanelView parent) {
		this.context = context;
		this.parent = parent;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (slots == null)
			return 0;
		
		return slots.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return slots.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PannelItemLayout layout = (PannelItemLayout)convertView;
		Slot slot = slots.get(position);
		if (layout == null) {
			layout = new PannelItemLayout(context, this.parent);
		}
		layout.init(slot, isShowDel);
		
		return layout;
	}
	
	public void refreshData(final List<Slot> slots) {
		this.slots = slots;
		
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
	
	public void showDelete(final boolean isShowDel) {
		this.isShowDel = isShowDel;
		
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

}
