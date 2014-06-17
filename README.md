# AndroidImageTagger
##### This code is an example of tagging images in Android.

#### Simple setup
```java
ImageTaggerFragment taggerFragment = new ImageTaggerFragment(R.layout.fragment_tagger);
```

#### Define your own set of animations
```java
ImageTaggerFragment taggerFragment = new ImageTaggerFragment(R.layout.fragment_tagger, 
  R.anim.zoom_in, // on tag created
  R.anim.zoom_out, // on tag removed
  R.anim.zoom_large, // on tag selected
  R.anim.zoom_normal); // on tag deselsted
```

#### Custom tag event handling
```java
taggerFragment = ImageTaggerFragment.newInstance(R.layout.fragment_tagger);
{
  @Override
  public boolean onTagEvent(TagFragment tag, String tagEvent, Object data) {
    return super.onTagEvent(tag, tagEvent, data);
  }
};
```

#### Adding tags
```java
// Method signature: addTagFragment(int xPos, int yPos, Object metaData)
taggerFragment.addTagFragment(100, 100, "One");
```

#### Adding your fragments using android.support.v4.
```java
getSupportFragmentManager().beginTransaction()
  .add(R.id.fragment_container, taggerFragment, "imageTaggerFragment")
  .commit();
```

