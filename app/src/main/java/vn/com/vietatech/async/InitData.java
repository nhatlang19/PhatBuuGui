package vn.com.vietatech.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import vn.com.vietatech.dao.CitiesDataSource;
import vn.com.vietatech.dao.UsersDataSource;
import vn.com.vietatech.dto.City;
import vn.com.vietatech.dto.User;


public class InitData extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public InitData(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        initAdminUser();
        return null;
    }

    private void initAdminUser() {
        User user = new User("Administrator", "admin", "123", "Admin", "12345678");
        UsersDataSource dataSource = UsersDataSource.getInstance(mContext);
        dataSource.open();
        dataSource.createUser(user);
        dataSource.close();
        System.out.println("Init Admin user successful");
    }

}
