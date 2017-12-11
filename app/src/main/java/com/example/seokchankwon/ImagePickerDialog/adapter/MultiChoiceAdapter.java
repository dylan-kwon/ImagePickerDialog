package com.example.seokchankwon.ImagePickerDialog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by chan on 2017. 4. 2..
 * <p>
 * 어뎁터에서 사용할 객체(제너릭)는 equals @override(필수),
 * hashcode, clone(선택) 등을 @override해서 사용해야한다.
 */

public abstract class MultiChoiceAdapter<T> extends BaseRecyclerViewAdapter<T> {
    private ArrayList<T> mCheckedList;

    public MultiChoiceAdapter(Context context) {
        this(context, null);
    }

    public MultiChoiceAdapter(Context context, ArrayList<T> list) {
        super(context, list);
        init();
    }

    private void init() {
        this.mCheckedList = new ArrayList<>();
    }

    @Override
    public void setItem(@Nullable T item) {
        ArrayList<T> tempCheckedList = new ArrayList<>();
        tempCheckedList.addAll(mCheckedList);

        mCheckedList.clear();

        if (item != null) {
            if (tempCheckedList.contains(item)) {
                mCheckedList.add(item);
            }
        }
        super.setItem(item);
    }

    @Override
    public void setItems(@Nullable ArrayList<T> items) {
        ArrayList<T> tempCheckedList = new ArrayList<>();
        tempCheckedList.addAll(mCheckedList);

        mCheckedList.clear();

        if (items != null) {
            for (T item : items) {
                if (tempCheckedList.contains(item)) {
                    mCheckedList.add(item);
                }
            }
        }
        super.setItems(items);
    }

    public void setCheckedClear() {
        int itemCount = getItemCount();
        mCheckedList.clear();
        notifyItemRangeChanged(0, itemCount);
    }

    @Override
    public void removeItem(@NonNull T item) {
        if (mCheckedList.contains(item)) {
            mCheckedList.remove(item);
        }
        super.removeItem(item);
    }

    @Override
    public void removeItem(int position) {
        T item = getItem(position);

        if (mCheckedList.contains(item)) {
            mCheckedList.remove(item);
        }
        super.removeItem(position);
    }

    @NonNull
    public ArrayList<T> getCheckedItems() {
        return mCheckedList;
    }

    public T getCheckedItem(int position) {
        return mCheckedList.get(position);
    }

    private void insertCheckedItem(@NonNull T item) {
        if (!mCheckedList.contains(item)) {
            mCheckedList.add(item);
        }
    }

    private void insertCheckedItem(int position) {
        T item = getItem(position);
        if (!mCheckedList.contains(item)) {
            mCheckedList.add(item);
        }
    }

    private void removeCheckedItem(int position) {
        T item = getItem(position);
        if (mCheckedList.contains(item)) {
            mCheckedList.remove(item);
        }
    }

    private void removeCheckedItem(@NonNull T item) {
        if (mCheckedList.contains(item)) {
            mCheckedList.remove(item);
        }
    }

    public void checkedChange(int position) {
        T item = getItem(position);

        if (mCheckedList.contains(item)) {
            mCheckedList.remove(item);
        } else {
            mCheckedList.add(item);
        }
        notifyItemChanged(position);
    }

    public void checkedChange(@NonNull T item) {
        if (mCheckedList.contains(item)) {
            mCheckedList.remove(item);
        } else {
            mCheckedList.add(item);
        }

        int position = getItemIndex(item);
        notifyItemChanged(position);
    }

    public void setChecked(int position, boolean isChecked) {
        if (isChecked) {
            insertCheckedItem(position);
        } else {
            removeCheckedItem(position);
        }
        notifyItemChanged(position);
    }

    public void setChecked(@NonNull T item, boolean isChecked) {
        if (isChecked) {
            insertCheckedItem(item);
        } else {
            removeCheckedItem(item);
        }
        int position = getItemIndex(item);
        notifyItemChanged(position);
    }

    public void setChecked(@Nullable int[] positions, boolean isChecked) {
        if (positions == null) {
            return;
        }
        for (int position : positions) {
            if (isChecked) {
                insertCheckedItem(position);
            } else {
                removeCheckedItem(position);
            }
            notifyItemChanged(position);
        }
    }

    public void setChecked(@Nullable ArrayList<T> list, boolean isChecked) {
        if (list == null) {
            return;
        }
        for (T item : list) {
            if (isChecked) {
                insertCheckedItem(item);
            } else {
                removeCheckedItem(item);
            }
            int position = getItemIndex(item);
            notifyItemChanged(position);
        }
    }

    public void allChecked(boolean isChecked) {
        mCheckedList.clear();
        if (isChecked) {
            mCheckedList.addAll(getItems());
        }
        notifyDataSetChanged();
    }

    public boolean isChecked(int position) {
        T item = getItem(position);
        return mCheckedList.contains(item);
    }

    public boolean isChecked(T item) {
        return mCheckedList.contains(item);
    }

    public boolean isAllChecked() {
        return mCheckedList.size() == getItems().size();
    }

    public int getCheckedIndex(T item) {
        return mCheckedList.indexOf(item);
    }

    public int getCheckedItemCount() {
        return mCheckedList.size();
    }

}
