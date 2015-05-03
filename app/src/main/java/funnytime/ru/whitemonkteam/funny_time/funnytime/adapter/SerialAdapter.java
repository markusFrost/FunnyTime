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
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Serial;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;

public class SerialAdapter extends BaseAdapter
{

	Activity context;
	 List<Serial> list;
	
	 
	 public SerialAdapter(Activity context)
		{
			this.context = context;
			list = AppContext.dbAdapter.getSerials(0);
            //this.list = new ArrayList<>();
			
		}
	 
	 public SerialAdapter(Activity context, ArrayList<Serial> list)
		{
			this.context = context;
			this.list = list;
		}
	 
	 public long Add(Object obj)
		{
		 Serial item = (Serial) obj;
			list.add(0,item);
			
			return item.Id;
		}
		
		public void Update(Object obj)
		{
			 Serial item = (Serial) obj;
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
			 Serial item = (Serial) obj;
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).Id == item.Id)
				{
					list.remove(i);
					
					break;
				}
			}
		}
		
		/*public void Select(Boolean wasSeen)
		{
			list = AppContext.dbAdapter.getSerials();
			ArrayList<Serial> films = new ArrayList<Serial>();
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
			list = AppContext.dbAdapter.getSerials();
		}
		
		public void Search(String name)
		{
			
			list = AppContext.dbAdapter.getSerials();
			if (name == null)
			{
				return;
			}
			ArrayList<Serial> serials = new ArrayList<Serial>();
			for ( Serial s : list)
			{
				if (s.Name.contains(name))
				{
					serials.add(s);
				}
			}
			list = serials;
		}
		
		public List<Serial> Read()
		{
			return list;
		}
	 */
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
				convertView = inflater.inflate(R.layout.serial_layout,
						parent, false);

				ViewHolder viewHolder = new ViewHolder();

				viewHolder.tvName = (TextView) convertView
						.findViewById(R.id.serialTvName);
				viewHolder.tvLabel = (TextView) convertView
						.findViewById(R.id.serialTvLabelSeen);
				viewHolder.tvMark = (TextView) convertView
						.findViewById(R.id.serialTvResult);
                viewHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.serialIv);

				convertView.setTag(viewHolder);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			
			holder.tvName.setText(list.get(position).Name);

            Picasso.with(context)
                    .load(Constants.FILE + list.get(position).ImagePath)
                    .placeholder(android.R.drawable.ic_dialog_info)
                    .error(android.R.drawable.ic_dialog_info)
                    .into(holder.imageView);

            holder.tvLabel.setVisibility(View.GONE);
            holder.tvMark.setVisibility(View.GONE);

            Serial item = list.get(position);

            if ( item.Action == Constants.TYPE_WANT_TO_SEE)
            {
                holder.tvLabel.setVisibility(View.VISIBLE);
                holder.tvMark.setVisibility(View.GONE);
                holder.tvLabel.setText(   R.string.wantSee  );

            }
            else if ( item.Action == Constants.TYPE_ALREADY_SEE)
            {
                holder.tvLabel.setVisibility(View.VISIBLE);
                holder.tvMark.setVisibility(View.VISIBLE);
                holder.tvLabel.setText(R.string.strSeen);
                holder.tvMark.setText(list.get(position).Season + " . " + list.get(position).Series);
            }
            else if ( item.Action == Constants.TYPE_SAW)
            {
                holder.tvMark.setVisibility(View.VISIBLE);
                holder.tvLabel.setVisibility(View.VISIBLE);

                holder.tvLabel.setText(R.string.strMark);
                holder.tvMark.setText(list.get(position).Mark + "");
            }
            if ( item.Action == Constants.TYPE_DO_NOT_LIKE)
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
			TextView tvLabel;
			TextView tvMark;
            ImageView imageView;
        }

}
