package com.imatrixmob.ffime.popup;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imatrixmob.ffime.R;
import com.imatrixmob.ffime.core.OpenWnn;
import com.imatrixmob.ffime.utils.LogUtil;

/**
 * 
 * @ClassName: SymbolPopu
 * @Description: TODO(符号预览框popu)
 * @author :hulanlan
 * @date 2015-5-28 上午8:44:27
 * 
 */
public class SymbolPopu extends PopupWindow {
	private OpenWnn mContext;

	private View layout;

	private ArrayList<View> views;

	private LinearLayout linearLayout;

	public SymbolPopu(Context context) {
		// TODO Auto-generated constructor stub
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	class MyTextView extends TextView {

		public MyTextView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			init();
		}

		public void init() {
			RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			setLayoutParams(lp1);
			setTextSize(18);
			setGravity(Gravity.CENTER_VERTICAL);
		}

	}

	public SymbolPopu(Context context, int width, int height) {
		this.mContext = (OpenWnn) context;
		// 设置可以获得焦点
		setFocusable(false);
		// 设置弹窗内可点击
		setTouchable(true);
		// 设置弹窗外可点击
		setOutsideTouchable(true);
		views = new ArrayList<View>();

		// 设置弹窗的宽度和高度
		setWidth(width);
		setHeight(height);
		setBackgroundDrawable(new BitmapDrawable());
		// 设置弹窗的宽度和高度
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		layout = LayoutInflater.from(mContext).inflate(R.layout.symbol_popu_skin_one,
				null);

		// linearLayout = (LinearLayout) layout.findViewById(R.id.ll_);
		//
		// linearLayout.setBackground(mContext.getResources().getDrawable(
		// R.drawable.dian_shen));

		TextView t1 = (TextView) layout.findViewById(R.id.tv_1);
		TextView t2 = (TextView) layout.findViewById(R.id.tv_2);
		TextView t3 = (TextView) layout.findViewById(R.id.tv_3);
		TextView t4 = (TextView) layout.findViewById(R.id.tv_4);
		TextView t5 = (TextView) layout.findViewById(R.id.tv_5);
		TextView t6 = (TextView) layout.findViewById(R.id.tv_6);

		// linearLayout.addView(t1);
		// linearLayout.addView(t2);
		// linearLayout.addView(t3);
		t6.setSelected(true);
		views.add(t1);
		views.add(t2);
		views.add(t3);
		views.add(t4);
		views.add(t5);
		views.add(t6);
		// textView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// LogUtil.e("fff", "o");
		// }
		// });

		setContentView(layout);
		// showAtLocation(this, Gravity.NO_GRAVITY, popupKey.x, popupKey.y -
		// 10);
	}

	public ArrayList<View> getViews() {
		return views;
	}
}
