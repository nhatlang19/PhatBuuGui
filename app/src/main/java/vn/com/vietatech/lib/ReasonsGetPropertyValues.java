package vn.com.vietatech.lib;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vn.com.vietatech.dto.City;
import vn.com.vietatech.dto.Reason;

public class ReasonsGetPropertyValues {
    InputStream inputStream;

    Context context;

    public ReasonsGetPropertyValues(Context context) {
        this.context = context;
    }

    public List<Reason> getPropValues() throws IOException {
        List<Reason> list = new ArrayList<Reason>();
        try {
            Properties prop = new Properties();
            String propFileName = "reasons";

            inputStream = context.getResources().openRawResource(
                    context.getResources().getIdentifier(propFileName,
                            "raw", context.getPackageName()));

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            for (String key : prop.stringPropertyNames()) {
                Reason reason = new Reason();
                reason.setId(Integer.parseInt(key));
                reason.setName(prop.getProperty(key));
                System.out.println("key: " + key + " & name: " + prop.getProperty(key));
                list.add(reason);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return list;
    }
}
