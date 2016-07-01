package vn.com.vietatech.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import vn.com.vietatech.dao.ReasonSolutionMapsDataSource;
import vn.com.vietatech.dto.ReasonSolutionMap;
import vn.com.vietatech.lib.MapsGetPropertyValues;

public class ReasonSolutionMapAsync extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public ReasonSolutionMapAsync(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MapsGetPropertyValues propertyValues = new MapsGetPropertyValues(mContext);
            List<ReasonSolutionMap> maps = propertyValues.getPropValues();

            ReasonSolutionMapsDataSource dataSource = ReasonSolutionMapsDataSource.getInstance(mContext);
            dataSource.open();
            for(ReasonSolutionMap map : maps) {
                dataSource.createMap(map);
            }
            System.out.println("Batch maps successful");
            dataSource.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
