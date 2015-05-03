package funnytime.ru.whitemonkteam.funny_time.funnytime.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.StatisticGeneralItem;

/**
 * Created by Андрей on 21.04.2015.
 */
public class StatisticGeneralItemsAdapter extends BaseAdapter
{
    Activity context;
    List<StatisticGeneralItem> list;

    public StatisticGeneralItemsAdapter( Activity context, List<StatisticGeneralItem> list )
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public StatisticGeneralItem getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
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

        holder.imageView.setVisibility(View.GONE);
        holder.tvLabel.setVisibility(View.GONE);
        holder.tvName.setVisibility(View.GONE);
        holder.tvMark.setVisibility(View.GONE);

        StatisticGeneralItem item = getItem(position);

        holder.tvLabel.setVisibility(View.VISIBLE);
        holder.tvName.setVisibility(View.VISIBLE);

        holder.tvName.setText(item.Name);

        holder.tvLabel.setText(context.getResources().getString(R.string.count) + "  " + item.Count);

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
