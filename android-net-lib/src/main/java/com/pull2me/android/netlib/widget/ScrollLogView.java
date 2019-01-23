package com.pull2me.android.netlib.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by drz on 2017/3/2.
 */

public class ScrollLogView extends ScrollView {

    private Context mContext;
    private TextView mTextView;
    private String mSep;
    private int perLineHeight;

    public ScrollLogView(Context context) {
        this(context, null);
    }

    public ScrollLogView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScrollLogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextView = new TextView(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTextView.setLayoutParams(layoutParams);
        mSep = System.getProperty("line.separator", "\n");
        mTextView.setTextColor(Color.YELLOW);
        addView(mTextView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int bottom = getBottom();
        int top = getTop();
        float lineCount = (float) mTextView.getLineHeight();
        perLineHeight = (int) ((bottom - top) / (lineCount / 10 - 2));
        Log.e("debug", "- - -lineCount:" + lineCount + ", bottom=" + bottom
                + ", top:" + top + ", perLineHeight:" + perLineHeight);
    }

    public void log(CharSequence text) {
        if (mTextView.getText().length() > 0) {
            mTextView.append(mSep);
        }
        mTextView.append(text);
        float scaleY = getScaleY();
        int bottom = getBottom();
        int top = getTop();
        Log.e("debug", "- - -scaleY:" + scaleY + ", bottom=" + bottom
                + ", top:" + top + ", perLineHeight:" + perLineHeight);
        scrollBy(0, perLineHeight);
    }
}
