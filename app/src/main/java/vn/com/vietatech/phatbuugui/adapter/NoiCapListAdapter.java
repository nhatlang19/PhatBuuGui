package vn.com.vietatech.phatbuugui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.vietatech.dao.CitiesDataSource;
import vn.com.vietatech.dto.City;
import vn.com.vietatech.phatbuugui.R;

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

			City _city = new City();
			_city.setId(0);
			_city.setCode("0");
			_city.setDesc("[Chọn nơi cấp]");
			this.values.add(_city);
			ds.close();
		}
	}

	public int getItemIndexByDesc(String desc) {
		int index = 0;
		for(City city : values) {
			if(city.getDesc().equals(desc)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public int getCount() {
		int size = values.size();
		return size > 0 ? size - 1 : size;
	}

	public City getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater
		View rowView = inflater.inflate(R.layout.row, parent, false);

		// 3. Get the two text view from the rowView
		TextView labelView = (TextView) rowView.findViewById(R.id.label);
		labelView.setText(values.get(position).getDesc().trim());
		labelView.setPadding(5, 12, 5, 17);
		return rowView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater
		View rowView = inflater.inflate(R.layout.row, parent, false);

		// 3. Get the two text view from the rowView
		TextView labelView = (TextView) rowView.findViewById(R.id.label);
		labelView.setText(values.get(position).getDesc().trim());
		labelView.setPadding(5, 12, 5, 17);
		return rowView;
	}
}
