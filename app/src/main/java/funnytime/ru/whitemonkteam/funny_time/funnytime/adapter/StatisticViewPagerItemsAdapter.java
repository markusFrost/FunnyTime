package funnytime.ru.whitemonkteam.funny_time.funnytime.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.StatisticGenerlItemsFragment;

/**
 * Created by Андрей on 21.04.2015.
 */
public class StatisticViewPagerItemsAdapter extends FragmentPagerAdapter
{

    private static final int PAGE_COUNT = 4;
    Activity context;
    public StatisticViewPagerItemsAdapter(FragmentManager fm,  Activity context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return new StatisticGenerlItemsFragment().newInstance(position);
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String [] array = context.getResources().getStringArray(R.array.strViewPagerTitle);

        return array[position];
    }
}
