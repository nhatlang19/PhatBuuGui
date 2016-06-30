package vn.com.vietatech.phatbuugui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import vn.com.vietatech.dao.UsersDataSource;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.dialog.TransparentProgressDialog;


public class AddUserActivity  extends AppCompatActivity {
    private Context mContext = this;

    protected TextView txtUsername;
    protected TextView txtName;
    protected TextView txtPhone;
    protected TextView txtPassword;
    protected TextView txtConfirmPassword;

    public static int RESQUEST_CODE_ADD = 1;
    public static int RESQUEST_CODE_EDIT = 2;

    public static String ACTION_ADD = "add";
    public static String ACTION_EDIT = "edit";

    public static String EXTRA_PARAM_USER = "User";

    protected User selectedUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        txtUsername = (TextView) findViewById(R.id.user_txtUsername);
        txtName = (TextView) findViewById(R.id.user_txtName);
        txtPhone = (TextView) findViewById(R.id.user_txtPhone);
        txtPassword = (TextView) findViewById(R.id.user_txtPassword);
        txtConfirmPassword = (TextView) findViewById(R.id.user_txtConfirmPassword);

        Intent intent = getIntent();
        if(intent.getAction().equals(ACTION_EDIT)) {
            // update title
            this.setTitle(R.string.title_activity_edit_userscreen);

            selectedUser = (User) intent.getSerializableExtra(EXTRA_PARAM_USER);
            txtUsername.setText(selectedUser.getUsername());
            txtName.setText(selectedUser.getName());
            txtPhone.setText(selectedUser.getPhone());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnSaveUser) {
            try {
                User user = initUser();
                UsersDataSource dataSource = UsersDataSource.getInstance(this);
                dataSource.open();
                if(dataSource.existsUser(user)) {
                    throw new Exception(this.getString(R.string.user_exists));
                }

                if(selectedUser == null) {
                    user = dataSource.createUser(user);
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_PARAM_USER, user);
                    setResult(RESQUEST_CODE_ADD, intent);
                } else {
                    user = dataSource.updateUser(user);
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_PARAM_USER, user);
                    setResult(RESQUEST_CODE_EDIT, intent);
                }
                dataSource.close();
                finish();//finishing activity
            } catch (Exception ex) {
                Utils.showAlert(this, ex.getMessage());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private User initUser() throws Exception {
        String userName = txtUsername.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPassword = txtConfirmPassword.getText().toString().trim();
        String role = "User";

        if(userName.length() == 0) {
            throw new Exception(this.getString(R.string.username_is_empty));
        }

        if(name.length() == 0) {
            throw new Exception(this.getString(R.string.name_is_empty));
        }

        if(selectedUser == null) {
            if (password.length() == 0) {
                throw new Exception(this.getString(R.string.password_is_empty));
            }
        }
        if(selectedUser == null || password.length() > 0) {
            if (!password.equals(confirmPassword)) {
                throw new Exception(this.getString(R.string.password_does_not_match));
            }
        }
        if(selectedUser != null) {
            return new User(selectedUser.getId(), name, userName, password, role, phone);
        }
        return new User(name, userName, password, role, phone);
    }

}
