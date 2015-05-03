package funnytime.ru.whitemonkteam.funny_time.funnytime.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Андрей on 20.03.2015.
 */
public class ShPrefUtils
{
    private static String FILE_NAME = "FunnyTime";

    public static void setAccesToken(Context context, String token)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();
        editor.putString(Constants.VK_TOKEN, token);
        editor.commit();
    }

    public static String getAccesToken(Context context)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sd.getString(Constants.VK_TOKEN, null);

    }

    //---------------------------------------------------
    public static void setUserId(Context context, long id)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();
        editor.putLong(Constants.VK_USER_ID, id);
        editor.commit();
    }

    public static long  getUserId(Context context)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sd.getLong(Constants.VK_USER_ID, 0);

    }

    //----------------------------------------------------

    public static void setNoteFilmsContentId(Context context, long id)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();
        editor.putLong(Constants.PATH_NOTE_FILMS_CONTENT, id);
        editor.commit();
    }

    public static long  getNoteFilmsContentId(Context context)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sd.getLong(Constants.PATH_NOTE_FILMS_CONTENT, -1);

    }

    //----------------------------------------------------


    public static void setNoteSerialsContentId(Context context, long id)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();
        editor.putLong(Constants.PATH_NOTE_SERIALS_CONTENT, id);
        editor.commit();
    }

    public static long  getNoteSerialsContentId(Context context)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sd.getLong(Constants.PATH_NOTE_SERIALS_CONTENT, -1);

    }

    //----------------------------------------------------

    public static void setNoteBooksContentId(Context context, long id)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();
        editor.putLong(Constants.PATH_NOTE_BOOKS_CONTENT, id);
        editor.commit();
    }

    public static long  getNoteBooksContentId(Context context)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sd.getLong(Constants.PATH_NOTE_BOOKS_CONTENT, -1);

    }
    //----------------------------------------------------

    public static void setLastPage(Context context, int pos)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();
        editor.putInt(Constants.LAST_PAGE,  pos);
        editor.commit();
    }

    public static int getLastPage(Context context)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sd.getInt(Constants.LAST_PAGE, 1);

    }

    //----------------------------------------------------

    public static void setMainFilmNoteId(Context context, long user_id, long note_id)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();
        editor.putLong(Constants.PATH_NOTE_FILMS_CONTENT + user_id,  note_id);
        editor.commit();
    }

    public static long getMainFilmNoteId(Context context, long user_id)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sd.getLong(Constants.PATH_NOTE_FILMS_CONTENT + user_id, -1);

    }

    //----------------------------------------------------

    public static void setMainFilmNotes(Context context,  ArrayList<Long> array)
    {
        String json;
        if ( array == null)
        {
            json = null;
        }
        else
        {
            json = new Gson().toJson(array);
        }
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sd.edit();
        editor.putString(Constants.PATH_NOTE_FILMS_CONTENT,  json);
        editor.commit();
    }

    public static ArrayList<Long> getMainFilmNotes(Context context)
    {
        SharedPreferences sd = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String json =  sd.getString(Constants.PATH_NOTE_FILMS_CONTENT , null);

        ArrayList<Long> array;
        if ( json == null)
        {
           array =   new ArrayList<Long>();
        }
        else
        {
            array = new Gson().fromJson(json,
                    new TypeToken<ArrayList<Long>>() {
                    }.getType());
        }

        return array;

    }

    //----------------------------------------------------

}

