package vn.com.vietatech.phatbuugui;

import vn.com.vietatech.dto.User;

public class MyApplication extends android.support.multidex.MultiDexApplication {
    private User _user = null;

    public User getUser() {
        return _user;
    }

    public void setUser(User _user) {
        this._user = _user;
    }
}
