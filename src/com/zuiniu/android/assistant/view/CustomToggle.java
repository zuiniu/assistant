package com.zuiniu.android.assistant.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.zuiniu.android.assistant.R;
import com.zuiniu.android.assistant.utils.DensityUtil;

/**
 * 自定义toggle控件
 * <P>
 * 通过xml文件设定的key属性，自动保存相应设置到prefernce中
 * 
 * @author guoruiliang
 * 
 */
public class CustomToggle extends RelativeLayout {

	private ImageButton mImageButton;
	private ToggleButton mToogleButton;
	protected Context mContext;
	private static final int DEFAULT_TEXT_SIZE = 18;// 默认文字大小
	private static final int DEFAULT_TEXT_COLOR = Color.WHITE; // 默认文字颜色
	private static final int DEFAULT_MOVE_SIZE = 60;// 默认移动大小
	private boolean isCheck = false;

	private float moveSize = DEFAULT_MOVE_SIZE;// 图标移动距离
	private String key = "";// preference 名称

	private Runnable checkChanged;

	public void setCheckChanged(Runnable checkChanged) {
		this.checkChanged = checkChanged;
	}

	public CustomToggle(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setWillNotDraw(false);

		/***** 获取属性值 **********/

		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.CustomToggle);

		float textSize = array.getDimension(
				R.styleable.CustomToggle_toggle_text_size, DEFAULT_TEXT_SIZE);
		float textPadding = array.getDimension(
				R.styleable.CustomToggle_toggle_text_padding, 12);

		moveSize = array.getDimension(
				R.styleable.CustomToggle_toggle_move_size, DEFAULT_MOVE_SIZE);
		key = array.getString(R.styleable.CustomToggle_toggle_key);

		int textColor = array.getColor(
				R.styleable.CustomToggle_toggle_text_color, DEFAULT_TEXT_COLOR);

		String textOn = array
				.getString(R.styleable.CustomToggle_toggle_text_on);
		String textOff = array
				.getString(R.styleable.CustomToggle_toggle_text_off);

		Drawable background = array
				.getDrawable(R.styleable.CustomToggle_toggle_background);
		Drawable thumb = array
				.getDrawable(R.styleable.CustomToggle_toggle_thumb);

		array.recycle(); // 一定要调用，否则会有问题

		/**** 设置View ******/
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.custom_toggle, null);

		addView(layout);
		mToogleButton = (ToggleButton) layout.findViewById(R.id.toggle);
		mImageButton = (ImageButton) layout.findViewById(R.id.image);

		mToogleButton.setTextSize(DensityUtil.px2dip(context, textSize));
		mToogleButton.setTextColor(textColor);
		mToogleButton.setPadding((int) textPadding, 0, (int) textPadding, 0);
		mToogleButton.setBackgroundDrawable(background);
		mImageButton.setImageDrawable(thumb);
		mToogleButton.setTextOn(textOn);
		mToogleButton.setTextOff(textOff);

		initViews();
	}

	private void initViews() {
		// 保存设置
		mToogleButton.setChecked(SettingUtils.get(mContext, key, false));

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToogleButton
				.getLayoutParams();
		if (isCheck) { // 如果是自动播放
			// 调整位置
			params.addRule(RelativeLayout.ALIGN_RIGHT, -1);
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.image);
			mToogleButton.setLayoutParams(params);
			mToogleButton.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		} else {
			// 调整位置
			params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.toggle);
			params.addRule(RelativeLayout.ALIGN_LEFT, -1);
			mToogleButton.setLayoutParams(params);
			mToogleButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		}

		mToogleButton.setOnCheckedChangeListener(new ToggleListener());
	}

	public void toggle() {
		mToogleButton.toggle();
	}

	class ToggleListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			// 保存设置
			SettingUtils.set(mContext, key, isChecked);
			// 播放动画
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageButton
					.getLayoutParams();
			if (isChecked) {
				// 调整位置
				params.addRule(RelativeLayout.ALIGN_RIGHT, -1);
				params.addRule(RelativeLayout.ALIGN_LEFT, R.id.toggle);

				mImageButton.setLayoutParams(params);
				mToogleButton.setGravity(Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);
				// 播放动画
				TranslateAnimation animation = new TranslateAnimation(moveSize,
						0, 0, 0);
				animation.setDuration(200);
				mImageButton.startAnimation(animation);
			} else {
				// 调整位置
				params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.toggle);
				params.addRule(RelativeLayout.ALIGN_LEFT, -1);
				mImageButton.setLayoutParams(params);

				mToogleButton
						.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				// 播放动画
				TranslateAnimation animation = new TranslateAnimation(
						-moveSize, 0, 0, 0);
				animation.setDuration(200);
				mImageButton.startAnimation(animation);
			}

			if (checkChanged != null) {
				checkChanged.run();
			}
		}

	}

	static class SettingUtils {

		/**
		 * 获取配置
		 * 
		 * @param context
		 * @param name
		 * @param defaultValue
		 * @return
		 */
		public static boolean get(Context context, String name,
				boolean defaultValue) {
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			boolean value = prefs.getBoolean(name, defaultValue);
			return value;
		}

		/**
		 * 保存用户配置
		 * 
		 * @param context
		 * @param name
		 * @param value
		 * @return
		 */
		public static boolean set(Context context, String name, boolean value) {
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor editor = prefs.edit();
			editor.putBoolean(name, value);
			return editor.commit(); // 提交
		}
	}

}