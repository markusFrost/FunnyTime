package funnytime.ru.whitemonkteam.funny_time.funnytime.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.Api;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.Note;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.User;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ShPrefUtils;

/**
 * Created by Андрей on 21.03.2015.
 */
public class FriendVkProfileFragment extends Fragment
{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.friend_vk_profile, container, false);

        Bundle args = getArguments();
        User user = null;
        if ( args != null)
        {
             user = (User) getArguments().getSerializable(Constants.EXTRA_USER);
        }

        SecondActivity activity = (SecondActivity) getActivity();

       // android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();

      // actionBar .setTitle("Andrey Vystavkin");




        Button btnFilms, btnSerials, btnBooks, btnStatistic;
        ImageView iv;

        OnBtnClickListner onClick = new OnBtnClickListner();

        btnFilms = (Button) v.findViewById(R.id.profile_btnFilm);
        btnSerials = (Button) v.findViewById(R.id.profile_btnSerials);
        btnBooks = (Button) v.findViewById(R.id.profile_btnBooks);
        btnStatistic = (Button) v.findViewById(R.id.profile_btnStatistic);
        iv = (ImageView) v.findViewById(R.id.profile_imageView);

        btnFilms.setOnClickListener(onClick);
        btnSerials.setOnClickListener(onClick);
        btnBooks.setOnClickListener(onClick);
        btnStatistic.setOnClickListener(onClick);

        if ( user != null)
        {
           // actionBar.setTitle(user.first_name + " " + user.last_name);

            Picasso.with(getActivity())
                    .load(user.photo_200)
                    .placeholder(R.drawable.vk_user)
                    .error(R.drawable.vk_user)
                    .into(iv);
        }

        searchMainNote();

        return v;
    }

    private void searchMainNote()
    {
        /*
        пока не нашли заметку с нужным названием отодвигаем диапазон поиска
        т е пока лист size != 0 если мы её так и не нашли значит пользователь бесполезен
        если же нашли то уже вытаскиваем все заметки и сохраняем все в БД
        не забыть про кэширование
         */


        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    Api api = new Api(ShPrefUtils.getAccesToken(getActivity()), getActivity().getResources().getString(R.string.vk_id));
                    //  api.createWallPost(account.user_id, text, null, null, false, false, false, null, null, null, 0L, null, null);

                    long uid = ShPrefUtils.getUserId(getActivity()); // пока тестирую на себе а дажее тут будет ид текущего друга
                    ArrayList<Long> nids = new ArrayList<Long>();

                    long count = 100;
                    long offset = 0;

                    ArrayList<Note> array ;
                    boolean findFilms, findSerials, findBooks;
                    findFilms = findSerials = findBooks = false;

                    do
                    {
                        array = api.getNotes(uid, nids,"0",count, offset);

                        for ( Note note : array)
                        {
                                if ( note.title.equals(Constants.PATH_NOTE_FILMS_CONTENT))
                                {
                                    findFilms = true;
                                    ShPrefUtils.setNoteFilmsContentId(getActivity(), note.nid);
                                }
                            else if ( note.title.equals(Constants.PATH_NOTE_SERIALS_CONTENT))
                                {
                                    findSerials = true;
                                    ShPrefUtils.setNoteSerialsContentId(getActivity(), note.nid);
                                }
                                else if ( note.title.equals(Constants.PATH_NOTE_BOOKS_CONTENT))
                                {
                                    findBooks = true;
                                    ShPrefUtils.setNoteBooksContentId(getActivity(), note.nid);
                                }
                        }

                        if ( findFilms && findSerials && findBooks)
                        {
                            break;
                        }

                        offset += 100;

                    }while (array.size() > 0);

                    String res = "";
                    if ( findFilms && findSerials && findBooks)
                    {
                        res = "all find";
                    }
                    else
                    {
                        res = "wrong nothing find";
                    }



                    //Показать сообщение в UI потоке
                    final String finalRes = res;
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {



                            Toast.makeText(getActivity().getApplicationContext(), finalRes, Toast.LENGTH_LONG).show();
                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private class OnBtnClickListner implements Button.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.profile_btnFilm :
                {

                } break;
                case R.id.profile_btnSerials :
                {

                } break;
                case R.id.profile_btnBooks :
                {

                } break;
                case R.id.profile_btnStatistic :
                {

                } break;
            }
        }
    }
}
