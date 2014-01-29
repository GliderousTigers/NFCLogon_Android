package com.glideroustigers.nfclogon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.glideroustigers.nfclogon.R;

public class Wizard extends RelativeLayout
{
    private WizardPager pager;
    private WizardDots dots;

    private int currentPage;
    private int pageCount;

    public Wizard(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate()
    {
        View[] children = new View[this.getChildCount()];
        for (int i = 0; i < this.getChildCount(); i++)
        {
            children[i] = this.getChildAt(i);
        }
        this.removeAllViews();

        this.currentPage = 1;
        this.pageCount = children.length;

        this.dots = new WizardDots(this.getContext(), children.length);
        this.pager = new WizardPager(this.getContext(), children);

        this.dots.setId(Integer.MAX_VALUE);
        LayoutParams paramsDots = new LayoutParams(LayoutParams.MATCH_PARENT, this.getResources().getDimensionPixelSize(R.dimen.wizard_dots_section_height));
        paramsDots.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.dots.setLayoutParams(paramsDots);

        LayoutParams paramsPager = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        paramsPager.addRule(RelativeLayout.ABOVE, this.dots.getId());
        this.pager.setLayoutParams(paramsPager);

        this.addView(this.dots);
        this.addView(this.pager);
    }

    public void nextPage()
    {
        if (this.currentPage < this.pageCount)
        {
            this.dots.nextPage();
            this.pager.nextPage();
            this.currentPage++;
        }
    }

    public void previousPage()
    {
        if (this.currentPage > 0)
        {
            this.dots.previousPage();
            this.pager.previousPage();
            this.currentPage--;
        }
    }
}
