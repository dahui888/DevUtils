package dev.widget.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import dev.utils.app.WidgetUtils;
import dev.widget.utils.WidgetAttrs;

/**
 * detail: 自定义 HorizontalScrollView 滑动监听、滑动控制
 * @author Ttt
 * <pre>
 *     app:dev_slide=""
 *     app:dev_maxWidth=""
 *     app:dev_maxHeight=""
 * </pre>
 */
public class CustomHorizontalScrollView
        extends HorizontalScrollView {

    private WidgetAttrs    mWidgetAttrs;
    // 滑动监听回调
    private ScrollCallback mCallback = null;

    public CustomHorizontalScrollView(Context context) {
        super(context);
        initAttrs(context, null, 0, 0);
    }

    public CustomHorizontalScrollView(
            Context context,
            AttributeSet attrs
    ) {
        super(context, attrs);
        initAttrs(context, attrs, 0, 0);
    }

    public CustomHorizontalScrollView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomHorizontalScrollView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化
     * @param context      {@link Context}
     * @param attrs        {@link AttributeSet}
     * @param defStyleAttr 默认样式
     * @param defStyleRes  默认样式资源
     */
    private void initAttrs(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes
    ) {
        mWidgetAttrs = new WidgetAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(
            int widthMeasureSpec,
            int heightMeasureSpec
    ) {
        int[] measureSpecs = WidgetUtils.viewMeasure(
                this, widthMeasureSpec, heightMeasureSpec,
                mWidgetAttrs.getMaxWidth(), mWidgetAttrs.getMaxHeight()
        );
        super.onMeasure(measureSpecs[0], measureSpecs[1]);
    }

    @Override
    protected void onScrollChanged(
            int left,
            int top,
            int oldLeft,
            int oldTop
    ) {
        super.onScrollChanged(left, top, oldLeft, oldTop);
        if (mCallback != null) {
            mCallback.onScrollChanged(left, top, oldLeft, oldTop);
        }
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0; // 解决禁止 ScrollView 内的控件改变之后自动滚动
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mWidgetAttrs.isSlide()) return false;
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!mWidgetAttrs.isSlide()) return false;
        return super.onInterceptTouchEvent(arg0);
    }

    /**
     * 获取 View 最大显示宽度
     * @return View 最大显示宽度
     */
    public int getMaxWidth() {
        return mWidgetAttrs.getMaxWidth();
    }

    /**
     * 设置 View 最大显示宽度
     * @param maxWidth View 最大显示宽度
     * @return {@link CustomHorizontalScrollView}
     */
    public CustomHorizontalScrollView setMaxWidth(int maxWidth) {
        mWidgetAttrs.setMaxWidth(maxWidth);
        return this;
    }

    /**
     * 获取 View 最大显示高度
     * @return View 最大显示高度
     */
    public int getMaxHeight() {
        return mWidgetAttrs.getMaxHeight();
    }

    /**
     * 设置 View 最大显示高度
     * @param maxHeight View 最大显示高度
     * @return {@link CustomHorizontalScrollView}
     */
    public CustomHorizontalScrollView setMaxHeight(int maxHeight) {
        mWidgetAttrs.setMaxHeight(maxHeight);
        return this;
    }

    /**
     * 是否允许滑动
     * @return {@code true} yes, {@code false} no
     */
    public boolean isSlide() {
        return mWidgetAttrs.isSlide();
    }

    /**
     * 设置是否允许滑动
     * @param isSlide {@code true} yes, {@code false} no
     * @return {@link CustomHorizontalScrollView}
     */
    public CustomHorizontalScrollView setSlide(boolean isSlide) {
        mWidgetAttrs.setSlide(isSlide);
        return this;
    }

    /**
     * 切换滑动控制状态
     * @return {@link CustomHorizontalScrollView}
     */
    public CustomHorizontalScrollView toggleSlide() {
        mWidgetAttrs.toggleSlide();
        return this;
    }

    /**
     * 设置滑动监听回调
     * @param callback {@link ScrollCallback}
     * @return {@link CustomHorizontalScrollView}
     */
    public CustomHorizontalScrollView setScrollCallback(ScrollCallback callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * detail: 滑动监听回调
     * @author Ttt
     */
    public interface ScrollCallback {

        /**
         * 滑动改变通知
         * @param left    距离左边距离
         * @param top     距离顶部距离
         * @param oldLeft 旧的 ( 之前 ) 距离左边距离
         * @param oldTop  旧的 ( 之前 ) 距离顶部距离
         */
        void onScrollChanged(
                int left,
                int top,
                int oldLeft,
                int oldTop
        );
    }
}