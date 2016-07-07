package vn.com.vietatech.phatbuugui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.vietatech.dao.ReasonsDataSource;
import vn.com.vietatech.dto.Reason;

public class ReasonListAdapter extends ArrayAdapter<Reason>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Session)
    private static ArrayList<Reason> values;

    public ReasonListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
		this.context = context;


		if(null == values) {
			ReasonsDataSource ds = ReasonsDataSource.getInstance(context);
			ds.open();
			this.values = (ArrayList<Reason>) ds.getAllReasons();
			ds.close();
		}
	}

	public int getCount() {
		return values.size();
	}

	public Reason getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label;
		
		if (convertView != null){
			label = (TextView) convertView;
        } else {
        	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        	label = (TextView) inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false
            );
        }
        label.setTextSize(20);
		label.setText(values.get(position).getName().trim());
		return label;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView label;
		if (convertView != null){
			label = (TextView) convertView;
        } else {
        	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        	label = (TextView) inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false
            );
        }
        label.setTextSize(20);
		label.setText(values.get(position).getName().trim());
		return label;
	}
}
