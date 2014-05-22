package com.example.ImageTagger;

public interface TagCallbackHandler {
	void onTagEvent(TagFragment tag, String eventName, Object data);
}
