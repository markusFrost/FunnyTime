package funnytime.ru.whitemonkteam.funny_time.funnytime.utils;


import android.os.Environment;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;

public class Constants {


   // public static final String NETWORK_ID = "networkId";
    public static final String VK_TOKEN = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.vk_token";
    public static final String VK_USER_ID = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.vk_user_id";
    public static final String LAST_PAGE = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.lastPage";

    public static final String PATH_USERS_IMAGES = Environment.getExternalStorageDirectory().toString() + "/FunnyTime/UsersImages";
    public static final String PATH_ITEMS_IMAGES = Environment.getExternalStorageDirectory().toString() + "/FunnyTime/ItemsImages";

    public static final String PATH_NOTE_FILMS_CONTENT = "funnytime.ru.whitemonkteam.funny_time.funnytime." + "Films_CONTENT_ID";
    public static final String PATH_NOTE_SERIALS_CONTENT = "funnytime.ru.whitemonkteam.funny_time.funnytime." + "Serials_CONTENT_ID";
    public static final String PATH_NOTE_BOOKS_CONTENT = "funnytime.ru.whitemonkteam.funny_time.funnytime." + "Books_CONTENT_ID";


    public static final String ACTION_TO_SECOND_ACTIVITY_SHOW_PROFILE = "funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity.ACTION_TO_SECOND_ACTIVITY_SHOW_PROFILE";
    public static final String ACTION_TO_SECOND_ACTIVITY_SHOW_FRIENDS = "funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity.ACTION_TO_SECOND_ACTIVITY_SHOW_FRIENDS";
    public static final String ACTION_TO_SECOND_ACTIVITY_DISPLAY_STATISTIC = "funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity.ACTION_TO_SECOND_ACTIVITY_DISPLAY_STATISTIC";
    public static final String ACTION_TO_SECOND_ACTIVITY_DISPLAY_ITEMS_BY_DATE = "funnytime.ru.whitemonkteam.funny_time.funnytime.SecondActivity.ACTION_TO_SECOND_ACTIVITY_DISPLAY_ITEMS_BY_DATE";



    public static final String EXTRA_USER = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.EXTRA_USER";

    public static final String TYPE_ITEM = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.type_item";
    public static final String EXTRA_ITEM = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.extra_item";

    public static final String FILMS =  "Films";
    public static final String SERIALS =  "Serials";
    public static final String BOOKS =  "Books";

    public static final String FILE = "file://";

    public static final int TYPE_FILM = 0;
    public static final int TYPE_SERIAL = 1;
    public static final int TYPE_BOOK = 2;
    public static final int TYPE_STATISTIC = 3;

    public static final int TYPE_WANT_TO_SEE = 12;
    public static final int TYPE_ALREADY_SEE = 13;
    public static final int TYPE_SAW = 14;
    public static final int TYPE_DO_NOT_LIKE = 15;


    public static final int CODE_ADD_FILM = 3;
    public static final int CODE_ADD_SERIAL = 4;
    public static final int CODE_ADD_BOOK = 5;



    public static final int CODE_UPDATE_FILM = 6;
    public static final int CODE_UPDATE_SERIAL = 7;
    public static final int CODE_UPDATE_BOOK = 8;

    public static final int CODE_DELETE_FILM = 9;
    public static final int CODE_DELETE_SERIAL = 10;
    public static final int CODE_DELETE_BOOK = 11;
    public static final int CODE_ADD_IMAGE = 12;

    public static final int ADD = 13;
    public static final int UPDATE = 14;
    public static final int DELETE = 15;

    public static final String ACTION_ADD = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Type";
    public static final String TYPE = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.ADD";
    public static final String ACTION_UPDATE = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.UPDATE";
    public static final String ACTION_DELETE = "funnytime.ru.whitemonkteam.funny_time.funnytime.utils.DELETE";

    public static final int MAX_FILE_LENGTH = 8700;
    public static final int MAX_COUNT = 30;

    public enum SharePost {
        NONE,
        POST_MESSAGE,
        POST_PHOTO,
        POST_LINK,
        POST_DIALOG
    }

    public static final int[] logo = {R.drawable.ic_twitter, R.drawable.ic_linkedin,
            R.drawable.ic_googleplus, R.drawable.ic_facebook, R.drawable.ic_vk,
            R.drawable.ic_odnoklassniki, R.drawable.ic_instagram};
    public static final String[] socialName = {"Twitter","LinkedIn","Google+","Facebook",
            "Vkontakte","Odnoklassniki", "Instagram"};
    public static final int[] userPhoto = {R.drawable.twitter_user, R.drawable.linkedin_user,
            R.drawable.g_plus_user, R.drawable.ic_facebook,
            R.drawable.vk_user, R.drawable.ok_user, R.drawable.ic_instagram};
    public static final int[] color = {R.color.twitter, R.color.linkedin, R.color.google_plus,
            R.color.facebook, R.color.vk, R.color.ok, R.color.instagram};

    public static final String[] fbShare ={"Post status update","Post photo","Post Link",
            "Post Dialog"};
    public static final SharePost[] fbShareNum ={SharePost.POST_MESSAGE, SharePost.POST_PHOTO,
            SharePost.POST_LINK, SharePost.POST_DIALOG};
    public static final String[] twShare ={"Tweet","Tweet with image"};
    public static final SharePost[] twShareNum ={SharePost.POST_MESSAGE, SharePost.POST_PHOTO};
    public static final String[] gpShare ={"Share Dialog"};
    public static final SharePost[] gpShareNum ={SharePost.POST_DIALOG};
    public static final String[] inShare ={"Post status update", "Post share Link"};
    public static final SharePost[] inShareNum ={SharePost.POST_MESSAGE, SharePost.POST_LINK};
    public static final String[] vkShare ={"Post message","Post photo to wall","Post Link"};
    public static final SharePost[] vkShareNum ={SharePost.POST_MESSAGE, SharePost.POST_PHOTO,
            SharePost.POST_LINK};
    public static final String[] okShare ={"Post Link"};
    public static final SharePost[] okShareNum ={SharePost.POST_LINK};
    public static final String[] instagramShare ={"Post Photo"};
    public static final SharePost[] instagramShareNum ={SharePost.POST_PHOTO};
    public static final String[][] share = {twShare, inShare, gpShare, fbShare, vkShare, okShare, instagramShare};
    public static final SharePost[][] shareNum = {twShareNum, inShareNum, gpShareNum, fbShareNum,
            vkShareNum, okShareNum, instagramShareNum};

    public static final String message = "Hello from ASNE!";
    public static final String link = "https://github.com/gorbin/ASNE";

    public static String handleError(int socialNetworkID, String requestID, String errorMessage){
        return "ERROR: " + errorMessage + "in "
                + socialName[socialNetworkID-1] + "SocialNetwork" + " by " + requestID;
    }
}
