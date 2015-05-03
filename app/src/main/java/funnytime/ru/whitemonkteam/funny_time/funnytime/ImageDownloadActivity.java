package funnytime.ru.whitemonkteam.funny_time.funnytime;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.image_loager.adapters.GoogleImageBean;
import funnytime.ru.whitemonkteam.funny_time.funnytime.image_loager.adapters.ListViewImageAdapter;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;


public class ImageDownloadActivity extends ListActivity
{




    private ListViewImageAdapter adapter;
    private ArrayList<Object> listImages;
    private Activity activity;

    String strSearch = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_image_download);
        activity = this;

        Intent intent = getIntent();
        if ( intent != null)
        {
            strSearch = getIntent().getStringExtra(Constants.EXTRA_ITEM);
            strSearch = Uri.encode(strSearch);

           // System.out.println("Search string => "+strSearch);
            new getImagesTask().execute();
        }
        else
        {
            finish();
        }
    }

    public class getImagesTask extends AsyncTask<Void, Void, Void>
    {
        JSONObject json;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = ProgressDialog.show(ImageDownloadActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            URL url;
            try {
                url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                        "v=1.0&q="+strSearch+"&rsz=8"); //&key=ABQIAAAADxhJjHRvoeM2WF3nxP5rCBRcGWwHZ9XQzXD3SWg04vbBlJ3EWxR0b0NVPhZ4xmhQVm3uUBvvRF-VAA&userip=192.168.0.172");

                URLConnection connection = url.openConnection();
                connection.addRequestProperty("Referer", "http://technotalkative.com");

                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                System.out.println("Builder string => "+builder.toString());

                json = new JSONObject(builder.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if(dialog.isShowing())
            {
                dialog.dismiss();
            }

            try {
                JSONObject responseObject = json.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");

                listImages = getImageList(resultArray);
                SetListViewAdapter(listImages);
                System.out.println("Result array length => "+resultArray.length());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public ArrayList<Object> getImageList(JSONArray resultArray)
    {
        ArrayList<Object> listImages = new ArrayList<Object>();
        GoogleImageBean bean;

        try
        {
            for(int i=0; i<resultArray.length(); i++)
            {
                JSONObject obj;
                obj = resultArray.getJSONObject(i);
                bean = new GoogleImageBean();

                bean.setTitle(obj.getString("title"));
                bean.setThumbUrl(obj.getString("tbUrl"));

                System.out.println("Thumb URL => "+obj.getString("tbUrl"));

                listImages.add(bean);

            }
            return listImages;
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    OnImageViewClickListner onClick;
    public void SetListViewAdapter(ArrayList<Object> images)
    {
        onClick = new OnImageViewClickListner(this);
        adapter = new ListViewImageAdapter(activity, images, onClick);
       setListAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_download, menu);
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

    private class OnImageViewClickListner implements ImageView.OnClickListener
    {

        Activity activity;
        public OnImageViewClickListner( Activity activity)
        {
            this.activity = activity;
        }

        @Override
        public void onClick(View v)
        {
            ImageView iv = (ImageView) v;
            Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();

            String url = iv.getTag().toString();
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_ITEM, bitmap);
            intent.putExtra(Constants.EXTRA_USER, url);
            activity.setResult(activity.RESULT_OK, intent);
            activity.finish();
        }
    }
}
