package funnytime.ru.whitemonkteam.funny_time.funnytime.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;

/**
 * Created by Андрей on 23.03.2015.
 */
public class BitmapUtils
{
    public static void saveAsImage(Bitmap bitmap, long id) throws FileNotFoundException
    {


        String fname = id +".jpg";
        File file = new File (Constants.PATH_USERS_IMAGES, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String saveAsImage(Bitmap bitmap, int type , long id, long userId) throws FileNotFoundException
    {


        String fname = id +".jpg";
        String path = "";

        path = Constants.PATH_ITEMS_IMAGES + File.separator + userId ;
        if ( type == Constants.TYPE_FILM)
        {
            path += File.separator + Constants.FILMS;
        }
        else if ( type == Constants.TYPE_SERIAL)
        {
            path += File.separator + Constants.SERIALS;
        }
        else if ( type == Constants.TYPE_BOOK)
        {
            path += File.separator + Constants.BOOKS;
        }
        else
        {
            return null;
        }
   boolean fl =      AppContext.createDirIfNotExists(path);
//   /storage/sdcard0/FunnyTime/ItemsImages/0/Films
        File file = new File (path, fname);
        if (file.exists ())
        {
            file.delete ();
        }
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return path + File.separator + fname;

        } catch (Exception e) {
            e.printStackTrace();
            //java.io.FileNotFoundException: /storage/sdcard0/FunnyTime/ItemsImages/0/Films/204.jpg: open failed: ENOENT (No such file or directory)
        }

        return null;
    }
}
