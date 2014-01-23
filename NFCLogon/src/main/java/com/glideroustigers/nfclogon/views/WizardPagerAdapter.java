package com.glideroustigers.nfclogon.views;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

class WizardPagerAdapter extends PagerAdapter
{
    private View[] views;

    WizardPagerAdapter(View[] views)
    {
        this.views = views;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position)
    {
        collection.addView(this.views[position]);
        return this.views[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(this.views[position]);
    }

    @Override
    public int getCount()
    {
        return this.views.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view == (View) o);
    }
}
