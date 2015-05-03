package funnytime.ru.whitemonkteam.funny_time.funnytime.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity;
import funnytime.ru.whitemonkteam.funny_time.funnytime.adapter.StatisticGeneralItemsAdapter;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.StatisticGeneralItem;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ShPrefUtils;

/**
 * Created by Андрей on 21.04.2015.
 */
public class StatisticGenerlItemsFragment extends ListFragment
{
    public static StatisticGenerlItemsFragment newInstance(int position)
    {
        Bundle b = new Bundle();
        b.putInt(Constants.EXTRA_ITEM, position);

        StatisticGenerlItemsFragment fragment = new StatisticGenerlItemsFragment();
        fragment.setArguments(b);

        return fragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        final int position = getArguments().getInt(Constants.EXTRA_ITEM);

        if ( position == Constants.TYPE_FILM)
        {
            ArrayList<StatisticGeneralItem> list = AppContext.dbAdapter.getStatisticFilmItems(ShPrefUtils.getUserId(getActivity()));

            StatisticGeneralItemsAdapter adapter = new StatisticGeneralItemsAdapter(getActivity(), list);

            setListAdapter(adapter);
        }
        else if ( position == Constants.TYPE_SERIAL)
        {
            ArrayList<StatisticGeneralItem> list = AppContext.dbAdapter.getStatisticSerialItems(ShPrefUtils.getUserId(getActivity()));

            StatisticGeneralItemsAdapter adapter = new StatisticGeneralItemsAdapter(getActivity(), list);

            setListAdapter(adapter);
        }
        if ( position == Constants.TYPE_BOOK)
        {
            ArrayList<StatisticGeneralItem> list = AppContext.dbAdapter.getStatisticBookItems(ShPrefUtils.getUserId(getActivity()));

            StatisticGeneralItemsAdapter adapter = new StatisticGeneralItemsAdapter(getActivity(), list);

            setListAdapter(adapter);
        }
        else if ( position == 3)
        {
            ArrayList<StatisticGeneralItem> list = AppContext.dbAdapter.getFavoriteAutors(ShPrefUtils.getUserId(getActivity()));

            StatisticGeneralItemsAdapter adapter = new StatisticGeneralItemsAdapter(getActivity(), list);

            setListAdapter(adapter);
        }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
            {
                StatisticGeneralItem item = (StatisticGeneralItem) getListAdapter().getItem(pos);

                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.setAction(Constants.ACTION_TO_SECOND_ACTIVITY_DISPLAY_ITEMS_BY_DATE);

                if ( item.Time >= 0) // если меньше нуля то имеем дело с любимыми авторами
                {
                    intent.putExtra(Constants.EXTRA_ITEM, item.Time);
                }
                else
                {
                    intent.putExtra(Constants.EXTRA_ITEM, item.Name);
                }


                intent.putExtra(Constants.TYPE_ITEM, position);

                startActivity(intent);
            }
        });
    }
}
