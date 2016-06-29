package vn.com.vietatech.phatbuugui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import vn.com.vietatech.dao.UsersDataSource;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.dialog.DialogConfirm;

public class ListViewUserAdapter extends BaseSwipeAdapter {

    private Context mContext;
    List<User> users;
    UsersDataSource dataSource;

    final ListViewUserAdapter adapter = this;

    public ListViewUserAdapter(Context mContext) {
        this.mContext = mContext;

        dataSource = UsersDataSource.getInstance(mContext);
        dataSource.open();
        users = dataSource.getAllUsers();
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
//        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
//            }
//        });
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
                            User user = users.get(_position);
                            dataSource.open();
                            dataSource.deleteUser(user);
                            dataSource.close();

                            users.remove(_position);
                            adapter.removeShownLayouts(swipeLayout);
                            adapter.notifyDataSetInvalidated();
                        } catch(Exception ex) {
                            Utils.showAlert(mContext, ex.getMessage());
                        }
                        Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click edit", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView txtNameView = (TextView)convertView.findViewById(R.id.txtNameView);
        txtNameView.setText(users.get(position).getId() + ". " + users.get(position).getUsername());

        TextView txtRoleView = (TextView)convertView.findViewById(R.id.txtRoleView);
        txtRoleView.setText(users.get(position).getRole());
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
