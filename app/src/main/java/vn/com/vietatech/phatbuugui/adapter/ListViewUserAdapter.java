package vn.com.vietatech.phatbuugui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import vn.com.vietatech.dao.UsersDataSource;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.AddUserActivity;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.UserActivity;
import vn.com.vietatech.phatbuugui.dialog.DialogConfirm;

public class ListViewUserAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<User> users;
    private UsersDataSource dataSource;

    final ListViewUserAdapter adapter = this;
    private User selectedUser = null;
    private int selectedUserIndex = -1;

    public ListViewUserAdapter(Context mContext) {
        this.mContext = mContext;

        dataSource = UsersDataSource.getInstance(mContext);
        dataSource.open();
        setUsers(dataSource.getAllUsers());
        dataSource.close();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        final int _position = position;

        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogConfirm(mContext, mContext.getString(R.string.confirm_delete)) {
                    public void run() {
                        try {
                            User user = getUsers().get(_position);
                            dataSource.open();
                            dataSource.deleteUser(user);
                            dataSource.close();

                            getUsers().remove(_position);
                            adapter.removeShownLayouts(swipeLayout);
                            adapter.notifyDataSetInvalidated();
                            adapter.closeItem(_position);
                        } catch (Exception ex) {
                            Utils.showAlert(mContext, ex.getMessage());
                        }
                    }
                };
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddUserActivity.class);
                intent.setAction(AddUserActivity.ACTION_EDIT);
                setSelectedUser(getUsers().get(_position));
                setSelectedUserIndex(_position);
                intent.putExtra(AddUserActivity.EXTRA_PARAM_USER, getSelectedUser());
                UserActivity userActivity = (UserActivity)mContext;
                userActivity.startActivityForResult(intent, AddUserActivity.RESQUEST_CODE_EDIT);
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView txtNameView = (TextView)convertView.findViewById(R.id.txtNameView);
        txtNameView.setText(getUsers().get(position).getName());

        TextView txtUserNameView = (TextView)convertView.findViewById(R.id.txtUserNameView);
        txtUserNameView.setText("@" + getUsers().get(position).getUsername());

        TextView txtRoleView = (TextView)convertView.findViewById(R.id.txtRoleView);
        txtRoleView.setText("Role: " + getUsers().get(position).getRole());

        TextView txtPhoneView = (TextView)convertView.findViewById(R.id.txtPhoneView);
        txtPhoneView.setText(getUsers().get(position).getPhone());
    }

    @Override
    public int getCount() {
        return getUsers().size();
    }

    @Override
    public Object getItem(int position) {
        return getUsers().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public int getSelectedUserIndex() {
        return selectedUserIndex;
    }

    public void setSelectedUserIndex(int selectedUserIndex) {
        this.selectedUserIndex = selectedUserIndex;
    }
}
