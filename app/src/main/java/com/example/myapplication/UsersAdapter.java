package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class UsersAdapter extends ArrayAdapter<Entry> implements Filterable {
    public List<Entry> now, orig;
    private CustomFilter cs;
    private Context c;
    public UsersAdapter(Context context, List<Entry> entries) {
        super(context,  R.layout.item_user, entries);
        this.now = entries;
        this.orig = entries;
        this.c = context;

    }

    @Override
    public int getCount() { return orig.size(); }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public Filter getFilter()
    {
        if (cs == null)
        {
            cs = new CustomFilter();
        }
        return cs;
    }


    public void removeEntry(Entry i)
    {
        orig.remove(i);
        now.remove(i);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_user, null);
        Entry entry = orig.get(position);

        TextView tvName = (TextView) row.findViewById(R.id.name);

        TextView tvHome = (TextView) row.findViewById(R.id.desc);

        tvName.setText(entry.getName());

        tvHome.setText(entry.getDesc());
        return row;
    }


    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence cs) {
            FilterResults fr = new FilterResults();

            if(cs != null && cs.length() > 0)
            {
                cs = cs.toString().toUpperCase();
                ArrayList<Entry> filters = new ArrayList<>();
                for (int i = 0; i < now.size(); i++)
                {
                    if (now.get(i).getName().toUpperCase().contains(cs))
                    {
                        Entry en = new Entry(now.get(i).getName(), now.get(i).getDesc(), now.get(i).getPass(), now.get(i).getName());
                        filters.add(en);
                    }
                }
                fr.count = filters.size();
                fr.values = filters;
            }
            else
            {
                fr.count = now.size();
                fr.values = now;
            }
            return fr;
        }

        @Override
        protected void publishResults(CharSequence cs, FilterResults fr)
        {
            orig = (List<Entry>) fr.values;
            notifyDataSetChanged();
        }
    }
}
