package com.example.seokchankwon.ImagePickerDialog.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.seokchankwon.ImagePickerDialog.R;

/**
 * Created by seokchan.kwon on 2017. 9. 26..
 */

public class SelectedImageAdapter extends BasePagerAdapter<Uri> {

    private RequestManager mRequestManager;

    public SelectedImageAdapter(Context context, RequestManager requestManager) {
        super(context);
        this.mRequestManager = requestManager;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.25f;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getInflater().inflate(R.layout.viewpager_selected_image, container, false);
        Holder holder = new Holder(view);

        Uri uri = getItem(position);

        mRequestManager
                .load(uri.getPath())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(holder.getIvImage());

        view.setTag(holder);

        container.addView(view);
        return view;
    }

    public class Holder {

        private ImageView ivImage;

        public Holder(View view) {
            ivImage = view.findViewById(R.id.iv_viewpager_selected_image);
        }

        public ImageView getIvImage() {
            return ivImage;
        }
    }
}
