package dev.widget.decoration.horizontal;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.widget.decoration.BaseItemDecoration;
import dev.widget.decoration.LineItemDecoration;

/**
 * detail: RecyclerView 分割线
 * @author Ttt
 * <pre>
 *     {@link LineItemDecoration} 横向滑动版实现
 *     不合并为一个实现类是防止在 {@link #onDrawOver} 中多次获取方向并进行判断处理
 * </pre>
 */
public class LineHorizontalItemDecoration
        extends BaseItemDecoration {

    public LineHorizontalItemDecoration(float lineHeight) {
        super(lineHeight);
    }

    public LineHorizontalItemDecoration(
            float lineHeight,
            int lineColor
    ) {
        super(lineHeight, lineColor);
    }

    // ==========
    // = 处理方法 =
    // ==========

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        int itemCount = state.getItemCount();
        if (itemCount <= 1) return;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set((int) mLineHeight, 0, 0, 0);
        }
    }

    @Override
    public void onDrawOver(
            @NonNull Canvas canvas,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        super.onDrawOver(canvas, parent, state);

        int itemCount = state.getItemCount();
        if (itemCount <= 1) return;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) != 0) {
                canvas.drawRect(
                        child.getLeft() - mLineHeight,
                        child.getTop() + mLineLeft,
                        child.getLeft(),
                        child.getBottom() - mLineRight,
                        mLinePaint
                );
            }
        }
    }
}