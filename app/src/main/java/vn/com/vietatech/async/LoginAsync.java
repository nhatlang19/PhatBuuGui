package vn.com.vietatech.async;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import vn.com.vietatech.dao.UsersDataSource;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.lib.UserUtil;
import vn.com.vietatech.phatbuugui.FullscreenActivity;
import vn.com.vietatech.phatbuugui.LoginActivity;
import vn.com.vietatech.phatbuugui.MyApplication;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.dialog.TransparentProgressDialog;

public class LoginAsync extends AsyncTask<String, String, User> {

	private Context mContext;
	private TransparentProgressDialog pd;
	MyApplication globalVariable;
	
	public LoginAsync(Context context, Application app) {
		this.mContext = context;

		globalVariable = (MyApplication) app;
		
		pd = new TransparentProgressDialog(mContext, R.drawable.spinner);
		pd.show();
	}

	@Override
	protected User doInBackground(String... params) {
		String username = params[0];
		String password = params[1];
		User user = new User(username, password);
		try {
			UsersDataSource dataSource = UsersDataSource.getInstance(mContext);
            dataSource.open();
			user = dataSource.login(new User(username, password));
			if(user == null) {
				user = new User(username, password);
			}
			dataSource.close();
		} catch (Exception e) {
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return user;
	}

	@Override
	protected void onPostExecute(User user) {
		if (user.getId() != 0) {
			// cache user info
			globalVariable.setUser(user);

			// log recent login
			try {
				UserUtil.write(user, mContext);

				LoginActivity act = (LoginActivity) mContext;
				Intent intent = new Intent(mContext, FullscreenActivity.class);
				act.startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT)
						.show();
			} finally {
				pd.dismiss();
			}

		} else {
			Toast.makeText(mContext, "Invalid Username / password",
					Toast.LENGTH_SHORT).show();
			pd.dismiss();
		}

		super.onPostExecute(user);
	}
}
