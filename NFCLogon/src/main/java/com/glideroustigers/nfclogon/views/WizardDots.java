package com.glideroustigers.nfclogon.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.glideroustigers.nfclogon.R;

class WizardDots extends LinearLayout
{
    private final int dotSize;
    private final int selectedDotSize;
    private final int dotMargin;
    private final Paint paint;

    private Dot[] dots;
    private int currentPage;

    WizardDots(Context context, int pageCount)
    {
        super(context);

        this.dotSize = this.getResources().getDimensionPixelSize(R.dimen.wizard_dots_size);
        this.selectedDotSize = this.getResources().getDimensionPixelSize(R.dimen.wizard_dots_selected_size);
        this.dotMargin = this.getResources().getDimensionPixelSize(R.dimen.wizard_dots_padding);

        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setStyle(Paint.Style.FILL);

        this.setGravity(Gravity.CENTER);

        this.dots = new Dot[pageCount];
        for (int i = 0; i < pageCount; i++)
        {
            this.dots[i] = new Dot(context);
            this.addView(this.dots[i]);
        }
        this.currentPage = 0;
        this.dots[0].setPageSelected(true);
    }

    void setPage(int index)
    {
        this.dots[this.currentPage].setPageSelected(false);
        this.currentPage = index;
        this.dots[this.currentPage].setPageSelected(true);
    }

    private class Dot extends View
    {
        private LayoutParams params;

        private Dot(Context context)
        {
            super(context);

            this.params = new LayoutParams(dotSize, dotSize);
            this.params.setMargins(dotMargin, dotMargin, dotMargin, dotMargin);
            this.setLayoutParams(this.params);
        }

        private void setPageSelected(boolean selected)
        {
            if (selected)
            {
                this.params.width = selectedDotSize;
                this.params.height = selectedDotSize;
            }
            else
            {
                this.params.width = dotSize;
                this.params.height = dotSize;
            }
            this.setLayoutParams(this.params);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            paint.setShader(new RadialGradient(this.getWidth() / 2, this.getWidth() / 2, this.getWidth() / 2, Color.WHITE, Color.argb(25, 255, 255, 255), Shader.TileMode.CLAMP));
            canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2, paint);
        }
    }
}
