package com.appunison.appcollage.utils;

import android.widget.EditText;

public class EditTextProperty {

	public static void disable(EditText editText) {
		//editText.setClickable(true);
		editText.setFocusable(false);
		editText.setLongClickable(false);
	}

	public static void enable(EditText editText) {
		//editText.setClickable(true);
		
		editText.setFocusableInTouchMode(true);
		editText.setLongClickable(true);
		editText.requestFocus();
	}

}
