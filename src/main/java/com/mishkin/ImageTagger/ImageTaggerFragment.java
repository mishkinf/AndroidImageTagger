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

import java.util.ArrayList;
import java.util.List;

public class ImageTaggerFragment extends Fragment implements TagCallbackHandler, View.OnTouchListener {
	private static final String TAG = ImageTaggerFragment.class.getSimpleName();
	private static final String TAG_CONTAINER = "tag_container";
	private static final String TAG_FRAGMENTS = "tag_fragments";
	public static int Instances = 0;
	public int instanceNum = -1;
	boolean fingerDown = false;
	TagFragment selectedTagFragment = null;
	int mTagContainer = R.layout.fragment_tagger;
	int mTagEnterAnimation = R.anim.zoom_in;
	int mTagExitAnimation = R.anim.zoom_out;
	int mTagSelectedAnimation = R.anim.zoom_large;
	int mTagDeselectedAnimation = R.anim.zoom_normal;
	int mTagWidth = 140, mTagHeight = 140;
	List<TagFragment> mTagFragmentList = new ArrayList<TagFragment>();

	public ImageTaggerFragment() {
		selectedTagFragment = null;
		mTagContainer = R.layout.fragment_tagger;
		mTagEnterAnimation = R.anim.zoom_in;
		mTagExitAnimation = R.anim.zoom_out;
		mTagSelectedAnimation = R.anim.zoom_large;
		mTagDeselectedAnimation = R.anim.zoom_normal;
		mTagWidth = mTagHeight = 140;
		instanceNum = Instances++;
	}

	public static ImageTaggerFragment newInstance(int tagContainer) {
		ImageTaggerFragment taggerFragment = new ImageTaggerFragment();
		taggerFragment.setTagContainer(tagContainer);
		return taggerFragment;
	}

	public static ImageTaggerFragment newInstance(int tagContainer, int tagEnterAnimation, int tagExitAnimation) {
		ImageTaggerFragment taggerFragment = new ImageTaggerFragment();
		taggerFragment.setTagContainer(tagContainer);
		taggerFragment.setTagEnterAnimation(tagEnterAnimation);
		taggerFragment.setTagExitAnimation(tagExitAnimation);
		return taggerFragment;

	}

	public static ImageTaggerFragment newInstance(int tagContainer, int tagEnterAnimation, int tagExitAnimation, int tagSelectedAnimation, int tagDeselectedAnimation) {
		ImageTaggerFragment taggerFragment = new ImageTaggerFragment();
		taggerFragment.setTagContainer(tagContainer);
		taggerFragment.setTagEnterAnimation(tagEnterAnimation);
		taggerFragment.setTagExitAnimation(tagExitAnimation);
		taggerFragment.setTagSelectedAnimation(tagSelectedAnimation);
		taggerFragment.setTagDeselectedAnimation(tagDeselectedAnimation);
		return taggerFragment;
	}

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		View v = inflater.inflate(mTagContainer, container, false);

		v.setOnTouchListener(this);

		for(TagFragment t : mTagFragmentList) {
			t.setHandler(this);
		}

		v.bringToFront();
		return v;
	}

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
////		outState.putInt(TAG_CONTAINER, mTagContainer);
////		outState.putParcelableArrayList(TAG_FRAGMENTS, (ArrayList<? extends android.os.Parcelable>)mTagFragmentList);
//	}
//
//	@Override
//	public void onViewStateRestored(Bundle savedInstanceState) {
//		super.onViewStateRestored(savedInstanceState);
//
////		if(savedInstanceState == null)
////			return;
////
////		if(savedInstanceState.containsKey(TAG_CONTAINER))
////			mTagContainer = savedInstanceState.getInt(TAG_CONTAINER);
////
////		if(savedInstanceState.containsKey(TAG_FRAGMENTS)) {
////			mTagFragmentList.clear();
////			mTagFragmentList = savedInstanceState.getParcelableArrayList(TAG_FRAGMENTS);
////
////			for (TagFragment t : mTagFragmentList) {
////				moveTagTo(t, t.getPosition().leftMargin, t.getPosition().topMargin);
////				t.setHandler(this);
////				if(t.getView() != null) {
////					t.getView().bringToFront();
////					t.getView().invalidate();
////				}
////			}
////		}
//
//	}

	public void removeTag(TagFragment tag) {
		mTagFragmentList.remove(tag);
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

	public void addTagFragment(TagFragment tag, int x, int y) {
		tag.setHandler(this);

		getFragmentManager()
			.beginTransaction()
			.setCustomAnimations(mTagEnterAnimation, mTagExitAnimation)
			.add(R.id.tagger_fragment_container, tag)
			.addToBackStack(null)
			.commit();

		mTagFragmentList.add(tag);

		moveTagTo(tag, x, y);

		fingerDown = false;
	}

	private void moveTagTo(TagFragment tag, Integer x, Integer y) {
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(x - mTagWidth, y - mTagHeight, 0, 0);
		tag.setPosition(lp);

		if(tag.getView() != null) {
			tag.getView().setLayoutParams(lp);
			tag.getView().bringToFront();
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
				TagFragment tag = TagFragment.newInstance(this, null);
				addTagFragment(tag, (int)event.getX(), (int)event.getY());
			}
			break;
		}

		return true;
	}

	public int getTagEnterAnimation() {
		return mTagEnterAnimation;
	}

	public void setTagEnterAnimation(int tagEnterAnimation) {
		mTagEnterAnimation = tagEnterAnimation;
	}

	public int getTagExitAnimation() {
		return mTagExitAnimation;
	}

	public void setTagExitAnimation(int tagExitAnimation) {
		mTagExitAnimation = tagExitAnimation;
	}

	public int getTagSelectedAnimation() {
		return mTagSelectedAnimation;
	}

	public void setTagSelectedAnimation(int tagSelectedAnimation) {
		mTagSelectedAnimation = tagSelectedAnimation;
	}

	public int getTagDeselectedAnimation() {
		return mTagDeselectedAnimation;
	}

	public void setTagDeselectedAnimation(int tagDeselectedAnimation) {
		mTagDeselectedAnimation = tagDeselectedAnimation;
	}

	public void setTagContainer(int tagContainer) {
		mTagContainer = tagContainer;
	}

	public int getTagContainer() {
		return mTagContainer;
	}
}
