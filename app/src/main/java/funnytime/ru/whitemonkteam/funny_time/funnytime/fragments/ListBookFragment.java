package funnytime.ru.whitemonkteam.funny_time.funnytime.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.CUActivity;
import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.XYChartBuilder;
import funnytime.ru.whitemonkteam.funny_time.funnytime.adapter.BookAdapter;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Book;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.swipemenulistview.SwipeMenu;
import funnytime.ru.whitemonkteam.funny_time.funnytime.swipemenulistview.SwipeMenuCreator;
import funnytime.ru.whitemonkteam.funny_time.funnytime.swipemenulistview.SwipeMenuItem;
import funnytime.ru.whitemonkteam.funny_time.funnytime.swipemenulistview.SwipeMenuListView;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ShPrefUtils;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.StatisticUtils;

/**
 * Created by Андрей on 21.03.2015.
 */
public class ListBookFragment extends Fragment
{
    BookAdapter adapter;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        adapter = new BookAdapter(getActivity());

        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity(), CUActivity.class);
                intent.putExtra(Constants.TYPE_ITEM, Constants.TYPE_BOOK);
                intent.putExtra(Constants.EXTRA_ITEM, (Book)adapter.getItem(position));

                startActivityForResult(intent, Constants.CODE_UPDATE_BOOK);
            }
        });


        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                  createDeleteAlert((Book)adapter.getItem(position));

               /* Intent intent = new Intent(getActivity(), XYChartBuilder.class);
                intent.putExtra(Constants.EXTRA_ITEM, (Book)adapter.getItem(position));
                startActivity(intent);
                return true;
            }
        });

    }
*/

    private SwipeMenuListView mListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.swipe_list_layout, container, false);
        mListView = (SwipeMenuListView) v.findViewById(R.id.listView);
        Bundle b = getArguments();
        if ( b == null)
        {
            adapter = new BookAdapter(getActivity());
        }
        else
        {
            long time = b.getLong(Constants.EXTRA_ITEM);
            ArrayList<Book> list = AppContext.dbAdapter.getBooksByDate(
                    ShPrefUtils.getUserId(getActivity()), time
            );

            adapter = new BookAdapter(getActivity(), list);
        }

        mListView.setAdapter(adapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem statItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
               /* statItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));*/
                statItem.setBackground(new ColorDrawable(Color.rgb(0x3F,
                         0x25, 0xF9)));
                // set item width
                statItem.setWidth(dp2px(90));
                // set a icon
                statItem.setIcon(R.drawable.ic_graph);
                // add to menu
                menu.addMenuItem(statItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        // open
                        Intent intent = new Intent(getActivity(), XYChartBuilder.class);
                        intent.putExtra(Constants.EXTRA_ITEM, (Book)adapter.getItem(position));
                        startActivity(intent);

                        break;
                    case 1:
                        // delete
//					delete(item);
                        createDeleteAlert((Book)adapter.getItem(position));

                        break;
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity(), CUActivity.class);
                intent.putExtra(Constants.TYPE_ITEM, Constants.TYPE_BOOK);
                intent.putExtra(Constants.EXTRA_ITEM, (Book)adapter.getItem(position));

                startActivityForResult(intent, Constants.CODE_UPDATE_BOOK);
            }
        });

        return  v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if ( id == R.id.menu_add)
        {
            Intent intent = new Intent(getActivity(), CUActivity.class);
            intent.putExtra(Constants.TYPE_ITEM, Constants.TYPE_BOOK);

            startActivityForResult(intent, Constants.CODE_ADD_BOOK);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == Constants.CODE_ADD_BOOK && resultCode == getActivity().RESULT_OK && data != null)
        {
            Book item = (Book) data.getSerializableExtra(Constants.EXTRA_ITEM);

            item.UserId = ShPrefUtils.getUserId(getActivity());
            long id = AppContext.dbAdapter.Add(item);

            item.Id = id;

           // Toast.makeText(getActivity(), "id = " + item.Id, Toast.LENGTH_SHORT).show();

          // item.DateChange = HelpUtils.getYerstaday(0);
            StatisticUtils.Save(item);

            adapter.Add(item);
            adapter.notifyDataSetChanged();

        }
        else if ( requestCode == Constants.CODE_UPDATE_BOOK && resultCode == getActivity().RESULT_OK && data != null)
        {
            Book item = (Book) data.getSerializableExtra(Constants.EXTRA_ITEM);

            item.UserId = ShPrefUtils.getUserId(getActivity());
            AppContext.dbAdapter.Update(item);

            //item.DateChange = HelpUtils.getYerstaday(0);
            StatisticUtils.Save(item);

            adapter.Update(item);
            adapter.notifyDataSetChanged();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createDeleteAlert(final Book item)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(R.string.alertNotif);
        String msg = getActivity().getResources().getString(R.string.alertShure) + " " + item.Name;
        alert.setMessage(msg);

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                AppContext.dbAdapter.Delete(item);
                adapter.Delete(item);
                adapter.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton(android.R.string.no, null);

        alert.show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }



}
