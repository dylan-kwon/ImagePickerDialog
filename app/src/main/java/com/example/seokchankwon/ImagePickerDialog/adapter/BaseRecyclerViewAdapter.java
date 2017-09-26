package com.example.seokchankwon.ImagePickerDialog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by chan on 2017. 3. 30..
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<T> mList;
    private LayoutInflater mInflater;

    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemLongClickListener<T> mOnItemLongClickListener;


    public BaseRecyclerViewAdapter(Context context) {
        init(context, null);
    }

    public BaseRecyclerViewAdapter(Context context, ArrayList<T> list) {
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
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    public ArrayList<T> getItems() {
        return mList;
    }

    @NonNull
    public T getItem(int position) {
        return mList.get(position);
    }

    public int getItemIndex(T item) {
        return mList.indexOf(item);
    }

    public void setItem(@Nullable T item) {
        mList.clear();

        if (item != null) {
            mList.add(item);
        }

        notifyDataSetChanged();
    }

    public void setItems(@Nullable ArrayList<T> items) {
        mList.clear();

        if (items != null) {
            mList.addAll(items);
        }

        notifyDataSetChanged();
    }

    public void insertItem(@Nullable T item) {
        int position = mList.size();

        if (item != null) {
            mList.add(item);
            notifyItemInserted(position);
        }
    }

    public void insertItem(int position, @Nullable T item) {
        if (item != null) {
            mList.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void insertItems(@Nullable ArrayList<T> items) {
        int startPosition = mList.size();

        if (items != null) {
            int itemCount = items.size();
            mList.addAll(items);

            notifyItemRangeInserted(startPosition, itemCount);
        }
    }

    public void insertItems(int position, @Nullable ArrayList<T> items) {
        if (items != null) {
            int itemCount = items.size();
            mList.addAll(position, items);

            notifyItemRangeInserted(position, itemCount);
        }
    }

    public void removeItem(@NonNull T item) {
        int position = mList.indexOf(item);
        mList.remove(item);
        notifyItemRemoved(position);
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int itemCount = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    @NonNull
    public Context getContext() {
        return mContext;
    }

    @NonNull
    public LayoutInflater getInflater() {
        return mInflater;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        mOnItemLongClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(int position, T item);
    }

    public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public BaseRecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener == null) {
                return;
            }

            int adapterPosition = getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }

            T item = getItem(adapterPosition);
            mOnItemClickListener.onItemClick(adapterPosition, item);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mOnItemLongClickListener == null) {
                return false;
            }

            int adapterPosition = getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return false;
            }

            T item = getItem(adapterPosition);
            mOnItemLongClickListener.onItemLongClick(adapterPosition, item);

            return false;
        }
    }

}
