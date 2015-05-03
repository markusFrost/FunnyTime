package funnytime.ru.whitemonkteam.funny_time.funnytime.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity;
import funnytime.ru.whitemonkteam.funny_time.funnytime.adapter.FriendsListVKAdapter;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.Api;
import funnytime.ru.whitemonkteam.funny_time.funnytime.api.User;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ShPrefUtils;

/**
 * Created by Андрей on 21.03.2015.
 */
public class FriendsVKFragment extends ListFragment
{
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        new Thread()
        {
            @Override
            public void run() {
                try {
                    Api api = new Api(ShPrefUtils.getAccesToken(getActivity()), getActivity().getResources().getString(R.string.vk_id));
                    //  api.createWallPost(account.user_id, text, null, null, false, false, false, null, null, null, 0L, null, null);

                    long user_id = ShPrefUtils.getUserId(getActivity());
                    String fields = "photo_200,first_name,last_name,id";
                    final ArrayList<User> list = api.getFriends(user_id, fields, null, "0", "0");


                    //Показать сообщение в UI потоке
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            //    setCardVKHasIternetHasAccesKeyTimeToKesh(us);

                           initializeListView(list);
                            Toast.makeText(getActivity().getApplicationContext(), "Size =  " + list.size(), Toast.LENGTH_LONG).show();
                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

     }
    FriendsListVKAdapter adapter;
    private void initializeListView(ArrayList<User> list)
    {
        adapter = new FriendsListVKAdapter(getActivity(), list);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                User user = (User) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.setAction(Constants.ACTION_TO_SECOND_ACTIVITY_SHOW_PROFILE);
                intent.putExtra(Constants.EXTRA_USER, user);
                startActivity(intent);


            }
        });


    }
}
