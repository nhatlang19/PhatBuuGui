package vn.com.vietatech.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import vn.com.vietatech.dao.CitiesDataSource;
import vn.com.vietatech.dao.ReasonsDataSource;
import vn.com.vietatech.dto.City;
import vn.com.vietatech.dto.Reason;
import vn.com.vietatech.lib.CitiesGetPropertyValues;
import vn.com.vietatech.lib.ReasonsGetPropertyValues;

public class ReasonsAsync extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public ReasonsAsync(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            ReasonsGetPropertyValues propertyValues = new ReasonsGetPropertyValues(mContext);
            List<Reason> reasons = propertyValues.getPropValues();

            ReasonsDataSource dataSource = ReasonsDataSource.getInstance(mContext);
            dataSource.open();
            for(Reason reason : reasons) {
                dataSource.createReason(reason);
            }
            System.out.println("Batch reasons successful");
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

        new ReasonSolutionMapAsync(mContext).execute();
    }
}
