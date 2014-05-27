package com.mishkin.ImageTagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

public class ImageTaggerFragment extends Fragment implements TagCallbackHandler, View.OnTouchListener {
	private static final String TAG = ImageTaggerFragment.class.getSimpleName();
	boolean fingerDown = false;
	TagFragment selectedTagFragment = null;
	int mTagContainer, mTagEnterAnimation, mTagExitAnimation, mTagSelectedAnimation, mTagDeselectedAnimation;
	int mTagWidth = 140, mTagHeight = 140;

	public ImageTaggerFragment(int tagContainer) {
		mTagContainer = tagContainer;
		mTagEnterAnimation = R.anim.zoom_in;
		mTagExitAnimation = R.anim.zoom_out;
		mTagDeselectedAnimation = R.anim.zoom_normal;
		mTagSelectedAnimation = R.anim.zoom_large;
	}

	public ImageTaggerFragment(int tagContainer, int tagEnterAnimation, int tagExitAnimation) {
		mTagContainer = tagContainer;
		mTagEnterAnimation = tagEnterAnimation;
		mTagExitAnimation = tagExitAnimation;
		mTagDeselectedAnimation = R.anim.zoom_normal;
		mTagSelectedAnimation = R.anim.zoom_large;
	}

	public ImageTaggerFragment(int tagContainer, int tagEnterAnimation, int tagExitAnimation, int tagSelectedAnimation, int tagDeselectedAnimation) {
		mTagContainer = tagContainer;
		mTagEnterAnimation = tagEnterAnimation;
		mTagExitAnimation = tagExitAnimation;
		mTagSelectedAnimation = tagSelectedAnimation;
		mTagDeselectedAnimation = tagDeselectedAnimation;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		View v = inflater.inflate(mTagContainer, container, false);

		v.setOnTouchListener(this);

		return v;
	}

	public void removeTag(TagFragment tag) {
		getFragmentManager().beginTransaction()
			.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out)
			.remove(tag)
			.addToBackStack(null)
			.commit();
	}

	public void deselectTag(TagFragment tag) {
		fingerDown = false;
		selectedTagFragment = null;
		Animation zoomToLargeAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), mTagDeselectedAnimation);
		tag.getView().startAnimation(zoomToLargeAnimation);
	}

	public void selectTag(TagFragment tag) {
		if(selectedTagFragment == null) {
			Animation zoomToLargeAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), mTagSelectedAnimation);
			tag.getView().startAnimation(zoomToLargeAnimation);
			tag.getView().bringToFront();
			selectedTagFragment = tag;
			fingerDown = true;
		}
	}

	private void addNewTagFragment(int x, int y) {
		TagFragment tag = new TagFragment(this, null);

		getFragmentManager()
			.beginTransaction()
			.setCustomAnimations(mTagEnterAnimation, mTagExitAnimation)
			.add(R.id.tagger_fragment_container, tag)
			.addToBackStack(null)
			.commit();

		moveTagTo(tag, x, y);

		fingerDown = false;
	}

	private void moveTagTo(TagFragment tag, Integer x, Integer y) {
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(x - mTagWidth, y - mTagHeight, 0, 0);
		tag.setPosition(lp);

		if(tag.getView() != null) {
			tag.getView().setLayoutParams(lp);
			getView().invalidate();
		}
	}

	@Override
	public boolean onTagEvent(TagFragment tag, String tagEvent, Object data) {
		Toast.makeText(getActivity().getApplicationContext(), "Tag Event: " + tagEvent, Toast.LENGTH_SHORT).show();

		if( tagEvent.equals("CloseClicked")) {
			removeTag(tag);
		} else if (tagEvent.equals("ClickDown")) {
			selectTag(tag);
		} else if (tagEvent.equals("ClickUp")) {
			deselectTag(tag);
		}

		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if(selectedTagFragment != null) {
				moveTagTo(selectedTagFragment, (int)event.getX(), (int)event.getY());
			}
			break;
		case MotionEvent.ACTION_UP:
			if(selectedTagFragment != null) {
				Animation zoomToNormalAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), mTagDeselectedAnimation);
				selectedTagFragment.getView().startAnimation(zoomToNormalAnimation);
				selectedTagFragment = null;
			} else {
				addNewTagFragment((int)event.getX(), (int)event.getY());
			}
			break;
		}

		return true;
	}
}
