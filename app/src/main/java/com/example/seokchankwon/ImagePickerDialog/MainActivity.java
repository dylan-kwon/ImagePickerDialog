package com.example.seokchankwon.ImagePickerDialog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE_READ_EXTERNAL = 100;

    public static final String DIALOG_TAG_IMAGE_PICKER = "MainActivity.dialog_tag.image_picker";

    private Context mContext;

    private Toolbar mToolbar;
    private TextView tvSelectedImagesPath;
    private FloatingActionButton fabShowSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setSupportActionBar(mToolbar);

        mContext = this;

        fabShowSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker();
            }
        });

        requestPermission();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tb_activity_main);
        tvSelectedImagesPath = (TextView) findViewById(R.id.tv_activity_main_selected_images_path);
        fabShowSelector = (FloatingActionButton) findViewById(R.id.fab_main_show_image_picker);
    }

    private void showImagePicker() {
        if (!requestPermission()) {
            return;
        }

        Fragment dialog = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_IMAGE_PICKER);

        if (dialog != null && dialog.isAdded()) {
            return;
        }

        ImagePickerDialog imagePickerDialog = ImagePickerDialog.newInstance(10);
        imagePickerDialog.setOnImageSelectedListener(new ImagePickerDialog.OnImageSelectedListener() {
            @Override
            public void onConfirm(@NonNull ArrayList<Uri> uris) {
                editImagesPath(uris);
            }
        });

        imagePickerDialog.show(getSupportFragmentManager(), DIALOG_TAG_IMAGE_PICKER);
    }

    private void editImagesPath(@NonNull ArrayList<Uri> uris) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Uri uri : uris) {
            stringBuilder
                    .append(uri.getPath())
                    .append("\n")
                    .append("\n");
        }

        tvSelectedImagesPath.setText(stringBuilder.toString());
    }

    private boolean requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE_READ_EXTERNAL);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        showImagePicker();
    }
}
