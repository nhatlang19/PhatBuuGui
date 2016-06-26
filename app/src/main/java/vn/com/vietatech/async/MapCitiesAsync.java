package vn.com.vietatech.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import vn.com.vietatech.dao.CitiesDataSource;
import vn.com.vietatech.dto.City;
import vn.com.vietatech.lib.CitiesGetPropertyValues;

public class MapCitiesAsync extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public MapCitiesAsync(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            CitiesGetPropertyValues propertyValues = new CitiesGetPropertyValues(mContext);
            List<City> cities = propertyValues.getPropValues();

            CitiesDataSource dataSource = CitiesDataSource.getInstance(mContext);
            for(City city : cities) {
                dataSource.createCity(city);
            }
            dataSource.close();
            System.out.println("Batch cities successful");
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
