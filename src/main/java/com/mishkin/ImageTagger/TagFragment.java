package com.mishkin.ImageTagger;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TagFragment extends Fragment implements Parcelable, View.OnTouchListener {
	private static final String TAG = TagFragment.class.getSimpleName();
	ImageView closeImageView, mainImageView;
	FrameLayout.LayoutParams mLayoutParams;
	Object mData;
	TagCallbackHandler mHandler;
	public double percentWidth = 0.0;
	public double percentHeight = 0.0;

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

	public TagCallbackHandler getHandler() {
		return mHandler;
	}

	public void setHandler(TagCallbackHandler handler) {
		mHandler = handler;
	}

}
