package com.pull2me.android.netlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Scroller;

/**
 * Created by drz on 2017/3/2.
 */

public class LogView extends android.support.v7.widget.AppCompatTextView {
    private Context mContext;
    private Scroller scroller;
    private int mHeight;
    private int mLineCount;
    private float mPerLineHeight;

    public LogView(Context context) {
        this(context, null);

    }

    public LogView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight();
        mLineCount = getLineHeight();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            float lineSpacingExtra = getLineSpacingExtra();
            mPerLineHeight = (mHeight / (float) ((mLineCount / 2) + 3)) + lineSpacingExtra;
        } else {
            mPerLineHeight = mHeight / (float) ((mLineCount + 2) / 2);
        }
        Log.d("debug", "mHeight=" + mHeight + ", mLineCount=" + mLineCount + ", mPerLineHeight=" + mPerLineHeight);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    public void log(CharSequence text) {
        if (getText().length() > 0) {
            append(System.getProperty("line.separator", "\n"));
        }
        if (scroller == null) {
            scroller = new Scroller(mContext);
            setScroller(scroller);
        }
        int lineCount = getLineCount();
        append(text);
        int i = (mLineCount / 2) + 3;
        if (lineCount >= i) {
            int currX = scroller.getCurrX();
            int finalY = scroller.getFinalY();
            scroller.startScroll(currX, finalY, 0, (int) mPerLineHeight, 5);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
}
