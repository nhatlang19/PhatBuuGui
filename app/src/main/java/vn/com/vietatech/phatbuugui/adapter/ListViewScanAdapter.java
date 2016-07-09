package vn.com.vietatech.phatbuugui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dao.DeliveryDataSource;
import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.dialog.DialogConfirm;

public class ListViewScanAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private static ArrayList<Delivery> deliveries;
    private DeliveryDataSource dataSource;
    private Delivery selectedDelivery = null;
    private int selectedDeliveryIndex = -1;
    private List<String> listCodes;

    public ListViewScanAdapter(Context mContext, List<String> listCodes) {
        this.mContext = mContext;
        this.listCodes = listCodes;
        deliveries = new ArrayList<>();
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setListCodes(List<String> listCodes) {
        this.listCodes = listCodes;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_scan_item, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        final int _position = position;
        final ListViewScanAdapter adapter = this;
        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DialogConfirm(mContext, mContext.getString(R.string.confirm_delete)) {
                    public void run() {
                        try {
                            getDeliveries().remove(_position);
                            adapter.removeShownLayouts(swipeLayout);
                            adapter.notifyDataSetInvalidated();
                            adapter.closeItem(_position);

                            listCodes.remove(_position);
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

    public void setDeliveries(ArrayList<Delivery> deliveries) {
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
