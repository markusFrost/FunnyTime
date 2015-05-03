package funnytime.ru.whitemonkteam.funny_time.funnytime.help;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.api.Note;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Film;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ShPrefUtils;

/**
 * Created by Андрей on 28.04.2015.
 */
public class SyncUtils
{

    public static void syncEmptyFilmData(final Activity context, final long user_id)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    ArrayList<Long> nids = ShPrefUtils.getMainFilmNotes(context);
                    long count = 100;
                    long offset = 0;
                    String json = "";

                    // user_id = 53074293;

                    ArrayList<Note> notes = new ArrayList<Note>();
                    ArrayList<Film> films = new ArrayList<Film>();


                        do {
                            notes = AppContext.api.getNotes(user_id, nids, "0", count, offset);


                            for (Note note : notes) {
                                if (note.title.contains(Constants.PATH_NOTE_FILMS_CONTENT)) {
                                    json = note.text.replace("<div class=\"wikiText\"><!--4-->", "").replace(" </div>", "");
                                    ArrayList<Film> filmArrayList = new Gson().fromJson(json,
                                            new TypeToken<ArrayList<Film>>() {
                                            }.getType());

                                    films.addAll(filmArrayList);


                                }
                            }

                            offset += 100;
                        } while (notes.size() > 0);


                        for (Film f : films)
                        {
                            f.UserId = user_id;
                            AppContext.dbAdapter.Add(f);
                        }



                } catch (Exception e)
                {
                    e.printStackTrace();

                }
            }
        }.start();
    }

    public static void readFriendsData(final Activity context,  final long user_id )
    {

        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    ArrayList<Long> nids = ShPrefUtils.getMainFilmNotes(context);
                    long count = 100;
                    long offset = 0;
                    String json;

                   // user_id = 53074293;

                     ArrayList<Note> notes = new ArrayList<Note>();

                    do
                    {
                        notes = AppContext.api.getNotes(user_id, nids, "0", count, offset);

                        ArrayList<Film> films = new ArrayList<Film>();
                        for (Note note : notes)
                        {
                            if (note.title.contains(Constants.PATH_NOTE_FILMS_CONTENT))
                            {
                                json = note.text.replace("<div class=\"wikiText\"><!--4-->", "").replace(" </div>", "");
                                ArrayList<Film> filmArrayList = new Gson().fromJson(json,
                                        new TypeToken<ArrayList<Film>>() {
                                        }.getType());

                                films.addAll(filmArrayList);

                                for ( Film f : filmArrayList)
                                {
                                    //f.UserId = user_id;
                                    //AppContext.dbAdapter.Add(f);
                                }
                            }
                        }

                        offset += 100;
                    }while (notes.size() > 0);



                } catch (Exception e)
                {
                    e.printStackTrace();

                }
            }
        }.start();
    }

    public static void syncFilms(final Activity context)
    {
/*
перед синхронизацией предыдущие заметки удалить - хранить в преференсах
 */

        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    long uid = ShPrefUtils.getUserId(context);
                    ArrayList<Film> list = AppContext.dbAdapter.getFilms(uid);
                    ArrayList<Long> nids = ShPrefUtils.getMainFilmNotes(context);

                  //  nids = new ArrayList<Long>();
                    if ( nids.size() > 0)
                    {
                        for ( long id : nids)
                        {
                            AppContext.api.deleteNote(id);
                        }
                        nids.clear();
                    }


                    list.addAll(AppContext.dbAdapter.getFilms(uid));

                    for (int i = 0; i < list.size(); i++)
                    {
                        list.get(i).ImageURL = "";
                    }

                    String json = "";
                    int pos = 0;
                    int k = 0;
                    int right = 0;

                    do
                    {

                    ArrayList<Film> array = new ArrayList<Film>();

                        if ( pos + Constants.MAX_COUNT > list.size())
                        {
                            right = list.size();
                        }
                        else
                        {
                            right = pos + Constants.MAX_COUNT;
                        }
                    for (int i = pos; i < right; i++)
                    {
                        array.add(list.get(i));
                    }
                    pos += Constants.MAX_COUNT;

                    json = new Gson().toJson(array);
                    k = json.length();

                    while (k >= Constants.MAX_FILE_LENGTH)
                    {
                        array.remove(array.size() - 1);
                        json = new Gson().toJson(array);
                        k = json.length();
                        pos--;
                    }

                        String cap_id = "732966905369";
                        String text = "qunv";
                        long note_id =  AppContext.api.createNote(Constants.PATH_NOTE_FILMS_CONTENT, json,  text, cap_id);
                        nids.add(note_id);
                }while (pos < list.size());

                    ShPrefUtils.setMainFilmNotes(context, nids);

                    context.runOnUiThread( new Runnable()
                    {
                        @Override
                        public void run() {

                            Toast.makeText(context.getApplicationContext(), "AllAded", Toast.LENGTH_LONG).show();
                        }
                    });

/*
алгоритм считывания данных

long count = 100;
                    long offset = 0;
                    long user_id = 53074293;
                    ArrayList<Note> notes  =   AppContext.api.getNotes(user_id, nids,"0",count, offset);

                    ArrayList<Film> films = new ArrayList<Film>();
                    for ( Note note : notes)
                    {
                        json = note.text.replace("<div class=\"wikiText\"><!--4-->", "").replace(" </div>","");
                        ArrayList<Film> filmArrayList = new Gson().fromJson(json,
                            new TypeToken<ArrayList<Film>>()
                            {
                            }.getType()  );

                        films.addAll(filmArrayList);
                    }


                   json += "";
                   */
                  // Toast.makeText(context.getApplicationContext(), "Size " + list.size(), Toast.LENGTH_LONG).show();

                }
               /* catch (KException e)
                {
                    // создать msg И повторить запрос

                   if ( e.error_code == 14)
                    {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        String url = e.captcha_img;
                       final String sid = e.captcha_sid;
                        alert.setTitle(R.string.capture_text);
                        View view = context.getLayoutInflater().inflate(R.layout.capture_layout, null);

                        ImageView iv = (ImageView) view.findViewById(R.id.capture_iv);
                        final EditText edit = (EditText) view.findViewById(R.id.capture_editKey);

                        Picasso.with(context).load(url).into(iv);

                        alert.setView(view);

                        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String key = edit.getText().toString();
                                try
                                {
                                    AppContext.api.createNote(Constants.PATH_NOTE_FILMS_CONTENT, key,key, sid);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (KException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });

                        alert.setNeutralButton(android.R.string.cancel, null);

                        alert.show();
                    }
                }*/

                //http://api.vk.com/captcha.php?sid=990093342020&s=1
                //990093342020
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    /*if ( e.getMessage().contains("Note not found"))
                    {


                    }*/

                }
            }
        }.start();
    }
}
