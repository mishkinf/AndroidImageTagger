package com.mishkin.ImageTagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TagFragment extends Fragment implements View.OnTouchListener {
	ImageView closeImageView, mainImageView;
	FrameLayout.LayoutParams mLayoutParams;
	TagCallbackHandler mHandler;
	double mPercentWidth = 0.0;
	double mPercentHeight = 0.0;
	boolean mSelected;
	Object mData;

	public TagFragment() {
		mData = null;
	}

	public static TagFragment newInstance(TagCallbackHandler handler, Object data) {
		TagFragment tagFragment = new TagFragment();
		tagFragment.setHandler(handler);
		tagFragment.setData(data);
		return tagFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tag, container, false);
		TextView tagTextView;

		if(mLayoutParams != null) {
			v.setLayoutParams(mLayoutParams);
		}

		final TagFragment self = this;
		mainImageView = (ImageView)v.findViewById(R.id.main_image_view);
		mainImageView.setOnTouchListener(this);

		tagTextView = (TextView)v.findViewById(R.id.tag_text_view);
		if(mData != null)
			tagTextView.setText(mData.toString());

		closeImageView = (ImageView)v.findViewById(R.id.close_image_view);
		closeImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHandler.onTagEvent(self, "CloseClicked", null);
			}
		});

		setSelected(true);
		mHandler.onTagCreated(this);

		return v;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mHandler.onTagEvent(this, "ClickDown", null);
			return false;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			return false;
		case MotionEvent.ACTION_MOVE:
			mHandler.onTagEvent(this, "ClickMove", event);
			return false;
		case MotionEvent.ACTION_UP:
			mHandler.onTagEvent(this, "ClickUp", null);
			return false;
		}
		return false;
	}

	public void setSelected(boolean selected) {
		mSelected = selected;

		if(mSelected) {
			closeImageView.setVisibility(View.VISIBLE);
		} else {
			closeImageView.setVisibility(View.GONE);
		}
	}

	public boolean getSelect() {
		return mSelected;
	}

	public void setPosition(FrameLayout.LayoutParams layoutParams) {
		this.mLayoutParams = layoutParams;
	}

	public FrameLayout.LayoutParams getPosition() {
		return mLayoutParams;
	}

	public void setData(Object data) {
		this.mData = data;
	}

	public Object getData() {
		return this.mData;
	}

	public TagCallbackHandler getHandler() {
		return mHandler;
	}

	public void setHandler(TagCallbackHandler handler) {
		mHandler = handler;
	}

	public double getPercentWidth() {
		return mPercentWidth;
	}

	public void setPercentWidth(double percentWidth) {
		this.mPercentWidth = percentWidth;
	}

	public double getPercentHeight() {
		return mPercentHeight;
	}

	public void setPercentHeight(double percentHeight) {
		this.mPercentHeight = percentHeight;
	}

}
