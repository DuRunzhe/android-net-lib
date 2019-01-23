package com.pull2me.android.netlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by drz on 2017/3/2.
 */

public class LittleLogView extends android.support.v7.widget.AppCompatTextView {
    private Context mContext;
    private int mLineCount;
    private Queue<CharSequence> mLineTextQueue;
    private int mContentLineCount;
    private String mSep;

    public LittleLogView(Context context) {
        this(context, null);

    }

    public LittleLogView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LittleLogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLineCount = getLineHeight();
        mContentLineCount = (mLineCount / 2) + 3 - 13;
        mLineTextQueue = new LinkedBlockingDeque<>(mContentLineCount);
        mSep = System.getProperty("line.separator", "\n");
        Log.d("debug", " mLineCount=" + mLineCount + ", mContentLineCount=" + mContentLineCount);
    }

    public void log(CharSequence text) {
        if (mLineTextQueue.offer(text)) {
            //显示数据
            showContent();
        } else {
            //队列已满
            //移除并返回队头元素
            mLineTextQueue.poll();
            //添加新元素
            mLineTextQueue.offer(text);
            //显示数据
            showContent();

        }
    }

    private void showContent() {
        Iterator<CharSequence> iterator = mLineTextQueue.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            sb.append(mSep);
        }
        setText(sb.toString());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
}
