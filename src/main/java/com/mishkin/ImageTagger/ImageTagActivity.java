package com.mishkin.ImageTagger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ImageTagActivity extends FragmentActivity {
	ImageTaggerFragment taggerFragment;
	static boolean alreadyRun = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

//		---- Simple setup ----
//		ImageTaggerFragment taggerFragment = new ImageTaggerFragment(R.layout.fragment_tagger);

//		---- Define your own set of animations ----
//		ImageTaggerFragment taggerFragment = new ImageTaggerFragment(R.layout.fragment_tagger,
//																																	R.anim.zoom_in,
//																																	R.anim.zoom_out,
//																																	R.anim.zoom_large,
//																																	R.anim.zoom_normal);

		if(!alreadyRun) {
//	---- Custom tag event handling ----
			taggerFragment = ImageTaggerFragment.newInstance(R.layout.fragment_tagger);
//		{
//			@Override
//			public boolean onTagEvent(TagFragment tag, String tagEvent, Object data) {
//				return super.onTagEvent(tag, tagEvent, data);
//			}
//		};


			getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, taggerFragment, "imageTaggerFragment")
				.commit();

			alreadyRun = true;
		}
//		taggerFragment.addTagFragment(100, 100, "One");
//		taggerFragment.addTagFragment(200, 200, "Two");
//		taggerFragment.addTagFragment(300, 300, "Three");
//		taggerFragment.addTagFragment(400, 400, "Four");
	}
}