package com.glideroustigers.nfclogon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.glideroustigers.nfclogon.R;

/**
 * Class to create a wizard view from XML layout files.
 * @author Alexandre Cormier
 */
public class Wizard extends RelativeLayout
{
    // the view containing the different pages
    private WizardPager pager;

    // the page dots at the bottom
    private WizardDots dots;

    // the index of the currently selected page
    private int currentPage;

    // the number of pages in this wizard
    private int pageCount;

    /**
     * Construct a new wizard from data provided by the XML layout.
     * @param context the context to use.
     * @param attrs the attributes to set.
     */
    public Wizard(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFinishInflate()
    {
        View[] children = new View[this.getChildCount()];
        for (int i = 0; i < this.getChildCount(); i++)
        {
            children[i] = this.getChildAt(i);
        }
        this.removeAllViews();

        this.currentPage = 0;
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

    /**
     * Selects the page at the index provided.
     * @param index index of the page to select.
     * @return whether or not the page was set.
     */
    public boolean setPage(int index)
    {
        if (index >= 0 && index < this.pageCount)
        {
            this.dots.setPage(index);
            this.pager.setPage(index);
            this.currentPage = index;
            return true;
        }
        return false;
    }

    /**
     * Goes to the next page.
     * @return whether or not the page was set.
     */
    public boolean nextPage()
    {
        return this.setPage(this.currentPage + 1);
    }

    /**
     * Goes to the previous page.
     * @return whether or not the page was set.
     */
    public boolean previousPage()
    {
        return this.setPage(this.currentPage - 1);
    }
}
