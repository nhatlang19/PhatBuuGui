package vn.com.vietatech.lib;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vn.com.vietatech.dto.ReasonSolutionMap;

public class MapsGetPropertyValues {
    InputStream inputStream;

    Context context;

    public MapsGetPropertyValues(Context context) {
        this.context = context;
    }

    public List<ReasonSolutionMap> getPropValues() throws IOException {
        List<ReasonSolutionMap> list = new ArrayList<ReasonSolutionMap>();
        try {
            Properties prop = new Properties();
            String propFileName = "maps";

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
                String[] item = key.split("_");
                String reasonId = item[0];
                String solutionId = item[1];

                ReasonSolutionMap map = new ReasonSolutionMap();
                map.setReasonId(Integer.parseInt(reasonId));
                char c = solutionId.charAt(0); // c='a'
                int id = (int)c;
                map.setSolutionId(id);
                System.out.println("reason_Id: " + reasonId + " & solution_id: " + solutionId + "(" + id + ")");
                list.add(map);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return list;
    }
}
