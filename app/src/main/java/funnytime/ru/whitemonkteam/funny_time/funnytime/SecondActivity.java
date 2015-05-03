package funnytime.ru.whitemonkteam.funny_time.funnytime;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.FriendVkProfileFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.FriendsVKFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.ListBookFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.ListFavoriteAutorsFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.ListFilmsFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.ListSerialsFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.ViewPagerFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;


public class SecondActivity extends FragmentActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        if ( getIntent() != null && getIntent().getAction() != null)
        {
            if ( getIntent().getAction().equals(Constants.ACTION_TO_SECOND_ACTIVITY_SHOW_PROFILE))
            {
                FragmentManager fragmentManager = getFragmentManager();

                FriendVkProfileFragment fragment = new FriendVkProfileFragment();
                fragment.setArguments(getIntent().getExtras());

                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment ).commit();
            }
            else  if ( getIntent().getAction().equals(Constants.ACTION_TO_SECOND_ACTIVITY_SHOW_FRIENDS))
            {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FriendsVKFragment()).commit();
            }
            else  if ( getIntent().getAction().equals(Constants.ACTION_TO_SECOND_ACTIVITY_DISPLAY_STATISTIC))
            {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ViewPagerFragment()).commit();
            }
            else  if ( getIntent().getAction().equals(Constants.ACTION_TO_SECOND_ACTIVITY_DISPLAY_ITEMS_BY_DATE))
            {

                if ( getIntent().getIntExtra(Constants.TYPE_ITEM,0) == Constants.TYPE_FILM)
                {
                    ListFilmsFragment fragment = new ListFilmsFragment();
                    fragment.setArguments(getIntent().getExtras());

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment).commit();
                }
               else  if ( getIntent().getIntExtra(Constants.TYPE_ITEM,0) == Constants.TYPE_SERIAL)
                {
                    ListSerialsFragment fragment = new ListSerialsFragment();
                    fragment.setArguments(getIntent().getExtras());

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment).commit();
                }
                else  if ( getIntent().getIntExtra(Constants.TYPE_ITEM,0) == Constants.TYPE_BOOK)
                {
                    ListBookFragment fragment = new ListBookFragment();
                    fragment.setArguments(getIntent().getExtras());

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment).commit();
                }
                else if ( getIntent().getIntExtra(Constants.TYPE_ITEM,0) == 3)
                {
                    ListFavoriteAutorsFragment fragment = new ListFavoriteAutorsFragment();
                    fragment.setArguments(getIntent().getExtras());

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment).commit();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
