package funnytime.ru.whitemonkteam.funny_time.funnytime.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.Api;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.Note;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.ChangeItem;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Film;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;

/**
 * Created by Андрей on 24.03.2015.
 */
public class SynchronizeUtils
{
    public static void Sysnc(Activity activity, long id)
    {
        // сначала синхронизация со своим акаунтом
        if ( ShPrefUtils.getUserId(activity) == id)
        {
            //id = 0;
            getMainNotes(id, activity);
        }
    }



    private static void getMainNotes(final long uid, final Activity activity)// ищем главные заметки
    {
        final HashMap<String, Note> map = new HashMap<>();

        new Thread()
        {
            @Override
            public void run()
            {
                try {

                    Api api = new Api(ShPrefUtils.getAccesToken(activity), activity.getResources().getString(R.string.vk_id));
                    //  api.createWallPost(account.user_id, text, null, null, false, false, false, null, null, null, 0L, null, null);

                   // long uid = ShPrefUtils.getUserId(activity); // пока тестирую на себе а дажее тут будет ид текущего друга
                    ArrayList<Long> nids = new ArrayList<Long>();

                    long count = 100;
                    long offset = 0;

                    ArrayList<Note> array ;
                    boolean findFilms, findSerials, findBooks;
                    findFilms = findSerials = findBooks = false;

                    do
                    {
                        nids.clear();
                        array = api.getNotes(uid, nids,"0",count, offset);

                        for ( Note note : array)
                        {
                            if ( note.title.equals(Constants.PATH_NOTE_FILMS_CONTENT))
                            {
                                findFilms = true;
                                map.put(Constants.PATH_NOTE_FILMS_CONTENT, note);
                                //ShPrefUtils.setNoteFilmsContentId(activity, note.nid);
                            }
                            else if ( note.title.equals(Constants.PATH_NOTE_SERIALS_CONTENT))
                            {
                                findSerials = true;
                                map.put(Constants.PATH_NOTE_SERIALS_CONTENT, note);
                               // ShPrefUtils.setNoteSerialsContentId(activity, note.nid);
                            }
                            else if ( note.title.equals(Constants.PATH_NOTE_BOOKS_CONTENT))
                            {
                                findBooks = true;
                                map.put(Constants.PATH_NOTE_BOOKS_CONTENT, note);
                                //ShPrefUtils.setNoteBooksContentId(activity, note.nid);
                            }
                        }

                        if ( findFilms && findSerials && findBooks)
                        {
                            break;
                        }

                        offset += 100;

                    }while (array.size() > 0);


                    ArrayList<Film> films = AppContext.dbAdapter.getFilms(uid);

                    if ( films.size() == 0)
                    {
                        films = AppContext.dbAdapter.getFilms(0);
                    }

                    ArrayList<Long> listIds = new ArrayList<Long>();

                    Gson gson = new Gson();
                    //----------------------------------------------
                    if (films.size() == 0 && map.get(Constants.PATH_NOTE_FILMS_CONTENT) == null)
                    {

                    }
                    else if (films.size() > 0 && map.get(Constants.PATH_NOTE_FILMS_CONTENT) == null)
                    {
                        ProgressDialog pd = new ProgressDialog(activity);
                        pd.setTitle("Идёт синхронизация с приложением");

                        pd.setMax(films.size());
                        pd.setProgress(0);
                        pd.show();
                        for ( int i = 0; i < films.size(); i++)
                        {
                            Film f = films.get(i);
                                long id = api.createNote(Constants.PATH_NOTE_FILMS_CONTENT + "_" + f.Id, AppContext.crypter.encrypt(gson.toJson(f)));
                                listIds.add(id);
                                f.NoteId = id;
                                f.UserId = uid;
                                AppContext.dbAdapter.Update(f);


                                if (listIds.size() > 3)
                                {
                                    api.deleteNote(id);
                                    listIds.remove(id);
                                }
                            pd.setProgress(i);
                            Thread.sleep(200);


                        }


                        long main_film_id = api.createNote(Constants.PATH_NOTE_FILMS_CONTENT, AppContext.crypter.encrypt( gson.toJson(listIds)));

                        pd.setProgress(films.size() + 1);
                        pd.dismiss();
                        // обновить наименование директории

                        ShPrefUtils.setMainFilmNoteId(activity, uid,main_film_id);
                    }
                    else if (films.size() == 0 && map.get(Constants.PATH_NOTE_FILMS_CONTENT) != null)
                    {

                    }
                    else if (films.size() > 0 && map.get(Constants.PATH_NOTE_FILMS_CONTENT) != null)
                    {



                        long main_note_id = ShPrefUtils.getMainFilmNoteId(activity, uid);
                        count = 100;
                         offset = 0;

                        //----------------------------------------------------------------
                        ArrayList<ChangeItem> list = AppContext.dbAdapter.getChangeItems(main_note_id, Constants.ADD);

                        if ( list.size() > 0) // если есть новые заметки которые нужно добавить на сервер
                        {
                            nids.clear();
                            nids.add(ShPrefUtils.getMainFilmNoteId(activity, uid));
                            Note mainNote =   api.getNotes(uid, nids,"0",count, offset).get(0);

                            String json = mainNote.text.replace("<div class=\"wikiText\"><!--4-->", "").replace(" </div>","");

                            json = AppContext.crypter.decrypt(json);


                            ArrayList<Long> longArrayList = new ArrayList<Long>(); // нужно получить реальный массив
                           longArrayList =  gson.fromJson(json,
                                new TypeToken<ArrayList<Long>>() {
                                }.getType()  );

                            for ( ChangeItem item : list)
                            {
                                Film f = AppContext.dbAdapter.getFilm(uid, item.NoteId ); //uid
                                // добавляем заметку и дописываем её в главную заметку
                                long id = api.createNote(Constants.PATH_NOTE_FILMS_CONTENT + f.Name, AppContext.crypter.encrypt( gson.toJson(f)));
                              longArrayList.add(id);
                                f.NoteId = id;
                                f.UserId = uid;

                                AppContext.dbAdapter.Update(f);

                            }

                            api.editNote(mainNote.nid, Constants.PATH_NOTE_FILMS_CONTENT,  AppContext.crypter.encrypt(gson.toJson(longArrayList)));

                            AppContext.dbAdapter.deleteChangeItems(ShPrefUtils.getMainFilmNoteId(activity, uid));

                        }

                        //----------------------------------------------------------------
                        list = AppContext.dbAdapter.getChangeItems(main_note_id, Constants.UPDATE);

                        if ( list.size() > 0) // если есть новые заметки которые нужно добавить на сервер
                        {
                            for ( ChangeItem item : list)
                            {
                                Film f = AppContext.dbAdapter.getFilm(uid, item.NoteId );
                                // добавляем заметку и дописываем её в главную заметку
                                 api.editNote(uid, Constants.PATH_NOTE_FILMS_CONTENT + f.Name, AppContext.crypter.encrypt(gson.toJson(f)));
                            }
                        }

                        //----------------------------------------------------------------
                        list = AppContext.dbAdapter.getChangeItems(main_note_id, Constants.DELETE);

                        if ( list.size() > 0) // если есть новые заметки которые нужно добавить на сервер
                        {
                            nids.clear();
                            nids.add(ShPrefUtils.getMainFilmNoteId(activity, uid));
                            Note mainNote =   api.getNotes(uid, nids,"0",count, offset).get(0);

                            String json = mainNote.text.replace("<div class=\"wikiText\"><!--4-->", "").replace(" </div>","");

                            json = AppContext.crypter.decrypt(json);


                            ArrayList<Long> longArrayList = new ArrayList<Long>(); // нужно получить реальный массив
                            longArrayList =  gson.fromJson(json,
                                    new TypeToken<ArrayList<Long>>() {
                                    }.getType()  );

                            for ( ChangeItem item : list)
                            {
                                api.deleteNote(item.NoteId);
                                longArrayList.remove(item.NoteId);
                            }

                            api.editNote(mainNote.nid, Constants.PATH_NOTE_FILMS_CONTENT, AppContext.crypter.encrypt(gson.toJson(longArrayList)));
                        }

                      //  AppContext.dbAdapter.deleteChangeItems(ShPrefUtils.getMainFilmNoteId(activity, uid));
                    }


                    //----------------------------------------------

                   /* String res = "";
                    if ( findFilms && findSerials && findBooks)
                    {
                        res = "all find";
                    }
                    else
                    {
                        res = "wrong nothing find";
                    }



                    //Показать сообщение в UI потоке
                    final String finalRes = res;*/
                    activity.runOnUiThread( new Runnable()
                    {
                        @Override
                        public void run() {

                            Toast.makeText(activity.getApplicationContext(), "AllAded", Toast.LENGTH_LONG).show();
                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
