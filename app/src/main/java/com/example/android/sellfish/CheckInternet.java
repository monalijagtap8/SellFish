package com.example.android.sellfish;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by android on 1/24/18.
 */

public class CheckInternet
{
    public static boolean checkinternet(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
