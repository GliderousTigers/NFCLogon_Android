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
    private final int dotSize = this.getResources().getDimensionPixelSize(R.dimen.wizard_dots_size);
    private final int selectedDotSize = this.getResources().getDimensionPixelSize(R.dimen.wizard_dots_selected_size);
    private final int dotMargin = this.getResources().getDimensionPixelSize(R.dimen.wizard_dots_padding);
    private final Paint paint = new Paint();

    private Dot[] dots;
    private int currentPage;

    WizardDots(Context context, int pageCount)
    {
        super(context);

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

    void nextPage()
    {
        this.dots[currentPage++].setPageSelected(false);
        this.dots[currentPage].setPageSelected(true);
    }

    void previousPage()
    {
        this.dots[currentPage--].setPageSelected(false);
        this.dots[currentPage].setPageSelected(true);
    }

    private class Dot extends View
    {
        private Dot(Context context)
        {
            super(context);

            LayoutParams params = new LayoutParams(dotSize, dotSize);
            params.setMargins(dotMargin, dotMargin, dotMargin, dotMargin);
            this.setLayoutParams(params);
        }

        private void setPageSelected(boolean selected)
        {
            LayoutParams params = (LayoutParams) this.getLayoutParams();
            if (selected)
            {
                params.width = selectedDotSize;
                params.height = selectedDotSize;
            }
            else
            {
                params.width = dotSize;
                params.height = dotSize;
            }
            this.setLayoutParams(params);
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
