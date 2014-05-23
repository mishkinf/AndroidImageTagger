package com.mishkin.ImageTagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TagFragment extends Fragment {
	ImageView closeImageView, mainImageView;
	FrameLayout.LayoutParams mLayoutParams;
	Object metaData;
	TagCallbackHandler mHandler;

	public TagFragment() {
	}

	public TagFragment(TagCallbackHandler handler, Object metaData) {
		mHandler = handler;
		this.metaData = metaData;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tag, container, false);

		if(mLayoutParams != null)
			v.setLayoutParams(mLayoutParams);

		final TagFragment self = this;
		mainImageView = (ImageView)v.findViewById(R.id.main_image_view);
		mainImageView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mHandler.onTagEvent(self, "ClickDown", null);
					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_OUTSIDE:
					return false;
				case MotionEvent.ACTION_MOVE:
					mHandler.onTagEvent(self, "ClickMove", event);
					return false;
				case MotionEvent.ACTION_UP:
					mHandler.onTagEvent(self, "ClickUp", null);
					return false;
				}
				return false;
			}
		});

		closeImageView = (ImageView)v.findViewById(R.id.close_image_view);
		closeImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHandler.onTagEvent(self, "CloseClicked", null);
			}
		});

		return v;
	}

	public void setLayoutParams(FrameLayout.LayoutParams layoutParams) {
		this.mLayoutParams = layoutParams;
	}

	public FrameLayout.LayoutParams getLayoutParams() {
		return mLayoutParams;
	}

	public void setMetaData(Object data) {
		this.metaData = data;
	}

	public Object getMetaData() {
		return this.metaData;
	}
}
