package net.whend.soodal.whend.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

/**
 * Created by Administrator on 2015-08-29.
 */
public class MultiAutoCompleteTextViewSharp extends MultiAutoCompleteTextView {
    public MultiAutoCompleteTextViewSharp(Context context) {
        super(context);
    }

    public MultiAutoCompleteTextViewSharp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiAutoCompleteTextViewSharp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiAutoCompleteTextViewSharp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void performFiltering(CharSequence text, int start, int end, int keyCode) {
        if (text.charAt(start) == '#') {
            start = start + 1;
        } else {
            text = text.subSequence(0, start);
            for (int i = start; i < end; i++) {
                text = text + "*";
            }
        }
        super.performFiltering(text, start, end, keyCode);
    }
}
