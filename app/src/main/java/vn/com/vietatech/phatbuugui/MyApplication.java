package vn.com.vietatech.phatbuugui;

import android.app.Application;

import vn.com.vietatech.dto.User;

public class MyApplication extends Application {
	private User _user = null;


	public User getUser() {
		return _user;
	}

	public void setUser(User _user) {
		this._user = _user;
	}
}
