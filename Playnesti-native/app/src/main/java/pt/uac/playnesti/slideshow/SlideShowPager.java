package pt.uac.playnesti.slideshow;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 17-03-2015.
 */
public class SlideShowPager extends ViewPager {
    private boolean swipeEnabled = true;

    public SlideShowPager(final Context context) {
        super(context);
    }

    public SlideShowPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipeEnabled(final boolean swipeEnabled) {
        this.swipeEnabled = swipeEnabled;
    }

    public boolean isSwipeEnabled() {
        return swipeEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swipeEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (swipeEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }
}
