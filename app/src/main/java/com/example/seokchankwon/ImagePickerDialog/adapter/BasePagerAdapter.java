package com.example.seokchankwon.ImagePickerDialog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by chan on 2017. 3. 30..
 */

public abstract class BasePagerAdapter<T> extends PagerAdapter {

    private Context mContext;

    private ArrayList<T> mList;
    private LayoutInflater mInflater;


    public BasePagerAdapter(Context context) {
        init(context, null);
    }

    public BasePagerAdapter(Context context, ArrayList<T> list) {
        init(context, list);
    }

    private void init(Context context, ArrayList<T> list) {
        mList = list;
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    public T getItem(int position) {
        return mList.get(position);
    }

    @NonNull
    public ArrayList<T> getItems() {
        return mList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setItems(@Nullable ArrayList<T> list) {
        mList.clear();
        insertItems(list);
    }

    public void insertItems(@Nullable ArrayList<T> list) {
        if (list != null) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    public Context getContext() {
        return mContext;
    }

    @NonNull
    public LayoutInflater getInflater() {
        return mInflater;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
