/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel.view;

import java.util.List;

import com.zuiniu.android.assistant.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class WantToListAdapter extends ArrayAdapter<String> {
	private Context mContext;

	private LayoutInflater mInflater;

	public WantToListAdapter(Context context, List<String> objects) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.want_to_do_list_item, null);
			
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvName.setText(getItem(position));
		
		return convertView;
	}

	
	static class ViewHolder {
		public TextView tvName;
	}
}
