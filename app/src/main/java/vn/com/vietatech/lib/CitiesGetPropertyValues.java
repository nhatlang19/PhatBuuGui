package vn.com.vietatech.lib;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vn.com.vietatech.dto.City;

public class CitiesGetPropertyValues {
    InputStream inputStream;

    Context context;

    public CitiesGetPropertyValues(Context context) {
        this.context = context;
    }

    public List<City> getPropValues() throws IOException {
        List<City> list = new ArrayList<City>();
        try {
            Properties prop = new Properties();
            String propFileName = "cities";

            inputStream = context.getResources().openRawResource(
                    context.getResources().getIdentifier(propFileName,
                            "raw", context.getPackageName()));

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
                prop.load(isr);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            for (String key : prop.stringPropertyNames()) {
                City city = new City();
                city.setCode(key);
                city.setDesc(prop.getProperty(key));
                System.out.println("key: " + prop.getProperty(key));
                list.add(city);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return list;
    }
}
