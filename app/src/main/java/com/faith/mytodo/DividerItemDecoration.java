package com.faith.mytodo;


    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Paint;
   import android.graphics.Rect;
   import android.view.View;

    import org.checkerframework.checker.nullness.qual.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private Paint dividerPaint;
        private int dividerHeight;

        public DividerItemDecoration(Context context, int dividerHeight, int dividerColor) {
            this.dividerHeight = dividerHeight;
            dividerPaint = new Paint();
            dividerPaint.setColor(dividerColor);
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = dividerHeight;
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, RecyclerView.State state) {
            int childCount = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);
                int top = child.getBottom();
                int bottom = top + dividerHeight;
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }
    }


