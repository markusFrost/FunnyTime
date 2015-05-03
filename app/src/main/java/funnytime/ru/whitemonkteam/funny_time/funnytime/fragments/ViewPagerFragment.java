package funnytime.ru.whitemonkteam.funny_time.funnytime.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity;
import funnytime.ru.whitemonkteam.funny_time.funnytime.adapter.StatisticViewPagerItemsAdapter;

/**
 * Created by Андрей on 21.04.2015.
 */
public class ViewPagerFragment extends Fragment
{
    ViewPager viewPager;
    StatisticViewPagerItemsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.vp_layout, container,  false);

        viewPager = (ViewPager) v.findViewById(R.id.pager);

        /*

        FtActivity activity = (FtActivity)getActivity();
    	FragmentManager fragmentManager = activity.getSupportFragmentManager();
    	adapter = new MyPagerAdapter(fragmentManager);

    	viewPager.setAdapter(adapter);


         */

        SecondActivity activity = (SecondActivity) getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        adapter = new StatisticViewPagerItemsAdapter(fragmentManager, getActivity());

        viewPager.setAdapter(adapter);

        return v;
    }

}
