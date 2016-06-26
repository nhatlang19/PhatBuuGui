package vn.com.vietatech.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import vn.com.vietatech.dto.Config;

public class ConfigUtils {
    private static ConfigUtils ourInstance = new ConfigUtils();

    private static Context mContext;

    public static ConfigUtils getInstance(Context context) {
        mContext = context;
        return ourInstance;
    }

    private ConfigUtils() {
    }

    public Config displaySharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String server = prefs.getString("server", "");
        String port = prefs.getString("port", "0");
        String code = prefs.getString("code", "0");

        return new Config(server, port, code);
    }

}
