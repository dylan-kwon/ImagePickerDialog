What is ImagePickerDialog?
==========================

ImagePickerDialog is implemented by inheriting from BottomSheetDialogFragment.<br/>This example loads an image stored on the device, selects it, and returns an ArrayList <Uri>.

Preview
-------

<p><img src="http://drive.google.com/uc?export=view&id=1-xhzSN-Qqed-x47qAPzupaW96THurLgH" width="250" height="435"><p/>

<br/>

How to use.
-----------

### 1. Check Permission

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

### 2. ShowImagePickerDialog

```java
private void showImagePicker() {
        Fragment dialog = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_IMAGE_PICKER);

        if (dialog != null && dialog.isAdded()) {
            return;
        }

        ImagePickerDialog imagePickerDialog = new ImagePickerDialog.Builder()
                .setOnImageSelectedListener(uris -> mAdapter.setItems(uris))
                .build();

        imagePickerDialog.show(getSupportFragmentManager(), DIALOG_TAG_IMAGE_PICKER);
    }
```

### 3. Customize Function

-	`ImagePickerDialog.Builder().setLimitCount(int limitCount)`
-	`ImagePickerDialog.Builder().setState(int state)`
-	`ImagePickerDialog.Builder().setPeekHeight(int peekHeight)`
-	`ImagePickerDialog.Builder().setHideable(boolean hideable)`
-	`ImagePickerDialog.Builder().setCancelable(boolean cancelable)`
-	`ImagePickerDialog.Builder().setBottomSheetCallback(BottomSheetCallback callback)`
-	`ImagePickerDialog.Builder().setOnDismissListener(OnDismissListener listener)`
-	`ImagePickerDialog.Builder().setOnImageSelectedListener(OnImageSelectListener listener)`
