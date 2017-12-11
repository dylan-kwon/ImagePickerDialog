package com.example.seokchankwon.ImagePickerDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Created by chan on 2017. 3. 30..
 */

public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private final String TAG = this.getClass().getSimpleName();

    public static final String SAVED_STATE = "saved.state";
    public static final String SAVED_PEEK_HEIIGHT = "saved.peek_height";
    public static final String SAVED_HIDEABLE = "saved.hideable";

    private int mState;
    private int mPeekHeight;

    private boolean mHideable;

    private BottomSheetCallback mBottomSheetCallback;

    private RequestManager mRequestManager;

    private BottomSheetBehavior mBottomSheetBehavior;

    private OnDismissListener mOnDismissListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBottomSheetState(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            try {
                ViewGroup viewParent = (ViewGroup) getView().getParent();

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) viewParent.getLayoutParams();
                CoordinatorLayout.Behavior behavior = params.getBehavior();

                if (behavior instanceof BottomSheetBehavior) {
                    mBottomSheetBehavior = (BottomSheetBehavior) behavior;

                    if (mState != BottomSheetBehavior.STATE_COLLAPSED) {
                        mBottomSheetBehavior.setState(mState);
                    }

                    if (mPeekHeight != BottomSheetBehavior.PEEK_HEIGHT_AUTO) {
                        mBottomSheetBehavior.setPeekHeight(mPeekHeight);
                    }

                    mBottomSheetBehavior.setHideable(mHideable);

                    mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            switch (newState) {
                                case BottomSheetBehavior.STATE_HIDDEN:
                                    if (mBottomSheetBehavior.isHideable()) {
                                        dismiss();
                                    }
                                    break;
                            }
                            if (mBottomSheetCallback != null) {
                                mBottomSheetCallback.onStateChanged(bottomSheet, newState);
                            }
                            mState = newState;
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            if (mBottomSheetCallback != null) {
                                mBottomSheetCallback.onSlide(bottomSheet, slideOffset);
                            }
                        }
                    });
                }

            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_STATE, getState());
        outState.putInt(SAVED_PEEK_HEIIGHT, getPeekHeight());
        outState.putBoolean(SAVED_HIDEABLE, isHideable());
    }

    private void setupBottomSheetState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mState = savedInstanceState.getInt(SAVED_STATE, BottomSheetBehavior.STATE_COLLAPSED);
            mPeekHeight = savedInstanceState.getInt(SAVED_PEEK_HEIIGHT, BottomSheetBehavior.PEEK_HEIGHT_AUTO);
            mHideable = savedInstanceState.getBoolean(SAVED_HIDEABLE, true);
        }
    }

    @NonNull
    public RequestManager getRequestManager() {
        if (mRequestManager == null) {
            mRequestManager = Glide.with(this);
        }
        return mRequestManager;
    }

    @Nullable
    public BottomSheetBehavior getBottomSheetBehavior() {
        return mBottomSheetBehavior;
    }

    @BottomSheetBehavior.State
    public int getState() {
        if (mBottomSheetBehavior != null) {
            return mBottomSheetBehavior.getState();
        }
        return mState;
    }

    public void setState(@BottomSheetBehavior.State int state) {
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setState(state);
        }
        mState = state;
    }

    public int getPeekHeight() {
        if (mBottomSheetBehavior != null) {
            return mBottomSheetBehavior.getPeekHeight();
        }
        return mPeekHeight;
    }

    public void setPeekHeight(int peekHeight) {
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setPeekHeight(peekHeight);
        }
        mPeekHeight = peekHeight;
    }

    public boolean isHideable() {
        if (mBottomSheetBehavior != null) {
            return mBottomSheetBehavior.isHideable();
        }
        return mHideable;
    }

    public void setHideable(boolean hideable) {
        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setHideable(hideable);
        }
        mHideable = hideable;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
        if (isAdded()) {
            super.onDismiss(dialog);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.show(manager, tag);
        }
    }

    public void showAllowingStateLoss(FragmentManager fm, String tag) {
        if (!isAdded()) {
            fm.beginTransaction()
                    .add(this, tag)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();
        }
    }

    public void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public void setBottomSheetCallback(@Nullable BottomSheetCallback callback) {
        mBottomSheetCallback = callback;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public interface BottomSheetCallback {
        void onStateChanged(@NonNull View bottomSheet, int newState);

        void onSlide(@NonNull View bottomSheet, float slideOffset);
    }


    protected static abstract class Builder<Builder extends BaseBottomSheetDialogFragment.Builder, Dialog extends BaseBottomSheetDialogFragment> {

        private int mState;
        private int mPeekHeight;

        private boolean mHideable;
        private boolean mCancelable;

        private OnDismissListener mOnDismissListener;
        private BottomSheetCallback mBottomSheetCallback;

        public Builder() {
            mState = BottomSheetBehavior.STATE_COLLAPSED;
            mPeekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO;

            mHideable = true;
            mCancelable = true;

            mOnDismissListener = null;
            mBottomSheetCallback = null;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public Builder setState(@BottomSheetBehavior.State int state) {
            mState = state;
            return (Builder) this;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public Builder setPeekHeight(@Px int height) {
            mPeekHeight = height;
            return (Builder) this;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public Builder setHideable(boolean hideable) {
            mHideable = hideable;
            return (Builder) this;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return (Builder) this;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public Builder setOnDismissListener(@Nullable OnDismissListener listener) {
            mOnDismissListener = listener;
            return (Builder) this;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public Builder setBottomSheetCallback(@Nullable BottomSheetCallback callback) {
            mBottomSheetCallback = callback;
            return (Builder) this;
        }

        protected void setBottomSheetState(@NonNull Dialog dialog) {
            dialog.setState(mState);
            dialog.setPeekHeight(mPeekHeight);
            dialog.setHideable(mHideable);
            dialog.setCancelable(mCancelable);
            dialog.setOnDismissListener(mOnDismissListener);
            dialog.setBottomSheetCallback(mBottomSheetCallback);
        }

        @NonNull
        public abstract Dialog build();

    }

}
