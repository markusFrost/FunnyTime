package funnytime.ru.whitemonkteam.funny_time.funnytime.image_loager.adapters;

/**
 * @Author Paresh N. Mayani
 * @Web http://www.technotalkative.com
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.image_loager.adapters.utils.ImageLoader;


public class ListViewImageAdapter extends BaseAdapter {
    
	private Activity activity;
	public ArrayList<Object> listImages;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader;
    View.OnClickListener onClick;
	
    public ListViewImageAdapter(Activity a, ArrayList<Object> listImages,  View.OnClickListener onClick)
    {
        activity = a;
        this.onClick = onClick;
        this.listImages = listImages;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return listImages.size();
    }

    public Object getItem(int position) {
        //return position;
    	return listImages.get(position);
    }

    public long getItemId(int position) 
    {
    	return position;
    }
    
    public static class ViewHolder{
    	public ImageView imgViewImage;
    	//public TextView txtViewTitle;
    	
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	View vi=convertView;
    	ViewHolder holder;
		if(convertView==null){
			vi = inflater.inflate(R.layout.listview_row, null);
			holder=new ViewHolder();
			
			holder.imgViewImage=(ImageView)vi.findViewById(R.id.imageView01);
            holder.imgViewImage.setOnClickListener(onClick);
			//holder.txtViewTitle=(TextView)vi.findViewById(R.id.textView1);
			
			vi.setTag(holder);
		}
		else
			holder=(ViewHolder)vi.getTag();
		
		GoogleImageBean imageBean = (GoogleImageBean) listImages.get(position);
		holder.imgViewImage.setTag(imageBean.getThumbUrl());
		imageLoader.DisplayImage(imageBean.getThumbUrl(), activity, holder.imgViewImage);
		
		//holder.txtViewTitle.setText(Html.fromHtml(imageBean.getTitle()));
		//holder.txtViewTitle.setText("");
		return vi;
    }
}