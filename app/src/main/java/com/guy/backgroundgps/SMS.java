package com.guy.backgroundgps;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SMS {


    //taken from here: https://stackoverflow.com/questions/18353734/getting-all-sms-from-an-android-phone
    public List<Pair> getSMS(Activity activity) {
        List<Pair> sms = new ArrayList<Pair>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = activity.getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur != null && cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            Pair addressAndBody = new Pair(address, body);
            sms.add(addressAndBody);
//            sms.add("Number: " + address + " .Message: " + body);
        }

        if (cur != null) {
            cur.close();
        }
        Collections.sort(sms);
        return sms;
    }

    //"Number: AFEKA .Message: החלטות ועדת ההוראה בנושא בחינות סמסטר ב' והתנהלות סמסטר קיץ באפקה-נט"
    public static void turnSmsListToTextFile(List<Pair> smsList) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("sms.txt");
        String temp=smsList.get(0).getAddress(), str="Number"+temp+"\n"+"Body"+smsList.get(0).getBody();
        for (int i = 1; i < smsList.size() - 1; i++) {
            if( temp.equals(smsList.get(i).getAddress()))
                str+=smsList.get(i).getBody()+"\n \n";
            else{
                temp=smsList.get(i).getAddress();
                str+="\n Number: "+temp+"\n Body:";
            }
        }
    }
}


