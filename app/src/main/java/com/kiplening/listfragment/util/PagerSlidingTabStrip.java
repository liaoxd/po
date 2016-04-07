package com.kiplening.listfragment.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiplening.listfragment.R;

/**
 * Created by MOON on 4/4/2016.
 */
public class PagerSlidingTabStrip extends HorizontalScrollView{
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    private Paint rectPaint;
    private Paint dividerPaint;
    private int tabTextColor = 0xFF666666;
    private int indicatorColor = 0xFF666666;
    private int selectedTabTextColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0xFFFFFF;
    private int dividerWidth = 1;
    private int indicatorHeight = 8;
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;
    public ViewPager.OnPageChangeListener delegatePageListener;
    private float currentPositionOffset = 0f;
    private boolean shouldExpand = false;
    private int tabTextSize = 60;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private int selectedPosition = 0;
    private boolean textAllCaps = true;
    private int lastScrollX = 0;
    private int scrollOffset = 52;
    private int currentPosition = 0;
    private LinearLayout tabsContainer;
    private ViewPager pager;
    private final PageListener pageListener = new PageListener();

    private int tabCount;
    public PagerSlidingTabStrip(Context context){this(context, null);}
    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setFillViewport(true);
        setWillNotDraw(false);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);

        tabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_tabTextColor, indicatorColor);
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        tabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_tabTextColor, indicatorColor);
        selectedTabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_selectedTabTextColor, indicatorColor); //tab文字选中时的颜色,默认和滑动指示器的颜色一致

        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        System.out.println("初始化成功！");
    }


    public void setViewPager(ViewPager pager){
        this.pager = pager;

        if (pager.getAdapter() == null){
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0;i < tabCount;i++){
            System.out.println("get in "+pager.getAdapter().getPageTitle(i).toString());
            addTextTab(i,pager.getAdapter().getPageTitle(i).toString());
        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                scrollToChild(currentPosition, 0);
            }
        });
    }
    private void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX,tabTextSize);
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor(tabTextColor);
                System.out.println(tab.getText().toString() + "hello");
                tab.setText(tab.getText().toString());

                if (i == selectedPosition) {
                    tab.setTextColor(selectedTabTextColor);
                }
            }
        }
    }
    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    private void addTextTab(final int position, String title) {


        TextView tab = new TextView(getContext());
        tab.setText(title);
        //tab.setTextColor(0xFFFFFFFF);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        System.out.println(tab.getText().toString());
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
        tab.setPadding(20, 0, 20, 0);
        tabsContainer.addView(tab, position, defaultTabLayoutParams);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0){
            return;
        }
        final int height = getHeight();
        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw indicator line
        rectPaint.setColor(selectedTabTextColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        // draw divider

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position,(int)(positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();
            if (delegatePageListener != null){
                delegatePageListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageSelected(int position) {
            selectedPosition = position;
            updateTabStyles();
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
