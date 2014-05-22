package com.example.ImageTagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class ImageTagActivity extends FragmentActivity {
	final String TAG = "DamageTagActivity";

	Fragment currentFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		switchTo(new ImageTaggerFragment(), "newDamageFragment");

	}

	private void switchTo(Fragment fragment, String name)
	{
		if (fragment.isVisible())
			return;

		if (fragment == null)
			return;

		FragmentTransaction t = getSupportFragmentManager().beginTransaction();

		if (currentFragment == null) {
			t.add (R.id.fragment_container, fragment, name);
			currentFragment = fragment;
		} else {
			t.setCustomAnimations (R.anim.zoom_in, R.anim.zoom_out);
			Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(name);

			if (existingFragment != null)
				existingFragment.getView().bringToFront();

			currentFragment.getView().bringToFront();
			t.hide(currentFragment);

			if (existingFragment != null) {
				t.show(existingFragment);
			} else {
				t.add(R.id.fragment_container, fragment, name);
				existingFragment = fragment;
			}
//			fragment.rerefreshData ();
		}
		t.commit();
	}


//	List<String> damageList = new ArrayList<String>();
//	private class DamageAdapter extends ArrayAdapter<String> {
//		public DamageAdapter(Context context, List<String> damageEntries) {
//			super(context, 0, damageEntries);
//			mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View retval = convertView;
//			if ( retval == null ) {
//				retval = mLayoutInflater.inflate(R.layout.list_item_damage, parent, false);
//			}
//
//			String damageEntry = getItem(position);
//
//			TextView damageTextView = (TextView)retval.findViewById(R.id.damage_text_view);
//			damageTextView.setText(damageEntry);
//
//			return retval;
//		}
//
//		private LayoutInflater mLayoutInflater;
//	}
}