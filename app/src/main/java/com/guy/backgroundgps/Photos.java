package com.guy.backgroundgps;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
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

}
