package vn.com.vietatech.phatbuugui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.vietatech.dao.CitiesDataSource;
import vn.com.vietatech.dto.City;

public class NoiCapListAdapter extends ArrayAdapter<City>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Session)
    private ArrayList<City> values;

    public NoiCapListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
		this.context = context;


		if(null == values) {
			CitiesDataSource ds = CitiesDataSource.getInstance(context);
			ds.open();
			this.values = (ArrayList<City>) ds.getAllCities();
			ds.close();
		}
	}

	public int getCount() {
		return values.size();
	}

	public City getItem(int position) {
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
		label.setText(values.get(position).getDesc().trim());
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
		label.setText(values.get(position).getDesc().trim());
		return label;
	}
}
