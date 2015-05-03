package funnytime.ru.whitemonkteam.funny_time.funnytime.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.LoginVKActivity;
import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.Api;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.User;
import funnytime.ru.whitemonkteam.funny_time.funnytime.help.SyncUtils;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Book;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Film;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Serial;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.BitmapUtils;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ShPrefUtils;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.SocialCard;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.SynchronizeUtils;


/**
 * Created by Андрей on 20.03.2015.
 */
public class AccountsFragment extends Fragment implements OnRequestSocialPersonCompleteListener
{
    // отдельный файл с чистыми номерами заметок
    // друзья - сортировка те с кем синхр - ся а уж потом остальные
    private SocialCard cardVk;
    private SocialCard cardFb;
    private static final String NETWORK_ID = "NETWORK_ID";


    OnBtnClickLister onClick = new OnBtnClickLister();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        /*
        при загрузке приложения должны заполяться карточки
        если нет подключеия к интернету или время кэширования не прошло  заполяем все из кэша
        если итрет робит и время прошло то каждую карточку заполням индивидуально
        Аналогичные действия происходят когда мы залогинились

        Итак если вы вошли или же при загрузке нужно заполнять новые данные - врубаем библиотеку и тупо заполняем с итернета
        Если сделали логаут или при загрузке страницы этот акаунт не был активироано - то заполняем пустотой
        И третий ваиант - просто нет интерента но ключь есть или аремя кэширования не прошло

         */
        View rootView = inflater.inflate(R.layout.fragment_social_network, container, false);

        cardVk = (SocialCard) rootView.findViewById(R.id.vk_card);
        cardFb = (SocialCard) rootView.findViewById(R.id.fb_card);


        initializeData();

        createAlert();
       //fillCards();

      // postToWall();

      //  loadDataAboutUser();

        return  rootView;
    }



// Здесь мы проверйем какой вид у каждй карты есть ли ключ какие кнопки показывать

    private void initializeData()
    {
        //ShPrefUtils.setAccesToken(getActivity(), null);
        if (AppContext.isNetworkAvailable(getActivity()))
        {
            if ( ShPrefUtils.getAccesToken(getActivity()) != null)
            {
                loadDataAboutUser();
            }
            else
            {
                setCardVKHasIternetNoAccesKey();
            }
        }
        else
        {
            // начинаем вытаскивать какие есть сохранённые даные

          //  ShPrefUtils.setAccesToken(getActivity(), null);

            if ( ShPrefUtils.getAccesToken(getActivity()) != null)
            {
                setCardVKNoIternetHasAccesKey();
            }
            else
            {
                //делаем все пустым
                //fill vk data
                setCardVKNoIternetNoAccesKey();


            }
        }
    }

    private void setCardVKHasIternetNoAccesKey()
    {
        cardVk.detail.setText("");
        cardVk.setName("");
        cardVk.setId("");

        cardVk.image.setImageResource(R.drawable.vk_user);
        cardVk.divider.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));
        cardVk.image.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));
        cardVk.connect.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.connect.setText(getActivity().getResources().getString(R.string.vk_login));
        cardVk.connect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vk, 0, 0, 0);

        cardVk.logout.setVisibility(View.GONE);
        cardVk.friends.setVisibility(View.GONE);
        cardVk.share.setVisibility(View.GONE);

        cardVk.connect.setOnClickListener(onClick);
    }

    private void setCardVKNoIternetNoAccesKey()
    {
        cardVk.detail.setText("");
        cardVk.setName("");
        cardVk.setId("");

        cardVk.image.setImageResource(R.drawable.vk_user);
        cardVk.divider.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));
        cardVk.image.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));
        cardVk.connect.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.connect.setText(getActivity().getResources().getString(R.string.vk_login));
        cardVk.connect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vk, 0, 0, 0);

        cardVk.logout.setVisibility(View.GONE);
        cardVk.friends.setVisibility(View.GONE);
        cardVk.share.setVisibility(View.GONE);


    }

    private void setCardVKNoIternetHasAccesKey()
    {
        // из БД нужно выдернуть юзера
        User user = new User();
        user.first_name = "Andrey";
        user.last_name = "Vystavkin";
        user.uid = ShPrefUtils.getUserId(getActivity());
        user.photo_200 = Constants.PATH_USERS_IMAGES + File.separator +  user.uid +".jpg"; // создаём пока фейк

        cardVk.setName(user.first_name + " " + user.last_name);
        cardVk.detail.setText("");
        cardVk.setId(user.photo_200);
        cardVk.divider.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));
        cardVk.image.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));
        Picasso.with(getActivity()).load("file://" + user.photo_200).into(cardVk.image);



        cardVk.connect.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.connect.setText(getActivity().getResources().getString(R.string.vk_login));
        cardVk.connect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vk, 0, 0, 0);

        cardVk.connect.setVisibility(View.GONE);
        cardVk.logout.setVisibility(View.VISIBLE);
        cardVk.share.setVisibility(View.VISIBLE);
        cardVk.friends.setVisibility(View.VISIBLE);

        cardVk.logout.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.share.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.friends.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));

        cardVk.logout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vk, 0, 0, 0);


    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
        {
            cardVk.image.setImageBitmap(bitmap);
            try {
                BitmapUtils.saveAsImage(bitmap, ShPrefUtils.getUserId(getActivity()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }


    };

    private void setCardVKHasIternetHasAccesKeyTimeToKesh(final User user)
    {
        // подключен интренет, есть ключ и время кэшировать
        cardVk.setName(user.first_name + " " + user.last_name);
        cardVk.detail.setText("");
        cardVk.setId(user.photo_200);
        cardVk.divider.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));
        cardVk.image.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));

        Picasso.with(getActivity()).load(user.photo_200).into( target);

        cardVk.connect.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.connect.setText(getActivity().getResources().getString(R.string.vk_login));
        cardVk.connect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vk, 0, 0, 0);

        cardVk.connect.setVisibility(View.GONE);
        cardVk.logout.setVisibility(View.VISIBLE);
        cardVk.share.setVisibility(View.VISIBLE);
        cardVk.friends.setVisibility(View.VISIBLE);

        cardVk.logout.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.share.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.friends.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));

        cardVk.logout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vk, 0, 0, 0);

        cardVk.logout.setOnClickListener(onClick);
        cardVk.friends.setOnClickListener(onClick);
        cardVk.share.setOnClickListener(onClick);




    }
    //----------------------------------------------



    private void loadDataAboutUser()
    {
        //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
        new Thread(){
            @Override
            public void run(){
                try {
                    Api api = new Api(ShPrefUtils.getAccesToken(getActivity()), getActivity().getResources().getString(R.string.vk_id));
                    //  api.createWallPost(account.user_id, text, null, null, false, false, false, null, null, null, 0L, null, null);
                    //api.createNote("titleFT111", "text11111");


                    long user_id = ShPrefUtils.getUserId(getActivity());
                    // заполняем поля
                    ArrayList<Long> uids = new ArrayList<Long>();
                    uids.add(user_id);

                    String fields = "photo_200,first_name,last_name,id";

                    String name_case = "nom";
                    //---------------------------------

                    ArrayList<User> list = api.getProfiles(uids, null,fields, name_case, "0", "0" );

                    for ( final User us : list)
                    {
                        if ( us.uid == user_id)
                        {


                            //Показать сообщение в UI потоке
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setCardVKHasIternetHasAccesKeyTimeToKesh(us);
                                    Toast.makeText(getActivity().getApplicationContext(), "Запись успешно добавлена", Toast.LENGTH_LONG).show();
                                }
                            });

                            break;
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



    private void fillCards()
    {

        //fill vk data
        cardVk.detail.setText("Some Text");
        cardVk.setName("Андрей Выставкин");
        cardVk.setId("Some iD");

        cardVk.name.setTextColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.divider.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));
        cardVk.image.setBackgroundColor(getActivity().getResources().getColor(R.color.vk_light));

        cardVk.connect.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
        cardVk.connect.setText(getActivity().getResources().getString(R.string.vk_login));
        cardVk.connect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vk, 0, 0, 0);


//        cardVk.connect.setVisibility(View.GONE);
//        cardVk.logout.setVisibility(View.VISIBLE);
//        cardVk.share.setVisibility(View.VISIBLE);
//        cardVk.friends.setVisibility(View.VISIBLE);
//        cardVk.logout.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
//        cardVk.share.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
//        cardVk.friends.setBackgroundColor(getActivity().getResources().getColor(R.color.vk));
//        cardVk.logout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vk, 0, 0, 0);
        cardVk.connect.setOnClickListener(onClick);
        cardVk.logout.setOnClickListener(onClick);

        //------------------------
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CODE_LOGIN && resultCode == getActivity().RESULT_OK && data != null)
        {
            /*
             account.access_token=data.getStringExtra("token");
                account.user_id=data.getLongExtra("user_id", 0);
                account.save(MainActivity.this);
                api=new Api(account.access_token, Constants.API_ID);
             */
            String acces_token = data.getStringExtra(Constants.VK_TOKEN);
            long userId = data.getLongExtra(Constants.VK_USER_ID, 0);

           // Toast.makeText(getActivity(), "user id = " + userId , Toast.LENGTH_SHORT).show();


            if ( ShPrefUtils.getUserId(getActivity()) == 0)
            {
                ArrayList<Film> listF = AppContext.dbAdapter.getFilms(ShPrefUtils.getUserId(getActivity()));
                for (Film f : listF)
                {
                    f.UserId = userId;
                    AppContext.dbAdapter.Update(f);
                }


                ArrayList<Serial> listS = AppContext.dbAdapter.getSerials(ShPrefUtils.getUserId(getActivity()));
                for (Serial s : listS)
                {
                    s.UserId = userId;
                    AppContext.dbAdapter.Update(s);
                }

                ArrayList<Book> listB = AppContext.dbAdapter.getBooks(ShPrefUtils.getUserId(getActivity()));
                for (Book b : listB)
                {
                    b.UserId = userId;
                    AppContext.dbAdapter.Update(b);
                }
            }

            ShPrefUtils.setAccesToken(getActivity().getApplicationContext(), acces_token);
            ShPrefUtils.setUserId(getActivity().getApplicationContext(), userId);

            if (AppContext.dbAdapter.getFilms(ShPrefUtils.getUserId(getActivity())).size() == 0)
            {
                SyncUtils.syncEmptyFilmData(getActivity(), userId);
            }
            else
            {
                SyncUtils.syncFilms(getActivity());
            }
            // заполняем карту

            loadDataAboutUser();

            /*
            '1 - k = 0 s = 0
            2 - k = 1 s = 0
            3 k = 1 s = 1
             */

        }
        super.onActivityResult(requestCode, resultCode, data);
    }




   /* private void loadDataAboutUser()
    {
        new Thread(){
            @Override
            public void run(){
                try {
                    Api api = new Api(ShPrefUtils.getAccesToken(getActivity()), getActivity().getResources().getString(R.string.vk_id));
                    //  api.createWallPost(account.user_id, text, null, null, false, false, false, null, null, null, 0L, null, null);
                    api.createNote("titleFT111", "text11111");

                    //Показать сообщение в UI потоке
                    getActivity().runOnUiThread(successRunnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }*/

private static final int CODE_LOGIN = 1;

    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson)
    {
        // hide progressbar

        Toast.makeText(getActivity().getApplicationContext(), "name = " + socialPerson.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int i, String s, String s2, Object o) {

    }

    private class OnBtnClickLister implements Button.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            // если нет интерета то надо прейти на страничку где включается итерет
            switch (v.getId())
            {
                case R.id.connect:
                {
                    Intent intent = new Intent(getActivity(), LoginVKActivity.class);
                    startActivityForResult(intent, CODE_LOGIN);
                }break;
                case R.id.logout:
                {

                }break;
                case R.id.info:
                {
                    /**/
                    Intent intent = new Intent(getActivity(), SecondActivity.class);
                    intent.setAction(Constants.ACTION_TO_SECOND_ACTIVITY_SHOW_FRIENDS);
                    startActivity(intent);
                }
            }
        }
    }



    private void  createAlert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(R.string.alertNotif);
        alert.setMessage(R.string.alertNotSyncData);

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //syncData();
                SynchronizeUtils.Sysnc(getActivity(), ShPrefUtils.getUserId(getActivity()));
            }
        });

        alert.setNegativeButton(android.R.string.no, null);

        alert.show();

    }



}

