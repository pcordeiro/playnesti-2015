package pt.uac.playnesti.slideshow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import pt.uac.playnesti.R;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 17-03-2015.
 */
public class SlideShowPageIndicator extends View implements ViewPager.OnPageChangeListener {
    private static final long SLIDE_SHOW_INTERVAL = TimeUnit.SECONDS.toMillis(5);
    private static final float RADIUS = 10;

    private final Handler handler = new Handler();
    private Timer timer = new Timer();

    private final Paint paintPageFill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ViewPager pager;
    private int count;
    private int currentPage;
    private float pageOffset;
    private boolean timerEnabled;

    public SlideShowPageIndicator(final Context context) {
        super(context);
        setup();
    }

    public SlideShowPageIndicator(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public SlideShowPageIndicator(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public ViewPager getPager() {
        return pager;
    }

    public void setPager(ViewPager pager) {
        if (this.pager == pager) {
            return;
        }

        if (this.pager != null) {
            this.pager.setOnPageChangeListener(null);
        }

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        this.pager = pager;
        this.count = pager.getAdapter().getCount();
        this.currentPage = pager.getCurrentItem();

        pager.setOnPageChangeListener(this);

        invalidate();
    }

    public void setPager(final ViewPager pager, final int page) {
        setPager(pager);
        setCurrentPage(page);
    }

    private void setCurrentPage(final int page) {
        if (pager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }

        pager.setCurrentItem(page);
        currentPage = page;
        invalidate();
    }

    public void startTimer() {
        timerEnabled = true;
        doStartTimer();
    }

    public void stopTimer() {
        timerEnabled = false;
        doStopTimer();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPage = position;
        pageOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            doStartTimer();
        } else {
            doStopTimer();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircles(canvas);
    }

    private void doStartTimer() {
        if (timerEnabled && count > 1) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pager.setCurrentItem(++currentPage % count, true);
                        }
                    });
                }
            }, SLIDE_SHOW_INTERVAL, SLIDE_SHOW_INTERVAL);
        }
    }

    private void doStopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void drawCircles(final Canvas canvas) {
        final int longSize = getWidth();
        final int longPaddingBefore = getPaddingLeft();
        final int longPaddingAfter = getPaddingRight();
        final int shortPaddingBefore = getPaddingTop();
        final float threeRadius = RADIUS * 3;
        final float shortOffset = shortPaddingBefore + RADIUS;
        float pageFillRadius = RADIUS;
        float longOffset = longPaddingBefore + RADIUS;
        float dX;
        float dY;

        longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * threeRadius) / 2.0f);

        if (paintStroke.getStrokeWidth() > 0) {
            pageFillRadius -= paintStroke.getStrokeWidth() / 2.0f;
        }

        //Draw stroked circles
        for (int i = 0; i < count; i++) {
            dX = longOffset + (i * threeRadius);
            dY = shortOffset;

            // Only paint fill if not completely transparent
            if (paintPageFill.getAlpha() > 0) {
                canvas.drawCircle(dX, dY, pageFillRadius, paintPageFill);
            }

            // Only paint stroke if a stroke width was non-zero
            if (pageFillRadius != RADIUS) {
                canvas.drawCircle(dX, dY, RADIUS, paintStroke);
            }
        }

        //Draw the filled circle according to the currentPage scroll
        float cx = currentPage * threeRadius;

        cx += pageOffset * threeRadius;

        dX = longOffset + cx;
        dY = shortOffset;

        canvas.drawCircle(dX, dY, RADIUS, paintFill);
    }

    private void setup() {
        final Resources res = getResources();
        final int defaultPageColor = res.getColor(R.color.default_circle_indicator_page_color);
        final int defaultFillColor = res.getColor(R.color.default_circle_indicator_fill_color);
        final int defaultStrokeColor = res.getColor(R.color.default_circle_indicator_stroke_color);
        final float defaultStrokeWidth = res.getDimension(R.dimen.default_circle_indicator_stroke_width);
        ///final float defaultRadius = res.getDimension(R.dimen.default_circle_indicator_radius);

        paintPageFill.setStyle(Paint.Style.FILL);
        paintPageFill.setColor(defaultPageColor);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setColor(defaultStrokeColor);
        paintStroke.setStrokeWidth(defaultStrokeWidth);
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(defaultFillColor);
        //mRadius = a.getDimension(R.styleable.CirclePageIndicator_radius, defaultRadius);
    }
}
