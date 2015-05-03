package funnytime.ru.whitemonkteam.funny_time.funnytime.fragments;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.adapter.BookAdapter;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Book;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ShPrefUtils;

/**
 * Created by Андрей on 25.04.2015.
 */
public class ListFavoriteAutorsFragment extends ListFragment
{
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        String name = getArguments().getString(Constants.EXTRA_ITEM);

        ArrayList<Book> list = AppContext.dbAdapter.getBooksByAuthor(
                ShPrefUtils.getUserId(getActivity()), name
        );

        BookAdapter adapter = new BookAdapter(getActivity(), list);

        setListAdapter(adapter);
    }
}
