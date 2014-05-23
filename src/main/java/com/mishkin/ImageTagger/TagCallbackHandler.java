package com.mishkin.ImageTagger;

public interface TagCallbackHandler {
	boolean onTagEvent(TagFragment tag, String eventName, Object data);
}
