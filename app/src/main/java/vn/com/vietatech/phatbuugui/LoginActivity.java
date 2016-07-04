package vn.com.vietatech.phatbuugui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import vn.com.vietatech.async.InitData;
import vn.com.vietatech.async.LoginAsync;
import vn.com.vietatech.async.MapCitiesAsync;
import vn.com.vietatech.async.SolutionsAsync;
import vn.com.vietatech.dao.UsersDataSource;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.lib.ReasonsGetPropertyValues;

public class LoginActivity extends AppCompatActivity {

    protected Button btnLogin;
    protected Button  btnExit;
    protected TextView txtUsername;
    protected TextView txtPassword;
    private Context context = this;

    MyApplication globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsersDataSource dataSource = UsersDataSource.getInstance(context);
        dataSource.open();
        User user = new User("admin");
        boolean isSynced = dataSource.existsUser(user);
        dataSource.close();
        if(!isSynced) {
            new InitData(this).execute();
            new MapCitiesAsync(this).execute();
            new SolutionsAsync(this).execute();
        }

        globalVariable = (MyApplication) getApplicationContext();

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnExit = (Button) findViewById(R.id.btnExit);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPassword = (TextView) findViewById(R.id.txtPassword);

        txtPassword.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    return true;
                }
                return false;
            }
        });

        // exit application
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        username = "admin";
        password = "123";

        if (username.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Tên đăng nhập không thể trống", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
//
//        //new UpdateTimeAsync(context).execute();
        new LoginAsync(this, getApplication()).execute(username, password);
    }
}
