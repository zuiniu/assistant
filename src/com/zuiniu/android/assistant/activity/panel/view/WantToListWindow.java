/**
 * 
 */
package com.zuiniu.android.assistant.activity.panel.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.view.CustomPopupWindow;

/**
 * @author Administrator
 *
 */
public class WantToListWindow extends CustomPopupWindow implements OnClickListener {
	private View root;
	private Context mContext;
	private ListView listView;
	private List<String> list = new ArrayList<String>();
	private WantToListAdapter adapter;

	public WantToListWindow(View anchor) {
		super(anchor);
		// TODO Auto-generated constructor stub
		mContext = anchor.getContext();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		dismiss();
	}

	@Override
	protected View getContentView() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		root = (ViewGroup) inflater.inflate(R.layout.want_to_do_list, null);
		
		listView = (ListView)root.findViewById(R.id.lv_want_to);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if (view != null) {
					ApplicationListWindow window = new ApplicationListWindow(anchor);
					window.show();
				}
				
				dismiss();
			}
		});
		
		adapter = new WantToListAdapter(mContext, list);
		listView.setAdapter(adapter);
		
		initData();
		
		ViewTreeObserver vto = root.getViewTreeObserver();   
	    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
	        @Override   
	        public void onGlobalLayout() {  
	        	root.getViewTreeObserver().removeGlobalOnLayoutListener(this);   
	            
	        	int width = root.getWidth();
	    		int height = root.getHeight();
	    		
	    		Log.d("WantToListWindow", String.format("width=%d,height=%d", width, height));
	    		
	    		Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    		
	    		Log.d("WantToListWindow", String.format("display width=%d,display height=%d", display.getWidth(), display.getHeight()));
	    		int x = (display.getWidth() - width) / 2;
	    		int y = (display.getHeight()- height) / 3;
	    		
	    		setPosition(x, y);
	        }   
	    }); 
		
		return root;
	}

	private void initData() {
		// TODO Auto-generated method stub
		list.clear();
		list.add("测试一");
		list.add("测试二");
		list.add("测试三");
		list.add("测试四");
		adapter.notifyDataSetChanged();
	}
}
