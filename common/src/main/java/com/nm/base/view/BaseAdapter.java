package com.nm.base.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangming on 2016/10/4.
 */

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected Context context;
    protected List<T> list = null;
    protected LayoutInflater layoutInflater;

    public BaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list != null && position >= 0 && position < list.size() ? list
                .get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder<T> holder;
        if (convertView == null) {
            convertView = createView(context, parent);
            holder = createViewHolder();
            convertView.setTag(holder);
            holder.init(context, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        T data = list.get(position);
        holder.position = position;
        holder.size = list.size();
        holder.update(context, data);
        return convertView;
    }

    public View createView(Context context, ViewGroup parent) {
        return layoutInflater.inflate(getItemLayoutResId(), parent, false);
    }

    protected abstract int getItemLayoutResId();

    public abstract ViewHolder<T> createViewHolder();

    /**
     * 保存当前Item的view
     */
    public static abstract class ViewHolder<T> {
        public int position;

        public int size;

        public abstract void init(Context context, View convertView);

        public abstract void update(Context context, T data);
    }
}
