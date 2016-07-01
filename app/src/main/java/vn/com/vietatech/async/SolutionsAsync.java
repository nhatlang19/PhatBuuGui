package vn.com.vietatech.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import vn.com.vietatech.dao.SolutionsDataSource;
import vn.com.vietatech.dto.Solution;
import vn.com.vietatech.lib.SolutionsGetPropertyValues;

public class SolutionsAsync extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public SolutionsAsync(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            SolutionsGetPropertyValues propertyValues = new SolutionsGetPropertyValues(mContext);
            List<Solution> Solutions = propertyValues.getPropValues();

            SolutionsDataSource dataSource = SolutionsDataSource.getInstance(mContext);
            dataSource.open();
            for(Solution solution : Solutions) {
                dataSource.createSolution(solution);
            }
            System.out.println("Batch solutions successful");
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

        new ReasonsAsync(mContext).execute();
    }
}
