package com.guy.backgroundgps;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class SMS {


    //taken from here: https://stackoverflow.com/questions/18353734/getting-all-sms-from-an-android-phone
    public List<String> getSMS(Activity activity){
        List<String> sms = new ArrayList<String>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = activity.getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur != null && cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add("Number: " + address + " .Message: " + body);
        }

        if (cur != null) {
            cur.close();
        }
        return sms;
    }

}
