/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.datamanager.bean.panel.AppInfo;
import com.zuiniu.android.assistant.view.CustomPopupWindow;

/**
 * @author Administrator
 *
 */
public class ApplicationListWindow extends CustomPopupWindow implements OnClickListener {
	private View root;
	private Context mContext;
	private ListView listView;
	private List<AppInfo> appList = new ArrayList<AppInfo>();
	private ApplicationListAdapter mAdapter;
	

	public ApplicationListWindow(View anchor) {
		super(anchor);
		// TODO Auto-generated constructor stub
		mContext = anchor.getContext();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		// 关闭弹出框
		dismiss();
	}

	@Override
	protected View getContentView() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		root = (ViewGroup) inflater.inflate(R.layout.application_list, null);
		
		listView = (ListView)root.findViewById(R.id.application_listview);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mAdapter = new ApplicationListAdapter(mContext);
		listView.setAdapter(mAdapter);
		
		initData();
		
		return root;
	}

	private void initData() {
		// TODO Auto-generated method stub
		PackageManager pm = mContext.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.MATCH_DEFAULT_ONLY);
		// 调用系统排序 ， 根据name排序  
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序  
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
        for (ResolveInfo reInfo : resolveInfos) {
        	AppInfo bean = new AppInfo();
        	
        	bean.setActivityName(reInfo.activityInfo.name);
        	bean.setAppName((String) reInfo.loadLabel(pm));
        	bean.setPackageName(reInfo.activityInfo.packageName);
        	bean.setIcon(reInfo.loadIcon(pm));
        	
        	appList.add(bean);
        }
        
        mAdapter.setDatas(appList);
        mAdapter.notifyDataSetChanged();
	}
}
