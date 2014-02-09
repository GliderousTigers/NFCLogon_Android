package com.glideroustigers.nfclogon.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;


class WizardPager extends ViewPager
{
    WizardPager(Context context, View[] views)
    {
        super(context);
        this.setAdapter(new WizardPagerAdapter(views));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // Never allow swiping to switch between pages
        return false;
    }

    void setPage(int index)
    {
        this.setCurrentItem(index, true);
    }
}
