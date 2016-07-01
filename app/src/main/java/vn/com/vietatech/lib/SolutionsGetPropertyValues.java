package vn.com.vietatech.lib;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vn.com.vietatech.dto.Solution;

public class SolutionsGetPropertyValues {
    InputStream inputStream;

    Context context;

    public SolutionsGetPropertyValues(Context context) {
        this.context = context;
    }

    public List<Solution> getPropValues() throws IOException {
        List<Solution> list = new ArrayList<Solution>();
        try {
            Properties prop = new Properties();
            String propFileName = "solutions";

            inputStream = context.getResources().openRawResource(
                    context.getResources().getIdentifier(propFileName,
                            "raw", context.getPackageName()));

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            for (String key : prop.stringPropertyNames()) {
                Solution solution = new Solution();
                char c = key.charAt(0); // c='a'
                int id = (int)c;
                solution.setId(id);
                solution.setName(prop.getProperty(key));
                System.out.println("key: " + id + " & name: " + prop.getProperty(key));
                list.add(solution);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return list;
    }
}
