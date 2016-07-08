package vn.com.vietatech.phatbuugui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import vn.com.vietatech.dao.DeliveryDataSource;
import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.dialog.DialogConfirm;

public class ListViewDeliveryAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<Delivery> deliveries;
    private DeliveryDataSource dataSource;

    final ListViewDeliveryAdapter adapter = this;
    private Delivery selectedDelivery = null;
    private int selectedDeliveryIndex = -1;

    public ListViewDeliveryAdapter(Context mContext) {
        this.mContext = mContext;

        dataSource = DeliveryDataSource.getInstance(mContext);
        dataSource.open();
        setDeliveries(dataSource.getAllDeliveries());
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
//                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        final int _position = position;

        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogConfirm(mContext, mContext.getString(R.string.confirm_delete)) {
                    public void run() {
                        try {
                            Delivery Delivery = getDeliveries().get(_position);
                            dataSource.open();
                            dataSource.deleteDelivery(Delivery);
                            dataSource.close();

                            getDeliveries().remove(_position);
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
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView txtBuuGui = (TextView)convertView.findViewById(R.id.txtBuuGui);
        txtBuuGui.setText(getDeliveries().get(position).getItemCode());

        TextView txtNguoiNhan = (TextView)convertView.findViewById(R.id.txtNguoiNhan);
        txtNguoiNhan.setText("Người nhận: " + getDeliveries().get(position).getDeliveryUser());

        View uploadColor = (View) convertView.findViewById(R.id.uploadColor);
        if(getDeliveries().get(position).getItemCode().equals("1")) {
            uploadColor.setBackgroundColor(Color.parseColor("#1dc100"));
        }
    }

    @Override
    public int getCount() {
        return getDeliveries().size();
    }

    @Override
    public Object getItem(int position) {
        return getDeliveries().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Delivery> getDeliveries() {
        return this.deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public Delivery getSelectedDelivery() {
        return selectedDelivery;
    }

    public void setSelectedDelivery(Delivery selectedDelivery) {
        this.selectedDelivery = selectedDelivery;
    }

    public int getSelectedDeliveryIndex() {
        return selectedDeliveryIndex;
    }

    public void setSelectedDeliveryIndex(int selectedDeliveryIndex) {
        this.selectedDeliveryIndex = selectedDeliveryIndex;
    }
}
