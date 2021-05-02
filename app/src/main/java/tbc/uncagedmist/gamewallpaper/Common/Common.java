package tbc.uncagedmist.gamewallpaper.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import tbc.uncagedmist.gamewallpaper.Model.WallpaperItem;

public class Common {
    public static final String STR_CATEGORY_BACKGROUND = "CategoryBackground";

    public static String CATEGORY_SELECTED;
    public static String Current_Description;

    public static String STR_WALLPAPER = "Wallpapers";
    public static String CATEGORY_ID_SELECTED;

    public static String select_background_key;

    public static WallpaperItem select_background = new WallpaperItem();

    public static boolean IS_FAV = false;

    public static boolean isConnectedToInternet(Context context)    {

        ConnectivityManager connectivityManager = (
                ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)    {

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null)   {

                for (int i = 0; i <info.length;i++)   {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)  {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
