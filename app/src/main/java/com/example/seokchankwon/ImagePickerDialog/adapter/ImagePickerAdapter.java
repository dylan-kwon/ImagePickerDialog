package com.example.seokchankwon.ImagePickerDialog.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.seokchankwon.ImagePickerDialog.R;

/**
 * Created by bryanbaek on 16. 4. 12..
 */
public class ImagePickerAdapter extends MultiChoiceAdapter<Uri> {

    private RequestManager mRequestManager;


    public ImagePickerAdapter(Context context, RequestManager requestManager) {
        super(context);
        this.mRequestManager = requestManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.listview_image_picker, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition != RecyclerView.NO_POSITION) {

            PhotoViewHolder photoHolder = (PhotoViewHolder) holder;
            Uri item = getItem(position);

            if (isChecked(item)) {
                photoHolder.getTvIndex().setVisibility(View.VISIBLE);

                String index = String.valueOf(
                        getCheckedItems().indexOf(item) + 1);
                photoHolder.getTvIndex().setText(index);

            } else {
                photoHolder.getTvIndex().setVisibility(View.GONE);
            }

            mRequestManager
                    .load(item)
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(photoHolder.getIvImage());
        }
    }

    public void checkedChangedItem(int position) {
        if (getCheckedItemCount() > 0) {
            Uri beforeCheckedItem = getCheckedItem(0);
            setChecked(beforeCheckedItem, false);
        }
        setChecked(position, true);
    }

    /**
     * Holder
     */
    private class PhotoViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        private FrameLayout flItem;
        private ImageView ivImage;
        private TextView tvIndex;

        private PhotoViewHolder(final View itemView) {
            super(itemView);
            flItem = itemView.findViewById(R.id.fl_listview_image_picker_item);
            ivImage = itemView.findViewById(R.id.iv_listview_image_picker_image);
            tvIndex = itemView.findViewById(R.id.tv_listview_image_picker_index);
            resizeItem();
        }

        private void resizeItem() {
            flItem.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    flItem.getViewTreeObserver().removeOnPreDrawListener(this);
                    ViewGroup.LayoutParams lp = flItem.getLayoutParams();
                    lp.height = flItem.getWidth();
                    flItem.setLayoutParams(lp);
                    return false;
                }
            });
        }

        private FrameLayout getFlItem() {
            return flItem;
        }

        private ImageView getIvImage() {
            return ivImage;
        }

        private TextView getTvIndex() {
            return tvIndex;
        }
    }
}
