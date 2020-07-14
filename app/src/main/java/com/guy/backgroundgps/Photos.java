package com.guy.backgroundgps;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class Photos {
    //taken from here: https://stackoverflow.com/questions/25957644/get-all-photos-from-android-device-android-programming
    public static ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int countOfPhoto=0;
        while (cursor.moveToNext()&&countOfPhoto<30) {
            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
            countOfPhoto++;
        }
        return listOfAllImages;
    }


    public static ArrayList<String> findSpecificPhotosNames(ArrayList<String> listOfAllImages, String pattern)
    {
        ArrayList<String> listOfMatchedImages = new ArrayList();
        for (int i = 0; i < listOfAllImages.size(); i++)
        {
            if (listOfAllImages.get(i).matches(pattern))
                listOfMatchedImages.add(listOfAllImages.get(i));
        }
        return listOfMatchedImages;
    }

    public static Bitmap getImageByPath(String path){
        File imgFile = new  File(path);
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            return myBitmap;

        }
        return null;
    }
    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result= Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }


}
