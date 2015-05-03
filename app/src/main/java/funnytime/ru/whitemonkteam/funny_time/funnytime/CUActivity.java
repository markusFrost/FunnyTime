package funnytime.ru.whitemonkteam.funny_time.funnytime;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.CUBookFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.CUFilmFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.fragments.CUSerialFragment;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Book;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Film;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Serial;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;


public class CUActivity extends ActionBarActivity
{

    Fragment fragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getFragmentManager();
        int type = getIntent().getIntExtra(Constants.TYPE_ITEM, -1);

        switch( type)
        {
            case Constants.TYPE_FILM: {
                fragment = new CUFilmFragment();
                Film item = (Film) getIntent().getSerializableExtra(Constants.EXTRA_ITEM);
                if ( item != null)
                {
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.EXTRA_ITEM, item );
                    fragment.setArguments(b);
                }


            }
            break;
            case Constants.TYPE_SERIAL: {
                fragment = new CUSerialFragment();

                Serial item = (Serial) getIntent().getSerializableExtra(Constants.EXTRA_ITEM);
                if ( item != null)
                {
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.EXTRA_ITEM, item );
                    fragment.setArguments(b);
                }
            }
            break;
            case Constants.TYPE_BOOK: {
                fragment = new CUBookFragment();
                Book item = (Book) getIntent().getSerializableExtra(Constants.EXTRA_ITEM);
                if ( item != null)
                {
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.EXTRA_ITEM, item );
                    fragment.setArguments(b);
                }
            }
            break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cu, menu);
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
