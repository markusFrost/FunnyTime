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
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Book;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;

public class BookAdapter extends BaseAdapter
{

	Activity context;
	 List<Book> list;
	 
	
	public BookAdapter (Activity context)
		{
			this.context = context;
			list = AppContext.dbAdapter.getBooks(0);
           // this.list = new ArrayList<>();
			
			
			
		}
	
	public BookAdapter (Activity context, ArrayList<Book> list)
	{
		this.context = context;
		this.list = list;
		
	}
	
	public long Add(Object obj)
	{
		Book item = (Book) obj;
		list.add(0,item);
		
		return item.Id;
	}
	
	public void Update(Object obj)
	{
		Book item = (Book) obj;
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
	
	public void Delete(Object obj)
	{
		Book item = (Book) obj;
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).Id == item.Id)
			{
				list.remove(i);
			
				break;
			}
		}
	}
	
/*	public void Select(Boolean wasSeen)
	{
		list = AppContext.dbAdapter.getBooks();
		ArrayList<Book> films = new ArrayList<Book>();
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
		list = AppContext.dbAdapter.getBooks();
	}
	
	public void Search(String name)
	{
		
		list = AppContext.dbAdapter.getBooks();
		if (name == null)
		{
			return;
		}
		ArrayList<Book> books = new ArrayList<Book>();
		for ( Book b : list)
		{
			if (b.Name.contains(name))
			{
				books.add(b);
			}
		}
		
		if ( books.size() == 0)
		{
			for ( Book b : list)
			{
				if (b.Author.contains(name))
				{
					books.add(b);
				}
			}
		}
		list = books;
	}
	*/
	public List<Book> Read()
	{
		return list;
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
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if (convertView == null) 
			{
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.book_layout,
						parent, false);

				ViewHolder viewHolder = new ViewHolder();

				viewHolder.tvName = (TextView) convertView
						.findViewById(R.id.bookTvName);
				viewHolder.tvAutor = (TextView) convertView
						.findViewById(R.id.bookTvAutor);
				viewHolder.tvLabel = (TextView) convertView
						.findViewById(R.id.bookTvLabelMark);
				viewHolder.tvMark = (TextView) convertView
						.findViewById(R.id.bookTvResult);
                viewHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.bookIv);

				convertView.setTag(viewHolder);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			
			holder.tvName.setText(list.get(position).Name);
            holder.tvAutor.setText(list.get(position).Author);

            holder.tvLabel.setVisibility(View.GONE);
            holder.tvMark.setVisibility(View.GONE);

            Picasso.with(context)
                    .load(Constants.FILE + list.get(position).ImagePath)
                    .placeholder(android.R.drawable.ic_dialog_info)
                    .error(android.R.drawable.ic_dialog_info)
                    .into(holder.imageView);

            Book item = list.get(position);

            if ( item.Action == Constants.TYPE_WANT_TO_SEE)
            {
                holder.tvLabel.setVisibility(View.VISIBLE);
                holder.tvMark.setVisibility(View.GONE);
                holder.tvLabel.setText(   R.string.wantRead  );

            }
            else if ( item.Action == Constants.TYPE_ALREADY_SEE)
            {
                holder.tvLabel.setVisibility(View.VISIBLE);
                holder.tvMark.setVisibility(View.VISIBLE);
                holder.tvLabel.setText(R.string.strRead);
                holder.tvMark.setText(list.get(position).Page + "" );
            }
            else if ( item.Action == Constants.TYPE_SAW)
            {
                holder.tvMark.setVisibility(View.VISIBLE);
                holder.tvLabel.setVisibility(View.VISIBLE);

                holder.tvLabel.setText(R.string.strMark);
                holder.tvMark.setText(list.get(position).Mark + "");
            }
            else if ( item.Action == Constants.TYPE_DO_NOT_LIKE)
            {
                holder.tvLabel.setVisibility(View.VISIBLE);
                holder.tvMark.setVisibility(View.GONE);
                holder.tvLabel.setText(   R.string.doNotLike  );
            }

			return convertView;
					
		}
		
		static class ViewHolder
		{
			TextView tvName;
			TextView tvAutor;
			TextView tvLabel;
			TextView tvMark;
            ImageView imageView;
			
		}

}
