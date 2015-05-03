package funnytime.ru.whitemonkteam.funny_time.funnytime.objects;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;

import funnytime.ru.whitemonkteam.funny_time.funnytime.DbAdapter;
import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.Api;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ShPrefUtils;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.StringCrypter;

/**
 * Created by Андрей on 20.03.2015.
 */
public class AppContext extends Application
{

    public static DbAdapter dbAdapter;
    public static StringCrypter crypter;
    public static Api api;
    @Override
    public void onCreate()
    {
        super.onCreate();

        crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});

       createDirIfNotExists(Constants.PATH_USERS_IMAGES);
       // createDirIfNotExists(Constants.PATH_ITEMS_IMAGES);

        dbAdapter = new DbAdapter(getApplicationContext());

         api = new Api(ShPrefUtils.getAccesToken(getApplicationContext()), getApplicationContext().getResources().getString(R.string.vk_id));




    }

    public static boolean isNetworkAvailable(Activity activity)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean createDirIfNotExists(String path)
    {
        boolean ret = true;

        File file = new File( path);
        if (!file.exists())
        {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }

        String pat = file.getPath();
        return ret;
    }
}
