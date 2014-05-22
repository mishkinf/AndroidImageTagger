package com.example.ImageTagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageTaggerFragment extends Fragment implements TagCallbackHandler {
	ImageView image;
	FrameLayout tagsContainer;
	Animation mZoomLargeAnimation, mZoomNormalAnimation;
	boolean fingerDown = false;
	TagFragment selectedTagFragment = null;

	public ImageTaggerFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tagger, container, false);

		tagsContainer = (FrameLayout)v.findViewById(R.id.tagger_fragment_container);

		final ImageTaggerFragment self = this;
		image = (ImageView)v.findViewById(R.id.image_view);
		image.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					break;
				case MotionEvent.ACTION_MOVE:
					if(selectedTagFragment != null) {
						int width = 140;
						int height = 140;

						FrameLayout.LayoutParams lp = selectedTagFragment.getLayoutParams();

						FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(lp);
						newLp.setMargins((int)event.getX() - width, (int)event.getY() - height, 0, 0);

						selectedTagFragment.getView().setLayoutParams(newLp);
						tagsContainer.invalidate();
					}
					break;
				case MotionEvent.ACTION_UP:
					if(selectedTagFragment != null) {
						mZoomNormalAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_normal);
						selectedTagFragment.getView().startAnimation(mZoomNormalAnimation);
						selectedTagFragment = null;
					} else {

						TagFragment tag = new TagFragment(self, null);

						FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
						fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out);
						fragmentTransaction.add(R.id.tagger_fragment_container, tag);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();

						int width = 140;
						int height = 140;

						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
						lp.setMargins((int)event.getX() - width, (int)event.getY() - height, 0, 0);
						tag.setLayoutParams(lp);
						fingerDown = false;
					}
					break;
				}

				return true;
			}
		});

		return v;
	}

	@Override
	public void onTagEvent(TagFragment tag, String tagEvent, Object data) {
		if (tagEvent.equals("MagGlassClicked")) {
			// do something to handle mag glass being clicked
			Toast.makeText(getActivity().getApplicationContext(), "Magnifying glass clicked.", Toast.LENGTH_SHORT).show();
		} else if( tagEvent.equals("CloseClicked")) {
			// do something to handle close being clicked
			Toast.makeText(getActivity().getApplicationContext(), "Close clicked.", Toast.LENGTH_SHORT).show();

			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_out);
			fragmentTransaction.remove(tag);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

		} else if (tagEvent.equals("ClickDown")) {
			Toast.makeText(getActivity().getApplicationContext(), "ClickDown", Toast.LENGTH_SHORT).show();
			if(selectedTagFragment == null) {
				mZoomLargeAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_large);
				tag.getView().startAnimation(mZoomLargeAnimation);
				selectedTagFragment = tag;
				fingerDown = true;
				tag.getView().bringToFront();
			}
		}
		else if (tagEvent.equals("ClickMove")) {
			Toast.makeText(getActivity().getApplicationContext(), "ClickMove", Toast.LENGTH_SHORT).show();
		} else if (tagEvent.equals("ClickUp")) {
			fingerDown = false;
			selectedTagFragment = null;
			Toast.makeText(getActivity().getApplicationContext(), "ClickUp", Toast.LENGTH_SHORT).show();
			mZoomLargeAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_large);
			tag.getView().startAnimation(mZoomNormalAnimation);
		}
	}
}
