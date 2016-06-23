package vn.com.vietatech.phatbuugui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import vn.com.vietatech.async.MapCitiesAsync;

public class LoginActivity extends AppCompatActivity {

    protected Button btnLogin;
    protected Button  btnExit;
    protected TextView txtUsername;
    protected TextView txtPassword;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        new MapCitiesAsync(getApplicationContext()).execute();
//        if (username.length() == 0 || password.length() == 0) {
//            Toast.makeText(getApplicationContext(),
//                    "Username / password can not empty", Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }

        //new UpdateTimeAsync(context).execute();
        //new LoginAsync(context, getApplication()).execute(username, password);
    }
}
