package com.example.seokchankwon.ImagePickerDialog;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.seokchankwon.ImagePickerDialog.adapter.BaseRecyclerViewAdapter;
import com.example.seokchankwon.ImagePickerDialog.adapter.ImagePickerAdapter;

import java.util.ArrayList;

/**
 * Created by seokchan.kwon on 2017. 9. 26..
 */

public class ImagePickerDialog extends BaseBottomSheetDialogFragment {

    public static final String EXTRA_LIMIT_COUNT = "extra.limit_count";

    private int mLimitCount;

    private Context mContext;

    private TextView tvComplete;
    private TextView tvImageCount;

    private RecyclerView rvImagePicker;

    private ImagePickerAdapter mAdapter;

    private OnImageSelectedListener mOnImageSelectedListener;


    public ImagePickerDialog() {
        // newInstance를 사용할 것
    }

    private static ImagePickerDialog newInstance(Builder builder) {
        ImagePickerDialog dialog = new ImagePickerDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_LIMIT_COUNT, builder.mLimitCount);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInstanceState(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_image_picker, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());

        GridLayoutManager layoutManager =
                new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);

        mAdapter = new ImagePickerAdapter(mContext, Glide.with(this));
        mAdapter.setOnItemClickListener(this::selectImage);

        rvImagePicker.setHasFixedSize(true);
        rvImagePicker.setItemAnimator(null);
        rvImagePicker.setLayoutManager(layoutManager);
        rvImagePicker.setAdapter(mAdapter);

        mAdapter.setItems(loadImage());

        tvComplete.setOnClickListener(v -> selectComplete());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_LIMIT_COUNT, mLimitCount);
    }

    private void setupInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mLimitCount = savedInstanceState.getInt(EXTRA_LIMIT_COUNT, 0);

        } else if (getArguments() != null) {
            mLimitCount = getArguments().getInt(EXTRA_LIMIT_COUNT, 0);
        }
    }

    private void initView(View view) {
        tvComplete = view.findViewById(R.id.tv_dialog_image_picker_complete);
        tvImageCount = view.findViewById(R.id.tv_dialog_image_picker_count);
        rvImagePicker = view.findViewById(R.id.rv_dialog_image_picker);
    }

    private void selectComplete() {
        if (mOnImageSelectedListener != null) {
            if (mAdapter.getCheckedItemCount() > 0) {
                mOnImageSelectedListener.onConfirm(mAdapter.getCheckedItems());
            }
        }
        dismiss();
    }

    private ArrayList<Uri> loadImage() {
        ArrayList<Uri> uriList = new ArrayList<>();
        Cursor imageCursor = null;

        try {
            String[] e = new String[]{"_data", "orientation"};
            String orderBy = "date_added DESC";

            imageCursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, e, null, null, orderBy);

            if (imageCursor == null) {
                // 비어있는 arrayList
                return uriList;
            }

            while (imageCursor.moveToNext()) {

                String path = "file://" + imageCursor.getString(imageCursor.getColumnIndex("_data"));

                if (path.toLowerCase().endsWith(".gif")) { // gif 제외
                    continue;
                }

                Uri uri = Uri.parse(path);
                uriList.add(uri);
            }


        } catch (Exception var9) {
            var9.printStackTrace();

        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }

        return uriList;
    }

    private void selectImage(int position, Uri item) {
        if (mLimitCount <= 1) {
            // 한장만 선택 가능한 경우
            mAdapter.checkedChangedItem(position);
            updateViews();

        } else {

            // 최소 2장 이상 선택 가능한 경우
            int itemCount = mAdapter.getCheckedItemCount();

            if (itemCount < mLimitCount || mAdapter.isChecked(item)) {
                mAdapter.checkedChange(position);
                updateViews();

            } else {
                StringBuilder toastMsg = new StringBuilder()
                        .append(getString(R.string.max))
                        .append(" ")
                        .append(String.valueOf(mLimitCount))
                        .append(getString(R.string.image_select_over_count));

                Toast.makeText(mContext, toastMsg, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void updateViews() {
        int checkedItemCount = mAdapter.getCheckedItemCount();
        tvImageCount.setText(String.valueOf(checkedItemCount));

        if (checkedItemCount <= 0) {
            tvImageCount.setVisibility(View.INVISIBLE);
        } else {
            tvImageCount.setVisibility(View.VISIBLE);
        }
    }

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        mOnImageSelectedListener = listener;
    }

    public interface OnImageSelectedListener {
        void onConfirm(@NonNull ArrayList<Uri> uris);
    }

    public static class Builder extends BaseBottomSheetDialogFragment.Builder<Builder, ImagePickerDialog> {

        private int mLimitCount;

        private OnImageSelectedListener mOnImageSelectedListener;


        public Builder() {
            mLimitCount = 1;
            mOnImageSelectedListener = null;
        }

        public Builder setLimitCount(int limitCount) {
            mLimitCount = limitCount;
            return this;
        }

        public Builder setOnImageSelectedListener(OnImageSelectedListener listener) {
            mOnImageSelectedListener = listener;
            return this;
        }

        @NonNull
        @Override
        public ImagePickerDialog build() {
            ImagePickerDialog dialog = newInstance(this);
            dialog.setOnImageSelectedListener(mOnImageSelectedListener);
            setBottomSheetState(dialog);
            return dialog;
        }

    }

}
