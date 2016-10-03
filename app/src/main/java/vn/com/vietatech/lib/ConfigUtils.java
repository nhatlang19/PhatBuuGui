package vn.com.vietatech.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

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
        String bda = prefs.getString("bda", "");


        return new Config(server, port, code, bda);
    }

    public String getServiceUrl() throws IOException {
        Properties prop = new Properties();
        String propFileName = "config";
        InputStream inputStream = null;

        try {
            inputStream = mContext.getResources().openRawResource(mContext.getResources().getIdentifier(propFileName, "raw", mContext.getPackageName()));

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
                prop.load(isr);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }

        return prop.getProperty("service_url");
    }
}
