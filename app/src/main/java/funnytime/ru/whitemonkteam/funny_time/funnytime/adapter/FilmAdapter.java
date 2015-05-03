package funnytime.ru.whitemonkteam.funny_time.funnytime.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Film;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;

public class FilmAdapter extends BaseAdapter
{
	Activity context;
	 List<Film> list;
	
	
	public FilmAdapter(Activity context)
	{
		this.context = context;
		 this.list = AppContext.dbAdapter.getFilms(0);
        //this.list = new ArrayList<>();
		
		//String json = new Gson().toJson(list);

	}
	
	public FilmAdapter(Activity context, ArrayList<Film> list)
	{
		this.context = context;
		this.list = list;
	}
	
	
	public long Add(Object obj)
	{
	    Film item = (Film) obj;	
		list.add(0, item);
		
		return item.Id;
	}
	
	public void Update(Object obj)
	{
		 Film item = (Film) obj;
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).Id == item.Id)
			{
				//list.set(i, item);
                list.remove(i);
                list.add(0,item);
				
				break;
			}
		}
	}
	/*public void Select(Boolean wasSeen)
	{
		list = AppContext.dbAdapter.getFilms();
		ArrayList<Film> films = new ArrayList<Film>();
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).WasSeen == null && wasSeen == null)
			{
				films.add(list.get(i));
			}
			else if (list.get(i).WasSeen == wasSeen)
			{
				films.add(list.get(i));
			}
		}
		list = films;
	}
	public void Select()
	{
		list = AppContext.dbAdapter.getFilms();
	}
	public void Search(String name)
	{
		
		list = AppContext.dbAdapter.getFilms();
		if (name == null)
		{
			return;
		}
		ArrayList<Film> films = new ArrayList<Film>();
		for ( Film f : list)
		{
			if (f.Name.contains(name))
			{
				films.add(f);
			}
		}
		list = films;
	}
	
	public List<Film> Read()
	{
		return list;
	}*/
	
	public void Delete(Object obj)
	{
		 Film item = (Film) obj;
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).Id == item.Id)
			{
				list.remove(i);
				
				break;
			}
		}
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.films_layout,
					parent, false);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.filmTvName);
			viewHolder.tvLabel = (TextView) convertView
					.findViewById(R.id.filmTvLabelMark);
			viewHolder.tvMark = (TextView) convertView
                .findViewById(R.id.filmTvMark);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.filmIv);

			convertView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		holder.tvName.setText(list.get(position).Name);

        Picasso.with(context)
                .load(Constants.FILE + list.get(position).ImagePath)
                .placeholder(android.R.drawable.ic_dialog_info)
                .error(android.R.drawable.ic_dialog_info)
                .into(holder.imageView);

        Film item = list.get(position);

        holder.tvLabel.setVisibility(View.GONE);
        holder.tvMark.setVisibility(View.GONE);

        if ( item.Action == Constants.TYPE_WANT_TO_SEE)
        {
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvMark.setVisibility(View.GONE);

            holder.tvLabel.setText(R.string.wantSee);
        }
        else if (item.Action == Constants.TYPE_SAW)
        {
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvMark.setVisibility(View.VISIBLE);

            holder.tvLabel.setText(R.string.strMark);
            holder.tvMark.setText(" " + item.Mark  + "");
        }
        else if (item.Action == Constants.TYPE_DO_NOT_LIKE)
        {
            holder.tvLabel.setVisibility(View.GONE);
            holder.tvMark.setVisibility(View.VISIBLE);

            holder.tvLabel.setText(R.string.strMark);
            holder.tvMark.setText(R.string.doNotLike);
        }


		return convertView;
				
	}
	
	static class ViewHolder
	{
		TextView tvName;
		TextView tvLabel;
		TextView tvMark;
        ImageView imageView;
		
	}

}
