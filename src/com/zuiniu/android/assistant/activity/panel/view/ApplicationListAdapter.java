/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel.view;

import java.util.List;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.datamanager.bean.panel.AppInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class ApplicationListAdapter extends BaseAdapter {
	
	private List<AppInfo> applications;
	private Context mContext;

	private LayoutInflater mInflater;
	
	public ApplicationListAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (applications == null)
			return 0;
		
		return applications.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return applications.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AppInfo bean  = applications.get(position);
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.application_list_item, null);
			
			viewHolder.tvAppName = (TextView)convertView.findViewById(R.id.app_name);
			viewHolder.ivAppIcon = (ImageView)convertView.findViewById(R.id.app_icon);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvAppName.setText(bean.getAppName());
		if (bean.getIcon() != null) {
			viewHolder.ivAppIcon.setImageDrawable(bean.getIcon());
		}
		
		return convertView;
	}
	
	/** 设置数据源 */
	public void setDatas(List<AppInfo> applications) {
		this.applications = applications;
	}

	static class ViewHolder {
		public TextView tvAppName;
		public ImageView ivAppIcon;
	}
}
